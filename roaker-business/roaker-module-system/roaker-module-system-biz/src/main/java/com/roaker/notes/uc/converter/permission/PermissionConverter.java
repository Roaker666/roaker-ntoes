package com.roaker.notes.uc.converter.permission;

import com.roaker.notes.uc.dal.dataobject.permission.PermissionInfoDO;
import com.roaker.notes.uc.dal.dataobject.permission.RoleInfoDO;
import com.roaker.notes.uc.vo.permission.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Mapper
public interface PermissionConverter {

    PermissionConverter INSTANCE = Mappers.getMapper(PermissionConverter.class);

    List<PermissionInfoRespVO> convertList(List<PermissionInfoDO> list);

    @Mapping(target = "status", expression = "java(com.roaker.notes.dynamic.enums.CommonEnum.of(bean.getStatus(), com.roaker.notes.enums.CommonStatusEnum.class))")
    @Mapping(target = "type", expression = "java(com.roaker.notes.dynamic.enums.CommonEnum.of(bean.getType(), com.roaker.notes.enums.PermissionTypeEnum.class))")
    PermissionInfoDO convert(PermissionInfoCreateReqVO bean);

    @Mapping(target = "status", expression = "java(com.roaker.notes.dynamic.enums.CommonEnum.of(bean.getStatus(), com.roaker.notes.enums.CommonStatusEnum.class))")
    @Mapping(target = "type", expression = "java(com.roaker.notes.dynamic.enums.CommonEnum.of(bean.getType(), com.roaker.notes.enums.PermissionTypeEnum.class))")
    PermissionInfoDO convert(PermissionInfoUpdateReqVO bean);

    @Mapping(target = "status", expression = "java(bean.getStatus().getCode())")
    @Mapping(target = "type", expression = "java(bean.getType().getCode())")
    PermissionInfoRespVO convert(PermissionInfoDO bean);

    List<PermissionInfoSimpleRespVO> convertList04(List<PermissionInfoDO> list);

    @Mapping(target = "type", expression = "java(bean.getType().getCode())")
    PermissionInfoSimpleRespVO convert2(PermissionInfoDO bean);

    RoleInfoDO convert(RoleUpdateReqVO bean);

    @Mapping(target = "status", expression = "java(bean.getStatus().getCode())")
    RoleRespVO convert(RoleInfoDO bean);

    RoleInfoDO convert(RoleCreateReqVO bean);

    List<RoleSimpleRespVO> convertList02(List<RoleInfoDO> list);

    List<RoleExcelVO> convertList03(List<RoleInfoDO> list);

}
