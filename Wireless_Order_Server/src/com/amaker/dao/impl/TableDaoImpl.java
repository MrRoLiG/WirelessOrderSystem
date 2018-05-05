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

//TableDaoʵ����
public class TableDaoImpl implements TableDao {

	@Override
	public List getTableList() {
		// ��ѯSQL���
		String sql="select Id,CustomerNum,Flag,Remark from tabletbl ";
		// ����һ��TableList�����洢���в�����Ϣ
		List<Table> list=new ArrayList<Table>();
		// ������ݿ����ӹ�����
		DBUtil util=new DBUtil();
		// �������
		Connection conn =util.openConnection();
		try {
			// ���Ԥ�������
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
		// ����sql���
		String udpOldTableTblSql="update tabletbl set Flag = 0,CustomerNum = 0, Remark = 0  where id = (select tableId from ordertbl  as ot where ot.id = ? )";
		String updOrderTblSql="update ordertbl set tableid = ? where id = ? ";
		String udpNewTableTblSql="update tabletbl set Flag = 1,CustomerNum = ?,Remark = 0  where id = ? ";
		// ���ݿ����ӹ�����
		DBUtil util = new DBUtil();
		// �������
		Connection conn = util.openConnection();
		
		try {
			// ��ֹ�������ݿ��ʱ��ʧ�ܵ�������
			conn.setAutoCommit(false);
			
			// ���Ԥ�������
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(udpOldTableTblSql);
			// ���ò���
			pstmt.setInt(1, orderid);
			// ���¶�����
			pstmt.executeUpdate();
			
			pstmt=(PreparedStatement) conn.prepareStatement(updOrderTblSql);
			// ���ò���
			pstmt.setInt(1, tableid);
			pstmt.setInt(2, orderid);
			// ���¶�����
			pstmt.executeUpdate();
			
			pstmt=(PreparedStatement) conn.prepareStatement(udpNewTableTblSql);
			// ���ò���
			pstmt.setInt(1, custnum);
			pstmt.setInt(2, tableid);
			// ���¶�����
			pstmt.executeUpdate();
			
			conn.commit();
		} 
		catch (SQLException e) {
			e.printStackTrace();
			try {
				// ������ݿ�����쳣���ع�
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
		// ����sql���
		String updNewTableSql="update tabletbl set Flag = 1, CustomerNum = CustomerNum + ?, Remark = 0 where id = ? ";
		String updOldTableSql="update tabletbl set Flag = 0, CustomerNum = 0, Remark = 0 where id = ? ";
		String updateOrdertblSql="update ordertbl set tableid = ?,customernum = customernum + ? where id = ? ";
		// ���ݿ����ӹ�����
		DBUtil util = new DBUtil();
		// �������
		Connection conn = util.openConnection();
		
		try {
			// ��ֹ�������ݿ��ʱ��ʧ�ܵ�������
			conn.setAutoCommit(false);
			
			// ���Ԥ�������
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(updNewTableSql);
			// ���ò���
			pstmt.setInt(1, originTableCustNum);
			pstmt.setInt(2, targetTableId);
			// ���¶�����
			pstmt.executeUpdate();
			
			pstmt=(PreparedStatement) conn.prepareStatement(updOldTableSql);
			// ���ò���
			pstmt.setInt(1, originTableId);
			// ���¶�����
			pstmt.executeUpdate();
			
			pstmt=(PreparedStatement) conn.prepareStatement(updateOrdertblSql);
			// ���ò���
			pstmt.setInt(1, targetTableId);
			pstmt.setInt(2, originTableCustNum);
			pstmt.setInt(3, orderId);
			// ���¶�����
			pstmt.executeUpdate();
			
			conn.commit();
		} 
		catch (SQLException e) {
			e.printStackTrace();
			try {
				// ������ݿ�����쳣���ع�
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
		// ��ѯsql��䣬��ѯ��ǰ��Щ���ӵ�id��Ҫ����
		String querySql="select id from tabletbl where CustomerNum != 0 or Flag != 0 or Remark != 0 ";
		// ����һ�ű������洢��Ҫ���µ�����id��
		List<Integer> idList=new ArrayList<Integer>();
		// ���ݿ����ӹ�����
		DBUtil util = new DBUtil();
		// �������
		Connection conn = util.openConnection();
		try {
			// ��ô������
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(querySql);
			while(rs.next()) {
				idList.add(rs.getInt("Id"));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		// ���û�У�����
		if(0==idList.size()) {	
			util.closeConnection(conn);
			return "�������ӺŶ����ڳ�ʼ״̬����������!";
		}
		// ������и���
		else {
			try {
				// ��ֹ�������ݿ��ʱ��ʧ�ܵ�������
				conn.setAutoCommit(false);
				
				// ��ø���sql���
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
					// ������ݿ�����쳣���ع�
					conn.rollback();
				} 
				catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		util.closeConnection(conn);
		return "���ɹ�!";
	}

	@Override
	public String clearTableById(int tableid) {
		// ��ѯsql��䣬��ѯ��ǰ��Щ���ӵ�id��Ҫ����
		String sql="update tabletbl set CustomerNum = 0,Flag = 0,Remark = '��'   where Id = ? ";
		// ���ݿ����ӹ�����
		DBUtil util = new DBUtil();
		// �������
		Connection conn = util.openConnection();
		try {
			// ���Ԥ�������
			PreparedStatement pstmt =(PreparedStatement) conn.prepareStatement(sql);
			// ���ò���
			pstmt.setInt(1, tableid);
			// ִ�и���
			pstmt.executeUpdate();
			
			return "����"+tableid+"����ɹ�!";
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			util.closeConnection(conn);
		}
		
		return "����ʧ��!";
	}
}
