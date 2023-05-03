package com.roaker.notes.ac.controller.auth;

import com.roaker.notes.ac.controller.auth.vo.AuthSmsSendReqVO;
import com.roaker.notes.ac.service.auth.AdminAuthService;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理后台 - 认证")
@RestController
@RequestMapping("/auth")
@Validated
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AdminAuthService authService;
    @PostMapping("/send-sms")
    @Operation(summary = "发送手机验证码")
    public CommonResult<Boolean> sendSmsCode(@RequestBody @Valid AuthSmsSendReqVO reqVO) {
        authService.sendSmsCode(reqVO);
        return CommonResult.success(true);
    }
}
