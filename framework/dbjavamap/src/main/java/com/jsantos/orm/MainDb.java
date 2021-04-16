package com.jsantos.orm;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * @author javier santos
 */

public class MainDb {
	private static DataSource mainDataSource = null;
	private static DataSourceTransactionManager txManager = null;
	private static String databaseProductName = null;

	public static Connection getConnection(){
		return DataSourceUtils.getConnection(getMainDataSource());
	}
	
	public static String getDatabaseProductName() {
		return databaseProductName;
	}

	public static DataSource getMainDataSource(){
        return mainDataSource;
	}

	public static void setMainDataSource(DataSource ds) throws SQLException {
		mainDataSource = ds;
		try(Connection conn = ds.getConnection()){
			databaseProductName = conn.getMetaData().getDatabaseProductName();
		}
		txManager = new DataSourceTransactionManager(ds);
	}

	public static DataSourceTransactionManager getTxManager() {
		return txManager;
	}
	
	

	
}