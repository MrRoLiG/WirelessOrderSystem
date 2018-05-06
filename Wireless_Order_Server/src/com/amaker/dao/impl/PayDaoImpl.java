package com.amaker.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.amaker.dao.PayDao;
import com.amaker.entity.QueryOrder;
import com.amaker.entity.QueryOrderDetail;
import com.amaker.util.DBUtil;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

// PayDao接口的实现类 ，用于返回订单详情表和菜品表的联合表
public class PayDaoImpl implements PayDao{

	@Override
	public List<QueryOrderDetail> getOrderDetailList(int orderid) {
		// 查询sql语句
		String sql="select dt.id ,dt.name, dt.price, odt.dishnum, dt.price*odt.dishnum as totalcost, odt.remark from wireless_db.orderdetailtbl as odt left join wireless_db.dishestbl as dt "
				+ "on odt.dishid = dt.id where odt.orderid= ? ";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn=(Connection) util.openConnection();
		try {
			// 获得预处理语句
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			// 设置预处理语句参数
			pstmt.setInt(1, orderid);
			// 执行查询
			ResultSet rs=pstmt.executeQuery();
			// 定义一张表存放信息
			List<QueryOrderDetail> list=new ArrayList<QueryOrderDetail>();
			while(rs.next()) {
				// 获得订单详情信息
				int dishid=rs.getInt("id");
				String name=rs.getString("name");
				int price=rs.getInt("price");
				int dishnum=rs.getInt("dishnum");
				int totalcost=rs.getInt("totalcost");
				String remark=rs.getString("remark");
				
				QueryOrderDetail unityInfo=new QueryOrderDetail();
				unityInfo.setDishId(dishid);
				unityInfo.setName(name);
				unityInfo.setPrice(price);
				unityInfo.setDishNum(dishnum);
				unityInfo.setTotalCost(totalcost);
				unityInfo.setRemark(remark);
				
				list.add(unityInfo);
			}
			
			return list;
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
	public QueryOrder getOrderById(int orderid) {
		// 查询sql语句
		String sql="select ot.tableid,ut.name,ot.customernum,ot.orderdate from wireless_db.ordertbl as ot left join wireless_db.usertbl as ut on ot.userid = ut.id where ot.id=? ";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn=(Connection) util.openConnection();
		try {
			// 获得预处理语句
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			// 设置预处理语句参数
			pstmt.setInt(1, orderid);
			// 执行查询
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()) {
				// 获得订单信息
				int tableid=rs.getInt(1);
				String name=rs.getString(2);
				int customernum=rs.getInt(3);
				String orderdate=rs.getString(4);
				
				QueryOrder queryOrder=new QueryOrder();
				queryOrder.setTableId(tableid);
				queryOrder.setName(name);
				queryOrder.setCustomerNum(customernum);
				queryOrder.setOrderDate(orderdate);
				
				return queryOrder;
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
	public void pay(int orderid) {
		// 更新sql语句
		String sql="update ordertbl set ispay = 1 where id = ? ";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn=(Connection) util.openConnection();
		try {
			// 获得预处理语句
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			// 设置预处理语句参数
			pstmt.setInt(1, orderid);
			// 执行更新
			pstmt.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
	}
}
