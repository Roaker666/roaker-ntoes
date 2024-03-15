package com.roaker.notes.uc.dal.dataobject.logger;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.OperateTypeEnum;
import com.roaker.notes.enums.UserTypeEnum;
import com.roaker.notes.vo.CommonResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 操作日志表
 *
 * @author Roaker源码
 */
@TableName(value = "sys_operate_log", autoResultMap = true)
@KeySequence("sys_operate_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
public class OperateLogDO extends BaseDO {

    /**
     * {@link #javaMethodArgs} 的最大长度
     */
    public static final Integer JAVA_METHOD_ARGS_MAX_LENGTH = 8000;

    /**
     * {@link #resultData} 的最大长度
     */
    public static final Integer RESULT_MAX_LENGTH = 4000;

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
     * 操作模块
     */
    @TableField
    private String module;
    /**
     * 操作名
     */
    @TableField
    private String name;
    /**
     * 操作分类
     *
     * 枚举 {@link OperateTypeEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private OperateTypeEnum type;
    /**
     * 操作内容，记录整个操作的明细
     * 例如说，修改编号为 1 的用户信息，将性别从男改成女，将姓名从Roaker改成源码。
     */
    @TableField
    private String content;
    /**
     * 拓展字段，有些复杂的业务，需要记录一些字段
     * 例如说，记录订单编号，则可以添加 key 为 "orderId"，value 为订单编号
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(MySqlTypeConstant.JSON)
    private Map<String, Object> exts;

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

    /**
     * Java 方法名
     */
    @TableField
    private String javaMethod;
    /**
     * Java 方法的参数
     *
     * 实际格式为 Map<String, Object>
     *     不使用 @TableField(typeHandler = FastjsonTypeHandler.class) 注解的原因是，数据库存储有长度限制，会进行裁剪，会导致 JSON 反序列化失败
     *     其中，key 为参数名，value 为参数值
     */
    @TableField
    private String javaMethodArgs;
    /**
     * 开始时间
     */
    @TableField
    private LocalDateTime startTime;
    /**
     * 执行时长，单位：毫秒
     */
    @TableField
    private Integer duration;
    /**
     * 结果码
     *
     * 目前使用的 {@link CommonResult#getCode()} 属性
     */
    @TableField
    private Integer resultCode;
    /**
     * 结果提示
     *
     * 目前使用的 {@link CommonResult#getMsg()} 属性
     */
    @TableField
    private String resultMsg;
    /**
     * 结果数据
     *
     * 如果是对象，则使用 JSON 格式化
     */
    @TableField
    private String resultData;

}
