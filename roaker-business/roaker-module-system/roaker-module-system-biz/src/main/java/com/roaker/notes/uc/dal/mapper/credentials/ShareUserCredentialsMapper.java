package com.roaker.notes.uc.dal.mapper.credentials;

import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.commons.db.core.query.LambdaUpdateWrapperX;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.uc.dal.dataobject.credentials.ShareUserCredentialsDO;
import com.roaker.notes.uc.enums.oauth2.CredentialsIdentifyTypeEnums;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lei.rao
 * @since 1.0
 */
@Mapper
public interface ShareUserCredentialsMapper extends BaseMapperX<ShareUserCredentialsDO> {
    default ShareUserCredentialsDO selectByUidAndIdentityType(String uid, CredentialsIdentifyTypeEnums identifyType) {
        return selectOne(new LambdaQueryWrapperX<ShareUserCredentialsDO>()
                .eq(ShareUserCredentialsDO::getUid, uid)
                .eq(ShareUserCredentialsDO::getIdentifier, uid)
                .eq(ShareUserCredentialsDO::getIdentityType, identifyType)
                .eq(ShareUserCredentialsDO::getStatus, CommonStatusEnum.ENABLE));
    }


    default int updateByUidAndIdentityType(ShareUserCredentialsDO credentialsDO) {
        return update(credentialsDO, new LambdaUpdateWrapperX<ShareUserCredentialsDO>()
                .eq(ShareUserCredentialsDO::getUid, credentialsDO.getUid())
                .eq(ShareUserCredentialsDO::getIdentifier, credentialsDO.getIdentifier())
                .eq(ShareUserCredentialsDO::getIdentityType, credentialsDO.getIdentityType())
                .eq(ShareUserCredentialsDO::getStatus, credentialsDO.getStatus())
                .setIfPresent(ShareUserCredentialsDO::getCredentials, credentialsDO.getCredentials())
                .setIfPresent(ShareUserCredentialsDO::getExpireTime, credentialsDO.getExpireTime()));
    }
}
