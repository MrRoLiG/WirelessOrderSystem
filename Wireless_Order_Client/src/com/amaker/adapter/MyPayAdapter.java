package com.amaker.adapter;

import java.util.List;

import com.amaker.provider.QueryOrderDetail;
import com.amaker.util.CheckTable;
import com.amaker.wlo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyPayAdapter extends BaseAdapter{

	private Context context;
	private List<QueryOrderDetail> mList;
	
	public MyPayAdapter(List<QueryOrderDetail> list,Context context) {
		this.context=context;
		this.mList=list;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ����ͼƬ��ͼ
		LayoutInflater inflater =LayoutInflater.from(context);
		View view=null;
		
		ImageView dishImageIV=null;
		TextView dishIdTV=null;
		TextView dishNameTV=null;
		TextView dishNumTV=null;
		TextView dishPriceTV=null;
		TextView dishRemarkTV=null;
		
		if(null==convertView) {
			// ʵ����ͼƬ��ͼ
			view=inflater.inflate(R.layout.activity_pay_item, null);
		}
		else {
			view=(View)convertView;
		}
		
		// ����Ӳ����еĸ������
		dishImageIV=(ImageView) view.findViewById(R.id.pay_item_dishimage);
		dishIdTV=(TextView) view.findViewById(R.id.pay_item_dishid);
		dishNameTV=(TextView) view.findViewById(R.id.pay_item_dishname);
		dishNumTV=(TextView) view.findViewById(R.id.pay_item_dishnum);
		dishPriceTV=(TextView) view.findViewById(R.id.pay_item_dishprice);
		dishRemarkTV=(TextView) view.findViewById(R.id.pay_item_dishremark);
		
		if(position<mList.size()) {
			QueryOrderDetail queryOrderDetail=(QueryOrderDetail) mList.get(position);
			
			dishImageIV.setImageResource(R.drawable.dish);
			dishIdTV.setText(queryOrderDetail.getDishId()+"");
			dishNameTV.setText(queryOrderDetail.getName());
			dishNumTV.setText(queryOrderDetail.getDishNum()+"");
			dishPriceTV.setText(queryOrderDetail.getPrice()*queryOrderDetail.getDishNum()+"");
			dishRemarkTV.setText(queryOrderDetail.getRemark());
		}
		else {
			// ��������
		}
		
		return view;
	}

}
