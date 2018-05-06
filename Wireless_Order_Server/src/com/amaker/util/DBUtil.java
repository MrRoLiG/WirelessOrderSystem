package com.amaker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

//数据库工具类
public class DBUtil {
	
	//打开数据库连接
	public Connection openConnection() {
		Properties prop=new Properties();
		String driver=null;
		String url=null;
		String username=null;
		String password=null;
		
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("DBConfig.properties"));
			driver=prop.getProperty("driver");
			url=prop.getProperty("url");
			username=prop.getProperty("username");
			password=prop.getProperty("password");
			
			Class.forName(driver);
			System.out.println("MySQL　DATABASE　CONNECTION OPENED。....................");
			return DriverManager.getConnection(url,username,password);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//关闭数据库连接
	public void closeConnection(Connection conn) {
		try {
			conn.close();
			System.out.println("MySQL　DATABASE　CONNECTION CLOSED....................");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
