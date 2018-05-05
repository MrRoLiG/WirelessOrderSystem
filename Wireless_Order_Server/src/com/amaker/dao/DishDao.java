package com.amaker.dao;

import java.util.List;

import com.amaker.entity.Dish;

// DishDaoImpl的接口
public interface DishDao {
	// 用于管理端添加菜品
	public String addDish(Dish dish);
	// 用于管理端获取当前所有菜品信息
	public List getAllDishes();
	// 根据菜品名从数据库中删除该菜品
	public String deleteDishByName(String name);
	// 更新菜品信息
	public String updateDish(String name,String price,String remark);
}
