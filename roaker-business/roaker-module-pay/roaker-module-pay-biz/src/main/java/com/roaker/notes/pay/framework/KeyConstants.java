package com.roaker.notes.pay.framework;

/**
 * @author lei.rao
 * @since 1.0
 */
public class KeyConstants {
    /**
     * 通知任务的分布式锁
     * KEY 格式：pay_notify:lock:%d // 参数来自 DefaultLockKeyBuilder 类
     * VALUE 数据格式：HASH // RLock.class：Redisson 的 Lock 锁，使用 Hash 数据结构
     * 过期时间：不固定
     */
    public static final String PAY_NOTIFY_LOCK = "pay_notify:lock:%d";

    /**
     * 支付序号的缓存
     * KEY 格式：pay_no:{prefix}
     * VALUE 数据格式：编号自增
     */
    public static final String PAY_NO = "pay_no:";

    public static String formatNotifyTask(Long taskID) {
        return String.format(PAY_NOTIFY_LOCK, taskID);
    }

    public static String formatPayNo(Long payNo) {
        return PAY_NO + payNo;
    }
}
