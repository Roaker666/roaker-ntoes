package com.roaker.notes.uc.service.file.impl;

import com.roaker.notes.uc.api.encrypt.FileApi;
import com.roaker.notes.uc.service.file.FileService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author lei.rao
 * @since 1.0
 */
@Service
@Validated
@Slf4j
public class FileApiImpl implements FileApi {

    @Resource
    private FileService fileService;

    @Override
    public String createFile(String name, String path, byte[] content) {
        return fileService.createFile(name, path, content);
    }

    @Override
    public byte[] downloadFile(String fileUrl) {
        try {
            return fileService.downloadFile(fileUrl);
        } catch (Exception e) {
            log.error("download file[{}] error", fileUrl, e);
        }
        return null;
    }
}