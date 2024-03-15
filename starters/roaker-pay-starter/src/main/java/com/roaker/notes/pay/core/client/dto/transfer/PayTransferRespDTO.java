package com.roaker.notes.pay.core.client.dto.transfer;

import com.roaker.notes.pay.core.enums.transfer.PayTransferStatusRespEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class PayTransferRespDTO implements Serializable {
    /**
     * 转账状态
     * 关联 {@link  PayTransferStatusRespEnum#getCode()} ()}
     */
    private Integer status;
    /**
     * 外部转账单号
     *
     */
    private String outTransferNo;
    /**
     * 支付渠道编号
     */
    private String channelTransferNo;
    /**
     * 支付成功时间
     */
    private LocalDateTime successTime;
    /**
     * 原始的返回结果
     */
    private Object rawData;
    /**
     * 调用渠道的错误码
     */
    private String channelErrorCode;
    /**
     * 调用渠道报错时，错误信息
     */
    private String channelErrorMsg;
    /**
     * 创建【WAITING】状态的转账返回
     */
    public static PayTransferRespDTO waitingOf(String channelTransferNo,
                                               String outTransferNo, Object rawData) {
        PayTransferRespDTO respDTO = new PayTransferRespDTO();
        respDTO.status = PayTransferStatusRespEnum.WAITING.getCode();
        respDTO.channelTransferNo = channelTransferNo;
        respDTO.outTransferNo = outTransferNo;
        respDTO.rawData = rawData;
        return respDTO;
    }

    /**
     * 创建【IN_PROGRESS】状态的转账返回
     */
    public static PayTransferRespDTO dealingOf(String channelTransferNo,
                                               String outTransferNo, Object rawData) {
        PayTransferRespDTO respDTO = new PayTransferRespDTO();
        respDTO.status = PayTransferStatusRespEnum.IN_PROGRESS.getCode();
        respDTO.channelTransferNo = channelTransferNo;
        respDTO.outTransferNo = outTransferNo;
        respDTO.rawData = rawData;
        return respDTO;
    }

    /**
     * 创建【CLOSED】状态的转账返回
     */
    public static PayTransferRespDTO closedOf(String channelErrorCode, String channelErrorMsg,
                                              String outTransferNo, Object rawData) {
        PayTransferRespDTO respDTO = new PayTransferRespDTO();
        respDTO.status = PayTransferStatusRespEnum.CLOSED.getCode();
        respDTO.channelErrorCode = channelErrorCode;
        respDTO.channelErrorMsg = channelErrorMsg;
        // 相对通用的字段
        respDTO.outTransferNo = outTransferNo;
        respDTO.rawData = rawData;
        return respDTO;
    }

    /**
     * 创建【SUCCESS】状态的转账返回
     */
    public static PayTransferRespDTO successOf(String channelTransferNo, LocalDateTime successTime,
                                               String outTransferNo, Object rawData) {
        PayTransferRespDTO respDTO = new PayTransferRespDTO();
        respDTO.status = PayTransferStatusRespEnum.SUCCESS.getCode();
        respDTO.channelTransferNo = channelTransferNo;
        respDTO.successTime = successTime;
        // 相对通用的字段
        respDTO.outTransferNo = outTransferNo;
        respDTO.rawData = rawData;
        return respDTO;
    }
}
