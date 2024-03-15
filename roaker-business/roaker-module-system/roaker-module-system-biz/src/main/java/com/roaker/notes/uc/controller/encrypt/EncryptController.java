package com.roaker.notes.uc.controller.encrypt;

import com.roaker.notes.uc.api.encrypt.EncryptPlatformApi;
import com.roaker.notes.uc.dto.encrypt.DataDecryptDTO;
import com.roaker.notes.uc.dto.encrypt.DataEncryptDTO;
import com.roaker.notes.uc.dto.encrypt.DataItemLine;
import com.roaker.notes.uc.enums.encrypt.DataTypeEnums;
import com.roaker.notes.uc.service.encrypt.EncryptPlatformCoreService;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lei.rao
 * @since 1.0
 */
@RestController
@Tag(name = "加密平台中心 —— encrypt API")
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/")
public class EncryptController implements EncryptPlatformApi {
    private final EncryptPlatformCoreService encryptPlatformCoreService;

    @Override
    public CommonResult<List<String>> dataEncrypt(DataEncryptDTO dataEncryptDTO) {
        return CommonResult.success(dataEncryptDTO.getDataList().stream()
                .map(dataItemLine ->
                        encryptPlatformCoreService.dataEncrypt(
                                dataItemLine.getData(),
                                dataItemLine.getKeyId(),
                                dataEncryptDTO.getSysCode(),
                                DataTypeEnums.fromCode(dataItemLine.getDataType()),
                                dataItemLine.getRandomKey()))
                .collect(Collectors.toList()));
    }

    @Override
    public CommonResult<List<String>> dataDecrypt(DataDecryptDTO dataDecryptDTO) {
        return CommonResult.success(dataDecryptDTO.getEncryptList().stream()
                .map(dataItemLine ->
                        encryptPlatformCoreService.dataDecrypt(dataItemLine.getData()))
                .collect(Collectors.toList()));
    }

    @Override
    public CommonResult<String> encryptPassword(DataItemLine dataItemLine) {
        return CommonResult.success(
                encryptPlatformCoreService.passwordEncrypt(dataItemLine.getData(), dataItemLine.getRandomKey(), dataItemLine.getKeyId()));
    }

    @Override
    public CommonResult<String> encryptPin(DataItemLine dataItemLine) {
        return CommonResult.success(
                encryptPlatformCoreService.pinEncrypt(dataItemLine.getData(), dataItemLine.getRandomKey(), dataItemLine.getKeyId()));
    }

    @Override
    public CommonResult<String> decryptPassword(DataItemLine dataItemLine) {
        return CommonResult.success(encryptPlatformCoreService.pinDecrypt(dataItemLine.getData()));
    }

    @Override
    public CommonResult<String> decryptPin(DataItemLine dataItemLine) {
        return CommonResult.success(encryptPlatformCoreService.passwordDecrypt(dataItemLine.getData()));
    }
}
