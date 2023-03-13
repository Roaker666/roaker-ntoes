package com.roaker.notes.ac.dal.dataobject.oauth2;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
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
    @TableId
    private Long id;
    /**
     * 客户端编号
     */
    private String clientId;
    /**
     * 客户端密钥
     */
    private String secret;
    /**
     * 应用名
     */
    private String name;
    /**
     * 应用图标
     */
    private String logo;
    /**
     * 应用描述
     */
    private String description;
    /**
     * 状态,{@link com.roaker.notes.enums.CommonStatusEnum}
     */
    private Integer status;
    /**
     * 访问令牌有效期
     */
    private Integer accessTokenValiditySeconds;
    /**
     * 刷新令牌有效期
     */
    private Integer refreshTokenValiditySeconds;
    /**
     * 可重定向的URI地址
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> redirectUris;
    /**
     * 授权类型（模式） {@link com.roaker.notes.ac.api.enums.oath2.Oauth2GrantTypeEnum}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> authorizedGrantTypes;
    /**
     * 授权范围
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> scopes;
    /**
     * 自动授权的scope
     * code授权时，如果scope在这个范围内，则自动通过
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> autoApproveScopes;
    /**
     * 权限
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> authorities;
    /**
     * 资源
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> resourceIds;
    /**
     * 附加信息,JSON格式
     */
    private String additionalInformation;
}
