package com.amaker.adapter;

import java.util.List;

import com.amaker.util.CheckTable;
import com.amaker.wlo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyImageAdapter extends BaseAdapter{

	private Context context;
	private List<CheckTable> clist;
	
	public MyImageAdapter(Context c,List<CheckTable> list) {
		this.context=c;
		this.clist=list;
	}
	
	@Override
	public int getCount() {
		return clist.size();
	}

	@Override
	public Object getItem(int position) {
		return clist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// 声明图片视图
		LayoutInflater inflater =LayoutInflater.from(context);
		View view=null;
		ImageView tableIV =null;
		ImageView tableStateIV=null;
		TextView tableIdTV=null;
		TextView tableCustomerNumTV=null;
		TextView tableRemarkTV=null;
		
		if(null==convertView) {
			// 实例化图片视图
			view=inflater.inflate(R.layout.activity_check_table_item, null);
		}
		else {
			view=(View)convertView;
		}
		
		// 获得ImageView对象
		tableIV=(ImageView) view.findViewById(R.id.check_table_item_ImageView);
		tableStateIV=(ImageView) view.findViewById(R.id.check_table_item_stateImageView);
		// 获得TextView对象
		tableIdTV=(TextView) view.findViewById(R.id.check_table_item_tableid);
		tableCustomerNumTV=(TextView) view.findViewById(R.id.check_table_item_table_customer_number);
		tableRemarkTV=(TextView) view.findViewById(R.id.check_table_item_table_remark);
	
		if(position<clist.size()) {
			CheckTable checkTable=(CheckTable) clist.get(position);
			// 如果有人，显示有人图片
			tableIV.setImageResource(R.drawable.table);
			if(1==checkTable.getFlag()) {
				tableStateIV.setImageResource(R.drawable.flag_1);
			}
			else {
				tableStateIV.setImageResource(R.drawable.flag_0);
			}
			// 设置桌位信息
			tableIdTV.setText("桌子号:"+checkTable.getId());
			tableCustomerNumTV.setText("桌位人数:"+checkTable.getCustomerNum());
			tableRemarkTV.setText("备注:"+checkTable.getRemark());
		}
		else {
			// 不做处理
		}
		return view;
	}
	
}
