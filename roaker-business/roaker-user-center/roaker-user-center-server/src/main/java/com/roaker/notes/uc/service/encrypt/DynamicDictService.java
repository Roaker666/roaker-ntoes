package com.roaker.notes.uc.service.encrypt;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.uc.dal.dataobject.encrypt.DynamicDictDO;
import com.roaker.notes.uc.dto.encrypt.DynamicDictDTO;
import com.roaker.notes.uc.vo.encrypt.DynamicCreateReqVO;
import com.roaker.notes.uc.vo.encrypt.DynamicExportReqVO;
import com.roaker.notes.uc.vo.encrypt.DynamicPageReqVO;
import com.roaker.notes.uc.vo.encrypt.DynamicUpdateReqVO;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
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
}
