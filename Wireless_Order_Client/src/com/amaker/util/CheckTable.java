package com.amaker.util;

public class CheckTable {
	private int id;				//桌子id
	private int flag;			//桌子是否已经有人标志
	private int customerNum;	//顾客数量
	private String remark;		//备注信息
	
	public int getId() {
		return this.id;
	}
	public int getFlag() {
		return this.flag;
	}
	public int getCustomerNum() {
		return this.customerNum;
	}
	public String getRemark() {
		return this.remark;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	public void setFlag(int flag) {
		this.flag=flag;
	}
	public void setCustmoerNum(int custmoerNum) {
		this.customerNum=custmoerNum;
	}
	public void setRemark(String remark) {
		this.remark=remark;
	}
}
