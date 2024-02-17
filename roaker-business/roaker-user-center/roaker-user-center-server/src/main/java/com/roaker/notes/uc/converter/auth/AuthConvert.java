package com.roaker.notes.uc.converter.auth;

import cn.hutool.core.collection.CollUtil;
import com.roaker.notes.enums.PermissionTypeEnum;
import com.roaker.notes.uc.api.sms.dto.code.SmsCodeSendReqDTO;
import com.roaker.notes.uc.api.sms.dto.code.SmsCodeUseReqDTO;
import com.roaker.notes.uc.api.social.dto.SocialUserBindReqDTO;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2AccessTokenDO;
import com.roaker.notes.uc.dal.dataobject.permission.PermissionInfoDO;
import com.roaker.notes.uc.dal.dataobject.permission.RoleInfoDO;
import com.roaker.notes.uc.dal.dataobject.user.AdminUserInfoDO;
import com.roaker.notes.uc.vo.auth.*;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.roaker.notes.commons.utils.RoakerCollectionUtils.convertSet;
import static com.roaker.notes.commons.utils.RoakerCollectionUtils.filterList;
import static com.roaker.notes.uc.dal.dataobject.permission.PermissionInfoDO.ID_ROOT;


@Mapper
public interface AuthConvert {


    AuthConvert INSTANCE = Mappers.getMapper(AuthConvert.class);

    AuthLoginRespVO convert(Oauth2AccessTokenDO bean);

    default AuthPermissionInfoRespVO convert(AdminUserInfoDO user, List<RoleInfoDO> roleList, List<PermissionInfoDO> menuList) {
        return AuthPermissionInfoRespVO.builder()
                .user(AuthPermissionInfoRespVO.UserVO.builder().uid(user.getUid()).nickname(user.getUsername()).avatar(user.getAvatar()).build())
                .roles(convertSet(roleList, RoleInfoDO::getCode))
                // 权限标识信息
                .permissions(convertSet(menuList, PermissionInfoDO::getPermission))
                // 菜单树
                .menus(buildMenuTree(menuList))
                .build();
    }

    AuthPermissionInfoRespVO.MenuVO convertTreeNode(PermissionInfoDO menu);

    /**
     * 将菜单列表，构建成菜单树
     *
     * @param menuList 菜单列表
     * @return 菜单树
     */
    default List<AuthPermissionInfoRespVO.MenuVO> buildMenuTree(List<PermissionInfoDO> menuList) {
        if (CollUtil.isEmpty(menuList)) {
            return Collections.emptyList();
        }
        // 移除按钮
        menuList.removeIf(menu -> menu.getType() == PermissionTypeEnum.BUTTON);
        // 排序，保证菜单的有序性
        menuList.sort(Comparator.comparing(PermissionInfoDO::getSort));

        // 构建菜单树
        // 使用 LinkedHashMap 的原因，是为了排序 。实际也可以用 Stream API ，就是太丑了。
        Map<Long, AuthPermissionInfoRespVO.MenuVO> treeNodeMap = new LinkedHashMap<>();
        menuList.forEach(menu -> treeNodeMap.put(menu.getId(), AuthConvert.INSTANCE.convertTreeNode(menu)));
        // 处理父子关系
        treeNodeMap.values().stream().filter(node -> !ID_ROOT.equals(node.getParentId())).forEach(childNode -> {
            // 获得父节点
            AuthPermissionInfoRespVO.MenuVO parentNode = treeNodeMap.get(childNode.getParentId());
            if (parentNode == null) {
                LoggerFactory.getLogger(getClass()).error("[buildRouterTree][resource({}) 找不到父资源({})]",
                        childNode.getId(), childNode.getParentId());
                return;
            }
            // 将自己添加到父节点中
            if (parentNode.getChildren() == null) {
                parentNode.setChildren(new ArrayList<>());
            }
            parentNode.getChildren().add(childNode);
        });
        // 获得到所有的根节点
        return filterList(treeNodeMap.values(), node -> ID_ROOT.equals(node.getParentId()));
    }

    SocialUserBindReqDTO convert(Long userId, Integer userType, AuthSocialLoginReqVO reqVO);

    SmsCodeSendReqDTO convert(AuthSmsSendReqVO reqVO);

    SmsCodeUseReqDTO convert(AuthSmsLoginReqVO reqVO, Integer scene, String usedIp);

}
