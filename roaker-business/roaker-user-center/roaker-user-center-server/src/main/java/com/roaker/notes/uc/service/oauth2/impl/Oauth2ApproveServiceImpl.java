package com.roaker.notes.uc.service.oauth2.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.google.common.annotations.VisibleForTesting;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2ApproveDO;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2ClientDO;
import com.roaker.notes.uc.dal.mapper.oauth2.Oauth2ApproveMapper;
import com.roaker.notes.uc.service.oauth2.Oauth2ApproveService;
import com.roaker.notes.uc.service.oauth2.Oauth2ClientService;
import com.roaker.notes.commons.utils.date.DateUtils;
import com.roaker.notes.enums.UserTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.roaker.notes.commons.utils.RoakerCollectionUtils.convertSet;

/**
 * Oauth2 批准 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class Oauth2ApproveServiceImpl implements Oauth2ApproveService {

    /**
     * 批准的过期时间，默认 30 天
     */
    private static final Integer TIMEOUT = 30 * 24 * 60 * 60; // 单位：秒

    @Resource
    private Oauth2ClientService oauth2ClientService;

    @Resource
    private Oauth2ApproveMapper oauth2ApproveMapper;

    @Override
    @Transactional
    public boolean checkForPreApproval(String userId, Integer userType, String clientId, Collection<String> requestedScopes) {
        // 第一步，基于 Client 的自动授权计算，如果 scopes 都在自动授权中，则返回 true 通过
        Oauth2ClientDO clientDO = oauth2ClientService.validOAuthClientFromCache(clientId);
        Assert.notNull(clientDO, "客户端不能为空"); // 防御性编程
        if (CollUtil.containsAll(clientDO.getAutoApproveScopes(), requestedScopes)) {
            // gh-877 - if all scopes are auto approved, approvals still need to be added to the approval store.
            LocalDateTime expireTime = LocalDateTime.now().plusSeconds(TIMEOUT);
            for (String scope : requestedScopes) {
                saveApprove(userId, userType, clientId, scope, true, expireTime);
            }
            return true;
        }

        // 第二步，算上用户已经批准的授权。如果 scopes 都包含，则返回 true
        List<Oauth2ApproveDO> approveDOs = getApproveList(userId, userType, clientId);
        Set<String> scopes = convertSet(approveDOs, Oauth2ApproveDO::getScope,
                Oauth2ApproveDO::getApproved); // 只保留未过期的 + 同意的
        return CollUtil.containsAll(scopes, requestedScopes);
    }

    @Override
    @Transactional
    public boolean updateAfterApproval(String userId, Integer userType, String clientId, Map<String, Boolean> requestedScopes) {
        // 如果 requestedScopes 为空，说明没有要求，则返回 true 通过
        if (CollUtil.isEmpty(requestedScopes)) {
            return true;
        }

        // 更新批准的信息
        boolean success = false; // 需要至少有一个同意
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(TIMEOUT);
        for (Map.Entry<String, Boolean> entry : requestedScopes.entrySet()) {
            if (entry.getValue()) {
                success = true;
            }
            saveApprove(userId, userType, clientId, entry.getKey(), entry.getValue(), expireTime);
        }
        return success;
    }

    @Override
    public List<Oauth2ApproveDO> getApproveList(String userId, Integer userType, String clientId) {
        List<Oauth2ApproveDO> approveDOs = oauth2ApproveMapper.selectListByUserIdAndUserTypeAndClientId(
                userId, userType, clientId);
        approveDOs.removeIf(o -> DateUtils.isExpired(o.getExpiresTime()));
        return approveDOs;
    }

    @VisibleForTesting
    void saveApprove(String userId, Integer userType, String clientId,
                     String scope, Boolean approved, LocalDateTime expireTime) {
        // 先更新
        Oauth2ApproveDO approveDO = new Oauth2ApproveDO().setUserId(userId).setUserType(UserTypeEnum.valueOf(userType))
                .setClientId(clientId).setScope(scope).setApproved(approved).setExpiresTime(expireTime);
        if (oauth2ApproveMapper.update(approveDO) == 1) {
            return;
        }
        // 失败，则说明不存在，进行更新
        oauth2ApproveMapper.insert(approveDO);
    }

}
