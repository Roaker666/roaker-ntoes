package com.roaker.notes.file.core.enums;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.file.core.client.FileClient;
import com.roaker.notes.file.core.client.FileClientConfig;
import com.roaker.notes.file.core.client.db.DBFileClient;
import com.roaker.notes.file.core.client.db.DBFileClientConfig;
import com.roaker.notes.file.core.client.ftp.FtpFileClient;
import com.roaker.notes.file.core.client.ftp.FtpFileClientConfig;
import com.roaker.notes.file.core.client.local.LocalFileClient;
import com.roaker.notes.file.core.client.local.LocalFileClientConfig;
import com.roaker.notes.file.core.client.s3.S3FileClient;
import com.roaker.notes.file.core.client.s3.S3FileClientConfig;
import com.roaker.notes.file.core.client.sftp.SftpFileClient;
import com.roaker.notes.file.core.client.sftp.SftpFileClientConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lei.rao
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public enum FileStorageEnum {

    DB(1, DBFileClientConfig.class, DBFileClient.class),

    LOCAL(10, LocalFileClientConfig.class, LocalFileClient.class),
    FTP(11, FtpFileClientConfig.class, FtpFileClient.class),
    SFTP(12, SftpFileClientConfig.class, SftpFileClient.class),

    S3(20, S3FileClientConfig.class, S3FileClient.class),
    ;

    /**
     * 存储器
     */
    @EnumValue
    private final Integer storage;

    /**
     * 配置类
     */
    private final Class<? extends FileClientConfig> configClass;
    /**
     * 客户端类
     */
    private final Class<? extends FileClient> clientClass;

    public static FileStorageEnum getByStorage(Integer storage) {
        return ArrayUtil.firstMatch(o -> o.getStorage().equals(storage), values());
    }
}
