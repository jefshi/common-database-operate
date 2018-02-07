package com.csp.sqlcipersample.operate;

import android.content.Context;

import com.csp.sqlcipersample.base.SQLiteHelper;

/**
 * Description: tbl_test 表操作类
 * <p>Create Date: 2017/7/18
 * <p>Modify Date: 无
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
public class MoreTableOperate {
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
}
