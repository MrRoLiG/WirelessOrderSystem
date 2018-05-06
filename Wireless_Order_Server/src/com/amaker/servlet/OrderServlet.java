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
		
		// ��ÿͻ����������
		String requestStr=request.getParameter("requestStr");
		switch(requestStr) {
			case "confirmDealReq":
				// ʵ����OrderDao
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
				// ��request�л�ø�������Ϣ
				String userId=request.getParameter("userid");
				String tableId=request.getParameter("tableid");
				String customerNum=request.getParameter("customernum");
				String orderDate=request.getParameter("orderdate");
				// ���orderDAO
				OrderDao startTableDao=new OrderDaoImpl();
				// ʵ����������
				Order order=new Order();
				// Ϊ�����ำֵ
				order.setUserId(Integer.parseInt(userId));
				order.setTableId(Integer.parseInt(tableId));
				order.setCustomerNum(Integer.parseInt(customerNum));
				order.setOrderDate(orderDate);
				// ��õ�ǰ������id���������
				int id=startTableDao.saveOrder(order);
				if(0!=id) {
					// ��������״̬Ϊ����
					startTableDao.updateTableStateStarted(order);
				}
				
				System.out.println("REQUEST START TABLE....................................");
				
				// ���ض���id
				out.print(id);
				break;
			case "checkTableReq":
				//ʵ����TableDao
				TableDao checkTabledao=new TableDaoImpl();
				
				System.out.println("REQUEST CHECK TABLES...................................");
				
				//ת�����ַ��������ظ��ͻ���
				out.print(gson.toJson(checkTabledao.getTableList()));
				break;
			case "orderDishesReq":
				// ʵ����OrderDao
				OrderDao orderDishDao=new OrderDaoImpl();
				
				System.out.println("REQUEST GET ORDERDISHES................................");
				
				// ���ظ��ͻ���
				out.print(gson.toJson(orderDishDao.getDishesList()));
				break;
			case "payReq":
				// ʵ����PayDao
				PayDao payDao=new PayDaoImpl();
				// ��ȡ����id,�ͻ��˷�������
				String szId=request.getParameter("orderid4pay");
				int orderid=Integer.parseInt(szId);
				// ��ѯ������Ϣ
				QueryOrder queryOrder=payDao.getOrderById(orderid);
				// ��ѯ���������
				@SuppressWarnings("unchecked") 
				List<QueryOrderDetail> list=payDao.getOrderDetailList(orderid);
				
				System.out.println("REQUEST PAY............................................");
				
				out.print(gson.toJson(queryOrder)+"|"+gson.toJson(list));
				break;
			case "updOrderStateReq":
				// ʵ����PayDao
				PayDao updOrderStateDao=new PayDaoImpl();
				// ��ȡ����id,�ͻ��˷�������
				String szUpdId=request.getParameter("orderid4pay");
				int nUpdOrderid=Integer.parseInt(szUpdId);
				// ִ�и���ordertbl
				updOrderStateDao.pay(nUpdOrderid);
				
				System.out.println("REQUEST UPDATE ORDER STATE AFTER PAY....................");
				
				// ��ͻ��˷�����Ϣ
				out.print("����:"+nUpdOrderid+"�������!");
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
