package com.roaker.notes.pay.controller.app.order;

import com.google.common.collect.Maps;
import com.roaker.notes.pay.converter.order.PayOrderConvert;
import com.roaker.notes.pay.core.enums.channel.PayChannelEnum;
import com.roaker.notes.pay.framework.pay.core.WalletPayClient;
import com.roaker.notes.pay.service.order.PayOrderService;
import com.roaker.notes.pay.vo.order.AppPayOrderSubmitReqVO;
import com.roaker.notes.pay.vo.order.AppPayOrderSubmitRespVO;
import com.roaker.notes.pay.vo.order.PayOrderRespVO;
import com.roaker.notes.pay.vo.order.PayOrderSubmitRespVO;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

import static com.roaker.notes.commons.utils.ServletUtils.getClientIP;
import static com.roaker.notes.commons.web.util.WebFrameworkUtils.*;
import static com.roaker.notes.vo.CommonResult.success;

@Tag(name = "用户 APP - 支付订单")
@RestController
@RequestMapping("/pay/order")
@Validated
@Slf4j
public class AppPayOrderController {

    @Resource
    private PayOrderService payOrderService;

    @GetMapping("/get")
    @Operation(summary = "获得支付订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<PayOrderRespVO> getOrder(@RequestParam("id") Long id) {
        return success(PayOrderConvert.INSTANCE.convert(payOrderService.getOrder(id)));
    }

    @PostMapping("/submit")
    @Operation(summary = "提交支付订单")
    public CommonResult<AppPayOrderSubmitRespVO> submitPayOrder(@RequestBody AppPayOrderSubmitReqVO reqVO) {
        // 1. 钱包支付事，需要额外传 user_id 和 user_type
        if (Objects.equals(reqVO.getChannelCode(), PayChannelEnum.WALLET.getChannelCode())) {
            Map<String, String> channelExtras = reqVO.getChannelExtras() == null ?
                    Maps.newHashMapWithExpectedSize(2) : reqVO.getChannelExtras();
            channelExtras.put(WalletPayClient.USER_ID_KEY, getLoginUserId());
            channelExtras.put(WalletPayClient.USER_TYPE_KEY, String.valueOf(getLoginUserType(getRequest())));
            reqVO.setChannelExtras(channelExtras);
        }

        // 2. 提交支付
        PayOrderSubmitRespVO respVO = payOrderService.submitOrder(reqVO, getClientIP());
        return success(PayOrderConvert.INSTANCE.convert3(respVO));
    }

}
