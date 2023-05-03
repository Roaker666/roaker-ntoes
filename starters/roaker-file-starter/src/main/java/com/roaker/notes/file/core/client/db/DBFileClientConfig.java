package com.roaker.notes.file.core.client.db;

import com.roaker.notes.file.core.client.FileClientConfig;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class DBFileClientConfig implements FileClientConfig {
    /**
     * 自定义域名
     */
    @NotEmpty(message = "domain 不能为空")
    @URL(message = "domain 必须是 URL 格式")
    private String domain;

}
