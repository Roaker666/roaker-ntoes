package com.roaker.notes.uc.api.encrypt;

import com.roaker.notes.exception.ServerException;
import com.roaker.notes.exception.enums.GlobalErrorCodeConstants;
import com.roaker.notes.uc.dto.encrypt.DataDecryptDTO;
import com.roaker.notes.uc.dto.encrypt.DataEncryptDTO;
import com.roaker.notes.uc.dto.encrypt.DataItemLine;
import com.roaker.notes.vo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
@Component
public class EncryptPlatformApiFallback implements FallbackFactory<EncryptPlatformApi> {
    @Override
    public EncryptPlatformApi create(Throwable throwable) {
        return new EncryptPlatformApi() {

            @Override
            public CommonResult<List<String>> dataEncrypt(DataEncryptDTO dataEncryptDTO) {
                log.info("Encrypt Platform client initiate fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.ENCRYPT_SERVER_ERROR);
            }

            @Override
            public CommonResult<List<String>> dataDecrypt(DataDecryptDTO dataDecryptDTO) {
                log.info("Encrypt Platform client initiate fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.ENCRYPT_SERVER_ERROR);
            }

            @Override
            public CommonResult<String> encryptPassword(DataItemLine dataItemLine) {
                log.info("Encrypt Platform client initiate fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.ENCRYPT_SERVER_ERROR);
            }

            @Override
            public CommonResult<String> encryptPin(DataItemLine dataItemLine) {
                log.info("Encrypt Platform client initiate fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.ENCRYPT_SERVER_ERROR);
            }

            @Override
            public CommonResult<String> decryptPassword(DataItemLine dataItemLine) {
                log.info("Encrypt Platform client initiate fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.ENCRYPT_SERVER_ERROR);
            }

            @Override
            public CommonResult<String> decryptPin(DataItemLine dataItemLine) {
                log.info("Encrypt Platform client initiate fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.ENCRYPT_SERVER_ERROR);
            }
        };
    }
}
