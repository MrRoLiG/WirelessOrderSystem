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
		// ����sql���
		String sql="insert into dishestbl (name,type,price,picture,remark) values (?,?,?,?,?) ";
		// ��ѯ��Ҫ��ӵĲ�Ʒ�Ƿ��Ѿ�����
		String querySql="select * from dishestbl where name = ? ";
		//������ݿ����ӹ�����
		DBUtil util=new DBUtil();
		//�������
		Connection conn=util.openConnection();
		try {
			//���Ԥ�������
			PreparedStatement pstmt=conn.prepareStatement(sql);
			PreparedStatement queryPstmt=conn.prepareStatement(querySql);

			queryPstmt.setString(1, dish.getName());
			ResultSet rs=queryPstmt.executeQuery();
			//�������
			if(rs.next()) {
				return "��Ʒ�Ѵ���!";
			}
			else {
				//���ò���
				pstmt.setString(1,dish.getName());
				pstmt.setString(2,dish.getType());
				pstmt.setString(3,dish.getPrice()+"");
				pstmt.setString(4,dish.getPicture());
				pstmt.setString(5,dish.getRemark());
				
				pstmt.executeUpdate();
				return "��ӳɹ�!";
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
		// ����һ�ű�������������û���Ϣ
		List<Dish> dishList=new ArrayList<Dish>();
		// ��ѯsql���
		String sql="select * from dishestbl ";
		//������ݿ����ӹ�����
		DBUtil util=new DBUtil();
		//�������
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
		// ɾ��sql���
		String sql="delete from dishestbl where name= ? ";
		//��ѯ���û��Ƿ��Ѿ�����
		String querySql="select * from dishestbl where name= ? ";
		//������ݿ����ӹ�����
		DBUtil util=new DBUtil();
		//��������
		Connection conn=util.openConnection();
		try {
			//���Ԥ�����ѯ���
			PreparedStatement pstmt=conn.prepareStatement(sql);
			PreparedStatement queryPstmt=conn.prepareStatement(querySql);
			//���ò���
			pstmt.setString(1,name);
			queryPstmt.setString(1, name);
			//ִ�����ݿ��ѯ
			ResultSet rs=queryPstmt.executeQuery();
			//�ж��û��Ƿ����
			if(!rs.next()) {
				return "��Ʒ������!�޷�ɾ��!";
			}
			else {
				pstmt.executeUpdate();
				return "ɾ����Ʒ�ɹ�!";
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
			return "��ָ�����µĲ�Ʒ!";
		}
		else {
			//��ѯ���û��Ƿ��Ѿ�����
			querySql="select * from dishestbl where name= ? ";
			
			// ���¼۸�ͽ���
			if(""!=price&&""!=remark) {
				// ����sql���
				sql="update dishestbl set price = ?, remark = ? where name = ? ";
				flag=3;
			}
			// ���½���
			else if(""==price&&""!=remark) {
				// ����sql���
				sql="update dishestbl set remark = ? where name = ? ";
				flag=1;
			}
			// ���¼۸�
			else if(""!=price&&""==remark) {
				// ����sql���
				sql="update dishestbl set price = ? where name = ? ";
				flag=2;
			}
			// �ò�Ʒδ�����κθ���
			else {
				return "ָ����Ʒδ�����κθ���!";
			}
		}
		//������ݿ����ӹ�����
		DBUtil util=new DBUtil();
		//��������
		Connection conn=util.openConnection();
		
		try {
			//���Ԥ�����ѯ���
			PreparedStatement pstmt=conn.prepareStatement(sql);
			PreparedStatement queryPstmt=conn.prepareStatement(querySql);
			//���ò���
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
			//ִ�����ݿ��ѯ
			ResultSet rs=queryPstmt.executeQuery();
			//�ж��û��Ƿ����
			if(!rs.next()) {
				return "��Ʒ������!�޷�����!";
			}
			else {
				while(0!=flag) {
					pstmt.executeUpdate();
					return "���²�Ʒ�ɹ�!";
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
