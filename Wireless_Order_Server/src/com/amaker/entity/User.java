package com.amaker.entity;

//用于封装UserTbl表的实体类
public class User {
	private int id;					//编号
	private String name;			//用户名称
	private String account;			//用户账号
	private String password;		//用户密码
	private String gender;			//用户性别
	private int permission;			//用户权限
	private String remark;			//用户备注
	
	public void setId(int id) {
		this.id=id;
	}
	public void setName(String name) {
		this.name=name;
	}
	public void setAccount(String account) {
		this.account=account;
	}
	public void setPassword(String password) {
		this.password=password;
	}
	public void setGender(String gender) {
		this.gender=gender;
	}
	public void setPermission(int permission) {
		this.permission=permission;
	}
	public void setRemark(String remark) {
		this.remark=remark;
	}
	
	public int getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public String getAccount() {
		return this.account;
	}
	public String getPassword() {
		return this.password;
	}
	public String getGender() {
		return this.gender;
	}
	public int getPermission() {
		return this.permission;
	}
	public String getRemark() {
		return this.remark;
	}
}
