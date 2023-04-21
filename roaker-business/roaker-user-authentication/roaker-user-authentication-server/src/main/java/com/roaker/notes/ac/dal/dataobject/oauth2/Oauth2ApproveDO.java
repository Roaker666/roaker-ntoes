package com.roaker.notes.ac.dal.dataobject.oauth2;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.UserTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "oauth2_approve_tab", autoResultMap = true)
@KeySequence("oauth2_approve_tab_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Oauth2ApproveDO extends BaseDO {
    /**
     * 编号
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    @TableField
    private Long userId;
    @ColumnType(MySqlTypeConstant.INT)
    @TableField
    private UserTypeEnum userType;
    @ColumnType
    @TableField
    private String clientId;
    /**
     * 授权范围
     */
    @TableField
    private String scope;
    /**
     * 是否接受
     * true - 接受
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private Boolean approved;
    /**
     * 过期时间
     */
    @TableField
    private LocalDateTime expiresTime;
}
