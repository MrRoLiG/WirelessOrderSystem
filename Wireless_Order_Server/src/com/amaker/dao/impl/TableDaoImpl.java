package com.amaker.dao.impl;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;

import com.amaker.dao.TableDao;
import com.amaker.entity.Table;
import com.amaker.util.DBUtil;
import com.mysql.jdbc.PreparedStatement;

//TableDao实现类
public class TableDaoImpl implements TableDao {

	@Override
	public List getTableList() {
		// 查询SQL语句
		String sql="select Id,CustomerNum,Flag,Remark from tabletbl ";
		// 定义一个TableList用来存储所有餐桌信息
		List<Table> list=new ArrayList<Table>();
		// 获得数据库连接工具类
		DBUtil util=new DBUtil();
		// 获得连接
		Connection conn =util.openConnection();
		try {
			// 获得预处理语句
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			
			while(rs.next()) {
				Table tbl=new Table();
				
				tbl.setId(rs.getInt("Id"));
				tbl.setCustomerNum(rs.getInt("CustomerNum"));
				tbl.setFlag(rs.getInt("Flag"));
				tbl.setRemark(rs.getString("Remark"));
				
				list.add(tbl);
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
	public void changeTable(int orderid, int tableid,int custnum) {
		// 更新sql语句
		String udpOldTableTblSql="update tabletbl set Flag = 0,CustomerNum = 0, Remark = 0  where id = (select tableId from ordertbl  as ot where ot.id = ? )";
		String updOrderTblSql="update ordertbl set tableid = ? where id = ? ";
		String udpNewTableTblSql="update tabletbl set Flag = 1,CustomerNum = ?,Remark = 0  where id = ? ";
		// 数据库连接工具类
		DBUtil util = new DBUtil();
		// 获得连接
		Connection conn = util.openConnection();
		
		try {
			// 防止更新数据库的时候失败导致锁表
			conn.setAutoCommit(false);
			
			// 获得预定义语句
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(udpOldTableTblSql);
			// 设置参数
			pstmt.setInt(1, orderid);
			// 更新订单表
			pstmt.executeUpdate();
			
			pstmt=(PreparedStatement) conn.prepareStatement(updOrderTblSql);
			// 设置参数
			pstmt.setInt(1, tableid);
			pstmt.setInt(2, orderid);
			// 更新订单表
			pstmt.executeUpdate();
			
			pstmt=(PreparedStatement) conn.prepareStatement(udpNewTableTblSql);
			// 设置参数
			pstmt.setInt(1, custnum);
			pstmt.setInt(2, tableid);
			// 更新订单表
			pstmt.executeUpdate();
			
			conn.commit();
		} 
		catch (SQLException e) {
			e.printStackTrace();
			try {
				// 如果数据库更新异常，回滚
				conn.rollback();
			} 
			catch (SQLException e1) {
				e1.printStackTrace();
			}
		} 
		finally {
			util.closeConnection(conn);
		}
	}

	
	@Override
	public boolean isTableStarted(int tableid) {
		boolean isTableStarted=false;
		String sql="select * from tabletbl where Id = "+tableid+" and Flag = 1 ";
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
				isTableStarted=true;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		return isTableStarted;
	}

	@Override
	public void unionTable(int originTableId, int targetTableId, int originTableCustNum,int orderId) {
		// TODO Auto-generated method stub
		// 更新sql语句
		String updNewTableSql="update tabletbl set Flag = 1, CustomerNum = CustomerNum + ?, Remark = 0 where id = ? ";
		String updOldTableSql="update tabletbl set Flag = 0, CustomerNum = 0, Remark = 0 where id = ? ";
		String updateOrdertblSql="update ordertbl set tableid = ?,customernum = customernum + ? where id = ? ";
		// 数据库连接工具类
		DBUtil util = new DBUtil();
		// 获得连接
		Connection conn = util.openConnection();
		
		try {
			// 防止更新数据库的时候失败导致锁表
			conn.setAutoCommit(false);
			
			// 获得预定义语句
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(updNewTableSql);
			// 设置参数
			pstmt.setInt(1, originTableCustNum);
			pstmt.setInt(2, targetTableId);
			// 更新订单表
			pstmt.executeUpdate();
			
			pstmt=(PreparedStatement) conn.prepareStatement(updOldTableSql);
			// 设置参数
			pstmt.setInt(1, originTableId);
			// 更新订单表
			pstmt.executeUpdate();
			
			pstmt=(PreparedStatement) conn.prepareStatement(updateOrdertblSql);
			// 设置参数
			pstmt.setInt(1, targetTableId);
			pstmt.setInt(2, originTableCustNum);
			pstmt.setInt(3, orderId);
			// 更新订单表
			pstmt.executeUpdate();
			
			conn.commit();
		} 
		catch (SQLException e) {
			e.printStackTrace();
			try {
				// 如果数据库更新异常，回滚
				conn.rollback();
			} 
			catch (SQLException e1) {
				e1.printStackTrace();
			}
		} 
		finally {
			util.closeConnection(conn);
		}
	}

	@Override
	public String clearTable() {
		// 查询sql语句，查询当前拿些桌子的id需要重置
		String querySql="select id from tabletbl where CustomerNum != 0 or Flag != 0 or Remark != 0 ";
		// 定义一张表用来存储需要更新的桌子id号
		List<Integer> idList=new ArrayList<Integer>();
		// 数据库连接工具类
		DBUtil util = new DBUtil();
		// 获得连接
		Connection conn = util.openConnection();
		try {
			// 获得处理语句
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(querySql);
			while(rs.next()) {
				idList.add(rs.getInt("Id"));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		// 如果没有，返回
		if(0==idList.size()) {	
			util.closeConnection(conn);
			return "所有桌子号都处于初始状态，无需重置!";
		}
		// 否则进行更新
		else {
			try {
				// 防止更新数据库的时候失败导致锁表
				conn.setAutoCommit(false);
				
				// 获得更新sql语句
				String sql="update tabletbl set CustomerNum = 0,Flag = 0,Remark = 0 where id = ? ";
				PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
				for(int i=0;i<idList.size();i++) {
					pstmt.setInt(1, idList.get(i));
					
					pstmt.executeUpdate();
				}
				
				conn.commit();
			}
			catch(SQLException e) {
				e.printStackTrace();
				try {
					// 如果数据库更新异常，回滚
					conn.rollback();
				} 
				catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		util.closeConnection(conn);
		return "清表成功!";
	}

	@Override
	public String clearTableById(int tableid) {
		// 查询sql语句，查询当前拿些桌子的id需要重置
		String sql="update tabletbl set CustomerNum = 0,Flag = 0,Remark = '无'   where Id = ? ";
		// 数据库连接工具类
		DBUtil util = new DBUtil();
		// 获得连接
		Connection conn = util.openConnection();
		try {
			// 获得预处理语句
			PreparedStatement pstmt =(PreparedStatement) conn.prepareStatement(sql);
			// 设置参数
			pstmt.setInt(1, tableid);
			// 执行更新
			pstmt.executeUpdate();
			
			return "桌子"+tableid+"清理成功!";
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		
		return "清桌失败!";
	}
}
