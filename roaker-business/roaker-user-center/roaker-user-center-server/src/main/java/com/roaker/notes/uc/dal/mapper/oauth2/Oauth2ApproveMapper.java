package com.roaker.notes.uc.dal.mapper.oauth2;

import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2ApproveDO;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Oauth2ApproveMapper extends BaseMapperX<Oauth2ApproveDO> {

    default int update(Oauth2ApproveDO updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<Oauth2ApproveDO>()
                .eq(Oauth2ApproveDO::getUserId, updateObj.getUserId())
                .eq(Oauth2ApproveDO::getUserType, updateObj.getUserType())
                .eq(Oauth2ApproveDO::getClientId, updateObj.getClientId())
                .eq(Oauth2ApproveDO::getScope, updateObj.getScope()));
    }

    default List<Oauth2ApproveDO> selectListByUserIdAndUserTypeAndClientId(String userId, Integer userType, String clientId) {
        return selectList(new LambdaQueryWrapperX<Oauth2ApproveDO>()
                .eq(Oauth2ApproveDO::getUserId, userId)
                .eq(Oauth2ApproveDO::getUserType, userType)
                .eq(Oauth2ApproveDO::getClientId, clientId));
    }

}
