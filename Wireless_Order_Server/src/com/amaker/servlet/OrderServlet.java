package com.amaker.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amaker.dao.OrderDao;
import com.amaker.dao.PayDao;
import com.amaker.dao.TableDao;
import com.amaker.dao.impl.OrderDaoImpl;
import com.amaker.dao.impl.PayDaoImpl;
import com.amaker.dao.impl.TableDaoImpl;
import com.amaker.entity.Order;
import com.amaker.entity.OrderDetail;
import com.amaker.entity.QueryOrder;
import com.amaker.entity.QueryOrderDetail;
import com.google.gson.Gson;

public class OrderServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		
		PrintWriter out=response.getWriter();
		Gson gson=new Gson();
		
		// 获得客户端请求参数
		String requestStr=request.getParameter("requestStr");
		switch(requestStr) {
			case "confirmDealReq":
				// 实例化OrderDao
				OrderDao confirmDealDao=new OrderDaoImpl();
				
				String szOrderId=request.getParameter("orderId");
				String szDishId=request.getParameter("dishId");
				String szDishNum=request.getParameter("number");
				
				int orderId=Integer.parseInt(szOrderId);
				if(0==orderId) {
					String szUserId=request.getParameter("userId");
					int nUserId=Integer.parseInt(szUserId);
					
					orderId = confirmDealDao.checkOrder(nUserId);
				}
				int dishId=Integer.parseInt(szDishId);
				int dishNum=Integer.parseInt(szDishNum);
				String remark=request.getParameter("remark");
				
				OrderDetail orderDetail=new OrderDetail();
				
				orderDetail.setOrderId(orderId);
				orderDetail.setDishId(dishId);
				orderDetail.setDishNum(dishNum);
				orderDetail.setRamark(remark);
				
				confirmDealDao.saveOrderDetail(orderDetail);
				confirmDealDao.updateOrderIsOrder(orderId);
				
				System.out.println("REQUEST CONFIRM DEAL..................................");
				
				out.print(orderId);
				break;
			case "startTableReq":
				// 从request中获得各参数信息
				String userId=request.getParameter("userid");
				String tableId=request.getParameter("tableid");
				String customerNum=request.getParameter("customernum");
				String orderDate=request.getParameter("orderdate");
				// 获得orderDAO
				OrderDao startTableDao=new OrderDaoImpl();
				// 实例化订单类
				Order order=new Order();
				// 为订单类赋值
				order.setUserId(Integer.parseInt(userId));
				order.setTableId(Integer.parseInt(tableId));
				order.setCustomerNum(Integer.parseInt(customerNum));
				order.setOrderDate(orderDate);
				// 获得当前订单的id即订单编号
				int id=startTableDao.saveOrder(order);
				if(0!=id) {
					// 更新桌子状态为有人
					startTableDao.updateTableStateStarted(order);
				}
				
				System.out.println("REQUEST START TABLE....................................");
				
				// 返回订单id
				out.print(id);
				break;
			case "checkTableReq":
				//实例化TableDao
				TableDao checkTabledao=new TableDaoImpl();
				
				System.out.println("REQUEST CHECK TABLES...................................");
				
				//转换成字符串，返回给客户端
				out.print(gson.toJson(checkTabledao.getTableList()));
				break;
			case "orderDishesReq":
				// 实例化OrderDao
				OrderDao orderDishDao=new OrderDaoImpl();
				
				System.out.println("REQUEST GET ORDERDISHES................................");
				
				// 返回给客户端
				out.print(gson.toJson(orderDishDao.getDishesList()));
				break;
			case "payReq":
				// 实例化PayDao
				PayDao payDao=new PayDaoImpl();
				// 获取订单id,客户端方传过来
				String szId=request.getParameter("orderid4pay");
				int orderid=Integer.parseInt(szId);
				// 查询订单信息
				QueryOrder queryOrder=payDao.getOrderById(orderid);
				// 查询订单详情表
				@SuppressWarnings("unchecked") 
				List<QueryOrderDetail> list=payDao.getOrderDetailList(orderid);
				
				System.out.println("REQUEST PAY............................................");
				
				out.print(gson.toJson(queryOrder)+"|"+gson.toJson(list));
				break;
			case "updOrderStateReq":
				// 实例化PayDao
				PayDao updOrderStateDao=new PayDaoImpl();
				// 获取订单id,客户端方传过来
				String szUpdId=request.getParameter("orderid4pay");
				int nUpdOrderid=Integer.parseInt(szUpdId);
				// 执行更新ordertbl
				updOrderStateDao.pay(nUpdOrderid);
				
				System.out.println("REQUEST UPDATE ORDER STATE AFTER PAY....................");
				
				// 向客户端发送信息
				out.print("订单:"+nUpdOrderid+"结算完成!");
				break;
			default:
				break;
		}
		
		out.flush();
		out.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
}
