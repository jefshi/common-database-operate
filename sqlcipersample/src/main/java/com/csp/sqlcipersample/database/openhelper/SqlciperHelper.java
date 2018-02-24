package com.csp.sqlcipersample.database.openhelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.csp.database.operate.bean.TableField;
import com.csp.database.operate.openhelper.SqlOpenHelperInterface;
import com.csp.database.operate.sql.SqlGenerate;
import com.csp.database.operate.util.LogCat;

/**
 * Description: sqlite open helper
 * <p>Create Date: 2017/04/24
 * <p>Modify Date: nothing
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
public class SqlciperHelper extends SQLiteOpenHelper implements SqlOpenHelperInterface {
    private final String ERROR_DATABASE_OPEN_FAILED = "mDatabase open failed";
    private final Context mContext;

    private String mDatabaseName;
    private TableField[] mTables;
    private SQLiteDatabase mDatabase;
    private int openDatabaseCount = 0; // if count less than 0, invoke close()

    public SqlciperHelper(Context context, String databaseName, CursorFactory factory, int version, TableField[] tables) {
        super(context, databaseName, factory, version);
        mContext = context;
        mDatabaseName = databaseName;
        mTables = tables;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        SqlGenerate builder = new SqlGenerate();
        for (TableField tableField : mTables) {
            builder.setTableName(tableField.getTableName());
            sql = builder.createTable(
                    tableField.getFields(),
                    tableField.getFieldsType(),
                    tableField.getUniqueFields());

            LogCat.e("--[Create SQL]: ", sql);

            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    @Override
    public synchronized boolean createDatabase(String password) {
        boolean result = false;
        try {
            getWritableDatabase();
            openDatabaseCount++;
            closeDatabase();
            result = true;
        } catch (Exception e) {
            LogCat.printStackTrace(e);
        }
        return result;
    }

    @Override
    public synchronized boolean deleteDatabase() {
        if (mContext.deleteDatabase(mDatabaseName)) {
            mDatabase = null;
            openDatabaseCount = 0;
            return true;
        }
        return false;
    }

    @Override
    public synchronized void openDatabase(String password) {
        openDatabaseCount++;
        try {
            if (mDatabase == null)
                mDatabase = getWritableDatabase();
        } catch (Exception e) {
            LogCat.printStackTrace(e);
        }
    }

    @Override
    public synchronized void closeDatabase() {
        openDatabaseCount--;
        if (openDatabaseCount <= 0) {
            this.close();
            mDatabase = null;
            openDatabaseCount = 0;
        }
    }

    @Override
    public Cursor querySQL(String sql, String[] selectionArgs) {
        if (mDatabase == null)
            throw new IllegalStateException(ERROR_DATABASE_OPEN_FAILED);
        else
            return mDatabase.rawQuery(sql, selectionArgs);
    }

    @Override
    public void execSQL(String sql) {
        mDatabase.execSQL(sql);
    }

    @Override
    public void beginTransaction() {
        if (mDatabase == null)
            throw new IllegalStateException(ERROR_DATABASE_OPEN_FAILED);

        mDatabase.beginTransaction();
    }

    @Override
    public void setTransactionSuccessful() {
        if (mDatabase == null)
            throw new IllegalStateException(ERROR_DATABASE_OPEN_FAILED);

        mDatabase.setTransactionSuccessful();
    }

    @Override
    public void endTransaction() {
        if (mDatabase == null)
            throw new IllegalStateException(ERROR_DATABASE_OPEN_FAILED);

        mDatabase.endTransaction();
    }
}