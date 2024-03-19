package com.roaker.notes.pay.api.order;

import com.roaker.notes.pay.api.core.order.PayOrderApi;
import com.roaker.notes.pay.api.core.order.dto.PayOrderCreateReqDTO;
import com.roaker.notes.pay.api.core.order.dto.PayOrderRespDTO;
import com.roaker.notes.pay.converter.order.PayOrderConvert;
import com.roaker.notes.pay.dal.dataobject.order.PayOrderDO;
import com.roaker.notes.pay.service.order.PayOrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 支付单 API 实现类
 *
 */
@Service
public class PayOrderApiImpl implements PayOrderApi {

    @Resource
    private PayOrderService payOrderService;

    @Override
    public Long createOrder(PayOrderCreateReqDTO reqDTO) {
        return payOrderService.createOrder(reqDTO);
    }

    @Override
    public PayOrderRespDTO getOrder(Long id) {
        PayOrderDO order = payOrderService.getOrder(id);
        return PayOrderConvert.INSTANCE.convert2(order);
    }

    @Override
    public void updatePayOrderPrice(Long id, Integer payPrice) {
        payOrderService.updatePayOrderPrice(id, payPrice);
    }

}
