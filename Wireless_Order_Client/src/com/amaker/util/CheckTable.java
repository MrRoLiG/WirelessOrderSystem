package com.amaker.util;

public class CheckTable {
	private int id;				//����id
	private int flag;			//�����Ƿ��Ѿ����˱�־
	private int customerNum;	//�˿�����
	private String remark;		//��ע��Ϣ
	
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
