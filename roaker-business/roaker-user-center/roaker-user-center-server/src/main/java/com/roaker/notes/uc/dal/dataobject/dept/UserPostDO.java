package com.roaker.notes.uc.dal.dataobject.dept;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.uc.dal.dataobject.user.AdminUserInfoDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户和岗位关联
 *
 * @author ruoyi
 */
@TableName("sys_user_post")
@KeySequence("sys_user_post_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserPostDO extends BaseDO {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 用户 ID
     *
     * 关联 {@link AdminUserInfoDO#getUid()}
     */
    @TableField
    private String userId;
    /**
     * 角色 ID
     *
     * 关联 {@link PostDO#getId()}
     */
    @TableField
    private Long postId;

}
