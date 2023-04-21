package com.roaker.notes.ac.dal.dataobject.oauth2;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.ac.api.enums.Oauth2GrantTypeEnum;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "oauth2_client_tab", autoResultMap = true)
@KeySequence("oauth2_client_tab_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class Oauth2ClientDO extends BaseDO {
    /**
     * 编号，数据库自增
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 客户端编号
     */
    @TableField
    private String clientId;
    /**
     * 客户端密钥
     */
    @TableField
    private String secret;
    /**
     * 应用名
     */
    @TableField
    private String name;
    /**
     * 应用图标
     */
    @TableField
    private String logo;
    /**
     * 应用描述
     */
    @TableField
    private String description;
    /**
     * 状态,{@link com.roaker.notes.enums.CommonStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;
    /**
     * 访问令牌有效期
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private Integer accessTokenValiditySeconds;
    /**
     * 刷新令牌有效期
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private Integer refreshTokenValiditySeconds;
    /**
     * 可重定向的URI地址
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(MySqlTypeConstant.JSON)
    private List<String> redirectUris;
    /**
     * 授权类型（模式） {@link Oauth2GrantTypeEnum}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(MySqlTypeConstant.JSON)
    private List<String> authorizedGrantTypes;
    /**
     * 授权范围
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(MySqlTypeConstant.JSON)
    private List<String> scopes;
    /**
     * 自动授权的scope
     * code授权时，如果scope在这个范围内，则自动通过
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(MySqlTypeConstant.JSON)
    private List<String> autoApproveScopes;
    /**
     * 权限
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(MySqlTypeConstant.JSON)
    private List<String> authorities;
    /**
     * 资源
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(MySqlTypeConstant.JSON)
    private List<String> resourceIds;
    /**
     * 附加信息,JSON格式
     */
    @TableField
    private String additionalInformation;
}
