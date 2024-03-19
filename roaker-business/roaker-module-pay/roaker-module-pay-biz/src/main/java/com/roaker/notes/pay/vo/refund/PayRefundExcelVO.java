package com.roaker.notes.pay.vo.refund;

import com.alibaba.excel.annotation.ExcelProperty;
import com.roaker.notes.commons.excel.annoatations.DictFormat;
import com.roaker.notes.commons.excel.convert.DictConvert;
import com.roaker.notes.commons.excel.convert.MoneyConvert;
import com.roaker.notes.pay.api.enums.DictTypeConstants;
import com.roaker.notes.pay.api.enums.refund.PayRefundStatusEnum;
import com.roaker.notes.pay.core.enums.channel.PayChannelEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 退款订单 Excel VO
 *
 * @author aquan
 */
@Data
public class PayRefundExcelVO {

    @ExcelProperty("支付退款编号")
    private Long id;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty(value = "支付金额", converter = MoneyConvert.class)
    private Integer payPrice;

    @ExcelProperty(value = "退款金额", converter = MoneyConvert.class)
    private Integer refundPrice;

    @ExcelProperty("商户退款单号")
    private String merchantRefundId;
    @ExcelProperty("退款单号")
    private String no;
    @ExcelProperty("渠道退款单号")
    private String channelRefundNo;

    @ExcelProperty("商户支付单号")
    private String merchantOrderId;
    @ExcelProperty("渠道支付单号")
    private String channelOrderNo;

    @ExcelProperty(value = "退款状态", converter = DictConvert.class)
    @DictFormat(PayRefundStatusEnum.class)
    private Integer status;

    @ExcelProperty(value = "退款渠道", converter = DictConvert.class)
    @DictFormat(PayChannelEnum.class)
    private String channelCode;

    @ExcelProperty("成功时间")
    private LocalDateTime successTime;

    @ExcelProperty(value = "支付应用")
    private String appName;

    @ExcelProperty("退款原因")
    private String reason;

}
