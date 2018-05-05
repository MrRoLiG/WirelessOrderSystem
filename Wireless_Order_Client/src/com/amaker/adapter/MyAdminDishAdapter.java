package com.amaker.adapter;

import java.util.List;

import com.amaker.provider.Dishes;
import com.amaker.wlo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdminDishAdapter extends BaseAdapter{
	private List<Dishes> mDishes;
	private Context mContext;
	
	public MyAdminDishAdapter(List<Dishes> list,Context context) {
		this.mDishes=list;
		this.mContext=context;
	}
	
	@Override
	public int getCount() {
		return mDishes.size();
	}

	@Override
	public Object getItem(int position) {
		return mDishes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater =LayoutInflater.from(this.mContext);
		View view=null;
		ImageView imageIV   =null;
		TextView nameTV 	=null;
		TextView typeTV 	=null;
		TextView priceTV	=null;
		TextView remarkTV	=null;
		
		if(null==convertView) {
			view=inflater.inflate(R.layout.admin_delete_dish_item, null);
		}
		else {
			view=(View)convertView;
		}
		
		imageIV=(ImageView) view.findViewById(R.id.admin_delete_dish_item_image);
		nameTV=(TextView) view.findViewById(R.id.admin_delete_dish_item_name);
		typeTV=(TextView) view.findViewById(R.id.admin_delete_dish_item_type);
		priceTV=(TextView) view.findViewById(R.id.admin_delete_dish_item_price);
		remarkTV=(TextView) view.findViewById(R.id.admin_delete_dish_item_remark);
		
		if(position<mDishes.size()) {
			imageIV.setImageResource(R.drawable.dish_item);
			nameTV.setText(mDishes.get(position).getName());
			typeTV.setText(mDishes.get(position).getType());
			priceTV.setText(mDishes.get(position).getPrice()+"");
			remarkTV.setText(mDishes.get(position).getRemark());
		}
		else {
			// 不做处理
		}
		
		return view;
	}

}
