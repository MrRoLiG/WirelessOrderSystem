package com.amaker.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.amaker.dao.DishDao;
import com.amaker.entity.Dish;
import com.amaker.util.DBUtil;
import com.mysql.jdbc.Statement;

public class DishDaoImpl implements DishDao{

	@Override
	public String addDish(Dish dish) {
		// 插入sql语句
		String sql="insert into dishestbl (name,type,price,picture,remark) values (?,?,?,?,?) ";
		// 查询需要添加的菜品是否已经存在
		String querySql="select * from dishestbl where name = ? ";
		//获得数据库链接工具类
		DBUtil util=new DBUtil();
		//获得连接
		Connection conn=util.openConnection();
		try {
			//获得预处理语句
			PreparedStatement pstmt=conn.prepareStatement(sql);
			PreparedStatement queryPstmt=conn.prepareStatement(querySql);

			queryPstmt.setString(1, dish.getName());
			ResultSet rs=queryPstmt.executeQuery();
			//如果存在
			if(rs.next()) {
				return "菜品已存在!";
			}
			else {
				//设置参数
				pstmt.setString(1,dish.getName());
				pstmt.setString(2,dish.getType());
				pstmt.setString(3,dish.getPrice()+"");
				pstmt.setString(4,dish.getPicture());
				pstmt.setString(5,dish.getRemark());
				
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
	public List getAllDishes() {
		// 定义一张表用来存放所有用户信息
		List<Dish> dishList=new ArrayList<Dish>();
		// 查询sql语句
		String sql="select * from dishestbl ";
		//获得数据库链接工具类
		DBUtil util=new DBUtil();
		//获得连接
		Connection conn=util.openConnection();
		try {
			Statement stmt=(Statement) conn.createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()) {

				Dish dish=new Dish();
				dish.setName(rs.getString("name"));
				dish.setType(rs.getString("type"));
				dish.setPrice(rs.getInt("price"));
				dish.setPicture(rs.getString("picture"));
				dish.setRemark(rs.getString("remark"));
				
				dishList.add(dish);
			}
			rs.close();
			stmt.close();
			
			return dishList;
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
	public String deleteDishByName(String name) {
		// 删除sql语句
		String sql="delete from dishestbl where name= ? ";
		//查询该用户是否已经存在
		String querySql="select * from dishestbl where name= ? ";
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
				return "菜品不存在!无法删除!";
			}
			else {
				pstmt.executeUpdate();
				return "删除菜品成功!";
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
	public String updateDish(String name, String price, String remark) {
		String sql="";
		String querySql="";
		int flag=0;
		
		if(""==name) {
			return "请指定更新的菜品!";
		}
		else {
			//查询该用户是否已经存在
			querySql="select * from dishestbl where name= ? ";
			
			// 更新价格和介绍
			if(""!=price&&""!=remark) {
				// 更新sql语句
				sql="update dishestbl set price = ?, remark = ? where name = ? ";
				flag=3;
			}
			// 更新介绍
			else if(""==price&&""!=remark) {
				// 更新sql语句
				sql="update dishestbl set remark = ? where name = ? ";
				flag=1;
			}
			// 更新价格
			else if(""!=price&&""==remark) {
				// 更新sql语句
				sql="update dishestbl set price = ? where name = ? ";
				flag=2;
			}
			// 该菜品未进行任何更新
			else {
				return "指定菜品未进行任何更新!";
			}
		}
		//获得数据库连接工具类
		DBUtil util=new DBUtil();
		//进行连接
		Connection conn=util.openConnection();
		
		try {
			//获得预定义查询语句
			PreparedStatement pstmt=conn.prepareStatement(sql);
			PreparedStatement queryPstmt=conn.prepareStatement(querySql);
			//设置参数
			if(1==flag) {
				pstmt.setString(1,remark);
				pstmt.setString(2, name);
			}
			if(2==flag) {
				pstmt.setInt(1,Integer.parseInt(price));
				pstmt.setString(2, name);
			}
			if(3==flag) {
				pstmt.setInt(1,Integer.parseInt(price));
				pstmt.setString(2,remark);
				pstmt.setString(3, name);
			}
			queryPstmt.setString(1, name);
			//执行数据库查询
			ResultSet rs=queryPstmt.executeQuery();
			//判断用户是否存在
			if(!rs.next()) {
				return "菜品不存在!无法更新!";
			}
			else {
				while(0!=flag) {
					pstmt.executeUpdate();
					return "更新菜品成功!";
				}
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
