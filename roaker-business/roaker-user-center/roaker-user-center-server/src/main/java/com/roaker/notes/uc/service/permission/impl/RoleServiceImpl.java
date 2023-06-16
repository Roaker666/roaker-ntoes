package com.roaker.notes.uc.service.permission.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.google.common.annotations.VisibleForTesting;
import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.dynamic.enums.CommonEnum;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.enums.RoleCodeEnum;
import com.roaker.notes.uc.converter.permission.PermissionConverter;
import com.roaker.notes.uc.dal.dataobject.permission.RoleInfoDO;
import com.roaker.notes.uc.dal.mapper.permission.RoleInfoMapper;
import com.roaker.notes.uc.service.permission.RolePermissionCoreService;
import com.roaker.notes.uc.service.permission.RoleService;
import com.roaker.notes.uc.vo.permission.RolePageReqVO;
import com.roaker.notes.uc.vo.permission.RoleCreateReqVO;
import com.roaker.notes.uc.vo.permission.RoleExportReqVO;
import com.roaker.notes.uc.vo.permission.RoleUpdateReqVO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    /**
     * 角色缓存
     * key：角色编号 {@link RoleInfoDO#getId()}
     * <p>
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    private volatile Map<String, RoleInfoDO> roleCache;

    @Resource
    private RolePermissionCoreService rolePermissionCoreService;

    @Resource
    private RoleInfoMapper roleInfoMapper;

    /**
     * 初始化 {@link #roleCache} 缓存
     */
    @Override
    @PostConstruct
    public void initLocalCache() {
        // 第一步：查询数据
        List<RoleInfoDO> roleList = roleInfoMapper.selectList();
        log.info("[initLocalCache][缓存角色，数量为:{}]", roleList.size());

        // 第二步：构建缓存
        roleCache = convertMap(roleList, RoleInfoDO::getRoleId);
    }

    @Override
    @Transactional
    public String createRole(RoleCreateReqVO reqVO, Integer type) {
        // 校验角色
        validateRoleDuplicate(reqVO.getRoleName(), reqVO.getRoleId(), null);
        // 插入到数据库
        RoleInfoDO role = PermissionConverter.INSTANCE.convert(reqVO);
        role.setStatus(CommonStatusEnum.ENABLE);
        roleInfoMapper.insert(role);
        // 发送刷新消息
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
//                roleProducer.sendRoleRefreshMessage();
            }
        });
        // 返回
        return role.getRoleId();
    }

    @Override
    public void updateRole(RoleUpdateReqVO reqVO) {
        // 校验是否可以更新
        validateRoleForUpdate(reqVO.getRoleId());
        // 校验角色的唯一字段是否重复
        validateRoleDuplicate(reqVO.getRoleName(), reqVO.getRoleId(), reqVO.getRoleId());

        // 更新到数据库
        RoleInfoDO updateObj = PermissionConverter.INSTANCE.convert(reqVO);
        roleInfoMapper.updateById(updateObj);
        // 发送刷新消息
//        roleProducer.sendRoleRefreshMessage();
    }

    @Override
    public void updateRoleStatus(String roleId, Integer status) {
        // 校验是否可以更新
        validateRoleForUpdate(roleId);

        // 更新状态
        RoleInfoDO updateObj = new RoleInfoDO().setRoleId(roleId).setStatus(CommonEnum.of(status, CommonStatusEnum.class));
        roleInfoMapper.updateById(updateObj);
        // 发送刷新消息
//        roleProducer.sendRoleRefreshMessage();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(String roleId) {
        // 校验是否可以更新
        validateRoleForUpdate(roleId);
        // 标记删除
        roleInfoMapper.selectByCode(roleId);
        // 删除相关数据
        rolePermissionCoreService.processRoleDeleted(roleId);
        // 发送刷新消息. 注意，需要事务提交后，在进行发送刷新消息。不然 db 还未提交，结果缓存先刷新了
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
//                roleProducer.sendRoleRefreshMessage();
            }

        });
    }

    @Override
    public RoleInfoDO getRoleFromCache(String roleId) {
        return roleCache.get(roleId);
    }

    @Override
    public List<RoleInfoDO> getRoleListByStatus(@Nullable Collection<Integer> statuses) {
        if (CollUtil.isEmpty(statuses)) {
            return roleInfoMapper.selectList();
        }
        return roleInfoMapper.selectListByStatus(statuses);
    }

    @Override
    public List<RoleInfoDO> getRoleListFromCache(Collection<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return roleCache.values().stream().filter(RoleInfoDO -> ids.contains(RoleInfoDO.getRoleId()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasAnySuperAdmin(Collection<RoleInfoDO> roleList) {
        if (CollectionUtil.isEmpty(roleList)) {
            return false;
        }
        return roleList.stream().anyMatch(role -> RoleCodeEnum.isSuperAdmin(role.getRoleId()));
    }

    @Override
    public RoleInfoDO getRole(String roleId) {
        return roleInfoMapper.selectByCode(roleId);
    }

    @Override
    public PageResult<RoleInfoDO> getRolePage(RolePageReqVO reqVO) {
        return roleInfoMapper.selectPage(reqVO);
    }

    @Override
    public List<RoleInfoDO> getRoleList(RoleExportReqVO reqVO) {
        return roleInfoMapper.selectList(reqVO);
    }

    /**
     * 校验角色的唯一字段是否重复
     * <p>
     * 1. 是否存在相同名字的角色
     * 2. 是否存在相同编码的角色
     *
     * @param name 角色名字
     * @param roleId 角色额编码
     * @param value   角色编号
     */
    @VisibleForTesting
    void validateRoleDuplicate(String name, String roleId, String value) {
        // 0. 超级管理员，不允许创建
        if (RoleCodeEnum.isSuperAdmin(roleId)) {
            throw exception(ROLE_ADMIN_CODE_ERROR, roleId);
        }
        // 1. 该 name 名字被其它角色所使用
        RoleInfoDO role = roleInfoMapper.selectByName(name);
        if (role != null && !StringUtils.equals(roleId, role.getRoleId())) {
            throw exception(ROLE_NAME_DUPLICATE, name);
        }
        // 2. 是否存在相同编码的角色
        if (StringUtils.isEmpty(roleId)) {
            return;
        }
        // 该 code 编码被其它角色所使用
        role = roleInfoMapper.selectByCode(roleId);
        if (role != null && !role.getRoleId().equals(roleId)) {
            throw exception(ROLE_CODE_DUPLICATE, roleId);
        }
    }

    /**
     * 校验角色是否可以被更新
     *
     * @param roleId 角色编号
     */
    @VisibleForTesting
    void validateRoleForUpdate(String roleId) {
        RoleInfoDO RoleInfoDO = roleInfoMapper.selectByCode(roleId);
        if (RoleInfoDO == null) {
            throw exception(ROLE_NOT_EXISTS);
        }
    }

    @Override
    public void validateRoleList(Collection<String> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return;
        }
        // 获得角色信息
        List<RoleInfoDO> roles = roleInfoMapper.selectBatchIds(roleIds);
        Map<String, RoleInfoDO> roleMap = convertMap(roles, RoleInfoDO::getRoleId);
        // 校验
        roleIds.forEach(roleId -> {
            RoleInfoDO role = roleMap.get(roleId);
            if (role == null) {
                throw exception(ROLE_NOT_EXISTS);
            }
            if (CommonStatusEnum.DISABLE == role.getStatus()) {
                throw exception(ROLE_IS_DISABLE, role.getRoleName());
            }
        });
    }
}
