package com.roaker.notes.uc.service.logger.impl;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.BeanUtils;
import com.roaker.notes.uc.api.logger.dto.LoginLogCreateReqDTO;
import com.roaker.notes.uc.dal.dataobject.logger.LoginLogDO;
import com.roaker.notes.uc.dal.mapper.logger.LoginLogMapper;
import com.roaker.notes.uc.service.logger.LoginLogService;
import com.roaker.notes.uc.vo.logger.loginlog.LoginLogPageReqVO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

/**
 * 登录日志 Service 实现
 */
@Service
@Validated
public class LoginLogServiceImpl implements LoginLogService {

    @Resource
    private LoginLogMapper loginLogMapper;

    @Override
    public PageResult<LoginLogDO> getLoginLogPage(LoginLogPageReqVO pageReqVO) {
        return loginLogMapper.selectPage(pageReqVO);
    }

    @Override
    public void createLoginLog(LoginLogCreateReqDTO reqDTO) {
        LoginLogDO loginLog = BeanUtils.toBean(reqDTO, LoginLogDO.class);
        loginLogMapper.insert(loginLog);
    }

}
