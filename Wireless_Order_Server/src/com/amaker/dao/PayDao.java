package com.amaker.dao;

import java.util.List;

import com.amaker.entity.QueryOrder;

// PayDao�Ľӿ�
public interface PayDao {
	// ͨ������id��ȡ���������Ͳ�Ʒ������ϱ�
	public List getOrderDetailList(int orderid);
	// ͨ������id��ȡ������Ϣ
	public QueryOrder getOrderById(int orderid); 
	// ����󽫶���id�Ķ���״̬��Ϊ��֧��
	public void pay(int orderid);
}
