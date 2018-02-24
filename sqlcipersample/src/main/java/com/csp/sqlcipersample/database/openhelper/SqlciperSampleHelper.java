package com.csp.sqlcipersample.database.openhelper;

import android.content.Context;

import com.csp.database.operate.openhelper.SqlOpenHelperInterface;
import com.csp.sqlcipersample.database.config.TableFields;

/**
 * Description: sqlite open helper
 * <p>Create Date: 2017/04/24
 * <p>Modify Date: nothing
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
public class SqlciperSampleHelper extends SqlciperHelper implements SqlOpenHelperInterface {
    private static SqlciperHelper instance;

    private SqlciperSampleHelper() {
        super(null, null, null, 0, null);
    }

    public static SqlciperHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (SqlciperSampleHelper.class) {
                if (instance == null)
                    instance = new SqlciperHelper(context,
                            TableFields.DATABASE_NAME,
                            null,
                            TableFields.DATABASE_VERSION,
                            TableFields.ALL_TABLES);
            }
        }
        return instance;
    }
}