package com.amaker.util;

public class CheckDish {
	private int id;			//��Ʒid
	private String name;	//��Ʒ����
	private String type;	//��Ʒ��ϵ
	private int price;		//��Ʒ�۸�
	private String picture;	//��ƷͼƬ	
	private String remark;	//��Ʒ��Ϣ
	
	public void setId(int id) {
		this.id=id;
	}
	public void setName(String name) {
		this.name=name;
	}
	public void setType(String type) {
		this.type=type;
	}
	public void setPrice(int price) {
		this.price=price;
	}
	public void setPicture(String picture) {
		this.picture=picture;
	}
	public void setRemark(String remark) {
		this.remark=remark;
	}
	
	public int getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public String getType() {
		return this.type;
	}
	public int getPrice() {
		return this.price;
	}
	public String getPicture() {
		return this.picture;
	}
	public String getRemark() {
		return this.remark;
	}
}
