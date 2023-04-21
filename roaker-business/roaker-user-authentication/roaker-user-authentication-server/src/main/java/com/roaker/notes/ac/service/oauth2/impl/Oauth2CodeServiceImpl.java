package com.roaker.notes.ac.service.oauth2.impl;

import cn.hutool.core.util.IdUtil;
import com.roaker.notes.ac.dal.dataobject.oauth2.Oauth2CodeDO;
import com.roaker.notes.ac.dal.mapper.oauth2.Oauth2CodeMapper;
import com.roaker.notes.ac.service.oauth2.Oauth2CodeService;
import com.roaker.notes.commons.utils.date.DateUtils;
import com.roaker.notes.enums.UserTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static com.roaker.notes.commons.constants.ErrorCodeConstants.OAUTH2_CODE_EXPIRE;
import static com.roaker.notes.commons.constants.ErrorCodeConstants.OAUTH2_CODE_NOT_EXISTS;
import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;

/**
 * Oauth2.0 授权码 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class Oauth2CodeServiceImpl implements Oauth2CodeService {

    /**
     * 授权码的过期时间，默认 5 分钟
     */
    private static final Integer TIMEOUT = 5 * 60;

    @Resource
    private Oauth2CodeMapper oauth2CodeMapper;

    @Override
    public Oauth2CodeDO createAuthorizationCode(Long userId, Integer userType, String clientId,
                                                List<String> scopes, String redirectUri, String state) {
        Oauth2CodeDO codeDO = new Oauth2CodeDO().setCode(generateCode())
                .setUserId(userId).setUserType(UserTypeEnum.valueOf(userType))
                .setClientId(clientId).setScopes(scopes)
                .setExpiresTime(LocalDateTime.now().plusSeconds(TIMEOUT))
                .setRedirectUri(redirectUri).setState(state);
        oauth2CodeMapper.insert(codeDO);
        return codeDO;
    }

    @Override
    public Oauth2CodeDO consumeAuthorizationCode(String code) {
        Oauth2CodeDO codeDO = oauth2CodeMapper.selectByCode(code);
        if (codeDO == null) {
            throw exception(OAUTH2_CODE_NOT_EXISTS);
        }
        if (DateUtils.isExpired(codeDO.getExpiresTime())) {
            throw exception(OAUTH2_CODE_EXPIRE);
        }
        oauth2CodeMapper.deleteById(codeDO.getId());
        return codeDO;
    }

    private static String generateCode() {
        return IdUtil.fastSimpleUUID();
    }

}
