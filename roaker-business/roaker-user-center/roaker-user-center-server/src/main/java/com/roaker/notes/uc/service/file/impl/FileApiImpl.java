package com.roaker.notes.uc.service.file.impl;

import com.roaker.notes.uc.api.encrypt.FileApi;
import com.roaker.notes.uc.service.file.FileService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

/**
 * @author lei.rao
 * @since 1.0
 */
@Service
@Validated
public class FileApiImpl implements FileApi {

    @Resource
    private FileService fileService;

    @Override
    public String createFile(String name, String path, byte[] content) {
        return fileService.createFile(name, path, content);
    }

}