package com.csp.sqlitesample.database.sqlite;

import android.content.Context;

import com.csp.database.operate.interfaces.SqlOperateInterface;
import com.csp.sqlitesample.database.config.DatabaseConfig;
import com.csp.sqlitesample.database.operate.PhoneInfoOperate;
import com.csp.sqlitesample.database.operate.UserInfoOperate;
import com.csp.sqlitesample.util.EmptyUtil;
import com.csp.sqlitesample.util.LogCat;

import java.util.List;

/**
 * Created by csp on 2017/4/21.
 * Description: 数据库基础操作类
 */

/**
 * Description: database operate util
 * <p>Create Date: 2017/04/24
 * <p>Modify Date: 2018/02/09
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
@SuppressWarnings("unused")
public class DatabaseOperate {

    public static void openDatabase(Context context) {
        SQLiteHelper.getInstance(context).openDatabase(null);
    }

    public static void closeDatabase(Context context) {
        SQLiteHelper.getInstance(context).closeDatabase();
    }

    public static void beginTransaction(Context context) {
        SQLiteHelper.getInstance(context).beginTransaction();
    }

    public static void endTransaction(Context context) {
        SQLiteHelper.getInstance(context).endTransaction();
    }

    public static void setTransactionSuccessful(Context context) {
        SQLiteHelper.getInstance(context).setTransactionSuccessful();
    }

    /**
     * 打印数据库数据
     *
     * @param operate 指定表的操作对象
     * @param explain 额外说明
     * @return Log 日志
     */
    private static String printTable(SqlOperateInterface operate, String explain) {
        if (!DatabaseConfig.DEBUG)
            return null;

        String tableName = operate.getTableName();
        List data = operate.getData(null, null, null);

        String msgTop = EmptyUtil.isEmpty(explain) ? "" : explain + "][";
        msgTop = "\n--[" + msgTop + "表, " + tableName + "]";

        String msg = "";
        if (EmptyUtil.isEmpty(data)) {
            msg = msgTop + ": null";
        } else {
            for (int i = 0; i < data.size(); i++) {
                msg += msgTop + "[" + i + "]: " + String.valueOf(data.get(i));
            }
        }
        return msg;
    }

    /**
     * 打印指定表格数据
     *
     * @param operate 指定表的操作对象
     * @param explain 额外说明
     */
    public static void printSpecificTable(Context context, SqlOperateInterface operate, String explain) {
        if (!DatabaseConfig.DEBUG)
            return;

        DatabaseOperate.openDatabase(context);

        String msgTop = EmptyUtil.isEmpty(explain) ? "" : explain + "][";
        String msg = "--[" + msgTop + "数据库查询][开始]------------------------------";
        msg += printTable(operate, explain);
        msg += "\n--[" + msgTop + "数据库查询][开始]------------------------------";
        LogCat.e(2, msg);

        DatabaseOperate.closeDatabase(context);
    }

    /**
     * 打印所有表格数据
     *
     * @param context context
     * @param explain 额外说明
     */
    public static void printAllTable(Context context, String explain) {
        if (!DatabaseConfig.DEBUG)
            return;

        DatabaseOperate.openDatabase(context);

        String msgTop = EmptyUtil.isEmpty(explain) ? "" : explain + "][";
        String msg = "--[" + msgTop + "数据库查询][开始]------------------------------";
        msg += printTable(new UserInfoOperate(context), null);
        msg += printTable(new PhoneInfoOperate(context), null);
        msg += "\n--[" + msgTop + "数据库查询][结束]------------------------------";
        LogCat.e(2, msg);

        DatabaseOperate.closeDatabase(context);
    }
}
