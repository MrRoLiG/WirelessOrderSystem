package com.amaker.provider;

public class Dishes {
	private int id;				//编号
	private String name;		//菜名
	private String type;		//菜系
	private int price;			//价格
	private String picture;		//图片
	private String remark;		//评价
	private int number;			//数量
	
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
