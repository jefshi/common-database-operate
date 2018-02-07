package com.csp.sqlitesample;

import android.app.Activity;
import android.os.Bundle;

import com.csp.sqlitesample.base.BaseSqlciper;
import com.csp.sqlitesample.operate.MoreTableOperate;
import com.csp.sqlitesample.operate.PhoneInfoOperate;
import com.csp.sqlitesample.operate.UserInfoOperate;
import com.csp.sqlitesample.tblbean.TblPhoneInfo;
import com.csp.sqlitesample.tblbean.TblUserInfo;

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
	}
}
