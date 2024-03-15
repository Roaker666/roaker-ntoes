package com.roaker.notes.uc.service.logger.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.BeanUtils;
import com.roaker.notes.commons.utils.StrUtils;
import com.roaker.notes.uc.api.logger.dto.OperateLogCreateReqDTO;
import com.roaker.notes.uc.api.logger.dto.OperateLogV2CreateReqDTO;
import com.roaker.notes.uc.api.logger.dto.OperateLogV2PageReqDTO;
import com.roaker.notes.uc.dal.dataobject.logger.OperateLogDO;
import com.roaker.notes.uc.dal.dataobject.logger.OperateLogV2DO;
import com.roaker.notes.uc.dal.dataobject.user.AdminUserInfoDO;
import com.roaker.notes.uc.dal.mapper.logger.OperateLogMapper;
import com.roaker.notes.uc.dal.mapper.logger.OperateLogV2Mapper;
import com.roaker.notes.uc.service.logger.OperateLogService;
import com.roaker.notes.uc.service.user.AdminUserService;
import com.roaker.notes.uc.vo.logger.operatelog.OperateLogPageReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.Collection;

import static com.roaker.notes.commons.utils.RoakerCollectionUtils.convertSet;
import static com.roaker.notes.uc.dal.dataobject.logger.OperateLogDO.JAVA_METHOD_ARGS_MAX_LENGTH;
import static com.roaker.notes.uc.dal.dataobject.logger.OperateLogDO.RESULT_MAX_LENGTH;

/**
 * 操作日志 Service 实现类
 *
 * @author Roaker源码
 */
@Service
@Validated
@Slf4j
public class OperateLogServiceImpl implements OperateLogService {

    @Resource
    private OperateLogMapper operateLogMapper;
    @Resource
    private OperateLogV2Mapper operateLogV2Mapper;

    @Resource
    private AdminUserService userService;

    @Override
    public void createOperateLog(OperateLogCreateReqDTO createReqDTO) {
        OperateLogDO log = BeanUtils.toBean(createReqDTO, OperateLogDO.class);
        log.setJavaMethodArgs(StrUtils.maxLength(log.getJavaMethodArgs(), JAVA_METHOD_ARGS_MAX_LENGTH));
        log.setResultData(StrUtils.maxLength(log.getResultData(), RESULT_MAX_LENGTH));
        operateLogMapper.insert(log);
    }

    @Override
    public PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqVO pageReqVO) {
        // 处理基于用户昵称的查询
        Collection<String> userIds = null;
        if (StrUtil.isNotEmpty(pageReqVO.getUserNickname())) {
            userIds = convertSet(userService.getUserListByNickname(pageReqVO.getUserNickname()), AdminUserInfoDO::getUid);
            if (CollUtil.isEmpty(userIds)) {
                return PageResult.empty();
            }
        }
        // 查询分页
        return operateLogMapper.selectPage(pageReqVO, userIds);
    }

    // ======================= LOG V2 =======================

    @Override
    public void createOperateLogV2(OperateLogV2CreateReqDTO createReqDTO) {
        OperateLogV2DO log = BeanUtils.toBean(createReqDTO, OperateLogV2DO.class);
        operateLogV2Mapper.insert(log);
    }

    @Override
    public PageResult<OperateLogV2DO> getOperateLogPage(OperateLogV2PageReqDTO pageReqDTO) {
        return operateLogV2Mapper.selectPage(pageReqDTO);
    }

}
