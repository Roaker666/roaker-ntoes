package com.roaker.notes.uc.dal.mapper.user;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.commons.db.core.query.LambdaUpdateWrapperX;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.uc.dal.dataobject.user.AdminUserInfoDO;
import com.roaker.notes.uc.vo.user.UserExportReqVO;
import com.roaker.notes.uc.vo.user.UserPageReqVO;

import java.util.Collection;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface AdminUserInfoMapper extends BaseMapperX<AdminUserInfoDO> {
    default int updateByUid(AdminUserInfoDO adminUserInfoDO) {
        return update(null, new LambdaUpdateWrapperX<AdminUserInfoDO>()
                .eq(AdminUserInfoDO::getUid, adminUserInfoDO.getUid())
                .setIfPresent(AdminUserInfoDO::getAvatar, adminUserInfoDO.getAvatar())
                .setIfPresent(AdminUserInfoDO::getUsername, adminUserInfoDO.getUsername())
                .setIfPresent(AdminUserInfoDO::getMobile, adminUserInfoDO.getMobile())
                .setIfPresent(AdminUserInfoDO::getEmail, adminUserInfoDO.getEmail())
                .setIfPresent(AdminUserInfoDO::getCyCode, adminUserInfoDO.getCyCode())
                .setIfPresent(AdminUserInfoDO::getStatus, adminUserInfoDO.getStatus())
                .setIfPresent(AdminUserInfoDO::getLoginDate, adminUserInfoDO.getLoginDate())
                .setIfPresent(AdminUserInfoDO::getLoginIp, adminUserInfoDO.getLoginIp()));
    }

    default AdminUserInfoDO selectByUid(String uid) {
        return selectOne(AdminUserInfoDO::getUid, uid, AdminUserInfoDO::getStatus, CommonStatusEnum.ENABLE);
    }

    default List<AdminUserInfoDO> selectList(UserExportReqVO reqVO, Collection<Long> deptIds) {
        return selectList(new LambdaQueryWrapperX<AdminUserInfoDO>()
                .likeIfPresent(AdminUserInfoDO::getUsername, reqVO.getUsername())
                .likeIfPresent(AdminUserInfoDO::getMobile, reqVO.getMobile())
                .eqIfPresent(AdminUserInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(AdminUserInfoDO::getCreateTime, reqVO.getCreateTime())
                .inIfPresent(AdminUserInfoDO::getDeptId, deptIds));
    }

    default List<AdminUserInfoDO> selectByBatchUid(List<String> uidList) {
        return selectList(new LambdaQueryWrapperX<AdminUserInfoDO>()
                .in(AdminUserInfoDO::getUid, uidList)
                .eq(AdminUserInfoDO::getStatus, CommonStatusEnum.ENABLE));
    }

    default AdminUserInfoDO selectByUserName(String username) {
        return selectOne(AdminUserInfoDO::getUsername, username, AdminUserInfoDO::getStatus, CommonStatusEnum.ENABLE);
    }

    default AdminUserInfoDO selectByMobile(String mobile) {
        return selectOne(AdminUserInfoDO::getMobile, mobile, AdminUserInfoDO::getStatus, CommonStatusEnum.ENABLE);
    }

    default AdminUserInfoDO selectByEmail(String email) {
        return selectOne(AdminUserInfoDO::getEmail, email, AdminUserInfoDO::getStatus, CommonStatusEnum.ENABLE);
    }


    default PageResult<AdminUserInfoDO> selectPage(UserPageReqVO reqVO, Collection<Long> deptIds) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AdminUserInfoDO>()
                .likeIfPresent(AdminUserInfoDO::getUsername, reqVO.getUsername())
                .likeIfPresent(AdminUserInfoDO::getMobile, reqVO.getMobile())
                .eqIfPresent(AdminUserInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(AdminUserInfoDO::getCreateTime, reqVO.getCreateTime())
                .inIfPresent(AdminUserInfoDO::getDeptId, deptIds));
    }

    default List<AdminUserInfoDO> selectListByDeptIds(Collection<Long> deptIds) {
        return selectList(AdminUserInfoDO::getDeptId, deptIds);
    }


    default List<AdminUserInfoDO> selectListByStatus(CommonStatusEnum status) {
        return selectList(AdminUserInfoDO::getStatus, status);
    }

}
