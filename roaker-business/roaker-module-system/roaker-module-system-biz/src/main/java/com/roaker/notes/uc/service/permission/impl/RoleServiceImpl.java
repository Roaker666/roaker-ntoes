package com.roaker.notes.uc.service.permission.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.google.common.annotations.VisibleForTesting;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.BeanUtils;
import com.roaker.notes.commons.utils.RoakerCollectionUtils;
import com.roaker.notes.dynamic.enums.CommonEnum;
import com.roaker.notes.enums.DataScopeEnum;
import com.roaker.notes.enums.RoleCodeEnum;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.enums.RoleTypeEnums;
import com.roaker.notes.uc.dal.dataobject.permission.RoleInfoDO;
import com.roaker.notes.uc.dal.mapper.permission.RoleInfoMapper;
import com.roaker.notes.uc.service.permission.PermissionInfoService;
import com.roaker.notes.uc.service.permission.RolePermissionCoreService;
import com.roaker.notes.uc.vo.permission.RoleCreateReqVO;
import com.roaker.notes.uc.vo.permission.RoleExportReqVO;
import com.roaker.notes.uc.vo.permission.RoleUpdateReqVO;
import com.roaker.notes.uc.converter.permission.PermissionConverter;
import com.roaker.notes.uc.service.permission.RoleService;
import com.roaker.notes.uc.vo.permission.RolePageReqVO;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.roaker.notes.commons.constants.ErrorCodeConstants.*;
import static com.roaker.notes.commons.utils.RoakerCollectionUtils.convertMap;
import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;

