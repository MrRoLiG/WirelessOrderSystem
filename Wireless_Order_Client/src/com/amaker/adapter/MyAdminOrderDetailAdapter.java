package com.amaker.adapter;

import java.util.ArrayList;
import java.util.List;

import com.amaker.provider.DishUpOrders;
import com.amaker.wlo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdminOrderDetailAdapter extends BaseAdapter{
	private List<DishUpOrders> mDishUpOrders;
	private Context mContext;
	
	public MyAdminOrderDetailAdapter(List<DishUpOrders> list,Context context) {
		this.mDishUpOrders=list;
		this.mContext=context;
	}
	
	@Override
	public int getCount() {
		return mDishUpOrders.size();
	}

	@Override
	public Object getItem(int position) {
		return mDishUpOrders.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater =LayoutInflater.from(this.mContext);
		View view=null;
		TextView orderIdTV=null;
		TextView orderInfoTV=null;
		
		if(null==convertView) {
			view=inflater.inflate(R.layout.admin_dish_up_item, null);
		}
		else {
			view=(View)convertView;
		}
		
		orderIdTV=(TextView) view.findViewById(R.id.admin_dish_up_item_orderid);
		orderInfoTV=(TextView) view.findViewById(R.id.admin_dish_up_item_info);
		
		
		if(position<mDishUpOrders.size()) {
			orderIdTV.setText(mDishUpOrders.get(position).getOrderId()+"");
			orderInfoTV.setText(mDishUpOrders.get(position).getName()+"  数量:"+mDishUpOrders.get(position).getDishNum()+"  备注:"+mDishUpOrders.get(position).getRemark());
		}
		else {
			// 不做处理
		}
		
		return view;
	}

}
