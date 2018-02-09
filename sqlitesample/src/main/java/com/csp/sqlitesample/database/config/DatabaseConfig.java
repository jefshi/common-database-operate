package com.csp.sqlitesample.database.config;

/**
 * Description: 数据库配置
 * <p>Create Date: 2017/04/22
 * <p>Modify Date: 无
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
public interface DatabaseConfig {
    boolean DEBUG = true;

    String DATABASE_NAME = "sqlite.db";
    int DATABASE_VERSION = 1;
}