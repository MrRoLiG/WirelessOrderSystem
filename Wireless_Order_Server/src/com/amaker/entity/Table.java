package com.amaker.entity;

//���ڷ�װTableTbl���ʵ����
public class Table {

	private int id;
	private int customerNum;
	private int flag;
	private String remark;
	
	//��ȡ����
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
	
	//���÷���
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
