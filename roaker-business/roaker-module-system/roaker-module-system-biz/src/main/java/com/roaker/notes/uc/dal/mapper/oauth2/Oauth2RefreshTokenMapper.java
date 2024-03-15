package com.roaker.notes.uc.dal.mapper.oauth2;

import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2RefreshTokenDO;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Oauth2RefreshTokenMapper extends BaseMapperX<Oauth2RefreshTokenDO> {

    default int deleteByRefreshToken(String refreshToken) {
        return delete(new LambdaQueryWrapperX<Oauth2RefreshTokenDO>()
                .eq(Oauth2RefreshTokenDO::getRefreshToken, refreshToken));
    }

    default Oauth2RefreshTokenDO selectByRefreshToken(String refreshToken) {
        return selectOne(Oauth2RefreshTokenDO::getRefreshToken, refreshToken);
    }

}
