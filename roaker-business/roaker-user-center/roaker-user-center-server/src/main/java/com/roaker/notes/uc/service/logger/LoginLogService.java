package com.roaker.notes.uc.service.logger;


import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.uc.api.logger.dto.LoginLogCreateReqDTO;
import com.roaker.notes.uc.dal.dataobject.logger.LoginLogDO;
import com.roaker.notes.uc.vo.logger.loginlog.LoginLogPageReqVO;
import jakarta.validation.Valid;

/**
 * 登录日志 Service 接口
 */
public interface LoginLogService {

    /**
     * 获得登录日志分页
     *
     * @param pageReqVO 分页条件
     * @return 登录日志分页
     */
    PageResult<LoginLogDO> getLoginLogPage(LoginLogPageReqVO pageReqVO);

    /**
     * 创建登录日志
     *
     * @param reqDTO 日志信息
     */
    void createLoginLog(@Valid LoginLogCreateReqDTO reqDTO);

}
