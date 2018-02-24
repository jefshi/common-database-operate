package com.csp.sqlitesample.database.openhelper;

import android.content.Context;

import com.csp.database.operate.openhelper.SQLiteHelper;
import com.csp.sqlitesample.database.config.TableFields;

/**
 * Description: 指定数据库及其表结构常量
 * <p>Create Date: 2018/02/06
 * <p>Modify Date: nothing
 *
 * @author csp
 * @version 1.0.1
 * @since common-database-operate 1.0.0
 */
public class SqliteSampleHelper extends SQLiteHelper {
    private static SQLiteHelper instance;

    private SqliteSampleHelper() {
        super(null, null, null, 0, null);
    }

    public static SQLiteHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (SqliteSampleHelper.class) {
                if (instance == null)
                    instance = new SQLiteHelper(context,
                            TableFields.DATABASE_NAME,
                            null,
                            TableFields.DATABASE_VERSION,
                            TableFields.ALL_TABLES);
            }
        }
        return instance;
    }
}
