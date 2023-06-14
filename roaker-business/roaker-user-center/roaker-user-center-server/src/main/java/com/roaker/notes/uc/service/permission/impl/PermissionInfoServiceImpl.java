package com.roaker.notes.uc.service.permission.impl;

import cn.hutool.core.util.StrUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.roaker.notes.commons.utils.RoakerCollectionUtils;
import com.roaker.notes.enums.PermissionTypeEnum;
import com.roaker.notes.uc.converter.permission.PermissionConverter;
import com.roaker.notes.uc.dal.dataobject.permission.PermissionInfoDO;
import com.roaker.notes.uc.dal.mapper.permission.PermissionInfoMapper;
import com.roaker.notes.uc.service.permission.PermissionInfoService;
import com.roaker.notes.uc.service.permission.RolePermissionCoreService;
import com.roaker.notes.uc.vo.permission.PermissionInfoCreateReqVO;
import com.roaker.notes.uc.vo.permission.PermissionInfoListReqVO;
import com.roaker.notes.uc.vo.permission.PermissionInfoUpdateReqVO;
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
import java.util.stream.Collectors;

import static com.roaker.notes.commons.constants.ErrorCodeConstants.*;
import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;
import static com.roaker.notes.uc.dal.dataobject.permission.PermissionInfoDO.ID_ROOT;


