package com.roaker.notes.ac.service.oauth2.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.annotations.VisibleForTesting;
import com.roaker.notes.ac.controller.admin.oauth2.vo.client.Oauth2ClientCreateReqVO;
import com.roaker.notes.ac.controller.admin.oauth2.vo.client.Oauth2ClientPageReqVO;
import com.roaker.notes.ac.controller.admin.oauth2.vo.client.Oauth2ClientUpdateReqVO;
import com.roaker.notes.ac.converter.Oauth2ClientConvert;
import com.roaker.notes.ac.dal.dataobject.oauth2.Oauth2ClientDO;
import com.roaker.notes.ac.dal.mapper.oauth2.Oauth2ClientMapper;
import com.roaker.notes.ac.service.oauth2.Oauth2ClientService;
import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.commons.utils.StrUtils;
import com.roaker.notes.enums.CommonStatusEnum;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.roaker.notes.commons.constants.ErrorCodeConstants.*;
import static com.roaker.notes.commons.utils.RoakerCollectionUtils.convertMap;
import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;

/**
 * Oauth2.0 Client Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class Oauth2ClientServiceImpl implements Oauth2ClientService {

    /**
     * 客户端缓存
     * key：客户端编号 {@link Oauth2ClientDO#getClientId()} ()}
     * <p>
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter // 解决单测
    @Setter // 解决单测
    private volatile Map<String, Oauth2ClientDO> clientCache;

    @Resource
    private Oauth2ClientMapper oauth2ClientMapper;

//    @Resource
//    private Oauth2ClientProducer oauth2ClientProducer;

    /**
     * 初始化 {@link #clientCache} 缓存
     */
    @Override
    @PostConstruct
    public void initLocalCache() {
        // 第一步：查询数据
        List<Oauth2ClientDO> clients = oauth2ClientMapper.selectList();
        log.info("[initLocalCache][缓存 Oauth2 客户端，数量为:{}]", clients.size());

        // 第二步：构建缓存。
        clientCache = convertMap(clients, Oauth2ClientDO::getClientId);
    }

    @Override
    public Long createOauth2Client(Oauth2ClientCreateReqVO createReqVO) {
        validateClientIdExists(null, createReqVO.getClientId());
        // 插入
        Oauth2ClientDO oauth2Client = Oauth2ClientConvert.INSTANCE.convert(createReqVO);
        oauth2ClientMapper.insert(oauth2Client);
        // 发送刷新消息
//        oauth2ClientProducer.sendOauth2ClientRefreshMessage();
        return oauth2Client.getId();
    }

    @Override
    public void updateOauth2Client(Oauth2ClientUpdateReqVO updateReqVO) {
        // 校验存在
        validateOauth2ClientExists(updateReqVO.getId());
        // 校验 Client 未被占用
        validateClientIdExists(updateReqVO.getId(), updateReqVO.getClientId());

        // 更新
        Oauth2ClientDO updateObj = Oauth2ClientConvert.INSTANCE.convert(updateReqVO);
        oauth2ClientMapper.updateById(updateObj);
        // 发送刷新消息
//        oauth2ClientProducer.sendOauth2ClientRefreshMessage();
    }

    @Override
    public void deleteOauth2Client(Long id) {
        // 校验存在
        validateOauth2ClientExists(id);
        // 删除
        oauth2ClientMapper.deleteById(id);
        // 发送刷新消息
//        oauth2ClientProducer.sendOauth2ClientRefreshMessage();
    }

    private void validateOauth2ClientExists(Long id) {
        if (oauth2ClientMapper.selectById(id) == null) {
            throw exception(OAUTH2_CLIENT_NOT_EXISTS);
        }
    }

    @VisibleForTesting
    void validateClientIdExists(Long id, String clientId) {
        Oauth2ClientDO client = oauth2ClientMapper.selectByClientId(clientId);
        if (client == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的客户端
        if (id == null) {
            throw exception(OAUTH2_CLIENT_EXISTS);
        }
        if (!client.getId().equals(id)) {
            throw exception(OAUTH2_CLIENT_EXISTS);
        }
    }

    @Override
    public Oauth2ClientDO getOauth2Client(Long id) {
        return oauth2ClientMapper.selectById(id);
    }

    @Override
    public PageResult<Oauth2ClientDO> getOauth2ClientPage(Oauth2ClientPageReqVO pageReqVO) {
        return oauth2ClientMapper.selectPage(pageReqVO);
    }

    @Override
    public Oauth2ClientDO validOAuthClientFromCache(String clientId, String clientSecret,
                                                    String authorizedGrantType, Collection<String> scopes, String redirectUri) {
        // 校验客户端存在、且开启
        Oauth2ClientDO client = clientCache.get(clientId);
        if (client == null) {
            throw exception(OAUTH2_CLIENT_NOT_EXISTS);
        }
        if (ObjectUtil.notEqual(client.getStatus(), CommonStatusEnum.ENABLE.getCode())) {
            throw exception(OAUTH2_CLIENT_DISABLE);
        }

        // 校验客户端密钥
        if (StrUtil.isNotEmpty(clientSecret) && ObjectUtil.notEqual(client.getSecret(), clientSecret)) {
            throw exception(OAUTH2_CLIENT_CLIENT_SECRET_ERROR);
        }
        // 校验授权方式
        if (StrUtil.isNotEmpty(authorizedGrantType) && !CollUtil.contains(client.getAuthorizedGrantTypes(), authorizedGrantType)) {
            throw exception(OAUTH2_CLIENT_AUTHORIZED_GRANT_TYPE_NOT_EXISTS);
        }
        // 校验授权范围
        if (CollUtil.isNotEmpty(scopes) && !CollUtil.containsAll(client.getScopes(), scopes)) {
            throw exception(OAUTH2_CLIENT_SCOPE_OVER);
        }
        // 校验回调地址
        if (StrUtil.isNotEmpty(redirectUri) && !StrUtils.startWithAny(redirectUri, client.getRedirectUris())) {
            throw exception(OAUTH2_CLIENT_REDIRECT_URI_NOT_MATCH, redirectUri);
        }
        return client;
    }

}
