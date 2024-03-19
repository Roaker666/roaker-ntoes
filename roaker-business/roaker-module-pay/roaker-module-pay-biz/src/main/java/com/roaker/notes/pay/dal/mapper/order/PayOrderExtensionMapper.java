package com.roaker.notes.pay.dal.mapper.order;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.dynamic.enums.CommonEnum;
import com.roaker.notes.pay.api.enums.order.PayOrderStatusEnum;
import com.roaker.notes.pay.dal.dataobject.order.PayOrderExtensionDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PayOrderExtensionMapper extends BaseMapperX<PayOrderExtensionDO> {

    default PayOrderExtensionDO selectByNo(String no) {
        return selectOne(PayOrderExtensionDO::getNo, no);
    }

    default int updateByIdAndStatus(Long id, Integer status, PayOrderExtensionDO update) {
        return update(update, new LambdaQueryWrapper<PayOrderExtensionDO>()
                .eq(PayOrderExtensionDO::getId, id).eq(PayOrderExtensionDO::getPayStatus, CommonEnum.of(status, PayOrderStatusEnum.class)));
    }

    default List<PayOrderExtensionDO> selectListByOrderId(Long orderId) {
        return selectList(PayOrderExtensionDO::getOrderId, orderId);
    }

    default List<PayOrderExtensionDO> selectListByStatusAndCreateTimeGe(Integer status, LocalDateTime minCreateTime) {
        return selectList(new LambdaQueryWrapper<PayOrderExtensionDO>()
                .eq(PayOrderExtensionDO::getPayStatus, CommonEnum.of(status, PayOrderStatusEnum.class))
                .ge(PayOrderExtensionDO::getCreateTime, minCreateTime));
    }

}
