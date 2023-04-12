package com.roaker.notes.infra.encrypt;

import com.roaker.notes.commons.constants.ApplicationNameConstants;
import com.roaker.notes.infra.encrypt.dto.DataDecryptDTO;
import com.roaker.notes.infra.encrypt.dto.DataEncryptDTO;
import com.roaker.notes.infra.encrypt.dto.DataItemLine;
import com.roaker.notes.vo.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@FeignClient(name = ApplicationNameConstants.ENCRYPT_NAME, fallbackFactory = EncryptPlatformApiFallback.class, dismiss404 = true)
public interface EncryptPlatformApi {
    @PostMapping("/encrypt/inner-call/data-encrypt")
    CommonResult<List<String>> dataEncrypt(@RequestBody DataEncryptDTO dataEncryptDTO);

    @PostMapping("/encrypt/inner-call/data-decrypt")
    CommonResult<List<String>> dataDecrypt(@RequestBody DataDecryptDTO dataDecryptDTO);

    @PostMapping("/encrypt/inner-call/encrypt-password")
    CommonResult<String> encryptPassword(@RequestBody DataItemLine dataItemLine);

    @PostMapping("/encrypt/inner-call/encrypt-pin")
    CommonResult<String> encryptPin(@RequestBody DataItemLine dataItemLine);

    @PostMapping("/encrypt/inner-call/decrypt-password")
    CommonResult<String> decryptPassword(@RequestBody DataItemLine dataItemLine);

    @PostMapping("/encrypt/inner-call/decrypt-pin")
    CommonResult<String> decryptPin(@RequestBody DataItemLine dataItemLine);
}
