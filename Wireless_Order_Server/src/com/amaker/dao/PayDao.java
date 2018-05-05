package com.amaker.dao;

import java.util.List;

import com.amaker.entity.QueryOrder;

// PayDao的接口
public interface PayDao {
	// 通过订单id获取订单详情表和菜品表的联合表
	public List getOrderDetailList(int orderid);
	// 通过订单id获取订单信息
	public QueryOrder getOrderById(int orderid); 
	// 结算后将对于id的订单状态置为已支付
	public void pay(int orderid);
}
