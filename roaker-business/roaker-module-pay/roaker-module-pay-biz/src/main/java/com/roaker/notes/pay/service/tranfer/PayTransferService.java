package com.roaker.notes.pay.service.tranfer;


import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.pay.api.core.transfer.dto.PayTransferCreateReqDTO;
import com.roaker.notes.pay.dal.dataobject.tranfer.PayTransferDO;
import com.roaker.notes.pay.vo.tranfer.PayTransferCreateReqVO;
import com.roaker.notes.pay.vo.tranfer.PayTransferPageReqVO;
import jakarta.validation.Valid;

/**
 * 转账 Service 接口
 *
 */
public interface PayTransferService {

    /**
     * 创建转账单，并发起转账
     *
     * 此时，会发起转账渠道的调用
     *
     * @param reqVO 请求
     * @param userIp 用户 ip
     * @return 渠道的返回结果
     */
    PayTransferDO createTransfer(@Valid PayTransferCreateReqVO reqVO, String userIp);

    /**
     * 创建转账单，并发起转账
     *
     * @param reqDTO 创建请求
     * @return 转账单编号
     */
    Long createTransfer(@Valid PayTransferCreateReqDTO reqDTO);

    /**
     * 获取转账单
     * @param id 转账单编号
     */
    PayTransferDO getTransfer(Long id);

    /**
     * 获得转账单分页
     *
     * @param pageReqVO 分页查询
     * @return 转账单分页
     */
    PageResult<PayTransferDO> getTransferPage(PayTransferPageReqVO pageReqVO);

    /**
     * 同步渠道转账单状态
     *
     * @return 同步到状态的转账数量，包括转账成功、转账失败、转账中的
     */
    int syncTransfer();
}
