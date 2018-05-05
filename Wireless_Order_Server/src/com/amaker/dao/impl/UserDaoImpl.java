package com.amaker.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;

import com.amaker.dao.UserDao;
import com.amaker.entity.User;
import com.amaker.util.DBUtil;
import com.mysql.jdbc.Statement;

//UserDao实现类
public class UserDaoImpl implements UserDao {

	@Override
	public User login(String account, String password) {
		//sql查询字符串
		String sql="select id,name,account,password,permission,remark "
				 + "from usertbl "
				 + "where account=? and password=? ";
		
		//获得数据库连接工具类
		DBUtil util=new DBUtil();
		//进行连接
		Connection conn=util.openConnection();
		try {
			//获得预定义查询语句
			PreparedStatement pstmt=conn.prepareStatement(sql);
			//设置参数
			pstmt.setString(1,account);
			pstmt.setString(2,password);
			//执行数据库查询
			ResultSet rs=pstmt.executeQuery();
			//判断用户是否存在
			String res = sql;
			if(rs.next()) {
				int id=rs.getInt(1);
				String name=rs.getString(2);
				int permission=rs.getInt(5);
				String remark=rs.getString(6);
				
				User user=new User();
				
				user.setId(id);
				user.setName(name);
				user.setAccount(account);
				user.setPassword(password);
				user.setPermission(permission);
				user.setRemark(remark);
				
				return user;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		
		return null;
	}

	@Override
	public String Register(String nickname, String account, String password) {
		//插入sql语句
		String sql="insert into usertbl (name,account,password,permission) values (?,?,?,2) ";
		//查询该用户是否已经存在
		String querySql="select * from usertbl where account=? ";
		//获得数据库链接工具类
		DBUtil util=new DBUtil();
		//获得连接
		Connection conn=util.openConnection();
		try {
			//获得预处理语句
			PreparedStatement pstmt=conn.prepareStatement(sql);
			PreparedStatement queryPstmt=conn.prepareStatement(querySql);
			//设置参数
			pstmt.setString(1,nickname);
			pstmt.setString(2,account);
			pstmt.setString(3, password);
			
			queryPstmt.setString(1, account);
			ResultSet rs=queryPstmt.executeQuery();
			
			String res=rs.toString();
			//如果存在直接返回“0”
			if(rs.next()) {
				return "0";
			}
			else {
				pstmt.executeUpdate();
				return "1";
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		return null;
	}

	@Override
	public String addUser(User user) {
		//插入sql语句
		String sql="insert into usertbl (name,account,password,gender,permission,remark) values (?,?,?,?,?,?) ";
		//查询该用户是否已经存在
		String querySql="select * from usertbl where account= ? ";
		//获得数据库链接工具类
		DBUtil util=new DBUtil();
		//获得连接
		Connection conn=util.openConnection();
		try {
			//获得预处理语句
			PreparedStatement pstmt=conn.prepareStatement(sql);
			PreparedStatement queryPstmt=conn.prepareStatement(querySql);

			queryPstmt.setString(1, user.getAccount());
			ResultSet rs=queryPstmt.executeQuery();
			//如果存在
			if(rs.next()) {
				return "用户已存在!";
			}
			else {
				//设置参数
				pstmt.setString(1,user.getName());
				pstmt.setString(2,user.getAccount());
				pstmt.setString(3,user.getPassword());
				pstmt.setString(4, user.getGender());
				pstmt.setInt(5, user.getPermission());
				pstmt.setString(6, user.getRemark());
				
				pstmt.executeUpdate();
				return "添加成功!";
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		
		return null;
	}

	@Override
	public List getAllUser() {
		// 定义一张表用来存放所有用户信息
		List<User> userList=new ArrayList<User>();
		// 查询sql语句
		String sql="select * from usertbl ";
		//获得数据库链接工具类
		DBUtil util=new DBUtil();
		//获得连接
		Connection conn=util.openConnection();
		try {
			Statement stmt=(Statement) conn.createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()) {

				User user=new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setAccount(rs.getString("account"));
				user.setPassword(rs.getString("password"));
				user.setGender(rs.getString("gender"));
				user.setPermission(rs.getInt("permission"));
				user.setRemark(rs.getString("remark"));
				
				userList.add(user);
			}
			rs.close();
			stmt.close();
			
			return userList;
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		return null;
	}

	@Override
	public String deleteUserByName(String name) {
		// 删除sql语句
		String sql="delete from usertbl where name= ? ";
		//查询该用户是否已经存在
		String querySql="select * from usertbl where name= ? ";
		//获得数据库连接工具类
		DBUtil util=new DBUtil();
		//进行连接
		Connection conn=util.openConnection();
		try {
			//获得预定义查询语句
			PreparedStatement pstmt=conn.prepareStatement(sql);
			PreparedStatement queryPstmt=conn.prepareStatement(querySql);
			//设置参数
			pstmt.setString(1,name);
			queryPstmt.setString(1, name);
			//执行数据库查询
			ResultSet rs=queryPstmt.executeQuery();
			//判断用户是否存在
			if(!rs.next()) {
				return "用户不存在!无法删除!";
			}
			else {
				pstmt.executeUpdate();
				return "删除用户成功!";
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		
		return null;
	}
	
}
