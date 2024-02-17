package com.roaker.notes.commons.utils;

import com.roaker.notes.commons.constants.ErrorCodeConstants;
import com.roaker.notes.exception.ErrorCode;
import com.roaker.notes.exception.ServerException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lei.rao
 * @since 1.0
 */
public class SqlUtils {
    /**
     * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * 检查字符，防止注入绕过
     */
    public static String escapeOrderBySql(String value) {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value))
        {
            throw new ServerException(ErrorCodeConstants.SQL_QUERY_PARAM_INVALID);
        }
        return value;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }
}
