package com.roaker.notes.uc.service.logger;


import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.uc.api.logger.dto.OperateLogCreateReqDTO;
import com.roaker.notes.uc.api.logger.dto.OperateLogV2CreateReqDTO;
import com.roaker.notes.uc.api.logger.dto.OperateLogV2PageReqDTO;
import com.roaker.notes.uc.dal.dataobject.logger.OperateLogDO;
import com.roaker.notes.uc.dal.dataobject.logger.OperateLogV2DO;
import com.roaker.notes.uc.vo.logger.operatelog.OperateLogPageReqVO;

/**
 * 操作日志 Service 接口
 *
 * @author Roaker源码
 */
public interface OperateLogService {

    /**
     * 记录操作日志
     *
     * @param createReqDTO 操作日志请求
     */
    void createOperateLog(OperateLogCreateReqDTO createReqDTO);

    /**
     * 获得操作日志分页列表
     *
     * @param pageReqVO 分页条件
     * @return 操作日志分页列表
     */
    PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqVO pageReqVO);

    // ======================= LOG V2 =======================

    /**
     * 记录操作日志 V2
     *
     * @param createReqDTO 创建请求
     */
    void createOperateLogV2(OperateLogV2CreateReqDTO createReqDTO);

    /**
     * 获得操作日志分页列表
     *
     * @param pageReqVO 分页条件
     * @return 操作日志分页列表
     */
    PageResult<OperateLogV2DO> getOperateLogPage(OperateLogV2PageReqDTO pageReqVO);

}