/**
 * 角色 Service 实现类
 *
 * @author lei.rao
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {


    @Resource
    private RolePermissionCoreService rolePermissionCoreService;

    @Resource
    private RoleInfoMapper roleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(RoleCreateReqVO createReqVO, Integer type) {
        // 校验角色
        validateRoleDuplicate(createReqVO.getName(), createReqVO.getCode(), null);
        // 插入到数据库
        RoleInfoDO role = BeanUtils.toBean(createReqVO, RoleInfoDO.class);
        role.setType(CommonEnum.of(ObjectUtil.defaultIfNull(type, RoleTypeEnums.CUSTOM.getCode()), RoleTypeEnums.class));
        role.setStatus(CommonStatusEnum.ENABLE);
        role.setDataScope(DataScopeEnum.ALL); // 默认可查看所有数据。原因是，可能一些项目不需要项目权限
        roleMapper.insert(role);
        // 返回
        return role.getId();
    }

    @Override
    public void updateRole(RoleUpdateReqVO updateReqVO) {
        // 校验是否可以更新
        validateRoleForUpdate(updateReqVO.getId());
        // 校验角色的唯一字段是否重复
        validateRoleDuplicate(updateReqVO.getName(), updateReqVO.getCode(), updateReqVO.getId());

        // 更新到数据库
        RoleInfoDO updateObj = BeanUtils.toBean(updateReqVO, RoleInfoDO.class);
        roleMapper.updateById(updateObj);
    }

    @Override
    public void updateRoleStatus(Long id, Integer status) {
        // 校验是否可以更新
        validateRoleForUpdate(id);

        // 更新状态
        RoleInfoDO updateObj = new RoleInfoDO().setId(id).setStatus(CommonEnum.of(status, CommonStatusEnum.class));
        roleMapper.updateById(updateObj);
    }

    @Override
    public void updateRoleDataScope(Long id, Integer dataScope, Set<Long> dataScopeDeptIds) {
        // 校验是否可以更新
        validateRoleForUpdate(id);

        // 更新数据范围
        RoleInfoDO updateObject = new RoleInfoDO();
        updateObject.setId(id);
        updateObject.setDataScope(CommonEnum.of(dataScope, DataScopeEnum.class));
        updateObject.setDataScopeDeptIds(dataScopeDeptIds);
        roleMapper.updateById(updateObject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        // 校验是否可以更新
        validateRoleForUpdate(id);
        // 标记删除
        roleMapper.deleteById(id);
        // 删除相关数据
        rolePermissionCoreService.processRoleDeleted(id);
    }

    /**
     * 校验角色的唯一字段是否重复
     *
     * 1. 是否存在相同名字的角色
     * 2. 是否存在相同编码的角色
     *
     * @param name 角色名字
     * @param code 角色额编码
     * @param id 角色编号
     */
    @VisibleForTesting
    void validateRoleDuplicate(String name, String code, Long id) {
        // 0. 超级管理员，不允许创建
        if (RoleCodeEnum.isSuperAdmin(code)) {
            throw exception(ROLE_ADMIN_CODE_ERROR, code);
        }
        // 1. 该 name 名字被其它角色所使用
        RoleInfoDO role = roleMapper.selectByName(name);
        if (role != null && !role.getId().equals(id)) {
            throw exception(ROLE_NAME_DUPLICATE, name);
        }
        // 2. 是否存在相同编码的角色
        if (!StringUtils.hasText(code)) {
            return;
        }
        // 该 code 编码被其它角色所使用
        role = roleMapper.selectByCode(code);
        if (role != null && !role.getId().equals(id)) {
            throw exception(ROLE_CODE_DUPLICATE, code);
        }
    }

    /**
     * 校验角色是否可以被更新
     *
     * @param id 角色编号
     */
    @VisibleForTesting
    void validateRoleForUpdate(Long id) {
        RoleInfoDO RoleInfoDO = roleMapper.selectById(id);
        if (RoleInfoDO == null) {
            throw exception(ROLE_NOT_EXISTS);
        }
        // 内置角色，不允许删除
        if (RoleTypeEnums.SYSTEM == RoleInfoDO.getType()) {
            throw exception(ROLE_CAN_NOT_UPDATE_SYSTEM_TYPE_ROLE);
        }
    }

    @Override
    public RoleInfoDO getRole(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    public RoleInfoDO getRoleFromCache(Long id) {
        return roleMapper.selectById(id);
    }


    @Override
    public List<RoleInfoDO> getRoleListByStatus(Collection<Integer> statuses) {
        return roleMapper.selectListByStatus(statuses);
    }

    @Override
    public List<RoleInfoDO> getRoleList() {
        return roleMapper.selectList();
    }

    @Override
    public List<RoleInfoDO> getRoleList(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return roleMapper.selectBatchIds(ids);
    }

    @Override
    public List<RoleInfoDO> getRoleListFromCache(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        // 这里采用 for 循环从缓存中获取，主要考虑 Spring CacheManager 无法批量操作的问题
        RoleServiceImpl self = getSelf();
        return RoakerCollectionUtils.convertList(ids, self::getRoleFromCache);
    }

    @Override
    public PageResult<RoleInfoDO> getRolePage(RolePageReqVO reqVO) {
        return roleMapper.selectPage(reqVO);
    }

    @Override
    public boolean hasAnySuperAdmin(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return false;
        }
        RoleServiceImpl self = getSelf();
        return ids.stream().anyMatch(id -> {
            RoleInfoDO role = self.getRoleFromCache(id);
            return role != null && RoleCodeEnum.isSuperAdmin(role.getCode());
        });
    }

    @Override
    public void validateRoleList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得角色信息
        List<RoleInfoDO> roles = roleMapper.selectBatchIds(ids);
        Map<Long, RoleInfoDO> roleMap = convertMap(roles, RoleInfoDO::getId);
        // 校验
        ids.forEach(id -> {
            RoleInfoDO role = roleMap.get(id);
            if (role == null) {
                throw exception(ROLE_NOT_EXISTS);
            }
            if (CommonStatusEnum.DISABLE == role.getStatus()) {
                throw exception(ROLE_IS_DISABLE, role.getName());
            }
        });
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private RoleServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }
}
