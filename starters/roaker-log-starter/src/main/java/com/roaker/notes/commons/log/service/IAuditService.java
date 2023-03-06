package com.roaker.notes.commons.log.service;

import com.roaker.notes.commons.log.model.Audit;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface IAuditService {
    void save(Audit audit);
}
