package com.amaker.entity;

//用于封装orderdetailtbl表的实体类
public class OrderDetail {
	private int id;
	private int orderid;
	private int dishid;
	private int dishnum;
	private String remark;
	
	public void setId(int id) {
		this.id=id;
	}
	public void setOrderId(int orderid) {
		this.orderid=orderid;
	}
	public void setDishId(int dishid) {
		this.dishid=dishid;
	}
	public void setDishNum(int dishnum) {
		this.dishnum=dishnum;
	}
	public void setRamark(String remark) {
		this.remark=remark;
	}
	
	public int getId() {
		return this.id;
	}
	public int getOrderId() {
		return this.orderid;
	}
	public int getDishId() {
		return this.dishid;
	}
	public int getDishNum() {
		return this.dishnum;
	}
	public String getRemark() {
		return this.remark;
	}
	
}
