package com.amaker.dao;

import java.util.List;

import com.amaker.entity.Order;
import com.amaker.entity.OrderDetail;

//OrderDao的接口
public interface OrderDao {
	
	// 得到菜单列表
	public List getDishesList();
	// 保存开桌信息
	public int saveOrder(Order order);
	// 更新桌子状态有人
	public void updateTableStateStarted(Order order);
	public void updateTableStateStarted(int tableid,int custnum);
	// 通过orderid更新桌子状态
	public void updateTableState(int orderid,boolean isStarted);
	// 保存OrderDetal到数据库
	public void saveOrderDetail(OrderDetail orderDetail);
	// 通过userid查询当前订单是否存在
	public int checkOrder(int nUserId);
	public int checkOrderEx(int nUserId);
	// 获得已下单的订单信息用以客户端跑马灯显示
	public List getPaidOrderInfo();
	// 获得已下单的订单详情信息用于客户端提示上菜
	public List getPaidOrderDetailInfo();
	// 通过订单号更新订单上菜状态
	public String updateOrderDishUpState(int orderid);
	// 获得当前订单状态
	public String getCurrentOrderState(int userid);
	// 更新订单是否完成
	public String updateOrderIsDone(int orderid);
	// 更新是否下单
	public void updateOrderIsOrder(int orderid);
	// 通过userid获得相关订单信息（用于注销后获取未完成订单信息）
	public String getOrderInfoAfterLogout(int userid);
}
