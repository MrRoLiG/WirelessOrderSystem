package com.amaker.wlo;

import java.util.ArrayList;
import java.util.List;

import com.amaker.adapter.MyImageAdapter;
import com.amaker.provider.User;
import com.amaker.util.CheckTable;
import com.amaker.util.HttpUtil;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

public class CheckTableActivity extends Activity {

	private ListView tableListView;
	
	private List<CheckTable> list = new ArrayList<CheckTable>();
	//private ArrayList<CheckTable> checkTableList=new ArrayList<CheckTable>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("查桌界面");
		// 设置当前Activity的界面布局
		setContentView(R.layout.activity_check_table);
		// 获得当卡GridView
		tableListView=(ListView)findViewById(R.id.check_table_listview);
		// 添加一个子线程来进行获取餐桌列表
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// 访问服务器url
				String url=HttpUtil.BASE_URL+"servlet/MainServlet?requestStr=checkTableReq";
				// 查询返回结果
				String result=(HttpUtil.queryStringForPost(url)).toString();
				
				Gson gson=new Gson();
				list = gson.fromJson(result, new TypeToken<List<CheckTable>>() {  
		        }.getType());
			}
			
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// 为GridView绑定数据
		tableListView.setAdapter(new MyImageAdapter(getApplicationContext(),list));
	}
	
	Handler checkTableHandler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		    Bundle data = msg.getData();
		    String temp = data.getString("value").toString();

		    Gson gson=new Gson();
			list = gson.fromJson(temp, new TypeToken<List<CheckTable>>() {  
	        }.getType());
			
		};
	};
}


