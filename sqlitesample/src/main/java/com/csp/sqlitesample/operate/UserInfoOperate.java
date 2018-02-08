package com.csp.sqlitesample.operate;

import android.content.Context;

import com.csp.sqlitesample.base.BaseSqlOperate;
import com.csp.sqlitesample.config.TableFields;
import com.csp.sqlitesample.tblbean.TblUserInfo;

/**
 * Description: tbl_test 表操作类
 * <p>Create Date: 2017/7/18
 * <p>Modify Date: 无
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
public class UserInfoOperate extends BaseSqlOperate<TblUserInfo> {
	public UserInfoOperate(Context context) {
		super(context, TableFields.TBL_USER_INFO, TblUserInfo.class);
	}
}
