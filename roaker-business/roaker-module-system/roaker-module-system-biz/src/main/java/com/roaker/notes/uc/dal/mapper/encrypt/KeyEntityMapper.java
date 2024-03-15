package com.roaker.notes.uc.dal.mapper.encrypt;

import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.exception.ServerException;
import com.roaker.notes.exception.enums.GlobalErrorCodeConstants;
import com.roaker.notes.uc.dal.dataobject.encrypt.KeyEntityDO;
import com.roaker.notes.uc.enums.encrypt.DataTypeEnums;
import com.roaker.notes.uc.enums.encrypt.KeyTypeEnums;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

/**
 * @author lei.rao
 * @since 1.0
 */
@Mapper
public interface KeyEntityMapper extends BaseMapperX<KeyEntityDO> {
    default KeyEntityDO selectByAnyType(Long id, String sysCode, KeyTypeEnums keyType, DataTypeEnums dataType) {
        KeyEntityDO keyEntityDO = null;
        if (id != null) {
            keyEntityDO = selectById(id);
        } else {
            keyEntityDO = selectOne(new LambdaQueryWrapperX<KeyEntityDO>()
                    .eqIfPresent(KeyEntityDO::getDataType, dataType)
                    .eqIfPresent(KeyEntityDO::getKeyType, keyType)
                    .eqIfPresent(KeyEntityDO::getSysCode, sysCode)
                    .eq(KeyEntityDO::getStatus, CommonStatusEnum.ENABLE)
                    .gt(KeyEntityDO::getExpireTime, LocalDateTime.now())
                    .orderByDesc(KeyEntityDO::getCreateTime));
        }
        if (keyEntityDO == null) {
            throw new ServerException(GlobalErrorCodeConstants.DATA_NOT_FOUND);
        }

        return keyEntityDO;
    }
}
