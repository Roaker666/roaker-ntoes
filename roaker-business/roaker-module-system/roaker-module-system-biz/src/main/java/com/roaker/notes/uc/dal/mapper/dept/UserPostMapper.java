package com.roaker.notes.uc.dal.mapper.dept;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.uc.dal.dataobject.dept.UserPostDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface UserPostMapper extends BaseMapperX<UserPostDO> {

    default List<UserPostDO> selectListByUserId(String userId) {
        return selectList(UserPostDO::getUserId, userId);
    }

    default void deleteByUserIdAndPostId(String userId, Collection<Long> postIds) {
        delete(new LambdaQueryWrapperX<UserPostDO>()
                .eq(UserPostDO::getUserId, userId)
                .in(UserPostDO::getPostId, postIds));
    }

    default List<UserPostDO> selectListByPostIds(Collection<Long> postIds) {
        return selectList(UserPostDO::getPostId, postIds);
    }

    default void deleteByUserId(String userId) {
        delete(Wrappers.lambdaUpdate(UserPostDO.class).eq(UserPostDO::getUserId, userId));
    }
}
