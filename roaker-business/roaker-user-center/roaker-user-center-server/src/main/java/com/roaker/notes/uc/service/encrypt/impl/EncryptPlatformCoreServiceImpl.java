package com.roaker.notes.uc.service.encrypt.impl;

import com.roaker.notes.commons.constants.ApplicationNameConstants;
import com.roaker.notes.exception.ServerException;
import com.roaker.notes.exception.enums.GlobalErrorCodeConstants;
import com.roaker.notes.uc.dal.dataobject.encrypt.KeyEntityDO;
import com.roaker.notes.uc.dal.mapper.encrypt.KeyEntityMapper;
import com.roaker.notes.uc.enums.encrypt.DataTypeEnums;
import com.roaker.notes.uc.enums.encrypt.KeyTypeEnums;
import com.roaker.notes.uc.service.encrypt.EncryptPlatformCoreService;
import com.roaker.notes.uc.service.encrypt.KeyGenerateService;
import com.roaker.notes.uc.utils.TinkUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EncryptPlatformCoreServiceImpl implements EncryptPlatformCoreService {
    private final KeyEntityMapper keyEntityMapper;

    private final KeyGenerateService keyGenerateService;

    @Override
    public String dataEncrypt(String plainText, Long id, String sysCode, DataTypeEnums dataType, String contextData) {
        KeyEntityDO keyEntityDO = keyEntityMapper.selectByAnyType(id, sysCode, null, dataType);
        keyGenerateService.export(keyEntityDO);
        return TinkUtils.encryptByAead(keyEntityDO.getId(), keyEntityDO.getSecretKey(), plainText, StringUtils.defaultString(contextData, keyEntityDO.getContextData()));
    }

    @Override
    public String dataDecrypt(String cipherText) {
        String[] cipherTextSplit = StringUtils.split(cipherText, TinkUtils.SEPARATOR_ENCRYPT);
        if (cipherTextSplit.length != 3) {
            throw new ServerException(GlobalErrorCodeConstants.BAD_REQUEST);
        }
        KeyEntityDO keyEntityDO = keyEntityMapper.selectById(cipherTextSplit[1]);
        if (keyEntityDO == null) {
            throw new ServerException(GlobalErrorCodeConstants.DATA_NOT_FOUND);
        }
        keyGenerateService.export(keyEntityDO);

        return TinkUtils.decryptByAead(keyEntityDO.getSecretKey(), cipherTextSplit[0], cipherTextSplit[2]);
    }

    @Override
    public String passwordEncrypt(String plainText, String contextData, Long id) {
        KeyEntityDO keyEntityDO = keyEntityMapper.selectByAnyType(id, null, KeyTypeEnums.KEY_PW, null);
        keyGenerateService.export(keyEntityDO);
        String encrypt = TinkUtils.encryptByHybrid(keyEntityDO.getId(), keyEntityDO.getPublicKey(), plainText, StringUtils.defaultString(contextData, keyEntityDO.getContextData()));
        return dataEncrypt(encrypt, id, ApplicationNameConstants.ENCRYPT_NAME, DataTypeEnums.PASSWORD, contextData);
    }

    @Override
    public String pinEncrypt(String plainText, String contextData, Long id) {
        KeyEntityDO keyEntityDO = keyEntityMapper.selectByAnyType(id, null, KeyTypeEnums.KEY_PIN, null);
        keyGenerateService.export(keyEntityDO);
        String encrypt = TinkUtils.encryptByHybrid(keyEntityDO.getId(), keyEntityDO.getPublicKey(), plainText, StringUtils.defaultString(contextData, keyEntityDO.getContextData()));
        return dataEncrypt(encrypt, id, ApplicationNameConstants.ENCRYPT_NAME, DataTypeEnums.PIN, contextData);
    }

    @Override
    public String passwordDecrypt(String cipherText) {
        String dataCipherText = dataDecrypt(cipherText);
        return decryptByHybrid(dataCipherText);
    }


    private String decryptByHybrid(String cipherText) {
        String[] cipherTextSplit = StringUtils.split(cipherText, TinkUtils.SP_SEPARATOR_ENCRYPT);
        if (cipherTextSplit.length != 3) {
            throw new ServerException(GlobalErrorCodeConstants.BAD_REQUEST);
        }
        KeyEntityDO keyEntityDO = keyEntityMapper.selectById(cipherTextSplit[1]);
        if (keyEntityDO == null) {
            throw new ServerException(GlobalErrorCodeConstants.DATA_NOT_FOUND);
        }
        keyGenerateService.export(keyEntityDO);
        return TinkUtils.decryptByHybrid(keyEntityDO.getPrivateKey(), cipherTextSplit[0], cipherTextSplit[2]);
    }

    @Override
    public String pinDecrypt(String cipherText) {
        String dataCipherText = dataDecrypt(cipherText);
        return decryptByHybrid(dataCipherText);
    }
}
