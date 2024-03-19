package com.roaker.notes.uc.service.dict;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.uc.dal.dataobject.dict.DynamicDictDO;
import com.roaker.notes.uc.dto.encrypt.DynamicDictDTO;
import com.roaker.notes.uc.vo.dict.data.DynamicCreateReqVO;
import com.roaker.notes.uc.vo.dict.data.DynamicExportReqVO;
import com.roaker.notes.uc.vo.dict.data.DynamicPageReqVO;
import com.roaker.notes.uc.vo.dict.data.DynamicUpdateReqVO;
import jakarta.validation.Valid;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface DynamicDictService {
    /**
     * 增量获得动态参数数组
     *
     * 如果 minUpdateTime 为空时，则获取所有动态参数
     *
     * @param minUpdateTime 最小更新时间
     * @return 动态参数数组
     */
    List<DynamicDictDTO> getDynamicDictList(LocalDateTime minUpdateTime);

    /**
     * 创建动态参数
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDynamicDict(@Valid DynamicCreateReqVO createReqVO);

    /**
     * 更新动态参数
     *
     * @param updateReqVO 更新信息
     */
    void updateDynamicDict(@Valid DynamicUpdateReqVO updateReqVO);

    /**
     * 删除动态参数
     *
     * @param id 编号
     */
    void deleteDynamicDict(Long id);

    /**
     * 获得动态参数
     *
     * @param id 编号
     * @return 动态参数
     */
    DynamicDictDO getDynamicDict(Long id);

    /**
     * 获得动态参数分页
     *
     * @param pageReqVO 分页查询
     * @return 动态参数分页
     */
    PageResult<DynamicDictDO> getDynamicDictPage(DynamicPageReqVO pageReqVO);

    /**
     * 获得动态参数列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 动态参数列表
     */
    List<DynamicDictDO> getDynamicDictList(DynamicExportReqVO exportReqVO);

    /**
     * 获得指定字典类型的数据数量
     *
     * @param dictType 字典类型
     * @return 数据数量
     */
    long getDictDataCountByDictType(String dictType);

    /**
     * 获得字典数据列表
     *
     * @param status   状态
     * @param dictType 字典类型
     * @return 字典数据全列表
     */
    List<DynamicDictDO> getDictDataList(@Nullable Integer status, @Nullable String dictType);


    /**
     * 校验字典数据们是否有效。如下情况，视为无效：
     * 1. 字典数据不存在
     * 2. 字典数据被禁用
     *
     * @param dictType 字典类型
     * @param values   字典数据值的数组
     */
    void validateDictDataList(String dictType, Collection<String> values);

    /**
     * 获得指定的字典数据
     *
     * @param dictType 字典类型
     * @param value    字典数据值
     * @return 字典数据
     */
    DynamicDictDO getDictData(String dictType, String value);

    /**
     * 解析获得指定的字典数据，从缓存中
     *
     * @param dictType 字典类型
     * @param label    字典数据标签
     * @return 字典数据
     */
    DynamicDictDO parseDictData(String dictType, String label);

    /**
     * 获得指定数据类型的字典数据列表
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    List<DynamicDictDO> getDictDataListByDictType(String dictType);
}
