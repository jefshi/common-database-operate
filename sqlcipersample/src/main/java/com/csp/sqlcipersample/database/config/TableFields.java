package com.csp.sqlcipersample.database.config;

import com.csp.database.operate.bean.TableField;

/**
 * Description: 指定数据库及其表结构常量
 * <p>Create Date: 2018/02/06
 * <p>Modify Date: nothing
 *
 * @author csp
 * @version 1.0.1
 * @since common-database-operate 1.0.0
 */
public interface TableFields {
    String DATABASE_NAME = "sqlcipersample.db";
    int DATABASE_VERSION = 1;

    TableField TBL_USER_INFO = new TableField(
            "userInfo",
            new String[]{"userId", "status"},
            new String[]{"text not null", "text"},
            new String[]{"userId"});

    TableField TBL_PHONE_INFO = new TableField(
            "phoneInfo",
            new String[]{"userId", "phone"},
            new String[]{"text not null", "text not null"},
            new String[]{"userId", "phone"});

    TableField[] ALL_TABLES = new TableField[]{
            TBL_USER_INFO,
            TBL_PHONE_INFO
    };
}
