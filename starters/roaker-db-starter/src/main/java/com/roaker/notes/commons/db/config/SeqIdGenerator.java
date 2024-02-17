package com.roaker.notes.commons.db.config;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.roaker.notes.commons.db.core.annotation.BizKey;
import com.roaker.notes.uc.api.seq.SeqApi;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author lei.rao
 * @since 1.0
 */
@RequiredArgsConstructor
public class SeqIdGenerator extends DefaultIdentifierGenerator {
    private final SeqApi seqApi;

    @Override
    public Long nextId(Object entity) {
        BizKey bizKey = AnnotationUtils.findAnnotation(entity.getClass(), BizKey.class);
        if (bizKey == null) {
            return super.nextId(entity);
        }

        return Long.parseLong(seqApi.getSegmentId(bizKey.bizName()));
    }

    @Override
    public String nextUUID(Object entity) {
        BizKey bizKey = AnnotationUtils.findAnnotation(entity.getClass(), BizKey.class);
        if (bizKey == null) {
            return super.nextUUID(entity);
        }
        return bizKey.bizPrefix() + nextId(entity);
    }
}
