package com.roaker.notes.ac.service.oauth2;

import com.roaker.notes.ac.controller.admin.oauth2.vo.client.Oauth2ClientCreateReqVO;
import com.roaker.notes.ac.controller.admin.oauth2.vo.client.Oauth2ClientPageReqVO;
import com.roaker.notes.ac.controller.admin.oauth2.vo.client.Oauth2ClientUpdateReqVO;
import com.roaker.notes.ac.dal.dataobject.oauth2.Oauth2ClientDO;
import com.roaker.notes.commons.db.core.dataobject.PageResult;
import jakarta.validation.Valid;

import java.util.Collection;

/**
 * Oauth2.0 Client Service 接口
 *
 * 从功能上，和 JdbcClientDetailsService 的功能，提供客户端的操作
 *
 * @author 芋道源码
 */
public interface Oauth2ClientService {

    /**
     * 初始化 Oauth2Client 的本地缓存
     */
    void initLocalCache();

    /**
     * 创建 Oauth2 客户端
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOauth2Client(@Valid Oauth2ClientCreateReqVO createReqVO);

    /**
     * 更新 Oauth2 客户端
     *
     * @param updateReqVO 更新信息
     */
    void updateOauth2Client(@Valid Oauth2ClientUpdateReqVO updateReqVO);

    /**
     * 删除 Oauth2 客户端
     *
     * @param id 编号
     */
    void deleteOauth2Client(Long id);

    /**
     * 获得 Oauth2 客户端
     *
     * @param id 编号
     * @return Oauth2 客户端
     */
    Oauth2ClientDO getOauth2Client(Long id);

    /**
     * 获得 Oauth2 客户端分页
     *
     * @param pageReqVO 分页查询
     * @return Oauth2 客户端分页
     */
    PageResult<Oauth2ClientDO> getOauth2ClientPage(Oauth2ClientPageReqVO pageReqVO);

    /**
     * 从缓存中，校验客户端是否合法
     *
     * @return 客户端
     */
    default Oauth2ClientDO validOAuthClientFromCache(String clientId) {
        return validOAuthClientFromCache(clientId, null, null, null, null);
    }

    /**
     * 从缓存中，校验客户端是否合法
     *
     * 非空时，进行校验
     *
     * @param clientId 客户端编号
     * @param clientSecret 客户端密钥
     * @param authorizedGrantType 授权方式
     * @param scopes 授权范围
     * @param redirectUri 重定向地址
     * @return 客户端
     */
    Oauth2ClientDO validOAuthClientFromCache(String clientId, String clientSecret,
                                             String authorizedGrantType, Collection<String> scopes, String redirectUri);

}
