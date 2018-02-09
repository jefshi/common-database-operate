package com.csp.sqlitesample;

import android.app.Activity;
import android.os.Bundle;

import com.csp.sqlitesample.database.operate.MoreTableOperate;
import com.csp.sqlitesample.database.operate.PhoneInfoOperate;
import com.csp.sqlitesample.database.operate.UserInfoOperate;
import com.csp.sqlitesample.database.sqlite.DatabaseOperate;
import com.csp.sqlitesample.database.tblbean.TblPhoneInfo;
import com.csp.sqlitesample.database.tblbean.TblUserInfo;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MoreTableOperate.resetDatabase(this);
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

        DatabaseOperate.printAllTable(this, null);
    }
}
