package com.roaker.notes.uc.service.permission.impl;

import com.google.common.annotations.VisibleForTesting;
import com.roaker.notes.commons.utils.BeanUtils;
import com.roaker.notes.enums.PermissionTypeEnum;
import com.roaker.notes.uc.dal.dataobject.permission.PermissionInfoDO;
import com.roaker.notes.uc.dal.mapper.permission.PermissionInfoMapper;
import com.roaker.notes.uc.service.permission.PermissionInfoService;
import com.roaker.notes.uc.service.permission.RolePermissionCoreService;
import com.roaker.notes.uc.vo.permission.PermissionInfoCreateReqVO;
import com.roaker.notes.uc.vo.permission.PermissionInfoListReqVO;
import com.roaker.notes.uc.vo.permission.PermissionInfoUpdateReqVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static com.roaker.notes.commons.constants.ErrorCodeConstants.*;
import static com.roaker.notes.commons.utils.RoakerCollectionUtils.convertList;
import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;
import static com.roaker.notes.uc.dal.dataobject.permission.PermissionInfoDO.ID_ROOT;


/**
 * 菜单 Service 实现
 *
 * @author lei.rao
 */
@Service
@Slf4j
public class PermissionInfoServiceImpl implements PermissionInfoService {

    @Resource
    private PermissionInfoMapper permissionInfoMapper;
    @Resource
    private RolePermissionCoreService rolePermissionCoreService;
    @Override
    public Long createMenu(PermissionInfoCreateReqVO createReqVO) {
        // 校验父菜单存在
        validateParentMenu(createReqVO.getParentId(), null);
        // 校验菜单（自己）
        validateMenu(createReqVO.getParentId(), createReqVO.getName(), null);

        // 插入数据库
        PermissionInfoDO menu = BeanUtils.toBean(createReqVO, PermissionInfoDO.class);
        initMenuProperty(menu);
        permissionInfoMapper.insert(menu);
        // 返回
        return menu.getId();
    }

    @Override
    public void updateMenu(PermissionInfoUpdateReqVO updateReqVO) {
        // 校验更新的菜单是否存在
        if (permissionInfoMapper.selectById(updateReqVO.getId()) == null) {
            throw exception(PermissionInfo_NOT_EXISTS);
        }
        // 校验父菜单存在
        validateParentMenu(updateReqVO.getParentId(), updateReqVO.getId());
        // 校验菜单（自己）
        validateMenu(updateReqVO.getParentId(), updateReqVO.getName(), updateReqVO.getId());

        // 更新到数据库
        PermissionInfoDO updateObj = BeanUtils.toBean(updateReqVO, PermissionInfoDO.class);
        initMenuProperty(updateObj);
        permissionInfoMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        // 校验是否还有子菜单
        if (permissionInfoMapper.selectCountByParentId(id) > 0) {
            throw exception(PermissionInfo_EXISTS_CHILDREN);
        }
        // 校验删除的菜单是否存在
        if (permissionInfoMapper.selectById(id) == null) {
            throw exception(PermissionInfo_NOT_EXISTS);
        }
        // 标记删除
        permissionInfoMapper.deleteById(id);
        // 删除授予给角色的权限
        rolePermissionCoreService.processMenuDeleted(id);
    }

    @Override
    public List<PermissionInfoDO> getMenuList() {
        return permissionInfoMapper.selectList();
    }

    @Override
    public List<PermissionInfoDO> getMenuListByTenant(PermissionInfoListReqVO reqVO) {
        List<PermissionInfoDO> menus = getMenuList(reqVO);
        // 开启多租户的情况下，需要过滤掉未开通的菜单
//        tenantService.handleTenantMenu(menuIds -> menus.removeIf(menu -> !CollUtil.contains(menuIds, menu.getId())));
        return menus;
    }

    @Override
    public List<PermissionInfoDO> getMenuList(PermissionInfoListReqVO reqVO) {
        return permissionInfoMapper.selectList(reqVO);
    }

    @Override
    public List<Long> getMenuIdListByPermissionFromCache(String permission) {
        List<PermissionInfoDO> menus = permissionInfoMapper.selectListByPermission(permission);
        return convertList(menus, PermissionInfoDO::getId);
    }

    @Override
    public PermissionInfoDO getMenu(Long id) {
        return permissionInfoMapper.selectById(id);
    }

    @Override
    public List<PermissionInfoDO> getMenuList(Collection<Long> ids) {
        return permissionInfoMapper.selectBatchIds(ids);
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
    void validateParentMenu(Long parentId, Long childId) {
        if (parentId == null || ID_ROOT.equals(parentId)) {
            return;
        }
        // 不能设置自己为父菜单
        if (parentId.equals(childId)) {
            throw exception(PermissionInfo_PARENT_ERROR);
        }
        PermissionInfoDO menu = permissionInfoMapper.selectById(parentId);
        // 父菜单不存在
        if (menu == null) {
            throw exception(PermissionInfo_PARENT_NOT_EXISTS);
        }
        // 父菜单必须是目录或者菜单类型
        if (PermissionTypeEnum.DIR != menu.getType()
                && PermissionTypeEnum.MENU != menu.getType()) {
            throw exception(PermissionInfo_PARENT_NOT_DIR_OR_PermissionInfo);
        }
    }

    /**
     * 校验菜单是否合法
     * <p>
     * 1. 校验相同父菜单编号下，是否存在相同的菜单名
     *
     * @param name     菜单名字
     * @param parentId 父菜单编号
     * @param id       菜单编号
     */
    @VisibleForTesting
    void validateMenu(Long parentId, String name, Long id) {
        PermissionInfoDO menu = permissionInfoMapper.selectByParentIdAndName(parentId, name);
        if (menu == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的菜单
        if (id == null) {
            throw exception(PermissionInfo_NAME_DUPLICATE);
        }
        if (!menu.getId().equals(id)) {
            throw exception(PermissionInfo_NAME_DUPLICATE);
        }
    }

    /**
     * 初始化菜单的通用属性。
     * <p>
     * 例如说，只有目录或者菜单类型的菜单，才设置 icon
     *
     * @param menu 菜单
     */
    private void initMenuProperty(PermissionInfoDO menu) {
        // 菜单为按钮类型时，无需 component、icon、path 属性，进行置空
        if (PermissionTypeEnum.BUTTON == menu.getType()) {
            menu.setComponent("");
            menu.setComponentName("");
            menu.setIcon("");
            menu.setPath("");
        }
    }

}
