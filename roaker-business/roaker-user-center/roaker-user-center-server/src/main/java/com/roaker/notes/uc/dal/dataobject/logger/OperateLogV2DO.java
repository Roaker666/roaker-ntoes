package com.roaker.notes.uc.dal.dataobject.logger;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.UserTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 操作日志表 V2
 *
 * @author Roaker源码
 */
@TableName(value = "sys_operate_log_v2", autoResultMap = true)
@KeySequence("sys_operate_log_seq_v2") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
public class OperateLogV2DO extends BaseDO {

    /**
     * 日志主键
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 链路追踪编号
     *
     * 一般来说，通过链路追踪编号，可以将访问日志，错误日志，链路追踪日志，logger 打印日志等，结合在一起，从而进行排错。
     */
    @TableField
    private String traceId;
    /**
     * 用户编号
     *
     * 关联 MemberUserDO 的 id 属性，或者 AdminUserDO 的 id 属性
     */
    @TableField
    private String userId;
    /**
     * 用户类型
     *
     * 关联 {@link  UserTypeEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private UserTypeEnum userType;
    /**
     * 操作模块类型
     */
    @TableField
    private String type;
    /**
     * 操作名
     */
    @TableField
    private String subType;
    /**
     * 操作模块业务编号
     */
    @TableField
    private Long bizId;
    /**
     * 日志内容，记录整个操作的明细
     *
     * 例如说，修改编号为 1 的用户信息，将性别从男改成女，将姓名从Roaker改成源码。
     */
    @TableField
    private String action;
    /**
     * 拓展字段，有些复杂的业务，需要记录一些字段 ( JSON 格式 )
     *
     * 例如说，记录订单编号，{ orderId: "1"}
     */
    @TableField
    private String extra;

    /**
     * 请求方法名
     */
    @TableField
    private String requestMethod;
    /**
     * 请求地址
     */
    @TableField
    private String requestUrl;
    /**
     * 用户 IP
     */
    @TableField
    private String userIp;
    /**
     * 浏览器 UA
     */
    @TableField
    private String userAgent;

}
