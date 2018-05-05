package com.amaker.dao;

import java.util.List;

import com.amaker.entity.User;

//UserDaoImpl的接口
public interface UserDao {
	
	//登录方法
	public User login(String account,String password);
	//注册方法
	public String Register(String nickname, String account,String password);
	//管理员添加用户
	public String addUser(User user);
	//得到当前所有用户
	public List getAllUser();
	//删除指定name的用户
	public String deleteUserByName(String name);
}
