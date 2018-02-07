package com.csp.sqlcipersample;

import android.app.Activity;
import android.os.Bundle;

import com.csp.sqlcipersample.base.BaseSqlciper;
import com.csp.sqlcipersample.operate.MoreTableOperate;
import com.csp.sqlcipersample.operate.PhoneInfoOperate;
import com.csp.sqlcipersample.operate.UserInfoOperate;
import com.csp.sqlcipersample.tblbean.TblPhoneInfo;
import com.csp.sqlcipersample.tblbean.TblUserInfo;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MoreTableOperate.resetDatabase(this);

		BaseSqlciper.openDatabase(this);

		UserInfoOperate uio = new UserInfoOperate(this);

		TblUserInfo tblUserInfo = new TblUserInfo();
		tblUserInfo.setUserId("police");
		tblUserInfo.setStatus("enable");
		uio.addData(tblUserInfo);

		tblUserInfo = new TblUserInfo();
		tblUserInfo.setUserId("firemen");
		tblUserInfo.setStatus("enable");
		uio.addData(tblUserInfo);

		TblPhoneInfo tblPhoneInfo = new TblPhoneInfo();
		tblPhoneInfo.setUserId("police");
		tblPhoneInfo.setPhone("110");

		new PhoneInfoOperate(this).addData(tblPhoneInfo);

		BaseSqlciper.closeDatabase(this);

		BaseSqlciper.printAllData(this, null);
	}
}
