package com.csp.sqlitesample.database.base;

import android.content.Context;

import com.csp.database.operate.base.CommonOperate;
import com.csp.database.operate.bean.TableField;
import com.csp.database.operate.interfaces.TableBeanInterface;
import com.csp.sqlitesample.database.sqlite.SQLiteHelper;
import com.csp.sqlitesample.util.LogCat;

import java.util.List;

/**
 * Description: sql operate common, specify the database type
 * <p>Create Date: 2017/04/24
 * <p>Modify Date: 2018/02/09
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
@SuppressWarnings("unused")
public abstract class BaseOperate<T extends TableBeanInterface> extends CommonOperate<T> {

    public BaseOperate(Context context, TableField tableField, Class<T> tblBeanClass) {
        super(SQLiteHelper.getInstance(context), tableField, tblBeanClass);
    }

    @Override
    protected void printStackTrace(Exception exception) {
        LogCat.printStackTrace(exception);
    }

    public List<T> getData() {
        return getData(null, null, null);
    }

    public List<T> getData(String[] whereKey, String[] whereValue) {
        return getData(whereKey, whereValue, null);
    }

    public T getDatum(String[] whereKey, String[] whereValue) {
        return getDatum(whereKey, whereValue, null);
    }
}