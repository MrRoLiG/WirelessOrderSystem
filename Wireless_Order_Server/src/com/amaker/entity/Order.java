package com.amaker.entity;

// ���ڷ�װordertbl���ʵ����
public class Order {
	private int id;				//����Id(����)
	private int dealid;			//����Id
	private int userid;			//�û�Id
	private int tableid;		//����Id
	private int customernum;	//�˿���
	private int ispay;			//�Ƿ��Ѿ�֧��
	private String orderdate; 	//��������
	private String remark;		//��ע
	
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
