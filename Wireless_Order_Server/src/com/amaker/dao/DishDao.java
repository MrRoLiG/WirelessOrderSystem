package com.amaker.dao;

import java.util.List;

import com.amaker.entity.Dish;

// DishDaoImpl�Ľӿ�
public interface DishDao {
	// ���ڹ������Ӳ�Ʒ
	public String addDish(Dish dish);
	// ���ڹ���˻�ȡ��ǰ���в�Ʒ��Ϣ
	public List getAllDishes();
	// ���ݲ�Ʒ�������ݿ���ɾ���ò�Ʒ
	public String deleteDishByName(String name);
	// ���²�Ʒ��Ϣ
	public String updateDish(String name,String price,String remark);
}
