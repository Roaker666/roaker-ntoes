package com.roaker.notes.pay.controller.app.wallet;

import cn.hutool.core.collection.CollUtil;
import com.roaker.notes.commons.db.PageParam;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.enums.UserTypeEnum;
import com.roaker.notes.pay.converter.wallet.PayWalletRechargeConvert;
import com.roaker.notes.pay.dal.dataobject.order.PayOrderDO;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import com.roaker.notes.pay.service.order.PayOrderService;
import com.roaker.notes.pay.service.wallet.PayWalletRechargeService;
import com.roaker.notes.pay.vo.wallet.recharge.AppPayWalletRechargeCreateReqVO;
import com.roaker.notes.pay.vo.wallet.recharge.AppPayWalletRechargeCreateRespVO;
import com.roaker.notes.pay.vo.wallet.recharge.AppPayWalletRechargeRespVO;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.roaker.notes.commons.utils.RoakerCollectionUtils.convertList;
import static com.roaker.notes.commons.utils.ServletUtils.getClientIP;
import static com.roaker.notes.commons.web.util.WebFrameworkUtils.*;
import static com.roaker.notes.vo.CommonResult.success;

@Tag(name = "用户 APP - 钱包充值")
@RestController
@RequestMapping("/pay/wallet-recharge")
@Validated
@Slf4j
public class AppPayWalletRechargeController {

    @Resource
    private PayWalletRechargeService walletRechargeService;
    @Resource
    private PayOrderService payOrderService;

    @PostMapping("/create")
    @Operation(summary = "创建钱包充值记录（发起充值）")
    public CommonResult<AppPayWalletRechargeCreateRespVO> createWalletRecharge(
            @Valid @RequestBody AppPayWalletRechargeCreateReqVO reqVO) {
        PayWalletRechargeDO walletRecharge = walletRechargeService.createWalletRecharge(
                getLoginUserId(), getLoginUserType(getRequest()), getClientIP(), reqVO.getChannelId(), reqVO);
        return success(PayWalletRechargeConvert.INSTANCE.convert(walletRecharge));
    }

    @GetMapping("/page")
    @Operation(summary = "获得钱包充值记录分页")
    public CommonResult<PageResult<AppPayWalletRechargeRespVO>> getWalletRechargePage(@Valid PageParam pageReqVO) {
        PageResult<PayWalletRechargeDO> pageResult = walletRechargeService.getWalletRechargePackagePage(
                getLoginUserId(), UserTypeEnum.MEMBER.getCode(), pageReqVO, true);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty(pageResult.getTotal()));
        }
        // 拼接数据
        List<PayOrderDO> payOrderList = payOrderService.getOrderList(
                convertList(pageResult.getList(), PayWalletRechargeDO::getPayOrderId));
        return success(PayWalletRechargeConvert.INSTANCE.convertPage(pageResult, payOrderList));
    }

}
