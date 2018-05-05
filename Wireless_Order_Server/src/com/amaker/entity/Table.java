package com.amaker.entity;

//用于封装TableTbl表的实体类
public class Table {

	private int id;
	private int customerNum;
	private int flag;
	private String remark;
	
	//获取方法
	public int getId() {
		return this.id;
	}
	public int getCustomerNum() {
		return this.customerNum;
	}
	public int getFlag() {
		return this.flag;
	}
	public String getRemark() {
		return this.remark;
	}
	
	//设置方法
	public void setId(int id) {
		this.id=id;
	}
	public void setCustomerNum(int customerNum) {
		this.customerNum=customerNum;
	}
	public void setFlag(int flag) {
		this.flag=flag;
	}
	public void setRemark(String remark) {
		this.remark=remark;
	}
}
