package com.csp.sqlcipersample.config;

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

    int DATABASE_VERSION = 1;
    String DATABASE_NAME = "test.db";

    // sqlciper 用
    boolean ENCRYPT = true;
}