package com.roaker.notes.pay.controller.app.wallet;

import com.roaker.notes.enums.UserTypeEnum;
import com.roaker.notes.pay.converter.wallet.PayWalletConvert;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletDO;
import com.roaker.notes.pay.service.wallet.PayWalletService;
import com.roaker.notes.pay.vo.wallet.wallet.AppPayWalletRespVO;
import com.roaker.notes.security.core.annotations.PreAuthenticated;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.roaker.notes.commons.web.util.WebFrameworkUtils.getLoginUserId;
import static com.roaker.notes.vo.CommonResult.success;

@Tag(name = "用户 APP - 钱包")
@RestController
@RequestMapping("/pay/wallet")
@Validated
@Slf4j
public class AppPayWalletController {

    @Resource
    private PayWalletService payWalletService;

    @GetMapping("/get")
    @Operation(summary = "获取钱包")
    @PreAuthenticated
    public CommonResult<AppPayWalletRespVO> getPayWallet() {
        PayWalletDO wallet = payWalletService.getOrCreateWallet(getLoginUserId(), UserTypeEnum.MEMBER.getCode());
        return success(PayWalletConvert.INSTANCE.convert(wallet));
    }

}
