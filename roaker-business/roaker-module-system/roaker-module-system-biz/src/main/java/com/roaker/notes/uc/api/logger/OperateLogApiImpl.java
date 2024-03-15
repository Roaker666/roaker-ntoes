package com.roaker.notes.uc.api.logger;

import cn.hutool.core.collection.CollUtil;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.uc.api.logger.dto.OperateLogCreateReqDTO;
import com.roaker.notes.uc.api.logger.dto.OperateLogV2CreateReqDTO;
import com.roaker.notes.uc.api.logger.dto.OperateLogV2PageReqDTO;
import com.roaker.notes.uc.api.logger.dto.OperateLogV2RespDTO;
import com.roaker.notes.uc.converter.logger.OperateLogConvert;
import com.roaker.notes.uc.dal.dataobject.logger.OperateLogV2DO;
import com.roaker.notes.uc.dal.dataobject.user.AdminUserInfoDO;
import com.roaker.notes.uc.service.logger.OperateLogService;
import com.roaker.notes.uc.service.user.AdminUserService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.List;

import static com.roaker.notes.commons.utils.RoakerCollectionUtils.convertSet;


/**
 * 操作日志 API 实现类
 *
 * @author Roaker
 */
@Service
@Validated
public class OperateLogApiImpl implements OperateLogApi {

    @Resource
    private OperateLogService operateLogService;
    @Resource
    private AdminUserService adminUserService;

    @Override
    public void createOperateLog(OperateLogCreateReqDTO createReqDTO) {
        operateLogService.createOperateLog(createReqDTO);
    }

    @Override
    @Async
    public void createOperateLogV2(OperateLogV2CreateReqDTO createReqDTO) {
        operateLogService.createOperateLogV2(createReqDTO);
    }

    @Override
    public PageResult<OperateLogV2RespDTO> getOperateLogPage(OperateLogV2PageReqDTO pageReqVO) {
        PageResult<OperateLogV2DO> operateLogPage = operateLogService.getOperateLogPage(pageReqVO);
        if (CollUtil.isEmpty(operateLogPage.getList())) {
            return PageResult.empty();
        }

        // 获取用户
        List<AdminUserInfoDO> userList = adminUserService.getUserList(
                convertSet(operateLogPage.getList(), OperateLogV2DO::getUserId));
        return OperateLogConvert.INSTANCE.convertPage(operateLogPage, userList);
    }

}
