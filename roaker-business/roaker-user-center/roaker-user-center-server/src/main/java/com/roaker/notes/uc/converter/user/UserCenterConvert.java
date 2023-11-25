package com.roaker.notes.uc.converter.user;

import com.roaker.notes.uc.dal.dataobject.user.ShareUserInfoDO;
import com.roaker.notes.uc.dto.user.ShareUserDTO;
import com.roaker.notes.uc.dto.user.SocialUserBindReqDTO;
import com.roaker.notes.uc.dto.user.SocialUserUnbindReqDTO;
import com.roaker.notes.uc.vo.user.AppSocialUserBindReqVO;
import com.roaker.notes.uc.vo.user.AppSocialUserUnbindReqVO;
import com.roaker.notes.uc.vo.user.SocialUserBindReqVO;
import com.roaker.notes.uc.vo.user.SocialUserUnbindReqVO;
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

    SocialUserBindReqDTO convert(String userId, Integer userType, SocialUserBindReqVO reqVO);

    SocialUserUnbindReqDTO convert(String userId, Integer userType, SocialUserUnbindReqVO reqVO);

}
