package com.roaker.notes.infra.encrypt.service;

import com.roaker.notes.infra.encrypt.dal.dataobject.KeyEntityDO;
import com.roaker.notes.infra.encrypt.enums.DataTypeEnums;
import com.roaker.notes.infra.encrypt.enums.KeyTypeEnums;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface KeyGenerateService {

    KeyEntityDO generateNewKey(String sysCode, DataTypeEnums dataType, KeyTypeEnums keyType, String contextData);


    KeyEntityDO export(final KeyEntityDO keyEntityDO);
}
