package com.roaker.notes.uc.converter;

import com.roaker.notes.uc.dal.dataobject.ShareUserInfoDO;
import com.roaker.notes.uc.dto.ShareUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Oauth2 客户端 Convert
 *
 * @author lei.rao
 */
@Mapper
public interface UserCenterConvert {

    UserCenterConvert INSTANCE = Mappers.getMapper(UserCenterConvert.class);

    ShareUserDTO convert(ShareUserInfoDO bean);

}
