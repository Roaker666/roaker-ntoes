package com.roaker.notes.uc.service.dict;


import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.uc.dal.dataobject.dict.DynamicDictTypeDO;
import com.roaker.notes.uc.vo.dict.type.DictTypePageReqVO;
import com.roaker.notes.uc.vo.dict.type.DictTypeSaveReqVO;

import java.util.List;

/**
 * 字典类型 Service 接口
 *
 */
public interface DynamicDictTypeService {

    /**
     * 创建字典类型
     *
     * @param createReqVO 字典类型信息
     * @return 字典类型编号
     */
    Long createDictType(DictTypeSaveReqVO createReqVO);

    /**
     * 更新字典类型
     *
     * @param updateReqVO 字典类型信息
     */
    void updateDictType(DictTypeSaveReqVO updateReqVO);

    /**
     * 删除字典类型
     *
     * @param id 字典类型编号
     */
    void deleteDictType(Long id);

    /**
     * 获得字典类型分页列表
     *
     * @param pageReqVO 分页请求
     * @return 字典类型分页列表
     */
    PageResult<DynamicDictTypeDO> getDictTypePage(DictTypePageReqVO pageReqVO);

    /**
     * 获得字典类型详情
     *
     * @param id 字典类型编号
     * @return 字典类型
     */
    DynamicDictTypeDO getDictType(Long id);

    /**
     * 获得字典类型详情
     *
     * @param type 字典类型
     * @return 字典类型详情
     */
    DynamicDictTypeDO getDictType(String type);

    /**
     * 获得全部字典类型列表
     *
     * @return 字典类型列表
     */
    List<DynamicDictTypeDO> getDictTypeList();

}
