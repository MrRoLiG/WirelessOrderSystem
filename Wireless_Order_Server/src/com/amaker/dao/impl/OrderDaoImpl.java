package com.amaker.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.amaker.dao.OrderDao;
import com.amaker.entity.AdminQueryOrderDetail;
import com.amaker.entity.Dish;
import com.amaker.entity.Order;
import com.amaker.entity.OrderDetail;
import com.amaker.util.DBUtil;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

// OrderDao接口的实现类
public class OrderDaoImpl implements OrderDao {

	private int id=0;
	private int flag=0;
	
	@Override
	public List getDishesList() {
		// 查询sql语句
		String sql="select id,name,type,price,picture,remark from dishestbl ";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn=(Connection) util.openConnection();
		try {
			// 获得预处理语句
			Statement stmt=(Statement) conn.createStatement();
			// 执行查询
			ResultSet rs=(ResultSet) stmt.executeQuery(sql);
			// 获得菜品List
			List<Dish> list=new ArrayList<Dish>();
			while(rs.next()) {
				// 获得菜品信息
				int id= rs.getInt("id");
				String name=rs.getString("name");
				String type=rs.getString("type");
				int price=rs.getInt("price");
				String picture=rs.getString("picture");
				String remark=rs.getString("remark");
				
				Dish dish=new Dish();
				dish.setId(id);
				dish.setName(name);
				dish.setType(type);
				dish.setPrice(price);
				dish.setPicture(picture);
				dish.setRemark(remark);
				
				list.add(dish);
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
	public int saveOrder(Order order) {
		// 添加sql语句
		String sql="insert into ordertbl(userid,tableid,customernum,orderdate) values(?,?,?,?) ";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn=(Connection) util.openConnection();
		try {
			// 获得预处理语句
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			// 设置上述sql语句参数
			pstmt.setInt(1, order.getUserId());
			pstmt.setInt(2, order.getTableId());
			pstmt.setInt(3, order.getCustomerNum());
			pstmt.setString(4, order.getOrderDate());
			// 更新表
			pstmt.executeUpdate();
			// 查找取得订单编号即主键id
			String querySql="select max(id) as id from ordertbl ";
			Statement stmt=(Statement) conn.createStatement();
			ResultSet rs=(ResultSet) stmt.executeQuery(querySql);
			if(rs.next()) {
				id= rs.getInt(1);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		return id;
	}

	@Override
	public void updateTableStateStarted(Order order) {
		// 更新sql语句
		String sql=" update tabletbl set Flag = 1,CustomerNum = ? where Id = ? ";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn= (Connection) util.openConnection();
		try {
			// 获得预处理语句
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			// 设置参数
			pstmt.setInt(1, order.getCustomerNum());
			pstmt.setInt(2, order.getTableId());
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
	
	@Override
	public void updateTableStateStarted(int tableid, int custnum) {
		// 更新sql语句
		String sql=" update tabletbl set Flag = 1,CustomerNum = ? where Id = ? ";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn= (Connection) util.openConnection();
		try {
			// 获得预处理语句
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			// 设置参数
			pstmt.setInt(1, custnum);
			pstmt.setInt(2, tableid);
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

	@Override
	public void saveOrderDetail(OrderDetail orderDetail) {
		// 添加SQL语句
		String sql="insert into orderdetailtbl(orderid,dishid,dishnum,remark) values(?,?,?,?) ";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn =(Connection) util.openConnection();
		try {
			// 获得预处理语句
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			// 设置参数
			pstmt.setInt(1, orderDetail.getOrderId());
			pstmt.setInt(2, orderDetail.getDishId());
			pstmt.setInt(3, orderDetail.getDishNum());
			pstmt.setString(4, orderDetail.getRemark());
			
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

	@Override
	public int checkOrder(int nUserId) {
		int orderId=0;
		String sql="select id from ordertbl where userid = "+nUserId+" and ispay = 0 and isorder = 1 ";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn =(Connection) util.openConnection();
		try {
			// 获得处理语句
			Statement stmt=(Statement) conn.createStatement();;
			// 执行查询
			ResultSet rs=(ResultSet) stmt.executeQuery(sql);
			if(rs.next()) {
				orderId= rs.getInt(1);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		return orderId;
	}

	@Override
	public void updateTableState(int orderid, boolean isStarted) {
		if(true==isStarted) {
			flag=1;
		}
		if(false==isStarted) {
			flag=0;
		}
		// 更新sql语句
		String sql="update tabletbl set Flag = "+ flag +",CustomerNum = 0,Remark = 0  where id = "+
		" (select tableid from ordertbl where id = ?) ";
		// 数据库连接工具类
		DBUtil util = new DBUtil();
		// 获得连接
		Connection conn = (Connection) util.openConnection();
		try {
			// 获得预定义语句
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
			// 设置参数
			pstmt.setInt(1, orderid);
			// 执行更新
			pstmt.executeUpdate();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			util.closeConnection(conn);
		}
	}

	@Override
	public List getPaidOrderInfo() {
		// 定义一个String的List存放数据库查询的订单信息
		List<String> strList=new ArrayList<String>();
		// 查询sql语句，查询已下单和结算的订单
		String sql="select * from ordertbl where ispay = 1 and isdishup = 0 ";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn =(Connection) util.openConnection();
		try {
			// 获得执行语句
			Statement stmt=(Statement) conn.createStatement();
			// 获得查询结果
			ResultSet rs = (ResultSet) stmt.executeQuery(sql);
			while(rs.next()) {
				String str="订单号:"+rs.getInt("id")+"~  已下单结算完成~  桌号:"+rs.getInt("tableid")
				+"~  客人"+rs.getInt("customernum")+"名~  备注:"+rs.getString("remark")+"~  请尽快上菜~~~";
				
				strList.add(str);
			}
			return strList;
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
	public List getPaidOrderDetailInfo() {
		// 查询sql语句，用来查询当前订单中已经下单和结算的所有订单
		String queryStr="select id from ordertbl where ispay = 1 and isdishup = 0 ";
		// 定义一个List用来存放所有已下单的结算的订单id
		List<Integer> idList=new ArrayList<Integer>();
		// 定义一个List用来存放订单详情信息
		List<AdminQueryOrderDetail> orderInfoList=new ArrayList<AdminQueryOrderDetail>();
		
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn =(Connection) util.openConnection();
		
		try {
			// 获得处理语句
			Statement stmt=(Statement) conn.createStatement();
			ResultSet rs=(ResultSet) stmt.executeQuery(queryStr);
			while(rs.next()) {
				idList.add(rs.getInt("id"));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		while(0!=idList.size()) {
			// 查询sql语句，用来查询当前订单的详情信息
			String sql=" select odt.orderid,dt.name, odt.dishnum, odt.remark from orderdetailtbl as odt left join dishestbl as dt " + 
					" on odt.dishid = dt.id where odt.orderid= ? ";
			try {
				// 获得预处理语句
				PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
				for(int i=0;i<idList.size();i++) {
					pstmt.setInt(1, idList.get(i));
					ResultSet rs=(ResultSet) pstmt.executeQuery();
					while(rs.next()) {
						AdminQueryOrderDetail AQOD=new AdminQueryOrderDetail();
						AQOD.setOrderId(rs.getInt("orderid"));
						AQOD.setName(rs.getString("name"));
						AQOD.setDishNum(rs.getInt("dishnum"));
						AQOD.setRemark(rs.getString("remark"));
						
						orderInfoList.add(AQOD);
					}
				}
				
				return orderInfoList;
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			finally {
				util.closeConnection(conn);
			}
		}
		
		util.closeConnection(conn);
		
		return null;
	}

	@Override
	public String updateOrderDishUpState(int orderid) {
		String res="没有找到订单"+orderid+"的上菜详情!";
		// 更新sql语句
		String sql="update ordertbl set isdishup = 1 where id = ? ";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn= (Connection) util.openConnection();
		try {
			// 获得预处理语句
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			// 设置参数
			pstmt.setInt(1, orderid);
			// 执行更新
			pstmt.executeUpdate();
			return orderid+"|订单"+orderid+"开始上菜!";
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		return res;
	}

	@Override
	public String getCurrentOrderState(int userid) {
		boolean isDone=true;
		boolean isFind=false;
		int isOrder=0;
		int isPay=0;
		int isDishUp=0;
		int orderid=0;
		// 查询sql语句
		if(0==userid) {
			return "未获取到用户id";
		}
		// 只开桌未进行其他操作，提示进行后续操作
		String sql="select isorder,ispay,isdishup from ordertbl where id = ?";
		// 先查询当前用户是否有未完成的订单，如果没有，提示创建，如果有，获取订单状态
		String queryStr="select id from ordertbl where userid = ? and isdone = 0 ";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn= (Connection) util.openConnection();
		try {
			// 获得预处理语句
			PreparedStatement queryPstmt=(PreparedStatement) conn.prepareStatement(queryStr);
			queryPstmt.setInt(1, userid);
			
			ResultSet queryRs=(ResultSet) queryPstmt.executeQuery();
			while(queryRs.next()) {
				PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
				orderid=queryRs.getInt("id");
				pstmt.setInt(1, orderid);
				isDone=false;
				ResultSet rs = (ResultSet) pstmt.executeQuery();
				while(rs.next()) {
					isFind=true;
					isOrder=rs.getInt("isorder");
					isPay=rs.getInt("ispay");
					isDishUp=rs.getInt("isdishup");
				}
			}
			
			if(isDone) {
				return "当前没有创建订单，需要用餐请进行开桌点餐下单结算";
			}
			else {
				if(isFind) {
					if(0==isOrder&&0==isPay&&0==isDishUp) {
						return "当前已开桌，请继续下单结算操作!订单号："+orderid;
					}
					if(1==isOrder&&0==isPay&&0==isDishUp) {
						return "当前已下单，请继续结算操作!订单号："+orderid;
					}
					if(1==isOrder&&1==isPay&&0==isDishUp) {
						return "当前已经结算，等待服务员听单上菜!订单号："+orderid;
					}
					if(1==isOrder&&1==isPay&&1==isDishUp) {
						return "服务员听单完成，正在通知上菜，请稍候~订单号："+orderid;
					}
				}
				else {
					return "未找到对应订单!";
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		
		return "无法查询到对应订单!";
	}

	@Override
	public String updateOrderIsDone(int orderid) {
		// 只开桌未进行其他操作，提示进行后续操作
		String sql="update ordertbl set isdone = 1 where id = ? ";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn= (Connection) util.openConnection();
		try {
			// 获得预处理语句
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, orderid);
			
			pstmt.executeUpdate();
			return "更新订单状态成功!";
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		return "更新失败!";
	}
	
	@Override
	public void updateOrderIsOrder(int orderid) {
		// 只开桌未进行其他操作，提示进行后续操作
		String sql="update ordertbl set isorder = 1 where id = ? ";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn= (Connection) util.openConnection();
		try {
			// 获得预处理语句
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, orderid);
			
			pstmt.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
	}

	@Override
	public int checkOrderEx(int nUserId) {
		int orderId=0;
		String sql="select id from ordertbl where userid = "+nUserId+" and ispay = 1 and (isdishup = 0 or isdone = 0 ) ";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn =(Connection) util.openConnection();
		try {
			// 获得处理语句
			Statement stmt=(Statement) conn.createStatement();;
			// 执行查询
			ResultSet rs=(ResultSet) stmt.executeQuery(sql);
			if(rs.next()) {
				orderId= rs.getInt(1);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		return orderId;
	}

	
	@Override
	public String getOrderInfoAfterLogout(int userid) {
		// 查询sql语句
		String sql="select id,tableid,customernum from ordertbl where userid = ? and (isorder = 0 or ispay = 0 )";
		// 获得数据库连接工具
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn= (Connection) util.openConnection();
		try {
			// 获得预处理语句
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			// 设置参数
			pstmt.setInt(1, userid);
			
			ResultSet rs=(ResultSet) pstmt.executeQuery();
			while(rs.next()) {
				return rs.getInt("id")+"|"+rs.getInt("tableid")+"|"+rs.getInt("customernum");
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
