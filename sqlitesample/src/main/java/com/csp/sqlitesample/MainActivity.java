package com.csp.sqlitesample;

import android.app.Activity;
import android.os.Bundle;

import com.csp.database.operate.util.LogCat;
import com.csp.sqlitesample.database.operate.DatabaseOperate;
import com.csp.sqlitesample.database.operate.PhoneInfoOperate;
import com.csp.sqlitesample.database.operate.UserInfoOperate;
import com.csp.sqlitesample.database.tblbean.TblPhoneInfo;
import com.csp.sqlitesample.database.tblbean.TblUserInfo;

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

        DatabaseOperate.resetDatabase(this); // 重置数据库
        DatabaseOperate.openDatabase(this); // 打开数据库
        boolean successful = true;

        try {
            DatabaseOperate.beginTransaction(this); // 事务

            List<TblUserInfo> list = new ArrayList<>();
            TblUserInfo tblUserInfo = new TblUserInfo();
            tblUserInfo.setUserId("police");
            tblUserInfo.setStatus("enable");
            list.add(tblUserInfo);

            tblUserInfo = new TblUserInfo();
            tblUserInfo.setUserId("firemen");
            tblUserInfo.setStatus("enable");
            list.add(tblUserInfo);

            UserInfoOperate uio = new UserInfoOperate(this);
            successful &= uio.addData(list); // 增

            list = uio.getData(); // 查
            list.get(1).setStatus("0000");
            successful &= uio.upData(list.get(1)); // 改
            successful &= uio.delData(list.get(0)); // 删

            TblPhoneInfo tblPhoneInfo = new TblPhoneInfo();
            tblPhoneInfo.setUserId("police");
            tblPhoneInfo.setPhone("110");
            successful &= new PhoneInfoOperate(this).addData(tblPhoneInfo);

            if (successful)
                DatabaseOperate.setTransactionSuccessful(this); // 事务
            else
                throw new Exception("数据库操作失败");
        } catch (Exception e) {
            LogCat.printStackTrace(e);
        } finally {
            DatabaseOperate.endTransaction(this); // 事务
        }

        DatabaseOperate.closeDatabase(this); // 关闭数据库

        DatabaseOperate.printAllTable(this, null);
    }
}
