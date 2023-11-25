package com.roaker.notes.uc.dal.mapper.oauth2;

import com.roaker.notes.uc.controller.oauth2.admin.vo.token.Oauth2AccessTokenPageReqVO;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2AccessTokenDO;
import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Mapper
public interface Oauth2AccessTokenMapper extends BaseMapperX<Oauth2AccessTokenDO> {
    default Oauth2AccessTokenDO selectByAccessToken(String accessToken) {
        return selectOne(Oauth2AccessTokenDO::getAccessToken, accessToken);
    }

    default List<Oauth2AccessTokenDO> selectListByRefreshToken(String refreshToken) {
        return selectList(Oauth2AccessTokenDO::getRefreshToken, refreshToken);
    }

    default PageResult<Oauth2AccessTokenDO> selectPage(Oauth2AccessTokenPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<Oauth2AccessTokenDO>()
                .eqIfPresent(Oauth2AccessTokenDO::getUserId, reqVO.getUserId())
                .eqIfPresent(Oauth2AccessTokenDO::getUserType, reqVO.getUserType())
                .likeIfPresent(Oauth2AccessTokenDO::getClientId, reqVO.getClientId())
                .gt(Oauth2AccessTokenDO::getExpiresTime, LocalDateTime.now())
                .orderByDesc(Oauth2AccessTokenDO::getId));
    }
}
