# 前言
通用数据库操作接口规范，目的是辅助完成数据库实现设计，降低数据库模块与其他模块的耦合性

PS：原本我是打算把他写成一个 Library 的，但写着写着，最后发现只能写成接口规范了，好无奈

除了接口规范，我还提供了以下数据库的写法
- sqlite
- sqlciper

# 简单介绍结构
由于结构简单，就先说结构吧，总共有以下几个文件：
- SQLiteHelperInterface.java： **数据库文件**操作，如创建删除、打开关闭、执行SQL、启用事务
- SqlOperateInterface.java：**SQL 语句**执行，如**增删改查**
- TableBeanInterface.java：表的(某条)记录对象规范
- TableField.java：表的结构对象
- SqlGenerate.java：SQL 语句生成工具

# 用法说明
必须要说明的是，用法中大部分实现，已经在 sample 中完成了，可以根据使用的数据库不同，选择不同的 sample 修改即可

基于 sample 的修改，只需补充完成以下三个步骤：
- 数据库表设计
- 表记录对象 Bean 设计
- 具体表操作设计即可（如果没有单独的业务，其实就是写个构造方法而已）

以下是详细的用法实现过程，包括 sample 中已实现的部分的说明，所以阅读时参考某个 sample（如，sqlitesample） 的源代码，则更容易理解

### 1. 外部调用
知道大伙看这么长的内容，没耐心，就先说下最终外部调用的理想方法
``` java
BaseSqlOperate.openDatabase(this); // open database

try {
    BaseSqlOperate.beginTransaction(); // begin transaction

    TblUserInfo tblUserInfo = new TblUserInfo();
    new UserInfoOperate(this).addData(tblUserInfo);

    TblPhoneInfo tblPhoneInfo = new TblPhoneInfo();
    new PhoneInfoOperate(this).addData(tblPhoneInfo);

    BaseSqlOperate.setTransactionSuccessful(); // transaction successful
} catch (Exception e) {
    LogCat.printStackTrace(e);
} finally {
    BaseSqlOperate.endTransaction(); // end transaction
}

BaseSqlOperate.closeDatabase(this); // close database
```
### 2. 设计表结构
接下说下表结构的设计方式，以下推荐实现方式，具体可参考 sample 代码：
``` java
public interface TableFields {
    // table name, field name, field type, pk field
    TableField TBL_USER_INFO = new TableField(
            "userInfo",
            new String[]{"userId", "status", "createDate", "updateDate"},
            new String[]{"text", "text", "text", "text"},
            new String[]{"userId"});
}
```
### 3. 完成表的记录对象的 Bean，需要继承接口 TableBeanInterface
接口如下，详细注释见 databaseoperate 源代码
``` java
public interface TableBeanInterface {

    String[] toFieldsValue();

    String[] toPKFieldsValue();

    void setCreateDate(long createDate);

    void setUpdateDate(long updateDate);
}
```
推荐实现方式，具体可参考 sample 代码：
``` java
public class TblUserInfo implements TableBeanInterface {
    private long _id;
    private long createDate;
    private long updateDate;

    private String userId;
    private String status;
}
```
### 4. 完成表的 SQL 语句操作，通过继承接口 SqlOperateInterface 实现
接口如下，详细注释见 databaseoperate 源代码
``` java
public interface SqlOperateInterface<T extends TableBeanInterface> {

    boolean addData(T dbObject);

    boolean addData(List<T> data);

    boolean delData(String[] whereKey, String[] whereValue);

    boolean delData(T dbObject);

    boolean upData(T dbObject);

    boolean upData(List<T> data);

    List<T> getData(String[] whereKey, String[] whereValue, String[] orderKey);

    T getDatum(String[] whereKey, String[] whereValue, String[] orderKey);

    List<T> querySQL(String sql, String[] value);

    boolean execSQL(String sql);

    boolean execSQLByTransaction(String[] sqls);

    boolean execSQLByTransaction(List<T> sqls);
}
```
推荐实现方式，具体可参考 sample 代码：
``` java
// abstract class, 实现通用的增删改查
public abstract class BaseSqlOperate<T extends TableBeanInterface> implements SqlOperateInterface<T> {
    private SQLiteHelperInterface sqLiteHelper;
    private TableField tableField;
    private SqlGenerate sqlGenerate;
    private Class<T> tblBeanClass;
}

// 具体表的操作类
public class UserInfoOperate extends BaseSqlOperate<TblUserInfo> {
    public UserInfoOperate(Context context) {
        super(context, TableFields.TBL_USER_INFO, TblUserInfo.class);
    }

    // 其他单独业务操作
}
```
### 5. 完成数据库文件的操作，通过继承接口 SQLiteHelperInterface 实现
接口如下，详细注释见 databaseoperate 源代码
``` java
public interface SQLiteHelperInterface {

    void createDatabase(String password);

    boolean deleteDatabase();

    void openDatabase(String password);

    void closeDatabase();

    Cursor querySQL(String sql, String[] selectionArgs);

    void execSQL(String sql);

    void beginTransaction();

    void setTransactionSuccessful();

    void endTransaction();
}
```
推荐实现方式，具体可参考 sample 代码：
``` java
public class SQLiteHelper extends SQLiteOpenHelper implements SQLiteHelperInterface {
    public final static String DATABASE_NAME = "sqlite.db";
    public final static int DATABASE_VERSION = 1;
    private final String ERROR_DATABASE_OPEN_FAILED = "database open failed";

    private static SQLiteHelper instance; // Singleton mode
    private SQLiteDatabase database;
    private int openDatabaseCount = 0; // if count less than 0, invoke close()
}
```

# 后话
后面尽量将 sample 中通用的内容集成到 databaseoperate 库中

另外后期匹配一个博客，进行详细说明吧，现在就先空着，**（^_^///）**
