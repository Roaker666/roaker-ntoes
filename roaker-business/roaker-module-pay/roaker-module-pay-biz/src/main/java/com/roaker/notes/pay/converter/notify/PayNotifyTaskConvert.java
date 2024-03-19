package com.roaker.notes.pay.converter.notify;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.RoakerMapUtils;
import com.roaker.notes.pay.dal.dataobject.app.PayAppDO;
import com.roaker.notes.pay.dal.dataobject.notify.PayNotifyLogDO;
import com.roaker.notes.pay.dal.dataobject.notify.PayNotifyTaskDO;
import com.roaker.notes.pay.vo.notify.PayNotifyTaskDetailRespVO;
import com.roaker.notes.pay.vo.notify.PayNotifyTaskRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * 支付通知 Convert
 *
 */
@Mapper
public interface PayNotifyTaskConvert {

    PayNotifyTaskConvert INSTANCE = Mappers.getMapper(PayNotifyTaskConvert.class);

    PayNotifyTaskRespVO convert(PayNotifyTaskDO bean);

    default PageResult<PayNotifyTaskRespVO> convertPage(PageResult<PayNotifyTaskDO> page, Map<Long, PayAppDO> appMap){
        PageResult<PayNotifyTaskRespVO> result = convertPage(page);
        result.getList().forEach(order -> RoakerMapUtils.findAndThen(appMap, order.getAppId(), app -> order.setAppName(app.getName())));
        return result;
    }
    PageResult<PayNotifyTaskRespVO> convertPage(PageResult<PayNotifyTaskDO> page);

    default PayNotifyTaskDetailRespVO convert(PayNotifyTaskDO task, PayAppDO app, List<PayNotifyLogDO> logs) {
        PayNotifyTaskDetailRespVO respVO = convert(task, logs);
        if (app != null) {
            respVO.setAppName(app.getName());
        }
        return respVO;
    }
    PayNotifyTaskDetailRespVO convert(PayNotifyTaskDO task, List<PayNotifyLogDO> logs);
}
