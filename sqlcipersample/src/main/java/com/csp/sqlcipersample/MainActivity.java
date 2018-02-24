package com.csp.sqlcipersample;

import android.app.Activity;
import android.os.Bundle;

import com.csp.database.operate.util.LogCat;
import com.csp.sqlcipersample.database.operate.DatabaseOperate;
import com.csp.sqlcipersample.database.operate.PhoneInfoOperate;
import com.csp.sqlcipersample.database.operate.UserInfoOperate;
import com.csp.sqlcipersample.database.tblbean.TblPhoneInfo;
import com.csp.sqlcipersample.database.tblbean.TblUserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: MainActivity
 * <p>Create Date: 2018/02/06
 * <p>Modify Date: nothing
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogCat.setDebug(true);

        DatabaseOperate.resetDatabase(this);
        DatabaseOperate.openDatabase(this);

        try {
            DatabaseOperate.beginTransaction(this);

            List<TblUserInfo> tblUserInfos = new ArrayList<>();
            TblUserInfo tblUserInfo = new TblUserInfo();
            tblUserInfo.setUserId("police");
            tblUserInfo.setStatus("enable");
            tblUserInfos.add(tblUserInfo);

            tblUserInfo = new TblUserInfo();
            tblUserInfo.setUserId("firemen");
            tblUserInfo.setStatus("enable");
            tblUserInfos.add(tblUserInfo);

            UserInfoOperate uio = new UserInfoOperate(this);
           uio.addData(tblUserInfos);

            TblPhoneInfo tblPhoneInfo = new TblPhoneInfo();
            tblPhoneInfo.setUserId("police");
            tblPhoneInfo.setPhone("110");
            new PhoneInfoOperate(this).addData(tblPhoneInfo);

            DatabaseOperate.setTransactionSuccessful(this);
        } catch (Exception e) {
            LogCat.printStackTrace(e);
        } finally {
            DatabaseOperate.endTransaction(this);
        }

        DatabaseOperate.closeDatabase(this);

        DatabaseOperate.printAllTable(this, null);
    }
}
