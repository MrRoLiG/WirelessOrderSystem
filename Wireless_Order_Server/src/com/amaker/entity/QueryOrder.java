package com.amaker.entity;
// ��װ��ѯ����Order��Ϣ
public class QueryOrder {
	private int tableid;
	private int customernum;
	private String name;
	private String orderdate;
	
	public void setTableId(int tableid) {
		this.tableid=tableid;
	}
	public void setCustomerNum(int customernum) {
		this.customernum=customernum;
	}
	public void setName(String name) {
		this.name=name;
	}
	public void setOrderDate(String orderdate) {
		this.orderdate=orderdate;
	}
	
	public int getTableId() {
		return this.tableid;
	}
	public int getCustomerNum() {
		return this.customernum;
	}
	public String getName() {
		return this.name;
	}
	public String getOrderDate() {
		return this.orderdate;
	}
}
