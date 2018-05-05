package com.amaker.dao;

import java.util.List;

public interface TableDao {
	// 获得桌子号id的List
	public List getTableList();
	// 通过orderid和tableid和custnum对数据库进行更换桌号设置
	public void changeTable(int orderid,int tableid,int custnum);
	// 通过桌子id查询该桌子是否有人
	public boolean isTableStarted(int tableid);
	// 通过桌子id(需要合并的桌子号)
	// 目前暂时支持合并两个桌子
	public void unionTable(int originTableId,int targetTableId,int originTableCustNum,int orderId);
	// 清桌，即将所有桌子的相关信息重置，用于管理员端
	public String clearTable();
	// 根据tableid清理对应的桌子信息
	public String clearTableById(int tableid);
}
