package com.roaker.notes.uc.service.encrypt;


import com.roaker.notes.uc.dal.dataobject.encrypt.KeyEntityDO;
import com.roaker.notes.uc.enums.encrypt.DataTypeEnums;
import com.roaker.notes.uc.enums.encrypt.KeyTypeEnums;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface KeyGenerateService {

    KeyEntityDO generateNewKey(String sysCode, DataTypeEnums dataType, KeyTypeEnums keyType, String contextData);


    KeyEntityDO export(final KeyEntityDO keyEntityDO);
}
