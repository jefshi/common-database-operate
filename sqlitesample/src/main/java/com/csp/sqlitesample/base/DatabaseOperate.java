package com.csp.sqlitesample.base;

import android.content.Context;

import com.csp.sqlitesample.config.DatabaseConfig;
import com.csp.sqlitesample.operate.PhoneInfoOperate;
import com.csp.sqlitesample.operate.UserInfoOperate;
import com.csp.sqlitesample.util.EmptyUtil;
import com.csp.sqlitesample.util.LogCat;

import java.util.List;

/**
 * Created by chenshp on 2018/2/8.
 */

public class DatabaseOperate {
    private static boolean debug = false;

    public static void setDebug(boolean debug) {
        DatabaseOperate.debug = debug;
    }

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
     * 数据库重置
     *
     * @param context context
     * @return true: 操作成功
     */
    public static boolean resetDatabase(Context context) {
        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
        sqLiteHelper.deleteDatabase();
        sqLiteHelper.createDatabase(null);
        return true;
    }

    /**
     * Log 打印数据库数据
     *
     * @param sqlciper 需要查询的数据库操作对象
     * @param explain  打印说明
     * @return Log 日志
     */
    private static String printData(String tableName, BaseSqlOperate sqlciper, String explain) {
        List data = sqlciper.getData();

        String msgTop = EmptyUtil.isEmpty(explain) ? "" : explain + "][";
        msgTop = "\n--[" + msgTop + "表, " + sqlciper.getTableName() + "]";

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
     * Log 打印数据库数据
     *
     * @param sqlciper 需要查询的数据库操作对象
     * @param explain  查询作用说明
     */
    public static void printOneData(Context context, BaseSqlOperate sqlciper, String explain) {
        if (!DatabaseConfig.DEBUG)
            return;

        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
        sqLiteHelper.openDatabase(null);

        String msgTop = EmptyUtil.isEmpty(explain) ? "" : explain + "][";
        String msg = "--[" + msgTop + "数据库查询][开始]------------------------------";
        msg += printData(sqlciper, explain);
        msg += "\n--[" + msgTop + "数据库查询][开始]------------------------------";
        LogCat.e(2, msg);

        sqLiteHelper.closeDatabase();
    }

    /**
     * Log 打印所有数据库数据
     *
     * @param context context
     * @param explain 查询作用说明
     */
    public static void printAllData(Context context, String explain) {
        if (!DatabaseConfig.DEBUG)
            return;

        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
        sqLiteHelper.openDatabase(null);

        String msgTop = EmptyUtil.isEmpty(explain) ? "" : explain + "][";
        String msg = "--[" + msgTop + "数据库查询][开始]------------------------------";
        msg += printData(new UserInfoOperate(context), null);
        msg += printData(new PhoneInfoOperate(context), null);
        msg += "\n--[" + msgTop + "数据库查询][结束]------------------------------";
        LogCat.e(2, msg);

        sqLiteHelper.closeDatabase();
    }
}
