package com.amaker.entity;

// 用于封装ordertbl表的实体类
public class Order {
	private int id;				//订单Id(主键)
	private int dealid;			//交易Id
	private int userid;			//用户Id
	private int tableid;		//桌子Id
	private int customernum;	//顾客数
	private int ispay;			//是否已经支付
	private String orderdate; 	//订单日期
	private String remark;		//备注
	
	public void setId(int id) {
		this.id=id;
	}
	public void setDealId(int dealid) {
		this.dealid=dealid;
	}
	public void setUserId(int userid) {
		this.userid=userid;
	}
	public void setTableId(int tableid) {
		this.tableid=tableid;
	}
	public void setCustomerNum(int customernum) {
		this.customernum=customernum;
	}
	public void setIsPay(int ispay) {
		this.ispay=ispay;
	}
	public void setOrderDate(String orderdate) {
		this.orderdate=orderdate;
	}
	public void setRemark(String remark) {
		this.remark=remark;
	}
	
	public int getId() {
		return this.id;
	}
	public int getDealId() {
		return this.dealid;
	}
	public int getUserId() {
		return this.userid;
	}
	public int getTableId() {
		return this.tableid;
	}
	public int getCustomerNum() {
		return this.customernum;
	}
	public int getIsPay() {
		return this.ispay;
	}
	public String getOrderDate() {
		return this.orderdate;
	}
	public String getRemark() {
		return this.remark;
	}
}
