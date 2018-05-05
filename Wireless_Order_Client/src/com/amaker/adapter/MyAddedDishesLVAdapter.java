package com.amaker.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.amaker.provider.Dishes;
import com.amaker.wlo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAddedDishesLVAdapter extends BaseAdapter{

	public List<Boolean> mChecked;
	private List<Dishes> mDishes;
	private Context mContext;
	private HashMap<Integer,View> map = new HashMap<Integer,View>();  
	
	public MyAddedDishesLVAdapter(List<Dishes> list,Context context) {
		mDishes=new ArrayList<Dishes>();
		mDishes=list;
		mContext=context;
		
		mChecked=new ArrayList<Boolean>();
		for(int i=0;i<list.size();i++) {
			mChecked.add(true);
		}
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
		View view;
		ViewHolder holder=null;
		
		if(null==convertView) {
			LayoutInflater mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
            view = mInflater.inflate(R.layout.added_dishes_listview_item, null);  
            
            holder = new ViewHolder();  
            holder.selected = (CheckBox)view.findViewById(R.id.dishes_flag_cb);  
            holder.id=(TextView)view.findViewById(R.id.dishes_id_lv);
            holder.name = (TextView)view.findViewById(R.id.dishes_name_lv);
            holder.price=(TextView)view.findViewById(R.id.dishes_price_lv);
            holder.number=(TextView)view.findViewById(R.id.dishes_number_lv);
            holder.remark=(TextView)view.findViewById(R.id.dishes_remark_lv); 
            holder.image=(ImageView) view.findViewById(R.id.added_dishes_listview_item_ImageView);
            
            final int tempPosition=position;
            map.put(position, view);
            
            holder.selected.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CheckBox cb=(CheckBox)v;
					mChecked.set(tempPosition, cb.isChecked());
				}
            	
            });
            view.setTag(holder);
		}
		else {
			view =map.get(position);
			holder=(ViewHolder)view.getTag();
		}
		
		holder.selected.setChecked(mChecked.get(position));
		holder.id.setText(mDishes.get(position).getId()+"");
		holder.name.setText(mDishes.get(position).getName());
		holder.price.setText("价格："+mDishes.get(position).getPrice()+"");
		holder.number.setText("数量："+mDishes.get(position).getNumber()+"");
		holder.remark.setText(mDishes.get(position).getRemark());
		holder.image.setImageResource(R.drawable.dish);
		
		return view;
	}

}

class ViewHolder{  
    CheckBox selected; 
    ImageView image;
    TextView id;
    TextView name;  
    TextView price;
    TextView number;
    TextView remark;  
}
