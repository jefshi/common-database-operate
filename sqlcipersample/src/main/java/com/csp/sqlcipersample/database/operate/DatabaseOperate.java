package com.csp.sqlcipersample.database.operate;

import android.content.Context;

import com.csp.database.operate.operate.SqlOperateInterface;
import com.csp.database.operate.util.EmptyUtil;
import com.csp.database.operate.util.LogCat;
import com.csp.sqlcipersample.database.config.DatabaseConfig;
import com.csp.sqlcipersample.database.openhelper.SqlciperHelper;
import com.csp.sqlcipersample.database.openhelper.SqlciperSampleHelper;

import java.util.List;

/**
 * Description: database operate util
 * <p>Create Date: 2017/04/24
 * <p>Modify Date: 2018/02/09
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class DatabaseOperate {

    public static void openDatabase(Context context) {
        SqlciperSampleHelper.getInstance(context).openDatabase("aaa");
    }

    public static void closeDatabase(Context context) {
        SqlciperSampleHelper.getInstance(context).closeDatabase();
    }

    public static void beginTransaction(Context context) {
        SqlciperSampleHelper.getInstance(context).beginTransaction();
    }

    public static void endTransaction(Context context) {
        SqlciperSampleHelper.getInstance(context).endTransaction();
    }

    public static void setTransactionSuccessful(Context context) {
        SqlciperSampleHelper.getInstance(context).setTransactionSuccessful();
    }

    /**
     * 数据库重置
     *
     * @return true: 操作成功
     */
    public static boolean resetDatabase(Context context) {
        SqlciperHelper helper = SqlciperSampleHelper.getInstance(context);
        return helper.deleteDatabase()
                && helper.createDatabase("aaa");
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
        LogCat.e(msg);

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
        LogCat.e(msg);

        DatabaseOperate.closeDatabase(context);
    }
}
