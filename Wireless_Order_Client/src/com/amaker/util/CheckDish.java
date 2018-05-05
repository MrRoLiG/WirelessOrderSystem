package com.amaker.util;

public class CheckDish {
	private int id;			//菜品id
	private String name;	//菜品名称
	private String type;	//菜品菜系
	private int price;		//菜品价格
	private String picture;	//菜品图片	
	private String remark;	//菜品信息
	
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
