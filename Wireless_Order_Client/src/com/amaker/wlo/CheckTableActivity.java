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
		setTitle("��������");
		// ���õ�ǰActivity�Ľ��沼��
		setContentView(R.layout.activity_check_table);
		// ��õ���GridView
		tableListView=(ListView)findViewById(R.id.check_table_listview);
		// ���һ�����߳������л�ȡ�����б�
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// ���ʷ�����url
				String url=HttpUtil.BASE_URL+"servlet/MainServlet?requestStr=checkTableReq";
				// ��ѯ���ؽ��
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
		
		// ΪGridView������
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


