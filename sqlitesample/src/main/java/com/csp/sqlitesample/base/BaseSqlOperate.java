package com.csp.sqlitesample.base;

import android.content.Context;
import android.database.Cursor;

import com.csp.database.operate.base.SqlGenerate;
import com.csp.database.operate.bean.TableField;
import com.csp.database.operate.interfaces.SQLiteHelperInterface;
import com.csp.database.operate.interfaces.SqlOperateInterface;
import com.csp.database.operate.interfaces.TableBeanInterface;
import com.csp.sqlitesample.config.DatabaseConfig;
import com.csp.sqlitesample.operate.PhoneInfoOperate;
import com.csp.sqlitesample.operate.UserInfoOperate;
import com.csp.sqlitesample.util.LogCat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csp on 2017/4/21.
 * Description: 数据库基础操作类
 */
public abstract class BaseSqlOperate<T extends TableBeanInterface> implements SqlOperateInterface<T> {
	private SQLiteHelperInterface sqLiteHelper;
	private TableField tableField;
	private SqlGenerate sqlGenerate;
	private Class<T> tblBeanClass;

	public BaseSqlOperate(Context context, TableField tableField, Class<T> tblBeanClass) {
		sqLiteHelper = SQLiteHelper.getInstance(context);
		this.tableField = tableField;
		this.tblBeanClass = tblBeanClass;
		sqlGenerate = new SqlGenerate(tableField.getTableName());
	}

	public String getTableName() {
		return tableField.getTableName();
	}

	/**
	 * 获取相关表的所有字段集合，除了"_id"字段
	 *
	 * @return 所有字段集合
	 */
	protected String[] getAllField() {
		return tableField.getFields();
	}

	/**
	 * 获取相关表的所有构成主键的字段集合，排除"_id"字段
	 *
	 * @return 所有构成主键的字段集合
	 */
	protected String[] getPKField() {
		return tableField.getPkFields();
	}

	@Override
	public boolean addData(T dbObject) {
		if (dbObject == null)
			return false;

		List<T> list = new ArrayList<>();
		list.add(dbObject);
		return addData(list);
	}

	@Override
	public boolean addData(List<T> data) {
		if (isEmpty(data))
			return true;

		List<String[]> values = new ArrayList<>();
		long time = System.currentTimeMillis();
		for (T dbObject : data) {
			dbObject.setCreateDate(time);
			dbObject.setUpdateDate(time);
			values.add(dbObject.toFieldsValue());
		}

		String[] key = getAllField();
		String sql = sqlGenerate.insert(key, values);
		return execSQL(sql);
	}

	@Override
	public boolean delData(String[] whereKey, String[] whereValue) {
		String sql = sqlGenerate.delete(whereKey, whereValue);
		return execSQL(sql);
	}

	@Override
	public boolean delData(T dbObject) {
		return delData(getPKField(), dbObject.toPKFieldsValue());
	}

	@Override
	public boolean upData(T dbObject) {
		if (dbObject == null)
			return false;

		List<T> list = new ArrayList<>();
		list.add(dbObject);
		return upData(list);
	}

	@Override
	public boolean upData(List<T> data) {
		if (isEmpty(data))
			return true;

		sqLiteHelper.beginTransaction();
		try {
			String[] key = getAllField();
			String[] value = null;
			String[] whereKey = getPKField();
			String[] whereValue = null;
			long time = System.currentTimeMillis();
			SqlGenerate builder = sqlGenerate;
			String sql = null;
			for (T object : data) {
				object.setUpdateDate(time);

				value = object.toFieldsValue();
				whereValue = object.toPKFieldsValue();
				sql = builder.update(key, value, whereKey, whereValue);
				sqLiteHelper.execSQL(sql);
			}

			sqLiteHelper.setTransactionSuccessful();
		} catch (Exception e) {
			LogCat.printStackTrace(e);
			return false;
		} finally {
			sqLiteHelper.endTransaction();
		}
		return true;
	}

	public List<T> getData() {
		return getData(null, null, null);
	}

	public List<T> getData(String[] whereKey, String[] whereValue) {
		return getData(whereKey, whereValue, null);
	}

	@Override
	public List<T> getData(String[] whereKey, String[] whereValue, String[] orderKey) {
		String[] selectKeys = getAllField();
		String sql = sqlGenerate.query(selectKeys, whereKey, whereValue, orderKey);
		return querySQL(sql, whereValue);
	}

	public T getDatum(String[] whereKey, String[] whereValue) {
		return getDatum(whereKey, whereValue, null);
	}

