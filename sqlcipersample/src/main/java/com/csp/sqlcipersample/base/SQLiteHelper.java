package com.csp.sqlcipersample.base;

import android.content.Context;

import com.csp.database.operate.base.SqlGenerate;
import com.csp.database.operate.bean.TableField;
import com.csp.database.operate.interfaces.SqlOpenHelperInterface;
import com.csp.sqlcipersample.config.DatabaseConfig;
import com.csp.sqlcipersample.config.TableFields;
import com.csp.sqlitesample.util.LogCat;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * Description: SQLiteOpenHelper 工具类
 * Created by csp on 2017/04/24.
 */
public class SQLiteHelper extends SQLiteOpenHelper implements SqlOpenHelperInterface {
	public final static String DATABASE_NAME = "sqlite.db";
	public final static int DATABASE_VERSION = 1;

	private final String DATABASE_NOT_OPEN = "database not open"; // 错误信息: 数据库未连接成功、数据库不存在
	private final Context context;
	private static SQLiteHelper instance = null;
	private SQLiteDatabase database;
	private int openDatabaseCount = 0; // 连接数据库次数

	private SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		SQLiteDatabase.loadLibs(context);
		this.context = context;
	}

	public static SQLiteHelper getInstance(Context context) {
		if (instance == null) {
			synchronized (SQLiteHelper.class) {
				instance = new SQLiteHelper(context.getApplicationContext());
			}
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		TableField[] tables = TableFields.ALL_TABLES;
		SqlGenerate builder = new SqlGenerate();
		for (TableField tableField : tables) {
			builder.setTableName(tableField.getTableName());
			String sql = builder.createTable(tableField.getFields(), tableField.getPkFields());
			if (DatabaseConfig.DEBUG)
				LogCat.e("--[Create SQL]: ", sql);

			sqLiteDatabase.execSQL(sql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
	}

	@Override
	public synchronized void createDatabase(String password) {
		getWritableDatabase(password);
		openDatabaseCount++;
		closeDatabase();
	}

	@Override
	public synchronized boolean deleteDatabase() {
		if (context.deleteDatabase(DATABASE_NAME)) {
			database = null;
			openDatabaseCount = 0;
			return true;
		}
		return false;
	}

	@Override
	public synchronized void openDatabase(String password) {
		openDatabaseCount++;
		try {
			if (database == null)
				database = getWritableDatabase(password);
		} catch (Exception e) {
			LogCat.printStackTrace(e);
		}
	}

	@Override
	public synchronized void closeDatabase() {
		openDatabaseCount--;
		if (openDatabaseCount <= 0) {
			this.close();
			database = null;
			openDatabaseCount = 0;
		}
	}

	@Override
	public Cursor querySQL(String sql, String[] selectionArgs) {
		if (database == null)
			throw new IllegalStateException(DATABASE_NOT_OPEN);
		else
			return database.rawQuery(sql, selectionArgs);
	}

	@Override
	public void execSQL(String sql) {
		database.execSQL(sql);
	}

	@Override
	public void beginTransaction() {
		if (database == null)
			throw new IllegalStateException(DATABASE_NOT_OPEN);

		database.beginTransaction();
	}

	@Override
	public void setTransactionSuccessful() {
		if (database == null)
			throw new IllegalStateException(DATABASE_NOT_OPEN);

		database.setTransactionSuccessful();
	}

	@Override
	public void endTransaction() {
		if (database == null)
			throw new IllegalStateException(DATABASE_NOT_OPEN);

		database.endTransaction();
	}
}