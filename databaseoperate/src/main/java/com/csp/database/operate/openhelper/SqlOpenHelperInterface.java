package com.csp.database.operate.openhelper;

import android.database.Cursor;

/**
 * Description: database operate interface, include open/connect, close, delete ...
 * <p>Create Date: 2017/04/24
 * <p>Modify Date: nothing
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public interface SqlOpenHelperInterface {
    /**
     * 创建数据库
     *
     * @param password 数据库加密密码, "" 或 null: 不加密
     */
    boolean createDatabase(String password);

    /**
     * 删除数据库
     *
     * @return true: 删除成功
     */
    boolean deleteDatabase();

    /**
     * 开启数据库
     *
     * @param password 密码
     */
    void openDatabase(String password);

    /**
     * 关闭数据库
     */
    void closeDatabase();

    /**
     * 查询数据库
     *
     * @param sql           查询语句
     * @param selectionArgs 查询条件值
     * @return 数据集合
     */
    Cursor querySQL(String sql, String[] selectionArgs);

    /**
     * 执行 SQL
     *
     * @param sql sql 语句
     */
    void execSQL(String sql);

    /**
     * 开启事务
     */
    void beginTransaction();

    /**
     * 设置事务成功
     */
    void setTransactionSuccessful();

    /**
     * 结束事务
     */
    void endTransaction();
}