	@Override
	public T getDatum(String[] whereKey, String[] whereValue, String[] orderKey) {
		String[] selectKeys = getAllField();
		String sql = sqlGenerate.query(selectKeys, whereKey, whereValue, orderKey);
		List<T> list = querySQL(sql + " LIMIT 1", whereValue);
		return isEmpty(list) ? null : list.get(0);
	}

	@Override
	public List<T> querySQL(String sql, String[] value) {
		List<T> list = new ArrayList<>();
		try {
			Cursor cursor = sqLiteHelper.querySQL(sql, value);
			List<T> data = parseData(cursor);
			list.addAll(data);
		} catch (Exception e) {
			LogCat.printStackTrace(e);
		}
		return list;
	}

	/**
	 * 将游标结果解析成表对象数据集合
	 *
	 * @param cursor 游标
	 * @return 表对象数据集合
	 */
	private List<T> parseData(Cursor cursor) throws ReflectiveOperationException {
		List<T> list = new ArrayList<>();
		if (cursor != null) {
			int columnCount = cursor.getColumnCount();
			T object = null;
			String methodName = null;
			Method method = null;
			try {
				while (cursor.moveToNext()) {
					object = tblBeanClass.newInstance();
					for (int i = 0; i < columnCount; i++) {
						methodName = getSetMethodName(cursor.getColumnName(i));
						method = tblBeanClass.getMethod(methodName, String.class);
						method.invoke(object, cursor.getString(i));
					}
					list.add(object);
				}
			} finally {
				cursor.close();
			}
		}
		return list;
	}

	/**
	 * 获取 set 方法名
	 *
	 * @param filedName 成员名
	 * @return set 方法名
	 */
	private String getSetMethodName(String filedName) {
		return "set" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
	}

	@Override
	public boolean execSQL(String sql) {
		try {
			sqLiteHelper.execSQL(sql);
		} catch (Exception e) {
			LogCat.printStackTrace(e);
			return false;
		}
		return true;
	}

	@Override
	public boolean execSQLByTransaction(String[] sqls) {
		sqLiteHelper.beginTransaction();
		try {
			for (String sql : sqls) {
				sqLiteHelper.execSQL(sql);
			}
			sqLiteHelper.setTransactionSuccessful();
		} catch (Exception e) {
			LogCat.printStackTrace(e);
			return false;
		} finally {
			sqLiteHelper.endTransaction();
		}
		return true;
	}

	@Override
	public boolean execSQLByTransaction(List<T> sqls) {
		return execSQLByTransaction((String[]) sqls.toArray());
	}

	/**
	 * 连接数据库, 一次操作只调用一次
	 *
	 * @param context context
	 */
	public static void openDatabase(Context context) {
		SQLiteHelper.getInstance(context).openDatabase(null);
	}

	/**
	 * 关闭数据库连接
	 *
	 * @param context context
	 */
	public static void closeDatabase(Context context) {
		SQLiteHelper.getInstance(context).closeDatabase();
	}

	/**
	 * 字符串是否为空
	 *
	 * @param str 字符串
	 * @return true: 是
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

	/**
	 * 集合是否为空
	 *
	 * @param list 集合
	 * @return true: 是
	 */
	public static boolean isEmpty(List list) {
		return list == null || list.isEmpty();
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 集合
	 * @return true: 是
	 */
	public static boolean isEmpty(Object[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * Log 打印数据库数据
	 *
	 * @param sqlciper 需要查询的数据库操作对象
	 * @param explain  打印说明
	 * @return Log 日志
	 */
	private static String printData(BaseSqlOperate sqlciper, String explain) {
		List data = sqlciper.getData();

		String msgTop = isEmpty(explain) ? "" : explain + "][";
		msgTop = "\n--[" + msgTop + "表, " + sqlciper.getTableName() + "]";

		String msg = "";
		if (isEmpty(data)) {
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
	@SuppressWarnings("PointlessBooleanExpression")
	public static void printOneData(Context context, BaseSqlOperate sqlciper, String explain) {
		if (!DatabaseConfig.DEBUG)
			return;

		SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
		sqLiteHelper.openDatabase(null);

		String msgTop = isEmpty(explain) ? "" : explain + "][";
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
	@SuppressWarnings("PointlessBooleanExpression")
	public static void printAllData(Context context, String explain) {
		if (!DatabaseConfig.DEBUG)
			return;

		SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
		sqLiteHelper.openDatabase(null);

		String msgTop = isEmpty(explain) ? "" : explain + "][";
		String msg = "--[" + msgTop + "数据库查询][开始]------------------------------";
		msg += printData(new UserInfoOperate(context), null);
		msg += printData(new PhoneInfoOperate(context), null);
		msg += "\n--[" + msgTop + "数据库查询][结束]------------------------------";
		LogCat.e(2, msg);

		sqLiteHelper.closeDatabase();
	}
}
