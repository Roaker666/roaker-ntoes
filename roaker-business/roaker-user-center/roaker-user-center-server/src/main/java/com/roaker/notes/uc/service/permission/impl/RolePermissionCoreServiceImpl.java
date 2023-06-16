package com.roaker.notes.uc.service.permission.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.roaker.notes.commons.utils.RoakerCollectionUtils;
import com.roaker.notes.commons.utils.RoakerMapUtils;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.enums.UserTypeEnum;
import com.roaker.notes.uc.dal.dataobject.permission.PermissionInfoDO;
import com.roaker.notes.uc.dal.dataobject.permission.RoleInfoDO;
import com.roaker.notes.uc.dal.dataobject.permission.RolePermissionDO;
import com.roaker.notes.uc.dal.dataobject.permission.UserRoleDO;
import com.roaker.notes.uc.dal.mapper.permission.RolePermissionMapper;
import com.roaker.notes.uc.dal.mapper.permission.UserRoleMapper;
import com.roaker.notes.uc.service.permission.PermissionInfoService;
import com.roaker.notes.uc.service.permission.RolePermissionCoreService;
import com.roaker.notes.uc.service.permission.RoleService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;

import static com.roaker.notes.commons.utils.RoakerCollectionUtils.convertSet;
import static java.util.Collections.singleton;

/**
 * 权限 Service 实现类
 *
 * @author lei.rao
 */
@Service
@Slf4j
public class RolePermissionCoreServiceImpl implements RolePermissionCoreService {

    /**
     * 角色编号与菜单编号的缓存映射
     * key：角色编号
     * value：菜单编号的数组
     * <p>
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    @Setter // 单元测试需要
    private volatile Multimap<String, String> rolePermissionCache;
    /**
     * 菜单编号与角色编号的缓存映射
     * key：菜单编号
     * value：角色编号的数组
     * <p>
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    @Setter // 单元测试需要
    private volatile Multimap<String, String> permissionRoleCache;

    /**
     * 用户编号与角色编号的缓存映射
     * key：用户编号
     * value：角色编号的数组
     * <p>
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    @Setter // 单元测试需要
    private volatile Map<String, Set<String>> userRoleCache;

    @Resource
    private RolePermissionMapper rolePermissionMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleService roleService;
    @Resource
    private PermissionInfoService permissionInfoService;

    @Override
    @PostConstruct
    public void initLocalCache() {
        initLocalCacheForRolePermission();
        initLocalCacheForUserRole();
    }

    /**
     * 刷新 RolePermission 本地缓存
     */
    @VisibleForTesting
    void initLocalCacheForRolePermission() {
        // 注意：忽略自动多租户，因为要全局初始化缓存
        // 第一步：查询数据
        List<RolePermissionDO> rolePermissionDOList = rolePermissionMapper.selectList();
        log.info("[initLocalCacheForRolePermission][缓存角色与菜单，数量为:{}]", rolePermissionDOList.size());

        // 第二步：构建缓存
        ImmutableMultimap.Builder<String, String> rolePermissionCacheBuilder = ImmutableMultimap.builder();
        ImmutableMultimap.Builder<String, String> permissionRoleCacheBuilder = ImmutableMultimap.builder();
        rolePermissionDOList.forEach(rolePermissionDO -> {
            rolePermissionCacheBuilder.put(rolePermissionDO.getRoleId(), rolePermissionDO.getPermissionId());
            permissionRoleCacheBuilder.put(rolePermissionDO.getPermissionId(), rolePermissionDO.getRoleId());
        });
        rolePermissionCache = rolePermissionCacheBuilder.build();
        permissionRoleCache = permissionRoleCacheBuilder.build();
    }

    /**
     * 刷新 UserRole 本地缓存
     */
    @VisibleForTesting
    void initLocalCacheForUserRole() {
        // 注意：忽略自动多租户，因为要全局初始化缓存
        // 第一步：加载数据
        List<UserRoleDO> userRoles = userRoleMapper.selectList();
        log.info("[initLocalCacheForUserRole][缓存用户与角色，数量为:{}]", userRoles.size());

        // 第二步：构建缓存。
        ImmutableMultimap.Builder<String, String> userRoleCacheBuilder = ImmutableMultimap.builder();
        userRoles.forEach(userRoleDO -> userRoleCacheBuilder.put(userRoleDO.getUid(), userRoleDO.getRoleId()));
        userRoleCache = RoakerCollectionUtils.convertMultiMap2(userRoles, UserRoleDO::getUid, UserRoleDO::getRoleId);
    }

