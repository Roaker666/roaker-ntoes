package com.roaker.notes.uc.converter.oauth2;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.roaker.notes.uc.controller.oauth2.open.vo.open.Oauth2OpenAccessTokenRespVO;
import com.roaker.notes.uc.controller.oauth2.open.vo.open.Oauth2OpenAuthorizeInfoRespVO;
import com.roaker.notes.uc.controller.oauth2.open.vo.open.Oauth2OpenCheckTokenRespVO;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2AccessTokenDO;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2ApproveDO;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2ClientDO;
import com.roaker.notes.uc.utils.Oauth2Utils;
import com.roaker.notes.commons.core.KeyValue;
import com.roaker.notes.commons.utils.RoakerCollectionUtils;
import com.roaker.notes.enums.UserTypeEnum;
import com.roaker.notes.uc.utils.SecurityFrameworkUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface Oauth2OpenConvert {

    Oauth2OpenConvert INSTANCE = Mappers.getMapper(Oauth2OpenConvert.class);

    default Oauth2OpenAccessTokenRespVO convert(Oauth2AccessTokenDO bean) {
        Oauth2OpenAccessTokenRespVO respVO = convert0(bean);
        respVO.setTokenType(SecurityFrameworkUtils.AUTHORIZATION_BEARER.toLowerCase());
        respVO.setExpiresIn(Oauth2Utils.getExpiresIn(bean.getExpiresTime()));
        respVO.setScope(Oauth2Utils.buildScopeStr(bean.getScopes()));
        return respVO;
    }

    Oauth2OpenAccessTokenRespVO convert0(Oauth2AccessTokenDO bean);

    default Oauth2OpenCheckTokenRespVO convert2(Oauth2AccessTokenDO bean) {
        Oauth2OpenCheckTokenRespVO respVO = convert3(bean);
        respVO.setExp(LocalDateTimeUtil.toEpochMilli(bean.getExpiresTime()) / 1000L);
        respVO.setUserType(UserTypeEnum.ADMIN.getCode());
        return respVO;
    }


    @Mapping(target = "userType", expression = "java(bean.getUserType().getCode())")
    Oauth2OpenCheckTokenRespVO convert3(Oauth2AccessTokenDO bean);

    default Oauth2OpenAuthorizeInfoRespVO convert(Oauth2ClientDO client, List<Oauth2ApproveDO> approves) {
        // 构建 scopes
        List<KeyValue<String, Boolean>> scopes = new ArrayList<>(client.getScopes().size());
        Map<String, Oauth2ApproveDO> approveMap = RoakerCollectionUtils.convertMap(approves, Oauth2ApproveDO::getScope);
        client.getScopes().forEach(scope -> {
            Oauth2ApproveDO approve = approveMap.get(scope);
            scopes.add(new KeyValue<>(scope, approve != null ? approve.getApproved() : false));
        });
        // 拼接返回
        return new Oauth2OpenAuthorizeInfoRespVO(
                new Oauth2OpenAuthorizeInfoRespVO.Client(client.getName(), client.getLogo()), scopes);
    }

}
