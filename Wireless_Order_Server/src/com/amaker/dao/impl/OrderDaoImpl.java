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

// OrderDao�ӿڵ�ʵ����
public class OrderDaoImpl implements OrderDao {

	private int id=0;
	private int flag=0;
	
	@Override
	public List getDishesList() {
		// ��ѯsql���
		String sql="select id,name,type,price,picture,remark from dishestbl ";
		// ������ݿ����ӹ���
		DBUtil util=new DBUtil();
		// �������
		Connection conn=(Connection) util.openConnection();
		try {
			// ���Ԥ�������
			Statement stmt=(Statement) conn.createStatement();
			// ִ�в�ѯ
			ResultSet rs=(ResultSet) stmt.executeQuery(sql);
			// ��ò�ƷList
			List<Dish> list=new ArrayList<Dish>();
			while(rs.next()) {
				// ��ò�Ʒ��Ϣ
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
		// ���sql���
		String sql="insert into ordertbl(userid,tableid,customernum,orderdate) values(?,?,?,?) ";
		// ������ݿ����ӹ���
		DBUtil util=new DBUtil();
		// �������
		Connection conn=(Connection) util.openConnection();
		try {
			// ���Ԥ�������
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			// ��������sql������
			pstmt.setInt(1, order.getUserId());
			pstmt.setInt(2, order.getTableId());
			pstmt.setInt(3, order.getCustomerNum());
			pstmt.setString(4, order.getOrderDate());
			// ���±�
			pstmt.executeUpdate();
			// ����ȡ�ö�����ż�����id
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
		// ����sql���
		String sql=" update tabletbl set Flag = 1,CustomerNum = ? where Id = ? ";
		// ������ݿ����ӹ���
		DBUtil util=new DBUtil();
		// �������
		Connection conn= (Connection) util.openConnection();
		try {
			// ���Ԥ�������
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			// ���ò���
			pstmt.setInt(1, order.getCustomerNum());
			pstmt.setInt(2, order.getTableId());
			// ִ�и���
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
		// ����sql���
		String sql=" update tabletbl set Flag = 1,CustomerNum = ? where Id = ? ";
		// ������ݿ����ӹ���
		DBUtil util=new DBUtil();
		// �������
		Connection conn= (Connection) util.openConnection();
		try {
			// ���Ԥ�������
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			// ���ò���
			pstmt.setInt(1, custnum);
			pstmt.setInt(2, tableid);
			// ִ�и���
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
		// ���SQL���
		String sql="insert into orderdetailtbl(orderid,dishid,dishnum,remark) values(?,?,?,?) ";
		// ������ݿ����ӹ���
		DBUtil util=new DBUtil();
		// �������
		Connection conn =(Connection) util.openConnection();
		try {
			// ���Ԥ�������
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			// ���ò���
			pstmt.setInt(1, orderDetail.getOrderId());
			pstmt.setInt(2, orderDetail.getDishId());
			pstmt.setInt(3, orderDetail.getDishNum());
			pstmt.setString(4, orderDetail.getRemark());
			
			// ִ�и���
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
		// ������ݿ����ӹ���
		DBUtil util=new DBUtil();
		// �������
		Connection conn =(Connection) util.openConnection();
		try {
			// ��ô������
			Statement stmt=(Statement) conn.createStatement();;
			// ִ�в�ѯ
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
		// ����sql���
		String sql="update tabletbl set Flag = "+ flag +",CustomerNum = 0,Remark = 0  where id = "+
		" (select tableid from ordertbl where id = ?) ";
		// ���ݿ����ӹ�����
		DBUtil util = new DBUtil();
		// �������
		Connection conn = (Connection) util.openConnection();
		try {
			// ���Ԥ�������
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
			// ���ò���
			pstmt.setInt(1, orderid);
			// ִ�и���
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
		// ����һ��String��List������ݿ��ѯ�Ķ�����Ϣ
		List<String> strList=new ArrayList<String>();
		// ��ѯsql��䣬��ѯ���µ��ͽ���Ķ���
		String sql="select * from ordertbl where ispay = 1 and isdishup = 0 ";
		// ������ݿ����ӹ���
		DBUtil util=new DBUtil();
		// �������
		Connection conn =(Connection) util.openConnection();
		try {
			// ���ִ�����
			Statement stmt=(Statement) conn.createStatement();
			// ��ò�ѯ���
			ResultSet rs = (ResultSet) stmt.executeQuery(sql);
			while(rs.next()) {
				String str="������:"+rs.getInt("id")+"~  ���µ��������~  ����:"+rs.getInt("tableid")
				+"~  ����"+rs.getInt("customernum")+"��~  ��ע:"+rs.getString("remark")+"~  �뾡���ϲ�~~~";
				
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
		// ��ѯsql��䣬������ѯ��ǰ�������Ѿ��µ��ͽ�������ж���
		String queryStr="select id from ordertbl where ispay = 1 and isdishup = 0 ";
		// ����һ��List��������������µ��Ľ���Ķ���id
		List<Integer> idList=new ArrayList<Integer>();
		// ����һ��List������Ŷ���������Ϣ
		List<AdminQueryOrderDetail> orderInfoList=new ArrayList<AdminQueryOrderDetail>();
		
		// ������ݿ����ӹ���
		DBUtil util=new DBUtil();
		// �������
		Connection conn =(Connection) util.openConnection();
		
		try {
			// ��ô������
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
			// ��ѯsql��䣬������ѯ��ǰ������������Ϣ
			String sql=" select odt.orderid,dt.name, odt.dishnum, odt.remark from orderdetailtbl as odt left join dishestbl as dt " + 
					" on odt.dishid = dt.id where odt.orderid= ? ";
			try {
				// ���Ԥ�������
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
		String res="û���ҵ�����"+orderid+"���ϲ�����!";
		// ����sql���
		String sql="update ordertbl set isdishup = 1 where id = ? ";
		// ������ݿ����ӹ���
		DBUtil util=new DBUtil();
		// �������
		Connection conn= (Connection) util.openConnection();
		try {
			// ���Ԥ�������
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			// ���ò���
			pstmt.setInt(1, orderid);
			// ִ�и���
			pstmt.executeUpdate();
			return orderid+"|����"+orderid+"��ʼ�ϲ�!";
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
		// ��ѯsql���
		if(0==userid) {
			return "δ��ȡ���û�id";
		}
		// ֻ����δ����������������ʾ���к�������
		String sql="select isorder,ispay,isdishup from ordertbl where id = ?";
		// �Ȳ�ѯ��ǰ�û��Ƿ���δ��ɵĶ��������û�У���ʾ����������У���ȡ����״̬
		String queryStr="select id from ordertbl where userid = ? and isdone = 0 ";
		// ������ݿ����ӹ���
		DBUtil util=new DBUtil();
		// �������
		Connection conn= (Connection) util.openConnection();
		try {
			// ���Ԥ�������
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
				return "��ǰû�д�����������Ҫ�ò�����п�������µ�����";
			}
			else {
				if(isFind) {
					if(0==isOrder&&0==isPay&&0==isDishUp) {
						return "��ǰ�ѿ�����������µ��������!�����ţ�"+orderid;
					}
					if(1==isOrder&&0==isPay&&0==isDishUp) {
						return "��ǰ���µ���������������!�����ţ�"+orderid;
					}
					if(1==isOrder&&1==isPay&&0==isDishUp) {
						return "��ǰ�Ѿ����㣬�ȴ�����Ա�����ϲ�!�����ţ�"+orderid;
					}
					if(1==isOrder&&1==isPay&&1==isDishUp) {
						return "����Ա������ɣ�����֪ͨ�ϲˣ����Ժ�~�����ţ�"+orderid;
					}
				}
				else {
					return "δ�ҵ���Ӧ����!";
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		
		return "�޷���ѯ����Ӧ����!";
	}

	@Override
	public String updateOrderIsDone(int orderid) {
		// ֻ����δ����������������ʾ���к�������
		String sql="update ordertbl set isdone = 1 where id = ? ";
		// ������ݿ����ӹ���
		DBUtil util=new DBUtil();
		// �������
		Connection conn= (Connection) util.openConnection();
		try {
			// ���Ԥ�������
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, orderid);
			
			pstmt.executeUpdate();
			return "���¶���״̬�ɹ�!";
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		return "����ʧ��!";
	}
	
	@Override
	public void updateOrderIsOrder(int orderid) {
		// ֻ����δ����������������ʾ���к�������
		String sql="update ordertbl set isorder = 1 where id = ? ";
		// ������ݿ����ӹ���
		DBUtil util=new DBUtil();
		// �������
		Connection conn= (Connection) util.openConnection();
		try {
			// ���Ԥ�������
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
		// ������ݿ����ӹ���
		DBUtil util=new DBUtil();
		// �������
		Connection conn =(Connection) util.openConnection();
		try {
			// ��ô������
			Statement stmt=(Statement) conn.createStatement();;
			// ִ�в�ѯ
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
		// ��ѯsql���
		String sql="select id,tableid,customernum from ordertbl where userid = ? and (isorder = 0 or ispay = 0 )";
		// ������ݿ����ӹ���
		DBUtil util=new DBUtil();
		// �������
		Connection conn= (Connection) util.openConnection();
		try {
			// ���Ԥ�������
			PreparedStatement pstmt=(PreparedStatement) conn.prepareStatement(sql);
			// ���ò���
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
