package com.amaker.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amaker.dao.DishDao;
import com.amaker.dao.OrderDao;
import com.amaker.dao.TableDao;
import com.amaker.dao.UserDao;
import com.amaker.dao.impl.DishDaoImpl;
import com.amaker.dao.impl.OrderDaoImpl;
import com.amaker.dao.impl.TableDaoImpl;
import com.amaker.dao.impl.UserDaoImpl;
import com.amaker.entity.Dish;
import com.amaker.entity.User;
import com.google.gson.Gson;

public class AdminServlet extends HttpServlet{
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
			case "userAddReq":
				// 添加用户，实例化userDao
				UserDao addUserDao=new UserDaoImpl();
				User user=new User();
				
				user.setName(request.getParameter("name"));
				user.setAccount(request.getParameter("account"));
				user.setPassword(request.getParameter("password"));
				user.setGender(request.getParameter("gender"));
				user.setPermission(Integer.parseInt(request.getParameter("permission")));
				user.setRemark(request.getParameter("remark"));
				
				System.out.println("REQUEST ADD USER..................................");
				
				String addUserRes = addUserDao.addUser(user);
				out.print(addUserRes);
				break;
			case "allUserReq":
				// 实例化用户userDao
				UserDao allUserDao=new UserDaoImpl();
				
				System.out.println("REQUEST GET ALL USERS.............................");
				
				String allUserRes=gson.toJson(allUserDao.getAllUser());
				out.print(allUserRes);
				break;
			case "userDeleteReq":
				// 实例化UserDao
				UserDao deleteUserDao=new UserDaoImpl();
				
				System.out.println("REQUEST DELETE USER...............................");
				
				String userName=request.getParameter("name");
				out.print(deleteUserDao.deleteUserByName(userName));
				break;
			case "dishAddReq":
				// 实例化菜品dishDao
				DishDao addDishDao=new DishDaoImpl();
				Dish dish=new Dish();
				
				System.out.println("REQUEST ADD DISH..................................");
				
				dish.setName(request.getParameter("name"));
				dish.setType(request.getParameter("type"));
				dish.setPrice(Integer.parseInt(request.getParameter("price")));
				dish.setPicture(request.getParameter("picture"));
				dish.setRemark(request.getParameter("remark"));
				
				String addDishRes=addDishDao.addDish(dish);
				out.print(addDishRes);
				break;
			case "allDishesReq":
				// 实例化DishDao
				DishDao allDishesDao=new DishDaoImpl();
				
				System.out.println("REQUEST GET ALL DISHES............................");
				
				String allDishesRes=gson.toJson(allDishesDao.getAllDishes());
				out.print(allDishesRes);
				break;
			case "dishDeleteReq":
				// 实例化DishDao
				DishDao deleteDishDao=new DishDaoImpl();
				
				System.out.println("REQUEST DELETE DISH...............................");
				
				String dishName=request.getParameter("name");
				out.print(deleteDishDao.deleteDishByName(dishName));
				break;
			case "dishUpdateReq":
				// 实例化 DishDao
				DishDao updateDishDao=new DishDaoImpl();
				
				System.out.println("REQUEST UPDATE DISH...............................");
				
				String name=request.getParameter("name");
				String price=request.getParameter("price");
				String remark=request.getParameter("remark");
				out.print(updateDishDao.updateDish(name, price, remark));
				break;
			case "clearTableReq":
				// 实例化TableDao
				TableDao clearTableDao=new TableDaoImpl();
				String szTableId=request.getParameter("tableid");
				
				System.out.println("REQUEST CLEAR TABLE...............................");
				
				out.print(clearTableDao.clearTableById(Integer.parseInt(szTableId)));
				break;
			case "isDoneReq":
				// 实例化OrderDao
				OrderDao isDoneDao=new OrderDaoImpl();
				String szIsDoneOrderId=request.getParameter("orderid");
				
				System.out.println("REQUEST ORDER IS DONE.............................");
				
				//isDoneDao.updateTableState(Integer.parseInt(szIsDoneOrderId), false);
				out.print(isDoneDao.updateOrderIsDone(Integer.parseInt(szIsDoneOrderId)));
				break;
			case "listenOrderReq":
				// 实例化OrderDao
				OrderDao listenOrderDao=new OrderDaoImpl();
				
				System.out.println("REQUEST LISTEN ORDER..............................");
				
				String listenOrderStr=gson.toJson(listenOrderDao.getPaidOrderInfo());
				out.println(listenOrderStr);
				break;
			case "allOrdersReq":
				// 实例化OrderDao
				OrderDao allOrdersDao=new OrderDaoImpl();
				
				System.out.println("REQUEST GET ALL ORDERS............................");
				
				String allOrdersRes=gson.toJson(allOrdersDao.getPaidOrderDetailInfo());
				out.print(allOrdersRes);
				break;
			case "dishUpReq":
				// 实例化OrderDao
				OrderDao dishUpDao=new OrderDaoImpl();
				
				System.out.println("REQUEST DISH UP...................................");
				
				String szOrderId=request.getParameter("orderid");
				out.print(dishUpDao.updateOrderDishUpState(Integer.parseInt(szOrderId)));
				break;
			default:
				// 不做处理
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
