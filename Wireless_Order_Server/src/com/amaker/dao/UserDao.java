package com.amaker.dao;

import java.util.List;

import com.amaker.entity.User;

//UserDaoImpl�Ľӿ�
public interface UserDao {
	
	//��¼����
	public User login(String account,String password);
	//ע�᷽��
	public String Register(String nickname, String account,String password);
	//����Ա����û�
	public String addUser(User user);
	//�õ���ǰ�����û�
	public List getAllUser();
	//ɾ��ָ��name���û�
	public String deleteUserByName(String name);
}