    @Override
    public List<PermissionInfoDO> getRolePermissionInfoListFromCache(Collection<String> roleIds, Collection<Integer> permissionTypes,
                                                           Collection<Integer> permissionsStatuses) {
        // 任一一个参数为空时，不返回任何菜单
        if (RoakerCollectionUtils.isAnyEmpty(roleIds, permissionTypes, permissionsStatuses)) {
            return Collections.emptyList();
        }

        // 判断角色是否包含超级管理员。如果是超级管理员，获取到全部
        List<RoleInfoDO> roleList = roleService.getRoleListFromCache(roleIds);
        if (roleService.hasAnySuperAdmin(roleList)) {
            return permissionInfoService.getPermissionInfoListFromCache(permissionTypes, permissionsStatuses);
        }

        // 获得角色拥有的菜单关联
        List<String> permissionIds = RoakerMapUtils.getList(rolePermissionCache, roleIds);
        return permissionInfoService.getPermissionInfoListFromCache(permissionIds, permissionTypes, permissionsStatuses);
    }

    @Override
    public Set<String> getUserRoleIdsFromCache(String userId, Collection<Integer> roleStatuses) {
        Set<String> cacheRoleIds = userRoleCache.get(userId);
        // 创建用户的时候没有分配角色，会存在空指针异常
        if (CollUtil.isEmpty(cacheRoleIds)) {
            return Collections.emptySet();
        }
        Set<String> roleIds = new HashSet<>(cacheRoleIds);
        // 过滤角色状态
        if (CollectionUtil.isNotEmpty(roleStatuses)) {
            roleIds.removeIf(roleId -> {
                RoleInfoDO role = roleService.getRoleFromCache(roleId);
                return role == null || !roleStatuses.contains(role.getStatus().getCode());
            });
        }
        return roleIds;
    }

