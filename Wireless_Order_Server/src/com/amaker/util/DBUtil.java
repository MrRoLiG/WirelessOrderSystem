package com.amaker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//���ݿ⹤����
public class DBUtil {
	
	//�����ݿ�����
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
			System.out.println("CONNECTION OPENED��....................");
			return DriverManager.getConnection(url,username,password);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//�ر����ݿ�����
	public void closeConnection(Connection conn) {
		try {
			conn.close();
			System.out.println("CONNECTION CLOSED....................");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//��Ϣ�б�ת��Ϊ�ַ���
	public static String buildJson(List list) {
		JSONArray array =new JSONArray();
		for(Object o:list) {
			array.add(JSONObject.fromObject(o));
		}
		return array.toString();
	}
}