/**
 * 菜单 Service 实现
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class PermissionInfoServiceImpl implements PermissionInfoService {

    /**
     * 菜单缓存
     * key：菜单编号
     * <p>
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    @Setter
    private volatile Map<Long, PermissionInfoDO> permissionInfoCache;
    /**
     * 权限与菜单缓存
     * key：权限 {@link PermissionInfoDO#getPermission()}
     * value：PermissionInfoDO 数组，因为一个权限可能对应多个 PermissionInfoDO 对象
     * <p>
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    @Setter
    private volatile Multimap<String, PermissionInfoDO> permissionPermissionInfoCache;

    @Resource
    private PermissionInfoMapper permissionInfoMapper;
    @Resource
    private RolePermissionCoreService rolePermissionCoreService;

    /**
     * 初始化 {@link #permissionInfoCache} 和 {@link #permissionPermissionInfoCache} 缓存
     */
    @Override
    @PostConstruct
    public synchronized void initLocalCache() {
        // 第一步：查询数据
        List<PermissionInfoDO> permissionInfoDOList = permissionInfoMapper.selectList();
        log.info("[initLocalCache][缓存菜单，数量为:{}]", permissionInfoDOList.size());

        // 第二步：构建缓存
        ImmutableMap.Builder<Long, PermissionInfoDO> PermissionInfoCacheBuilder = ImmutableMap.builder();
        ImmutableMultimap.Builder<String, PermissionInfoDO> permPermissionInfoCacheBuilder = ImmutableMultimap.builder();
        permissionInfoDOList.forEach(permissionInfoDO -> {
            PermissionInfoCacheBuilder.put(permissionInfoDO.getId(), permissionInfoDO);
            if (StrUtil.isNotEmpty(permissionInfoDO.getPermission())) { // 会存在 permission 为 null 的情况，导致 put 报 NPE 异常
                permPermissionInfoCacheBuilder.put(permissionInfoDO.getPermission(), permissionInfoDO);
            }
        });
        permissionInfoCache = PermissionInfoCacheBuilder.build();
        permissionPermissionInfoCache = permPermissionInfoCacheBuilder.build();
    }

    @Override
    public Long createPermissionInfo(PermissionInfoCreateReqVO reqVO) {
        // 校验父菜单存在
        validateParentPermissionInfo(reqVO.getParentId(), null);
        // 校验菜单（自己）
        validatePermissionInfo(reqVO.getParentId(), reqVO.getName(), null);

        // 插入数据库
        PermissionInfoDO permissionInfoDO = PermissionConverter.INSTANCE.convert(reqVO);
        initPermissionInfoProperty(permissionInfoDO);
        permissionInfoMapper.insert(permissionInfoDO);
        return permissionInfoDO.getId();
    }

    @Override
    public void updatePermissionInfo(PermissionInfoUpdateReqVO reqVO) {
        // 校验更新的菜单是否存在
        if (permissionInfoMapper.selectById(reqVO.getPermissionId()) == null) {
            throw exception(PermissionInfo_NOT_EXISTS);
        }
        // 校验父菜单存在
        validateParentPermissionInfo(reqVO.getParentId(), reqVO.getPermissionId());
        // 校验菜单（自己）
        validatePermissionInfo(reqVO.getParentId(), reqVO.getName(), reqVO.getPermissionId());

        // 更新到数据库
        PermissionInfoDO updateObject = PermissionConverter.INSTANCE.convert(reqVO);
        initPermissionInfoProperty(updateObject);
        permissionInfoMapper.updateById(updateObject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermissionInfo(String permissionId) {
        // 校验是否还有子菜单
        if (permissionInfoMapper.selectCountByParentId(permissionId) > 0) {
            throw exception(PermissionInfo_EXISTS_CHILDREN);
        }
        // 校验删除的菜单是否存在
        if (permissionInfoMapper.selectById(permissionId) == null) {
            throw exception(PermissionInfo_NOT_EXISTS);
        }
        // 标记删除
        permissionInfoMapper.deleteById(permissionId);
        // 删除授予给角色的权限
        rolePermissionCoreService.processPermissionInfoDeleted(permissionId);
        // 发送刷新消息. 注意，需要事务提交后，在进行发送刷新消息。不然 db 还未提交，结果缓存先刷新了
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
//                PermissionInfoProducer.sendPermissionInfoRefreshMessage();
            }

        });
    }

    @Override
    public List<PermissionInfoDO> getPermissionInfoList() {
        return permissionInfoMapper.selectList();
    }

    @Override
    public List<PermissionInfoDO> getPermissionInfoListByTenant(PermissionInfoListReqVO reqVO) {
        return getPermissionInfoList(reqVO);
    }

    @Override
    public List<PermissionInfoDO> getPermissionInfoList(PermissionInfoListReqVO reqVO) {
        return permissionInfoMapper.selectList(reqVO);
    }

    @Override
    public List<PermissionInfoDO> getPermissionInfoListFromCache(Collection<Integer> permissionTypes, Collection<Integer> permissionInfoStatuses) {
        // 任一一个参数为空，则返回空
        if (RoakerCollectionUtils.isAnyEmpty(permissionTypes, permissionInfoStatuses)) {
            return Collections.emptyList();
        }
        // 创建新数组，避免缓存被修改
        return permissionInfoCache.values().stream().filter(permissionInfo -> permissionTypes.contains(permissionInfo.getType().getCode())
                        && permissionInfoStatuses.contains(permissionInfo.getStatus().getCode()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionInfoDO> getPermissionInfoListFromCache(Collection<String> permissionInfoIds, Collection<Integer> permissionInfoTypes,
                                                                 Collection<Integer> permissionInfosStatuses) {
        // 任一一个参数为空，则返回空
        if (RoakerCollectionUtils.isAnyEmpty(permissionInfoIds, permissionInfoTypes, permissionInfosStatuses)) {
            return Collections.emptyList();
        }
        return permissionInfoCache.values().stream().filter(permissionInfoDO -> permissionInfoIds.contains(permissionInfoDO.getPermissionId())
                        && permissionInfoTypes.contains(permissionInfoDO.getType().getCode())
                        && permissionInfosStatuses.contains(permissionInfoDO.getStatus().getCode()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionInfoDO> getPermissionInfoListByPermissionInfoFromCache(String permission) {
        return new ArrayList<>(permissionPermissionInfoCache.get(permission));
    }

    @Override
    public PermissionInfoDO getPermissionInfo(String permissionId) {
        return permissionInfoMapper.selectByPermissionId(permissionId);
    }

    /**
     * 校验父菜单是否合法
     * <p>
     * 1. 不能设置自己为父菜单
     * 2. 父菜单不存在
     * 3. 父菜单必须是 {@link PermissionTypeEnum#MENU} 菜单类型
     *
     * @param parentId 父菜单编号
     * @param childId  当前菜单编号
     */
    @VisibleForTesting
    void validateParentPermissionInfo(String parentId, String childId) {
        if (parentId == null || ID_ROOT.equals(parentId)) {
            return;
        }
        // 不能设置自己为父菜单
        if (parentId.equals(childId)) {
            throw exception(PermissionInfo_PARENT_ERROR);
        }
        PermissionInfoDO permissionInfoDO = permissionInfoMapper.selectByPermissionId(parentId);
        // 父菜单不存在
        if (permissionInfoDO == null) {
            throw exception(PermissionInfo_PARENT_NOT_EXISTS);
        }
        // 父菜单必须是目录或者菜单类型
        if (PermissionTypeEnum.DIR != permissionInfoDO.getType()
                && PermissionTypeEnum.MENU != permissionInfoDO.getType()) {
            throw exception(PermissionInfo_PARENT_NOT_DIR_OR_PermissionInfo);
        }
    }

    /**
     * 校验菜单是否合法
     * <p>
     * 1. 校验相同父菜单编号下，是否存在相同的菜单名
     *
     * @param name         菜单名字
     * @param parentId     父菜单编号
     * @param permissionId 菜单编号
     */
    @VisibleForTesting
    void validatePermissionInfo(String parentId, String name, String permissionId) {
        PermissionInfoDO permissionInfoDO = permissionInfoMapper.selectByParentIdAndName(parentId, name);
        if (permissionInfoDO == null) {
            return;
        }
        // 如果 permissionId 为空，说明不用比较是否为相同 permissionId 的菜单
        if (permissionId == null) {
            throw exception(PermissionInfo_NAME_DUPLICATE);
        }
        if (!permissionInfoDO.getPermissionId().equals(permissionId)) {
            throw exception(PermissionInfo_NAME_DUPLICATE);
        }
    }

    /**
     * 初始化菜单的通用属性。
     * <p>
     * 例如说，只有目录或者菜单类型的菜单，才设置 icon
     *
     * @param permissionInfoDO 菜单
     */
    private void initPermissionInfoProperty(PermissionInfoDO permissionInfoDO) {
        // 菜单为按钮类型时，无需 component、icon、path 属性，进行置空
        if (PermissionTypeEnum.BUTTON == permissionInfoDO.getType()) {
            permissionInfoDO.setComponent("");
            permissionInfoDO.setComponentName("");
            permissionInfoDO.setIcon("");
            permissionInfoDO.setPath("");
        }
    }

}
