package com.roaker.notes.infra.encrypt.controller;

import com.roaker.notes.infra.encrypt.enums.DataTypeEnums;
import com.roaker.notes.infra.encrypt.enums.KeyTypeEnums;
import com.roaker.notes.infra.encrypt.vo.KeyGenerateRequestVO;
import com.roaker.notes.vo.CommonResult;
import com.roaker.notes.infra.encrypt.service.KeyGenerateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lei.rao
 * @since 1.0
 */
@RestController
@Tag(name = "加密平台中心 —— key generator API")
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/encrypt")
public class KeyGeneratorController {
    private final KeyGenerateService keyGenerateService;

    @PostMapping("/v1/generateKey")
    @Operation(summary = "密钥生成API(v1)")
    public CommonResult<Long> generateKey(@RequestBody KeyGenerateRequestVO keyGenerateRequestVO) {
        return CommonResult.success(keyGenerateService.generateNewKey(
                        keyGenerateRequestVO.getSysCode(),
                        DataTypeEnums.fromCode(keyGenerateRequestVO.getDataType()),
                        KeyTypeEnums.fromCode(keyGenerateRequestVO.getKeyType()),
                        keyGenerateRequestVO.getRandomKey())
                .getId());
    }
}
