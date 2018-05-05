package com.amaker.dao;

import java.util.List;

public interface TableDao {
	// ������Ӻ�id��List
	public List getTableList();
	// ͨ��orderid��tableid��custnum�����ݿ���и�����������
	public void changeTable(int orderid,int tableid,int custnum);
	// ͨ������id��ѯ�������Ƿ�����
	public boolean isTableStarted(int tableid);
	// ͨ������id(��Ҫ�ϲ������Ӻ�)
	// Ŀǰ��ʱ֧�ֺϲ���������
	public void unionTable(int originTableId,int targetTableId,int originTableCustNum,int orderId);
	// �����������������ӵ������Ϣ���ã����ڹ���Ա��
	public String clearTable();
	// ����tableid�����Ӧ��������Ϣ
	public String clearTableById(int tableid);
}
