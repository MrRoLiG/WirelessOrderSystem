package com.amaker.wlo;

import java.util.ArrayList;
import java.util.List;

import com.amaker.adapter.MyPayAdapter;
import com.amaker.provider.QueryOrder;
import com.amaker.provider.QueryOrderDetail;
import com.amaker.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PayActivity extends Activity{
	
	// WebView显示订单详情页面
	//private WebView webView;
	private ListView listView;
	// 结算按钮和取消按钮
	private Button payBtn,payCancelBtn;
	// 显示订单号的TextView
	private TextView orderIdTV;
	private TextView itemOrderIdTV;
	private TextView itemOrderUserNameTV;
	private TextView itemOrderTableIdTV;
	private TextView itemOrderCustomerNumTV;
	private TextView itemOrderDateTV;
	private TextView itemOrderTotalCostTV;
	// 订单id
	private int orderId=0;
	private int nTotalCost=0;
	// 定义一个字符串数组用来存放解析字符串之后的数据
	private String[] strArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		
		//webView=(WebView) findViewById(R.id.pay_webview);
		listView=(ListView) findViewById(R.id.pay_listview);
		payBtn=(Button) findViewById(R.id.pay_btn);
		payCancelBtn=(Button) findViewById(R.id.pay_cancel_btn);
		orderIdTV=(TextView) findViewById(R.id.pay_current_orderid_tv);
		
		itemOrderIdTV=(TextView) findViewById(R.id.activity_pay_orderid);
		itemOrderUserNameTV=(TextView) findViewById(R.id.activity_pay_username);
		itemOrderTableIdTV=(TextView) findViewById(R.id.activity_pay_tableid);
		itemOrderCustomerNumTV=(TextView) findViewById(R.id.activity_pay_customernum);
		itemOrderDateTV=(TextView) findViewById(R.id.activity_pay_order_date);
		itemOrderTotalCostTV=(TextView) findViewById(R.id.activity_pay_order_totalcost);
		
		// 获取开桌用户
		SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
		orderId = sharedPre.getInt("DealedOrderId", 0);
		
		orderIdTV.setText(orderId+"");
		
		Thread thread=new Thread(new Runnable() {

			@Override
			public void run() {
				// 请求服务器的url
				String url=HttpUtil.BASE_URL+"servlet/OrderServlet?requestStr=payReq&orderid4pay="+orderId;
				// 获得查询结果
				String result = HttpUtil.queryStringForPost(url);
				
				// 解析得到的字符串，
				strArray=result.split("\\|");
			}
			
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Gson gson=new Gson();
		QueryOrder queryOrder=gson.fromJson(strArray[0], QueryOrder.class);
		itemOrderIdTV.setText(orderId+"");
		itemOrderUserNameTV.setText(queryOrder.getName());
		itemOrderTableIdTV.setText(queryOrder.getTableId()+"");
		itemOrderCustomerNumTV.setText(queryOrder.getCustomerNum()+"");
		itemOrderDateTV.setText(queryOrder.getOrderDate());
		
		List<QueryOrderDetail> orderInfoList=new ArrayList<QueryOrderDetail>();
		orderInfoList = gson.fromJson(strArray[1], new TypeToken<List<QueryOrderDetail>>() {  
        }.getType());
			
		MyPayAdapter adapter=new MyPayAdapter(orderInfoList,getApplication()); 		
		listView.setAdapter(adapter);

		for(int i=0;i<orderInfoList.size();i++) {
			QueryOrderDetail ob=orderInfoList.get(i);
			
			nTotalCost += ob.getPrice()*ob.getDishNum();
		}
		itemOrderTotalCostTV.setText(nTotalCost+"");
		
		// 为结算按钮添加点击事件
		payBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击后,通知服务器将订单状态置为已支付状态
				// 开启一个子线程与服务器进行通信
				new Thread(new Runnable() {

					@Override
					public void run() {
						// 与服务器通信的url
						String postUrl=HttpUtil.BASE_URL+"servlet/OrderServlet?requestStr=updOrderStateReq&orderid4pay="+orderId;
						// 获得查询结果
						String result = HttpUtil.queryStringForPost(postUrl);
						
						Message msg = new Message();
				        Bundle data = new Bundle();
				        data.putString("value", result);
				        msg.setData(data);
				        payHandler.sendMessage(msg);
					}
					
				}).start();
			}
			
		});
		
		// 为取消结算按钮天机点击事件
		payCancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 进入主界面
				Intent intent = new Intent(PayActivity.this,MainActivity.class);
				startActivity(intent);
			}
			
		});
	}

	Handler payHandler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String temp = data.getString("value").toString();
	        
	        // 显示结算结果
			Toast.makeText(PayActivity.this, temp, Toast.LENGTH_LONG).show();
			// 使结算按钮失效
			payBtn.setEnabled(false);
			// 返回主界面
			Intent intent = new Intent(PayActivity.this,MainActivity.class);
			startActivity(intent);
		};
	};
}
