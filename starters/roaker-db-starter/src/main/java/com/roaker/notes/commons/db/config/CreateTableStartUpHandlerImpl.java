package com.roaker.notes.commons.db.config;

import com.gitee.sunchenbin.mybatis.actable.manager.handler.StartUpHandler;
import com.gitee.sunchenbin.mybatis.actable.manager.system.SysMysqlCreateTableManager;
import com.gitee.sunchenbin.mybatis.actable.manager.util.ConfigurationUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
public class CreateTableStartUpHandlerImpl implements StartUpHandler {
    @Autowired
    private ConfigurationUtil springContextUtil;
    public static String MYSQL = "mysql";
    public static String ORACLE = "oracle";
    public static String SQLSERVER = "sqlserver";
    public static String POSTGRESQL = "postgresql";
    private static String databaseType = null;
    @Autowired
    private SysMysqlCreateTableManager sysMysqlCreateTableManager;

    public CreateTableStartUpHandlerImpl() {
    }

    @PostConstruct
    public void startHandler() {
        databaseType = this.springContextUtil.getConfig("mybatis.database.type") == null ? MYSQL : this.springContextUtil.getConfig("mybatis.database.type");
        if (MYSQL.equals(databaseType)) {
            log.info("databaseType=mysql，开始执行mysql的处理方法");
            this.sysMysqlCreateTableManager.createMysqlTable();
        } else if (ORACLE.equals(databaseType)) {
            log.info("databaseType=oracle，开始执行oracle的处理方法");
        } else if (SQLSERVER.equals(databaseType)) {
            log.info("databaseType=sqlserver，开始执行sqlserver的处理方法");
        } else if (POSTGRESQL.equals(databaseType)) {
            log.info("databaseType=postgresql，开始执行postgresql的处理方法");
        } else {
            log.info("没有找到符合条件的处理方法！");
        }

    }
}
