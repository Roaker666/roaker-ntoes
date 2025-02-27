package com.roaker.notes.uc.service.dept.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.annotations.VisibleForTesting;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.uc.controller.dept.vo.dept.DeptCreateReqVO;
import com.roaker.notes.uc.controller.dept.vo.dept.DeptListReqVO;
import com.roaker.notes.uc.controller.dept.vo.dept.DeptUpdateReqVO;
import com.roaker.notes.uc.converter.dept.DeptConvert;
import com.roaker.notes.uc.dal.dataobject.dept.DeptDO;
import com.roaker.notes.uc.dal.mapper.dept.DeptMapper;
import com.roaker.notes.uc.enums.dept.DeptIdEnum;
import com.roaker.notes.uc.service.dept.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.*;

import static com.roaker.notes.commons.constants.ErrorCodeConstants.*;
import static com.roaker.notes.commons.utils.RoakerCollectionUtils.convertSet;
import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;


/**
 * 部门 Service 实现类
 *
 * @author Roaker
 */
@Service
@Validated
@Slf4j
public class DeptServiceImpl implements DeptService {

    @Resource
    private DeptMapper deptMapper;

    @Override
    public Long createDept(DeptCreateReqVO reqVO) {
        // 校验正确性
        if (reqVO.getParentId() == null) {
            reqVO.setParentId(DeptIdEnum.ROOT.getId());
        }
        validateForCreateOrUpdate(null, reqVO.getParentId(), reqVO.getName());
        // 插入部门
        DeptDO dept = DeptConvert.INSTANCE.convert(reqVO);
        deptMapper.insert(dept);
        return dept.getId();
    }

    @Override
    public void updateDept(DeptUpdateReqVO reqVO) {
        // 校验正确性
        if (reqVO.getParentId() == null) {
            reqVO.setParentId(DeptIdEnum.ROOT.getId());
        }
        validateForCreateOrUpdate(reqVO.getId(), reqVO.getParentId(), reqVO.getName());
        // 更新部门
        DeptDO updateObj = DeptConvert.INSTANCE.convert(reqVO);
        deptMapper.updateById(updateObj);
    }

    @Override
    public void deleteDept(Long id) {
        // 校验是否存在
        validateDeptExists(id);
        // 校验是否有子部门
        if (deptMapper.selectCountByParentId(id) > 0) {
            throw exception(DEPT_EXITS_CHILDREN);
        }
        // 删除部门
        deptMapper.deleteById(id);
    }

    private void validateForCreateOrUpdate(Long id, Long parentId, String name) {
        // 校验自己存在
        validateDeptExists(id);
        // 校验父部门的有效性
        validateParentDept(id, parentId);
        // 校验部门名的唯一性
        validateDeptNameUnique(id, parentId, name);
    }

    @VisibleForTesting
    void validateDeptExists(Long id) {
        if (id == null) {
            return;
        }
        DeptDO dept = deptMapper.selectById(id);
        if (dept == null) {
            throw exception(DEPT_NOT_FOUND);
        }
    }

    @VisibleForTesting
    void validateParentDept(Long id, Long parentId) {
        if (parentId == null || DeptIdEnum.ROOT.getId().equals(parentId)) {
            return;
        }
        // 不能设置自己为父部门
        if (parentId.equals(id)) {
            throw exception(DEPT_PARENT_ERROR);
        }
        // 父岗位不存在
        DeptDO dept = deptMapper.selectById(parentId);
        if (dept == null) {
            throw exception(DEPT_PARENT_NOT_EXITS);
        }
        // 父部门不能是原来的子部门
        List<DeptDO> children = getChildDeptList(id);
        if (children.stream().anyMatch(dept1 -> dept1.getId().equals(parentId))) {
            throw exception(DEPT_PARENT_IS_CHILD);
        }
    }

    @VisibleForTesting
    void validateDeptNameUnique(Long id, Long parentId, String name) {
        DeptDO dept = deptMapper.selectByParentIdAndName(parentId, name);
        if (dept == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw exception(DEPT_NAME_DUPLICATE);
        }
        if (ObjectUtil.notEqual(dept.getId(), id)) {
            throw exception(DEPT_NAME_DUPLICATE);
        }
    }

    @Override
    public DeptDO getDept(Long id) {
        return deptMapper.selectById(id);
    }

    @Override
    public List<DeptDO> getDeptList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return deptMapper.selectBatchIds(ids);
    }

    @Override
    public List<DeptDO> getDeptList(DeptListReqVO reqVO) {
        return deptMapper.selectList(reqVO);
    }

    @Override
    public List<DeptDO> getChildDeptList(Long id) {
        List<DeptDO> children = new LinkedList<>();
        // 遍历每一层
        Collection<Long> parentIds = Collections.singleton(id);
        for (int i = 0; i < Short.MAX_VALUE; i++) { // 使用 Short.MAX_VALUE 避免 bug 场景下，存在死循环
            // 查询当前层，所有的子部门
            List<DeptDO> depts = deptMapper.selectListByParentId(parentIds);
            // 1. 如果没有子部门，则结束遍历
            if (CollUtil.isEmpty(depts)) {
                break;
            }
            // 2. 如果有子部门，继续遍历
            children.addAll(depts);
            parentIds = convertSet(depts, DeptDO::getId);
        }
        return children;
    }

    @Override
    public Set<Long> getChildDeptIdListFromCache(Long id) {
        List<DeptDO> children = getChildDeptList(id);
        return convertSet(children, DeptDO::getId);
    }

    @Override
    public void validateDeptList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得科室信息
        Map<Long, DeptDO> deptMap = getDeptMap(ids);
        // 校验
        ids.forEach(id -> {
            DeptDO dept = deptMap.get(id);
            if (dept == null) {
                throw exception(DEPT_NOT_FOUND);
            }
            if (CommonStatusEnum.DISABLE == dept.getStatus()) {
                throw exception(DEPT_NOT_ENABLE, dept.getName());
            }
        });
    }

}
