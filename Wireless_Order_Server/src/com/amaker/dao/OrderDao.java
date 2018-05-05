package com.amaker.dao;

import java.util.List;

import com.amaker.entity.Order;
import com.amaker.entity.OrderDetail;

//OrderDao�Ľӿ�
public interface OrderDao {
	
	// �õ��˵��б�
	public List getDishesList();
	// ���濪����Ϣ
	public int saveOrder(Order order);
	// ��������״̬����
	public void updateTableStateStarted(Order order);
	public void updateTableStateStarted(int tableid,int custnum);
	// ͨ��orderid��������״̬
	public void updateTableState(int orderid,boolean isStarted);
	// ����OrderDetal�����ݿ�
	public void saveOrderDetail(OrderDetail orderDetail);
	// ͨ��userid��ѯ��ǰ�����Ƿ����
	public int checkOrder(int nUserId);
	public int checkOrderEx(int nUserId);
	// ������µ��Ķ�����Ϣ���Կͻ����������ʾ
	public List getPaidOrderInfo();
	// ������µ��Ķ���������Ϣ���ڿͻ�����ʾ�ϲ�
	public List getPaidOrderDetailInfo();
	// ͨ�������Ÿ��¶����ϲ�״̬
	public String updateOrderDishUpState(int orderid);
	// ��õ�ǰ����״̬
	public String getCurrentOrderState(int userid);
	// ���¶����Ƿ����
	public String updateOrderIsDone(int orderid);
	// �����Ƿ��µ�
	public void updateOrderIsOrder(int orderid);
	// ͨ��userid�����ض�����Ϣ������ע�����ȡδ��ɶ�����Ϣ��
	public String getOrderInfoAfterLogout(int userid);
}
