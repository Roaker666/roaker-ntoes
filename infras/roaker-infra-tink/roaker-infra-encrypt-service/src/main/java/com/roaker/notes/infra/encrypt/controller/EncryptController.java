package com.roaker.notes.infra.encrypt.controller;

import com.roaker.notes.infra.encrypt.EncryptPlatformApi;
import com.roaker.notes.infra.encrypt.dto.DataDecryptDTO;
import com.roaker.notes.infra.encrypt.dto.DataEncryptDTO;
import com.roaker.notes.infra.encrypt.dto.DataItemLine;
import com.roaker.notes.infra.encrypt.enums.DataTypeEnums;
import com.roaker.notes.vo.CommonResult;
import com.roaker.notes.infra.encrypt.service.EncryptPlatformCoreService;
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
