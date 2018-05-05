package com.amaker.provider;

// 用于封装dishestbl和orderdetailtbl联合查询的表
public class QueryOrderDetail {
	private int dishid;
	private String name;
	private int price;
	private int dishnum;
	private int totalcost;
	private String remark;
	
	public void setDishId(int dishid) {
		this.dishid=dishid;
	}
	public void setName(String name) {
		this.name=name;
	}
	public void setPrice(int price) {
		this.price=price;
	}
	public void setDishNum(int dishnum) {
		this.dishnum=dishnum;
	}
	public void setTotalCost(int totalcost) {
		this.totalcost=totalcost;
	}
	public void setRemark(String remark) {
		this.remark=remark;
	}
	
	public int getDishId() {
		return this.dishid;
	}
	public String getName() {
		return this.name;
	}
	public int getPrice() {
		return this.price;
	}
	public int getDishNum() {
		return this.dishnum;
	}
	public int getTotalCost() {
		return this.totalcost;
	}
	public String getRemark() {
		return this.remark;
	}
}
