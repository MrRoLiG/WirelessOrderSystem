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
	
	// WebView��ʾ��������ҳ��
	//private WebView webView;
	private ListView listView;
	// ���㰴ť��ȡ����ť
	private Button payBtn,payCancelBtn;
	// ��ʾ�����ŵ�TextView
	private TextView orderIdTV;
	private TextView itemOrderIdTV;
	private TextView itemOrderUserNameTV;
	private TextView itemOrderTableIdTV;
	private TextView itemOrderCustomerNumTV;
	private TextView itemOrderDateTV;
	private TextView itemOrderTotalCostTV;
	// ����id
	private int orderId=0;
	private int nTotalCost=0;
	// ����һ���ַ�������������Ž����ַ���֮�������
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
		
		// ��ȡ�����û�
		SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
		orderId = sharedPre.getInt("DealedOrderId", 0);
		
		orderIdTV.setText(orderId+"");
		
		Thread thread=new Thread(new Runnable() {

			@Override
			public void run() {
				// �����������url
				String url=HttpUtil.BASE_URL+"servlet/OrderServlet?requestStr=payReq&orderid4pay="+orderId;
				// ��ò�ѯ���
				String result = HttpUtil.queryStringForPost(url);
				
				// �����õ����ַ�����
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
		
		// Ϊ���㰴ť��ӵ���¼�
		payBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// �����,֪ͨ������������״̬��Ϊ��֧��״̬
				// ����һ�����߳������������ͨ��
				new Thread(new Runnable() {

					@Override
					public void run() {
						// �������ͨ�ŵ�url
						String postUrl=HttpUtil.BASE_URL+"servlet/OrderServlet?requestStr=updOrderStateReq&orderid4pay="+orderId;
						// ��ò�ѯ���
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
		
		// Ϊȡ�����㰴ť�������¼�
		payCancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����������
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
	        
	        // ��ʾ������
			Toast.makeText(PayActivity.this, temp, Toast.LENGTH_LONG).show();
			// ʹ���㰴ťʧЧ
			payBtn.setEnabled(false);
			// ����������
			Intent intent = new Intent(PayActivity.this,MainActivity.class);
			startActivity(intent);
		};
	};
}
