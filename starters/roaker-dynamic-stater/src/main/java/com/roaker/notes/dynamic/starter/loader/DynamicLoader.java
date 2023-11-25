package com.roaker.notes.dynamic.starter.loader;

import com.roaker.notes.exception.util.ServiceExceptionUtil;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface DynamicLoader {

    /**
     * 添加动态参数
     *
     * @param code 动态参数的编号
     * @param msg 动态参数的提示
     */
    default void putErrorCode(Integer code, String msg) {
        ServiceExceptionUtil.put(code, msg);
    }
}
