package com.amaker.entity;

public class AdminQueryOrderDetail {
	private int orderid;
	private String name;
	private int dishnum;
	private String remark;
	
	public void setOrderId(int orderid) {
		this.orderid=orderid;
	}
	public void setName(String name) {
		this.name=name;
	}
	public void setDishNum(int dishnum) {
		this.dishnum=dishnum;
	}
	public void setRemark(String remark) {
		this.remark=remark;
	}
	
	public int getOrderId() {
		return this.orderid;
	}
	public String getName() {
		return this.name;
	}
	public int getDishNum() {
		return this.dishnum;
	}
	public String getRemark() {
		return this.remark;
	}
}
