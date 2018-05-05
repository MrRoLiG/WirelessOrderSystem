package com.amaker.adapter;

import java.util.List;

import com.amaker.provider.User;
import com.amaker.wlo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdminUserAdapter extends BaseAdapter{
	private List<User> mUsers;
	private Context mContext; 
	
	public MyAdminUserAdapter(List<User> list,Context context) {
		this.mUsers=list;
		this.mContext=context;
	}
	
	@Override
	public int getCount() {
		return mUsers.size();
	}

	@Override
	public Object getItem(int position) {
		return mUsers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater =LayoutInflater.from(this.mContext);
		View view=null;
		ImageView imageIV=null;
		TextView nameTV =null;
		TextView permissionTV=null;
		
		if(null==convertView) {
			view=inflater.inflate(R.layout.admin_delete_user_item, null);
		}
		else {
			view=(View)convertView;
		}
		
		imageIV=(ImageView) view.findViewById(R.id.admin_delete_user_item_image);
		nameTV=(TextView) view.findViewById(R.id.admin_delete_user_item_name);
		permissionTV=(TextView) view.findViewById(R.id.admin_delete_user_item_permission);
		
		if(position<mUsers.size()) {
			imageIV.setImageResource(R.drawable.user);
			nameTV.setText("用户名:"+mUsers.get(position).getName());
			permissionTV.setText("身份权限:"+(mUsers.get(position).getPermission()==1?"管理员":"普通用户"));
		}
		else {
			// 不做处理
		}
		
		return view;
	}
}