    @Override
    public Set<String> getRolePermissionInfoIds(String roleId) {
        // 如果是管理员的情况下，获取全部菜单编号
        if (roleService.hasAnySuperAdmin(Collections.singleton(roleId))) {
            return convertSet(permissionInfoService.getPermissionInfoList(), PermissionInfoDO::getPermissionId);
        }
        // 如果是非管理员的情况下，获得拥有的菜单编号
        return convertSet(rolePermissionMapper.selectListByRoleId(roleId), RolePermissionDO::getPermissionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRolePermissionInfo(String roleId, Set<String> permissionInfoIds) {
        // 获得角色拥有菜单编号
        Set<String> permissionIds = convertSet(rolePermissionMapper.selectListByRoleId(roleId),
                RolePermissionDO::getPermissionId);
        // 计算新增和删除的菜单编号
        Collection<String> createPermissionIds = CollUtil.subtract(permissionInfoIds, permissionIds);
        Collection<String> deletePermissionIds = CollUtil.subtract(permissionIds, permissionInfoIds);
        // 执行新增和删除。对于已经授权的菜单，不用做任何处理
        if (!CollectionUtil.isEmpty(createPermissionIds)) {
            rolePermissionMapper.insertBatch(RoakerCollectionUtils.convertList(createPermissionIds, permissionId -> {
                RolePermissionDO entity = new RolePermissionDO();
                entity.setRoleId(roleId);
                entity.setPermissionId(permissionId);
                return entity;
            }));
        }
        if (!CollectionUtil.isEmpty(deletePermissionIds)) {
            rolePermissionMapper.deleteListByRoleIdAndPermissionIds(roleId, deletePermissionIds);
        }
        // 发送刷新消息. 注意，需要事务提交后，在进行发送刷新消息。不然 db 还未提交，结果缓存先刷新了
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
//                permissionProducer.sendRolePermissionRefreshMessage();
            }

        });
    }

    @Override
    public Set<String> getUserRoleIdListByUserId(String userId, UserTypeEnum userType) {
        return convertSet(userRoleMapper.selectListByUserId(userId, userType),
                UserRoleDO::getRoleId);
    }

    @Override
    public Set<String> getUserRoleIdListByRoleIds(Collection<String> roleIds, UserTypeEnum userType) {
        return convertSet(userRoleMapper.selectListByRoleIds(roleIds, userType),
                UserRoleDO::getUid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignUserRole(String userId, UserTypeEnum userType, Set<String> roleIds) {
        // 获得角色拥有角色编号
        Set<String> dbRoleIds = convertSet(userRoleMapper.selectListByUserId(userId, userType),
                UserRoleDO::getRoleId);
        // 计算新增和删除的角色编号
        Collection<String> createRoleIds = CollUtil.subtract(roleIds, dbRoleIds);
        Collection<String> deletePermissionIds = CollUtil.subtract(dbRoleIds, roleIds);
        // 执行新增和删除。对于已经授权的角色，不用做任何处理
        if (!CollectionUtil.isEmpty(createRoleIds)) {
            userRoleMapper.insertBatch(RoakerCollectionUtils.convertList(createRoleIds, roleId -> {
                UserRoleDO entity = new UserRoleDO();
                entity.setUid(userId);
                entity.setUserType(userType);
                entity.setRoleId(roleId);
                return entity;
            }));
        }
        if (!CollectionUtil.isEmpty(deletePermissionIds)) {
            userRoleMapper.deleteListByUserIdAndRoleIdIds(userId,userType, deletePermissionIds);
        }
        // 发送刷新消息. 注意，需要事务提交后，在进行发送刷新消息。不然 db 还未提交，结果缓存先刷新了
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
//                permissionProducer.sendUserRoleRefreshMessage();
            }

        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processRoleDeleted(String roleId) {
        // 标记删除 UserRole
        userRoleMapper.deleteListByRoleId(roleId);
        // 标记删除 RolePermission
        rolePermissionMapper.deleteListByRoleId(roleId);
        // 发送刷新消息. 注意，需要事务提交后，在进行发送刷新消息。不然 db 还未提交，结果缓存先刷新了
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
//                permissionProducer.sendRolePermissionRefreshMessage();
//                permissionProducer.sendUserRoleRefreshMessage();
            }

        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processPermissionInfoDeleted(String permissionId) {
        rolePermissionMapper.deleteListByPermissionId(permissionId);
        // 发送刷新消息. 注意，需要事务提交后，在进行发送刷新消息。不然 db 还未提交，结果缓存先刷新了
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
//                permissionProducer.sendRolePermissionRefreshMessage();
            }

        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processUserDeleted(String userId) {
        userRoleMapper.deleteListByUserId(userId, null);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
//                permissionProducer.sendUserRoleRefreshMessage();
            }

        });
    }

    @Override
    public boolean hasAnyPermissions(String userId, String... permissions) {
        // 如果为空，说明已经有权限
        if (ArrayUtil.isEmpty(permissions)) {
            return true;
        }

        // 获得当前登录的角色。如果为空，说明没有权限
        Set<String> roleIds = getUserRoleIdsFromCache(userId, singleton(CommonStatusEnum.ENABLE.getCode()));
        if (CollUtil.isEmpty(roleIds)) {
            return false;
        }
        // 判断是否是超管。如果是，当然符合条件
        if (roleService.hasAnySuperAdmin(roleIds)) {
            return true;
        }

        // 遍历权限，判断是否有一个满足
        return Arrays.stream(permissions).anyMatch(permission -> {
            List<PermissionInfoDO> permissionList = permissionInfoService.getPermissionInfoListByPermissionInfoFromCache(permission);
            // 采用严格模式，如果权限找不到对应的 Permission 的话，认为
            if (CollUtil.isEmpty(permissionList)) {
                return false;
            }
            // 获得是否拥有该权限，任一一个
            return permissionList.stream().anyMatch(permission1 -> CollUtil.containsAny(roleIds,
                    permissionRoleCache.get(permission1.getPermissionId())));
        });
    }

    @Override
    public boolean hasAnyRoles(String userId, String... roles) {
        // 如果为空，说明已经有权限
        if (ArrayUtil.isEmpty(roles)) {
            return true;
        }

        // 获得当前登录的角色。如果为空，说明没有权限
        Set<String> roleIds = getUserRoleIdsFromCache(userId, singleton(CommonStatusEnum.ENABLE.getCode()));
        if (CollUtil.isEmpty(roleIds)) {
            return false;
        }
        // 判断是否是超管。如果是，当然符合条件
        if (roleService.hasAnySuperAdmin(roleIds)) {
            return true;
        }
        Set<String> userRoles = convertSet(roleService.getRoleListFromCache(roleIds),
                RoleInfoDO::getRoleId);
        return CollUtil.containsAny(userRoles, Sets.newHashSet(roles));
    }
}
