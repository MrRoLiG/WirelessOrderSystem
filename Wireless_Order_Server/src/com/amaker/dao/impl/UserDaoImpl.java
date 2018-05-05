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

//UserDaoʵ����
public class UserDaoImpl implements UserDao {

	@Override
	public User login(String account, String password) {
		//sql��ѯ�ַ���
		String sql="select id,name,account,password,permission,remark "
				 + "from usertbl "
				 + "where account=? and password=? ";
		
		//������ݿ����ӹ�����
		DBUtil util=new DBUtil();
		//��������
		Connection conn=util.openConnection();
		try {
			//���Ԥ�����ѯ���
			PreparedStatement pstmt=conn.prepareStatement(sql);
			//���ò���
			pstmt.setString(1,account);
			pstmt.setString(2,password);
			//ִ�����ݿ��ѯ
			ResultSet rs=pstmt.executeQuery();
			//�ж��û��Ƿ����
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
		//����sql���
		String sql="insert into usertbl (name,account,password,permission) values (?,?,?,2) ";
		//��ѯ���û��Ƿ��Ѿ�����
		String querySql="select * from usertbl where account=? ";
		//������ݿ����ӹ�����
		DBUtil util=new DBUtil();
		//�������
		Connection conn=util.openConnection();
		try {
			//���Ԥ�������
			PreparedStatement pstmt=conn.prepareStatement(sql);
			PreparedStatement queryPstmt=conn.prepareStatement(querySql);
			//���ò���
			pstmt.setString(1,nickname);
			pstmt.setString(2,account);
			pstmt.setString(3, password);
			
			queryPstmt.setString(1, account);
			ResultSet rs=queryPstmt.executeQuery();
			
			String res=rs.toString();
			//�������ֱ�ӷ��ء�0��
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
		//����sql���
		String sql="insert into usertbl (name,account,password,gender,permission,remark) values (?,?,?,?,?,?) ";
		//��ѯ���û��Ƿ��Ѿ�����
		String querySql="select * from usertbl where account= ? ";
		//������ݿ����ӹ�����
		DBUtil util=new DBUtil();
		//�������
		Connection conn=util.openConnection();
		try {
			//���Ԥ�������
			PreparedStatement pstmt=conn.prepareStatement(sql);
			PreparedStatement queryPstmt=conn.prepareStatement(querySql);

			queryPstmt.setString(1, user.getAccount());
			ResultSet rs=queryPstmt.executeQuery();
			//�������
			if(rs.next()) {
				return "�û��Ѵ���!";
			}
			else {
				//���ò���
				pstmt.setString(1,user.getName());
				pstmt.setString(2,user.getAccount());
				pstmt.setString(3,user.getPassword());
				pstmt.setString(4, user.getGender());
				pstmt.setInt(5, user.getPermission());
				pstmt.setString(6, user.getRemark());
				
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
	public List getAllUser() {
		// ����һ�ű�������������û���Ϣ
		List<User> userList=new ArrayList<User>();
		// ��ѯsql���
		String sql="select * from usertbl ";
		//������ݿ����ӹ�����
		DBUtil util=new DBUtil();
		//�������
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
		// ɾ��sql���
		String sql="delete from usertbl where name= ? ";
		//��ѯ���û��Ƿ��Ѿ�����
		String querySql="select * from usertbl where name= ? ";
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
				return "�û�������!�޷�ɾ��!";
			}
			else {
				pstmt.executeUpdate();
				return "ɾ���û��ɹ�!";
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
