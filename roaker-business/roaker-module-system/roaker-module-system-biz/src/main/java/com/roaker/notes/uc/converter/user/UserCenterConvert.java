package com.roaker.notes.uc.converter.user;

import com.roaker.notes.commons.utils.BeanUtils;
import com.roaker.notes.commons.utils.RoakerCollectionUtils;
import com.roaker.notes.commons.utils.RoakerMapUtils;
import com.roaker.notes.uc.api.social.dto.SocialUserBindReqDTO;
import com.roaker.notes.uc.api.social.dto.SocialUserUnbindReqDTO;
import com.roaker.notes.uc.controller.dept.vo.dept.DeptSimpleRespVO;
import com.roaker.notes.uc.controller.dept.vo.post.PostSimpleRespVO;
import com.roaker.notes.uc.dal.dataobject.dept.DeptDO;
import com.roaker.notes.uc.dal.dataobject.dept.PostDO;
import com.roaker.notes.uc.dal.dataobject.permission.RoleInfoDO;
import com.roaker.notes.uc.dal.dataobject.user.AdminUserInfoDO;
import com.roaker.notes.uc.dal.dataobject.user.ShareUserInfoDO;
import com.roaker.notes.uc.dal.dataobject.user.SocialUserDO;
import com.roaker.notes.uc.vo.permission.RoleSimpleRespVO;
import com.roaker.notes.uc.vo.user.*;
import com.roaker.notes.uc.dto.user.ShareUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * Oauth2 客户端 Convert
 *
 * @author lei.rao
 */
@Mapper
public interface UserCenterConvert {

    UserCenterConvert INSTANCE = Mappers.getMapper(UserCenterConvert.class);


    @Mapping(source = "reqVO.type", target = "socialType")
    SocialUserBindReqDTO convert(String userId, Integer userType, SocialUserBindReqVO reqVO);

    default List<UserRespVO> convertList(List<AdminUserInfoDO> list, Map<Long, DeptDO> deptMap) {
        return RoakerCollectionUtils.convertList(list, user -> convert(user, deptMap.get(user.getDeptId())));
    }


    default UserRespVO convert(AdminUserInfoDO user, DeptDO dept) {
        UserRespVO userVO = BeanUtils.toBean(user, UserRespVO.class);
        if (dept != null) {
            userVO.setDeptName(dept.getName());
        }
        return userVO;
    }

    default List<UserSimpleRespVO> convertSimpleList(List<AdminUserInfoDO> list, Map<Long, DeptDO> deptMap) {
        return RoakerCollectionUtils.convertList(list, user -> {
            UserSimpleRespVO userVO = BeanUtils.toBean(user, UserSimpleRespVO.class);
            RoakerMapUtils.findAndThen(deptMap, user.getDeptId(), dept -> userVO.setDeptName(dept.getName()));
            return userVO;
        });
    }

    default UserProfileRespVO convert(AdminUserInfoDO user, List<RoleInfoDO> userRoles,
                                      DeptDO dept, List<PostDO> posts, List<SocialUserDO> socialUsers) {
        UserProfileRespVO userVO = BeanUtils.toBean(user, UserProfileRespVO.class);
        userVO.setRoles(BeanUtils.toBean(userRoles, RoleSimpleRespVO.class));
        userVO.setDept(BeanUtils.toBean(dept, DeptSimpleRespVO.class));
        userVO.setPosts(BeanUtils.toBean(posts, PostSimpleRespVO.class));
        userVO.setSocialUsers(BeanUtils.toBean(socialUsers, UserProfileRespVO.SocialUser.class));
        return userVO;
    }
    ShareUserDTO convert(ShareUserInfoDO bean);

    SocialUserUnbindReqDTO convert(String userId, Integer userType, SocialUserUnbindReqVO reqVO);

    UserPageItemRespVO convert(AdminUserInfoDO bean);

    UserPageItemRespVO.Dept convert(DeptDO bean);

    AdminUserInfoDO convert(UserCreateReqVO bean);

    AdminUserInfoDO convert(UserUpdateReqVO bean);

    UserExcelVO convert02(AdminUserInfoDO bean);

    AdminUserInfoDO convert(UserImportExcelVO bean);

    UserProfileRespVO convert03(AdminUserInfoDO bean);

    AdminUserInfoDO convert(UserProfileUpdateReqVO bean);

    AdminUserInfoDO convert(UserProfileUpdatePasswordReqVO bean);

    List<UserProfileRespVO.SocialUser> convertList03(List<SocialUserDO> list);

    List<UserSimpleRespVO> convertList04(List<AdminUserInfoDO> list);

    AdminUserRespDTO convert4(AdminUserInfoDO bean);

    List<AdminUserRespDTO> convertList4(List<AdminUserInfoDO> users);

    List<AdminUserRespDTO> convertList2(List<AdminUserInfoDO> list);


}
