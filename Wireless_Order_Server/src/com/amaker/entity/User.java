package com.amaker.entity;

//���ڷ�װUserTbl���ʵ����
public class User {
	private int id;					//���
	private String name;			//�û�����
	private String account;			//�û��˺�
	private String password;		//�û�����
	private String gender;			//�û��Ա�
	private int permission;			//�û�Ȩ��
	private String remark;			//�û���ע
	
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
