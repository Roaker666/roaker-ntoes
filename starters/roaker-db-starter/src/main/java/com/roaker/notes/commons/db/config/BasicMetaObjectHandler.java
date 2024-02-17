package com.roaker.notes.commons.db.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.roaker.notes.commons.db.properties.MybatisPlusAutoFillProperties;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author lei.rao
 * @since 1.0
 */

public class BasicMetaObjectHandler implements MetaObjectHandler {
    private static final String defaultUser = "system";

    private MybatisPlusAutoFillProperties autoFillProperties;

    public BasicMetaObjectHandler(MybatisPlusAutoFillProperties autoFillProperties) {
        this.autoFillProperties = autoFillProperties;
    }

    @Override
    public boolean openInsertFill() {
        return autoFillProperties.getEnableInsertFill();
    }

    @Override
    public boolean openUpdateFill() {
        return autoFillProperties.getEnableUpdateFill();
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = getFieldValByName(autoFillProperties.getCreateTimeField(), metaObject);
        Object updateTime = getFieldValByName(autoFillProperties.getUpdateTimeField(), metaObject);
        Object creator = getFieldValByName(autoFillProperties.getCreatorField(), metaObject);
        Object modifier = getFieldValByName(autoFillProperties.getModifierField(), metaObject);
        Object deleted = getFieldValByName(autoFillProperties.getDeletedField(), metaObject);
        Object version = getFieldValByName(autoFillProperties.getVersionFiled(), metaObject);
        if (createTime == null || updateTime == null) {
            LocalDateTime dateTime = LocalDateTime.now();
            if (createTime == null) {
                setFieldValByName(autoFillProperties.getCreateTimeField(), dateTime, metaObject);
            }
            if (updateTime == null) {
                setFieldValByName(autoFillProperties.getUpdateTimeField(), dateTime, metaObject);
            }
        }
        if (creator == null || modifier == null) {
            if (creator == null) {
                setFieldValByName(autoFillProperties.getCreatorField(), defaultUser, metaObject);
            }
            if (modifier == null) {
                setFieldValByName(autoFillProperties.getModifierField(), defaultUser, metaObject);
            }
        }

        if (deleted == null) {
            setFieldValByName(autoFillProperties.getDeletedField(), Boolean.FALSE, metaObject);
        }

        if (version == null) {
            setFieldValByName(autoFillProperties.getVersionFiled(), 1, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName(autoFillProperties.getUpdateTimeField(), new Date(), metaObject);
        Object modifier = getFieldValByName(autoFillProperties.getModifierField(), metaObject);
        if (modifier == null) {
            setFieldValByName(autoFillProperties.getModifierField(), defaultUser, metaObject);
        }
    }
}
