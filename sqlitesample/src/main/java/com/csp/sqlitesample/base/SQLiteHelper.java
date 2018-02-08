package com.csp.sqlitesample.base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.csp.database.operate.base.SqlGenerate;
import com.csp.database.operate.bean.TableField;
import com.csp.database.operate.interfaces.SQLiteHelperInterface;
import com.csp.sqlitesample.config.DatabaseConfig;
import com.csp.sqlitesample.config.TableFields;
import com.csp.sqlitesample.util.LogCat;

/**
 * Description: SQLiteOpenHelper 工具类
 * Created by csp on 2017/04/24.
 */
public class SQLiteHelper extends SQLiteOpenHelper implements SQLiteHelperInterface {
	public final static String DATABASE_NAME = "sqlite.db";
	public final static int DATABASE_VERSION = 1;
	private final String ERROR_DATABASE_OPEN_FAILED = "database open failed";

	private static SQLiteHelper instance; // Singleton mode
	private SQLiteDatabase database;
	private int openDatabaseCount = 0; // if count less than 0, invoke close()

	private final Context context;

	private SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
		getWritableDatabase();
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
				database = getWritableDatabase();
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
			throw new IllegalStateException(ERROR_DATABASE_OPEN_FAILED);
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
			throw new IllegalStateException(ERROR_DATABASE_OPEN_FAILED);

		database.beginTransaction();
	}

	@Override
	public void setTransactionSuccessful() {
		if (database == null)
			throw new IllegalStateException(ERROR_DATABASE_OPEN_FAILED);

		database.setTransactionSuccessful();
	}

	@Override
	public void endTransaction() {
		if (database == null)
			throw new IllegalStateException(ERROR_DATABASE_OPEN_FAILED);

		database.endTransaction();
	}
}