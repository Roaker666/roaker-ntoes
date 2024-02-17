package com.roaker.notes.uc.api.dept;

import com.roaker.notes.commons.utils.BeanUtils;
import com.roaker.notes.uc.api.dept.dto.PostRespDTO;
import com.roaker.notes.uc.dal.dataobject.dept.PostDO;
import com.roaker.notes.uc.service.dept.PostService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 岗位 API 实现类
 *
 * @author Roaker
 */
@Service
public class PostApiImpl implements PostApi {

    @Resource
    private PostService postService;

    @Override
    public void validPostList(Collection<Long> ids) {
        postService.validatePostList(ids);
    }

    @Override
    public List<PostRespDTO> getPostList(Collection<Long> ids) {
        List<PostDO> list = postService.getPostList(ids);
        return BeanUtils.toBean(list, PostRespDTO.class);
    }

}
