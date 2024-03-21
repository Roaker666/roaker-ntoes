package com.roaker.notes.pay.vo.order;
import com.alibaba.excel.annotation.ExcelProperty;
import com.roaker.notes.commons.excel.annoatations.DictFormat;
import com.roaker.notes.commons.excel.convert.DictConvert;
import com.roaker.notes.commons.excel.convert.MoneyConvert;
import com.roaker.notes.dynamic.enums.DynamicDictTypeEnums;
import com.roaker.notes.pay.api.enums.DictTypeConstants;
import com.roaker.notes.pay.core.enums.channel.PayChannelEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 支付订单 Excel VO
 *
 * @author aquan
 */
@Data
public class PayOrderExcelVO {

    @ExcelProperty("编号")
    private Long id;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty(value = "支付金额", converter = MoneyConvert.class)
    private Integer price;

    @ExcelProperty(value = "退款金额", converter = MoneyConvert.class)
    private Integer refundPrice;

    @ExcelProperty(value = "手续金额", converter = MoneyConvert.class)
    private Integer channelFeePrice;

    @ExcelProperty("商户单号")
    private String merchantOrderId;

    @ExcelProperty(value = "支付单号")
    private String no;

    @ExcelProperty("渠道单号")
    private String channelOrderNo;

    @ExcelProperty(value = "支付状态", converter = DictConvert.class)
    @DictFormat(DynamicDictTypeEnums.class)
    private DynamicDictTypeEnums status;

    @ExcelProperty(value = "渠道编号名称", converter = DictConvert.class)
    @DictFormat(PayChannelEnum.class)
    private String channelCode;

    @ExcelProperty("订单支付成功时间")
    private LocalDateTime successTime;

    @ExcelProperty("订单失效时间")
    private LocalDateTime expireTime;

    @ExcelProperty(value = "应用名称")
    private String appName;

    @ExcelProperty("商品标题")
    private String subject;

    @ExcelProperty("商品描述")
    private String body;

}
