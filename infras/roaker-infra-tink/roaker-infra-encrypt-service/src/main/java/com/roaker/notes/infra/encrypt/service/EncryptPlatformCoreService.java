package com.roaker.notes.infra.encrypt.service;

import com.roaker.notes.infra.encrypt.enums.DataTypeEnums;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface EncryptPlatformCoreService {
    String dataEncrypt(String plainText, Long id, String sysCode, DataTypeEnums dataType, String contextData);

    String dataDecrypt(String cipherText);

    String passwordEncrypt(String plainText, String contextData, Long id);

    String pinEncrypt(String plainText, String contextData, Long id);


    String passwordDecrypt(String cipherText);

    String pinDecrypt(String cipherText);
}
