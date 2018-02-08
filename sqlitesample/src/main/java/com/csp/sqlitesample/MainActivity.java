package com.csp.sqlitesample;

import android.app.Activity;
import android.os.Bundle;

import com.csp.sqlitesample.base.BaseSqlOperate;
import com.csp.sqlitesample.base.DatabaseOperate;
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

        DatabaseOperate.resetDatabase(this);
        DatabaseOperate.openDatabase(this);

        try {
            DatabaseOperate.beginTransaction(this);

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

            DatabaseOperate.setTransactionSuccessful(this);
        } catch (Exception e) {

        } finally {
            DatabaseOperate.endTransaction(this);
        }

        DatabaseOperate.closeDatabase(this);

        DatabaseOperate.printAllData(this, null);
    }
}
