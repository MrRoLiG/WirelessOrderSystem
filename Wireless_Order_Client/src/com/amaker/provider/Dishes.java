package com.amaker.provider;

public class Dishes {
	private int id;				//���
	private String name;		//����
	private String type;		//��ϵ
	private int price;			//�۸�
	private String picture;		//ͼƬ
	private String remark;		//����
	private int number;			//����
	
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
	public void setNumber(int number) {
		this.number=number;
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
	public int getNumber() {
		return this.number;
	}
}
