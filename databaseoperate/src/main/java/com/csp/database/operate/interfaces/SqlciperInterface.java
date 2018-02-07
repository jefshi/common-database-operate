package com.csp.database.operate.interfaces;

import java.util.List;

/**
 * Description: database operate interface, include add, delete, update, select
 * <p>Create Date: 2017/4/24
 * <p>Modify Date: 无
 * <p>Github: https://github.com/jefshi/common-database-operate.git
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
public interface SqlciperInterface<T extends TableBeanInterface> {
	/**
	 * insert 操作
	 *
	 * @param dbObject 表的（一条）记录的数据
	 * @return true: 操作成功
	 */
	boolean addData(T dbObject);

	/**
	 * insert 批量操作
	 *
	 * @param data 表的（一条）记录的数据集合
	 * @return true: 操作成功
	 */
	boolean addData(List<T> data);

	/**
	 * delete 操作
	 *
	 * @param whereKey   字段集合
	 * @param whereValue 值集合
	 * @return true: 操作成功
	 */
	boolean delData(String[] whereKey, String[] whereValue);

	/**
	 * delete 操作
	 *
	 * @param dbObject 表的（一条）记录的数据
	 * @return true: 操作成功
	 */
	boolean delData(T dbObject);

	/**
	 * updata 操作
	 *
	 * @param dbObject 表的（一条）记录的数据
	 * @return true: 操作成功
	 */
	boolean upData(T dbObject);

	/**
	 * updata 批量操作
	 *
	 * @param data 表的（一条）记录的数据集合
	 * @return true: 操作成功
	 */
	boolean upData(List<T> data);

	/**
	 * select 操作
	 *
	 * @param whereKey   字段集合
	 * @param whereValue 值集合
	 * @param orderKey   排序字段集合, 可以含"ASC", "DESC"标志
	 * @return 表的（一条）记录的数据集
	 */
	List<T> getData(String[] whereKey, String[] whereValue, String[] orderKey);

	/**
	 * select 操作(查询一条记录)
	 *
	 * @param whereKey   字段集合
	 * @param whereValue 值集合
	 * @param orderKey   排序字段集合, 可以含"ASC", "DESC"标志
	 * @return 一条表的（一条）记录的数据
	 */
	T getDatum(String[] whereKey, String[] whereValue, String[] orderKey);

	/**
	 * 执行自定义SQL, 查询数据库
	 *
	 * @param sql   自定义SQL, (SELECT * FROM tableName WHERE key=?)
	 * @param value SQL中[?]的填充值
	 * @return 表的（一条）记录的数据集
	 */
	List<T> querySQL(String sql, String[] value);

	/**
	 * 执行自定义 SQL
	 *
	 * @param sql SQL语句
	 * @return true: 操作成功
	 */
	boolean execSQL(String sql);

	/**
	 * 使用事务执行自定义 SQL
	 *
	 * @param sql SQL语句
	 * @return true: 操作成功
	 */
	boolean execSQLByTransaction(String[] sqls);
}
