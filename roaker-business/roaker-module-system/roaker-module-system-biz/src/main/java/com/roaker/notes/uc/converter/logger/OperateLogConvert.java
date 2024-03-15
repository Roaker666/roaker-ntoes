package com.roaker.notes.uc.converter.logger;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.BeanUtils;
import com.roaker.notes.commons.utils.RoakerCollectionUtils;
import com.roaker.notes.commons.utils.RoakerMapUtils;
import com.roaker.notes.uc.api.logger.dto.OperateLogV2RespDTO;
import com.roaker.notes.uc.dal.dataobject.logger.OperateLogDO;
import com.roaker.notes.uc.dal.dataobject.logger.OperateLogV2DO;
import com.roaker.notes.uc.dal.dataobject.user.AdminUserInfoDO;
import com.roaker.notes.uc.vo.logger.operatelog.OperateLogRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static com.roaker.notes.commons.utils.RoakerCollectionUtils.convertMap;
import static com.roaker.notes.commons.utils.RoakerMapUtils.findAndThen;


@Mapper
public interface OperateLogConvert {

    OperateLogConvert INSTANCE = Mappers.getMapper(OperateLogConvert.class);

    default List<OperateLogRespVO> convertList(List<OperateLogDO> list, Map<String, AdminUserInfoDO> userMap) {
        return RoakerCollectionUtils.convertList(list, log -> {
            OperateLogRespVO logVO = BeanUtils.toBean(log, OperateLogRespVO.class);
            findAndThen(userMap, log.getUserId(), user -> logVO.setUserNickname(user.getUsername()));
            return logVO;
        });
    }

    default PageResult<OperateLogV2RespDTO> convertPage(PageResult<OperateLogV2DO> operateLogPage, List<AdminUserInfoDO> userList) {
        return BeanUtils.toBean(operateLogPage, OperateLogV2RespDTO.class).setList(setUserInfo(operateLogPage.getList(), userList));
    }

    OperateLogV2RespDTO convert(OperateLogV2DO operateLogV2DO);

    default List<OperateLogV2RespDTO> setUserInfo(List<OperateLogV2DO> logList, List<AdminUserInfoDO> userList) {
        Map<String, AdminUserInfoDO> userMap = convertMap(userList, AdminUserInfoDO::getUid);
        return RoakerCollectionUtils.convertList(logList, item -> {
            OperateLogV2RespDTO respDTO = convert(item);
            findAndThen(userMap, item.getUserId(), user -> respDTO.setUserName(user.getUsername()));
            return respDTO;
        });
    }

}
