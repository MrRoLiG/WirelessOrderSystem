package com.amaker.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amaker.dao.OrderDao;
import com.amaker.dao.TableDao;
import com.amaker.dao.UserDao;
import com.amaker.dao.impl.OrderDaoImpl;
import com.amaker.dao.impl.TableDaoImpl;
import com.amaker.dao.impl.UserDaoImpl;
import com.amaker.entity.User;
import com.google.gson.Gson;

public class MainServlet extends HttpServlet{
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
			case "loginReq":
				// ʵ����UserDao
				UserDao loginDao=new UserDaoImpl();
				
				//��ÿͻ����������
				String szLoginUserName=request.getParameter("account");
				String szLoginPassword=request.getParameter("password");
				System.out.println("REQUEST LOGIN IN.........................");
				
				User user=loginDao.login(szLoginUserName, szLoginPassword);
				//�ж��Ƿ��ҵ��û�
				if(null!=user) {
					//�ҵ��û�,��user��Ϣת��json�����ͻ���
					System.out.println("FOUND USER ... WAITIONG LOGIN IN..........");
					out.print(gson.toJson(user));
				}
				else {
					//û���ҵ�
					System.out.println("NOT FOUND USER ... WAITIING BACK..........");
					out.print("0");
				}
				break;
			case "registerReq":
				UserDao userDao=new UserDaoImpl();
				//��ÿͻ����������
				String szRegUserName=request.getParameter("account");
				String szRegNickName=request.getParameter("nickname");
				String szRegPassword=request.getParameter("password");
				
				System.out.println("REQUEST REGISTER.......................");
				
				String res=userDao.Register(szRegNickName, szRegUserName, szRegPassword);
				//�ж��Ƿ�ע��ɹ�
				if(null!=res&&res.equals("0")) {
					System.out.println("REGISTER FAILED........................");
					out.print("0");
				}
				if(null!=res&&res.equals("1")) {
					System.out.println("REGISTER SUCCUESS......................");
					out.print("1");
				}
				break;
			case "getCurrentStateReq":
				// ʵ����OrderDao
				OrderDao getStateDao=new OrderDaoImpl();
				String szUserId=request.getParameter("userid");
				
				System.out.println("REQUEST GET CURRENT USER ORDER STATE.........");
				
				out.print(getStateDao.getCurrentOrderState(Integer.parseInt(szUserId)));
				break;
			case "getUserOrderStateReq":
				// ʵ����OrderDao
				OrderDao getUserOrderStateDao=new OrderDaoImpl();
				String szCheckUserId=request.getParameter("userid");
				
				System.out.println("REQUEST GET USER ORDER STATE.......������������..");
				
				out.print(getUserOrderStateDao.checkOrder(Integer.parseInt(szCheckUserId)));
				break;
			case "orderTwiceReq":
				// ʵ����OrderDao
				OrderDao orderTwiceDao=new OrderDaoImpl();
				String szCheckUserIdEx=request.getParameter("userid");
				
				System.out.println("REQUEST CLICK ORDERDISH BUTTON AFTER LOGOUT.........");
				
				out.print(orderTwiceDao.checkOrderEx(Integer.parseInt(szCheckUserIdEx)));
				break;
			case "logout4ActReq":
				// ʵ����OrderDao
				OrderDao logout4ActDao=new OrderDaoImpl();
				String szLogout4UserId=request.getParameter("userid");
				
				System.out.println("REQUEST ACT AFTER LOGOUT FOR UPDATE CONFIG.........");
				
				out.print(logout4ActDao.getOrderInfoAfterLogout(Integer.parseInt(szLogout4UserId)));
				break;
			case "changeTableReq":
				// ����������
				String orderId=request.getParameter("orderId");
				String tableId=request.getParameter("tableId");
				String custNum=request.getParameter("custNum");
				
				System.out.println("REQUEST CHANGE TABLE...............................");
				
				if("0"==orderId||"0"==tableId||"0"==custNum) {
					out.print("�뱣֤����������ȷ��");
				}
				else {
					// ���OrderDao��TableDao��ʵ��
					TableDao changeTableDao=new TableDaoImpl();
					// ���ж�ѡ������������Ƿ�����
					// ����
					if(changeTableDao.isTableStarted(Integer.parseInt(tableId))) {
						out.print("ѡ������������Ѿ����ˣ�������ѡ����λ�ţ�");
					}
					// û�˼����л�������
					else {
						changeTableDao.changeTable(Integer.parseInt(orderId), Integer.parseInt(tableId), Integer.parseInt(custNum));
						out.print("�����ɹ���");
					}
				}
				break;
			case "unionTableReq":
				// ����������
				String szUnionOrderId=request.getParameter("orderId");
				String szOriginTableId=request.getParameter("originTableId");
				String szTargetTableId=request.getParameter("targetTableId");
				String szCustNum=request.getParameter("custNum");
				
				System.out.println("REQUEST UNION TABLE..................................");
				
				if("0"==szUnionOrderId||"0"==szOriginTableId||"0"==szTargetTableId||"0"==szCustNum) {
					out.print("�뱣֤����������ȷ��");
				}
				else {
					// ���OrderDao��TableDao��ʵ��
					TableDao unionTableDao=new TableDaoImpl();
					// ���в�������
					unionTableDao.unionTable(Integer.parseInt(szOriginTableId), Integer.parseInt(szTargetTableId), Integer.parseInt(szCustNum), Integer.parseInt(szUnionOrderId));
					out.print("�����ɹ���");
				}
				break;
			case "checkTableReq":
				//ʵ����TableDao
				TableDao checkTableDao=new TableDaoImpl();
				
				System.out.println("REQUEST CHECK ALL TABLES..............................");
				
				//ת�����ַ��������ظ��ͻ���
				out.print(gson.toJson(checkTableDao.getTableList()));
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
