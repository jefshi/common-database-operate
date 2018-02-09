package com.csp.sqlcipersample.config;

import com.csp.database.operate.bean.TableField;

/**
 * Created by chenshp on 2018/2/6.
 */

public interface TableFields {
	TableField TBL_USER_INFO = new TableField(
			"userInfo",
			new String[]{"userId", "status", "createDate", "updateDate"},
			new String[]{null, null, "integer", "integer"},
			new String[]{"userId"});

	TableField TBL_PHONE_INFO = new TableField(
			"phoneInfo",
			new String[]{"userId", "phone", "createDate", "updateDate"},
			new String[]{null, null, "integer", "integer"},
			new String[]{"userId", "phone"});

	TableField[] ALL_TABLES = new TableField[]{
			TBL_USER_INFO,
			TBL_PHONE_INFO
	};
}
