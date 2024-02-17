package com.roaker.notes.infra.encrypt.service.impl;

import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.hybrid.HybridKeyTemplates;
import com.roaker.notes.exception.ServerException;
import com.roaker.notes.exception.enums.GlobalErrorCodeConstants;
import com.roaker.notes.infra.encrypt.TinkUtils;
import com.roaker.notes.infra.encrypt.dal.dataobject.KeyEntityDO;
import com.roaker.notes.infra.encrypt.dal.mapper.KeyEntityMapper;
import com.roaker.notes.infra.encrypt.enums.DataTypeEnums;
import com.roaker.notes.infra.encrypt.enums.KeyTypeEnums;
import com.roaker.notes.infra.encrypt.service.KeyGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

/**
 * @author lei.rao
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KeyGenerateServiceImpl implements KeyGenerateService {
    private final KeyEntityMapper keyEntityMapper;
    @Override
    public KeyEntityDO generateNewKey(String sysCode, DataTypeEnums dataType, KeyTypeEnums keyType, String contextData) {
        KeyEntityDO keyEntityDO = KeyEntityDO.from(sysCode, dataType, keyType, contextData);
        try {
            switch (keyType) {
                case KEY_DATA:
                    keyEntityDO.setSecretKey(TinkUtils.getSecretKey(AeadKeyTemplates.AES256_CTR_HMAC_SHA256));
                    break;
                case KEY_PIN:
                case KEY_PW:
                    String[] keys = TinkUtils.getKeys(HybridKeyTemplates.ECIES_P256_HKDF_HMAC_SHA256_AES128_CTR_HMAC_SHA256);
                    keyEntityDO.setPrivateKey(keys[0]);
                    keyEntityDO.setPublicKey(keys[1]);
                    break;

            }
        } catch (Exception e) {
            throw new ServerException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR);
        }

        keyEntityMapper.insert(keyEntityDO);
        return keyEntityDO;
    }

    @Override
    public KeyEntityDO export(final KeyEntityDO keyEntityDO) {
        KeyTypeEnums keyTypeEnum = keyEntityDO.getKeyType();
        switch (keyTypeEnum) {
            case KEY_DATA:
                keyEntityDO.setSecretKey(new String(Base64Utils.decodeFromString(keyEntityDO.getSecretKey())));
                break;
            case KEY_PIN:
            case KEY_PW:
                keyEntityDO.setPrivateKey(new String(Base64Utils.decodeFromString(keyEntityDO.getPrivateKey())));
                keyEntityDO.setPublicKey(new String(Base64Utils.decodeFromString(keyEntityDO.getPublicKey())));
                break;
        }
        return keyEntityDO;
    }
}
