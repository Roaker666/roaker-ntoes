package com.roaker.notes.uc.dal.mapper.user;

import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.uc.dal.dataobject.user.ShareUserInfoDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Mapper
public interface ShareUserInfoMapper extends BaseMapperX<ShareUserInfoDO> {
    default ShareUserInfoDO selectByMobile(String mobile) {
        return selectOne(ShareUserInfoDO::getMobile, mobile);
    }

    default List<ShareUserInfoDO> selectListByUsernameLike(String username) {
        return selectList(new LambdaQueryWrapperX<ShareUserInfoDO>()
                .likeIfPresent(ShareUserInfoDO::getUsername, username));
    }
}
