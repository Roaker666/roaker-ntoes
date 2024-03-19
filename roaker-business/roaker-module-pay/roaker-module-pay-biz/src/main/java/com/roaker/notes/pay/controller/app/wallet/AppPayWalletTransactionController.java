package com.roaker.notes.pay.controller.app.wallet;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.BeanUtils;
import com.roaker.notes.enums.UserTypeEnum;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import com.roaker.notes.pay.service.wallet.PayWalletTransactionService;
import com.roaker.notes.pay.vo.wallet.transaction.AppPayWalletTransactionPageReqVO;
import com.roaker.notes.pay.vo.wallet.transaction.AppPayWalletTransactionRespVO;
import com.roaker.notes.pay.vo.wallet.transaction.AppPayWalletTransactionSummaryRespVO;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

import static com.roaker.notes.commons.utils.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import static com.roaker.notes.commons.web.util.WebFrameworkUtils.getLoginUserId;
import static com.roaker.notes.vo.CommonResult.success;

@Tag(name = "用户 APP - 钱包余额明细")
@RestController
@RequestMapping("/pay/wallet-transaction")
@Validated
@Slf4j
public class AppPayWalletTransactionController {

    @Resource
    private PayWalletTransactionService payWalletTransactionService;

    @GetMapping("/page")
    @Operation(summary = "获得钱包流水分页")
    public CommonResult<PageResult<AppPayWalletTransactionRespVO>> getWalletTransactionPage(
            @Valid AppPayWalletTransactionPageReqVO pageReqVO) {
        PageResult<PayWalletTransactionDO> pageResult = payWalletTransactionService.getWalletTransactionPage(
                getLoginUserId(), UserTypeEnum.MEMBER.getCode(), pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppPayWalletTransactionRespVO.class));
    }

    @GetMapping("/get-summary")
    @Operation(summary = "获得钱包流水统计")
    @Parameter(name = "times", description = "时间段", required = true)
    public CommonResult<AppPayWalletTransactionSummaryRespVO> getWalletTransactionSummary(
            @RequestParam("createTime") @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND) LocalDateTime[] createTime) {
        AppPayWalletTransactionSummaryRespVO summary = payWalletTransactionService.getWalletTransactionSummary(
                getLoginUserId(), UserTypeEnum.MEMBER.getCode(), createTime);
        return success(summary);
    }

}
