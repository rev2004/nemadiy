package org.imirsel.dao;

import javax.sql.DataSource;


public class SQLDao {
	
	private static DataSource dataSource=null;

	// no access
	private SQLDao(){
		
	}
	
	public SQLDao(DataSource dataSource){
		this.setDataSource(dataSource);
	}

	public static void setDataSource(DataSource dataSource) {
		SQLDao.dataSource = dataSource;
	}

	public static DataSource getDataSource() {
		return dataSource;
	}

}
