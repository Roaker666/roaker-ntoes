package com.roaker.notes.uc.dal.dataobject.encrypt;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.uc.enums.encrypt.DataTypeEnums;
import com.roaker.notes.uc.enums.encrypt.KeyTypeEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author lei.rao
 * @since 1.0
 */

@TableName(value = "sys_key_entity_tab", autoResultMap = true)
@KeySequence("sys_key_entity_tab_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class KeyEntityDO extends BaseDO {
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;

    @TableField
    private String uuid;
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 1024)
    private String secretKey;
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 1024)
    private String privateKey;
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 1024)
    private String publicKey;
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private KeyTypeEnums keyType;
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private DataTypeEnums dataType;
    @TableField
    private String sysCode;
    @TableField
    private LocalDateTime expireTime;
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 512)
    private String contextData;

    public static KeyEntityDO from(String sysCode, DataTypeEnums dataType, KeyTypeEnums keyType, String contextData) {
        KeyEntityDO keyEntityDO = new KeyEntityDO();
        keyEntityDO.setDefault();
        keyEntityDO.setUuid(UUID.randomUUID().toString());
        keyEntityDO.setSysCode(sysCode);
        keyEntityDO.setDataType(dataType);
        keyEntityDO.setKeyType(keyType);
        keyEntityDO.setStatus(CommonStatusEnum.ENABLE);
        keyEntityDO.setExpireTime(LocalDateTime.now().plusYears(100));
        keyEntityDO.setContextData(contextData);
        return keyEntityDO;
    }
}
