package com.amaker.wlo;

import com.amaker.adapter.MyMarqueeTextView;
import com.amaker.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button orderBtn,combineBtn,changeBtn,checkBtn,dealBtn,logoffBtn;
	private MyMarqueeTextView mMarquee;
	
	private int nOrderId=0;
	private int nTableId=0;
	private int nCustNum=0;
	private int nChange2TableId=0;
	private int nUnion2TableId=0;
	
	// 用于拼接访问服务器url
	private String szQueryStr="";
	private String szRequestStr="";
	private String szMarqueeStr="";
	
	private boolean isOrderTwice=false;
	private String checkStr="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		//获得各个按钮组件
		orderBtn=(Button)findViewById(R.id.main_order_btn);
		combineBtn=(Button)findViewById(R.id.main_combine_table_btn);
		changeBtn=(Button)findViewById(R.id.main_change_table_btn);
		checkBtn=(Button)findViewById(R.id.main_check_table_btn);
		dealBtn=(Button)findViewById(R.id.main_deal_btn);
		logoffBtn=(Button)findViewById(R.id.main_logoff_btn);
		
		mMarquee=(MyMarqueeTextView) findViewById(R.id.activity_main_msg_marquee);

		// 获取开桌用户配置信息获取订单号
		SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
		String szUserName = sharedPre.getString("name", "");
		szMarqueeStr = "欢迎"+szUserName+"~                ";
		
		//为各个按钮添加点击事件
		orderBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(validate()) {
					// 进入点菜界面
					Intent intent = new Intent(MainActivity.this,OrderActivity.class);
					startActivity(intent);
				}
				else {
					Toast.makeText(MainActivity.this, "当前订单"+checkStr+"未完成，无法继续开桌!", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		combineBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				updateAfterLogout();
				
				// 获取开桌用户配置信息
				SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
				int nTableIdTemp = sharedPre.getInt("StartedTableId", 0);
				
				// 下单支付后不能进入该界面
				if(validate()) {
					if(0==nTableIdTemp) {
						Toast.makeText(MainActivity.this, "当前没有开桌，不能进行并桌操作!", Toast.LENGTH_LONG).show();
					}
					else {
						szRequestStr="unionTableReq";
						
						// 进行并桌操作
						unionTable();
					}
				}
				else {
					Toast.makeText(MainActivity.this, "当前订单已结算，请订单完成后再试!", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		changeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				updateAfterLogout();
				
				// 获取开桌用户配置信息
				SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
				int nTableIdTemp = sharedPre.getInt("StartedTableId", 0);
				
				// 下单支付后不能进入该界面
				
				if(validate()) {
					if(0==nTableIdTemp) {
						Toast.makeText(MainActivity.this, "当前没有开桌，不能进行换桌操作!", Toast.LENGTH_LONG).show();
					}
					else {
						szRequestStr="changeTableReq";
						
						// 进行换桌操作
						changeTable();
					}
				}
				else {
					Toast.makeText(MainActivity.this, "当前订单已结算，请订单完成后再试!", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		checkBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 进入查桌界面
				Intent intent = new Intent(MainActivity.this,CheckTableActivity.class);
				startActivity(intent);
			}
			
		});
		
		dealBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				updateAfterLogout();
				
				// 进入查看订单详情界面
				// 获取开桌用户配置信息
				SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
				int nOrderIdTemp = sharedPre.getInt("DealedOrderId", 0);
				int nTableIdTemp = sharedPre.getInt("StartedTableId", 0);
				
				// 下单支付后不能进入该界面
				
				if(validate()) {
					if(0==nOrderIdTemp||0==nTableIdTemp) {
						Toast.makeText(MainActivity.this, "当前没有开桌和下单，请先开桌下单之后再点击!", Toast.LENGTH_LONG).show();
					}
					else {
						Intent intent = new Intent(MainActivity.this,PayActivity.class);
						startActivity(intent);
					}
				}
				else {
					Toast.makeText(MainActivity.this, "当前订单已结算，请订单完成后再试!", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		logoffBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 进行注销操作，弹出退出提示框
				logout();
			}
			
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// 获取开桌用户配置信息获取订单号
		SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
		int userid = sharedPre.getInt("id", 0);
		szQueryStr="&userid="+userid;
		
		// 开启一个子线程访问服务获取当前用户状态(是否下单，下单了是否没有结算等等)
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// 访问服务器url
				String url = HttpUtil.BASE_URL+"servlet/MainServlet?requestStr=getCurrentStateReq"+szQueryStr;
				// 查询返回结果
	       		String result = HttpUtil.queryStringForPost(url);
	       		
	       		Message msg = new Message();
		        Bundle data = new Bundle();
		        data.putString("value", result);
		        msg.setData(data);
		        mainMarqueeHandler.sendMessage(msg);
			}
			
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// 换桌操作
	private void changeTable() {
		// 获得LayoutInflater实例
		LayoutInflater inflater = LayoutInflater.from(this);
		// 获得LinearLayout视图实例
		View v =inflater.inflate(R.layout.activity_change_table, null);
		// 从LinearLayout中获得EditText实例
		final EditText change2TableId = (EditText) v.findViewById(R.id.change_table_tableid_change_to);
		// 从LinearLayout中获得TextView实例
		final TextView currentOrderIdTV=(TextView) v.findViewById(R.id.change_table_orderid);
		final TextView currentTableIdTV=(TextView) v.findViewById(R.id.change_table_current_tableid);

    	// 获取开桌用户配置信息获取订单号
		SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
		nOrderId = sharedPre.getInt("DealedOrderId", 0);
		nTableId = sharedPre.getInt("StartedTableId", 0);
		nCustNum = sharedPre.getInt("StartedCusNum", 0);
		String szOrderId=nOrderId+"";
		String szTableId=nTableId+"";
		
		// 为TextView赋值
		currentOrderIdTV.setText(szOrderId);
		currentTableIdTV.setText(szTableId);
   		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(" 真的要换桌位吗？")
		       .setCancelable(false)
		       .setView(v)
		       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
			        	// 获得需要更换的桌号
			       		String szChange2TableId=change2TableId.getText().toString().trim();
			       		if(""!=szChange2TableId) {
			       			nChange2TableId =Integer.parseInt(szChange2TableId);
			       		}
			        	   
			       		// 开启一个子线程请求服务器
			       		new Thread(new Runnable() {
	
							@Override
							public void run() {
								// 设置查询参数
								String queryString = "&orderId="+nOrderId+"&tableId="+nChange2TableId+"&custNum="+nCustNum;
								// 请求服务器url
					       		String url = HttpUtil.BASE_URL+"servlet/MainServlet?requestStr="+szRequestStr+queryString; 
					       		// 查询返回结果
					       		String result = HttpUtil.queryStringForPost(url);
					       		
					       		Message msg = new Message();
						        Bundle data = new Bundle();
						        data.putString("value", result);
						        msg.setData(data);
						        changeTableHandler.sendMessage(msg);
							}
			       			
			       		}).start();
		           }
		       })
		       .setNegativeButton("取消", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	// 并桌操作
	private void unionTable() {
		// 获得LayoutInflater实例
		LayoutInflater inflater = LayoutInflater.from(this);
		// 获得LinearLayout视图实例
		View v =inflater.inflate(R.layout.activity_union_table, null);
		// 从LinearLayout中获得EditText实例
		final EditText union2TableId = (EditText) v.findViewById(R.id.union_table_tableid_to_union);
		// 从LinearLayout中获得TextView实例
		final TextView currentOrderIdTV=(TextView) v.findViewById(R.id.union_table_orderid);
		final TextView currentTableIdTV=(TextView) v.findViewById(R.id.union_table_current_tableid);
		
		// 获取开桌用户配置信息获取订单号
		SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
		nOrderId = sharedPre.getInt("DealedOrderId", 0);
		nTableId = sharedPre.getInt("StartedTableId", 0);
		nCustNum = sharedPre.getInt("StartedCusNum", 0);
		String szOrderId=nOrderId+"";
		String szTableId=nTableId+"";
		
		// 为TextView赋值
		currentOrderIdTV.setText(szOrderId);
		currentTableIdTV.setText(szTableId);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(" 确定合并桌位吗？")
		       .setCancelable(false)
		       .setView(v)
		       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
			        	// 获得需要合并的桌号
			       		String szUnion2TableId=union2TableId.getText().toString().trim();
			       		if(""!=szUnion2TableId) {
			       			nUnion2TableId =Integer.parseInt(szUnion2TableId);
			       		}
			        	   
			       		// 开启一个子线程请求服务器
			       		new Thread(new Runnable() {
	
							@Override
							public void run() {
								// 设置查询参数
								String queryString = "&orderId="+nOrderId+"&originTableId="+nTableId+"&targetTableId="+nUnion2TableId+"&custNum="+nCustNum;
								// 请求服务器url
					       		String url = HttpUtil.BASE_URL+"servlet/MainServlet?requestStr="+szRequestStr+queryString;
					       		// 查询返回结果
					       		String result = HttpUtil.queryStringForPost(url);
					       		
					       		Message msg = new Message();
						        Bundle data = new Bundle();
						        data.putString("value", result);
						        msg.setData(data);
						        UnionTableHandler.sendMessage(msg);
							}
			       			
			       		}).start();
		           }
		       })
		       .setNegativeButton("取消", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	// 注销操作
	private void logout() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("真的要退出系统吗？")
		       .setCancelable(false)
		       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	  // 确定注销用户时，将本地配置文件中的配置信息重置
		        	  SharedPreferences pres = getSharedPreferences("config", MODE_PRIVATE);
		        	  SharedPreferences.Editor editor = pres.edit();
		        	  
		        	  editor.putInt("id", 0);
		        	  editor.putString("name", "");
		        	  editor.putString("account", "");
		        	  editor.putString("password", "");
		        	  editor.putInt("DealedOrderId", 0);
		        	  editor.putInt("StartedTableId", 0);
		              editor.commit();
		        	  
		        	  Intent intent = new Intent();
		        	  intent.setClass(MainActivity.this, LoginActivity.class);
		        	  startActivity(intent);
		           }
		       })
		       .setNegativeButton("取消", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	Handler changeTableHandler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String temp = data.getString("value").toString();
	        
	        // 更新当前本地配置文件中的StartedTableId为未更换之后的桌子号
	        SharedPreferences pres = getSharedPreferences("config", MODE_PRIVATE);
	        SharedPreferences.Editor editor = pres.edit();
	        
	        editor.putInt("StartedTableId", nChange2TableId);
	        editor.commit();
	        
	        // 显示结算结果
			Toast.makeText(MainActivity.this, temp, Toast.LENGTH_LONG).show();
		};
	};
	Handler UnionTableHandler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String temp = data.getString("value").toString();
	        
	        // 更新当前本地配置文件中的StartedTableId为合并之后的桌子号
	        SharedPreferences pres = getSharedPreferences("config", MODE_PRIVATE);
	        SharedPreferences.Editor editor = pres.edit();
	        
	        editor.putInt("StartedTableId", nUnion2TableId);
	        editor.commit();
	        
	        // 显示结算结果
			Toast.makeText(MainActivity.this, temp, Toast.LENGTH_LONG).show();
		};
	};
	Handler mainMarqueeHandler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String temp = data.getString("value").toString();
	        
	        mMarquee.setText(szMarqueeStr+"    "+temp);
		};
	};
	
	// 根据用户当前状态判断用户当前是否能进入点菜界面
	// 已经下单但是没有添菜结算的可以再次进入，已经下单结算完之后需要等到服务器同步数据库之后才能继续开桌下单
	private boolean validate() {
		// 开启一个子线程访问服务器得到验证结果
		Thread thread=new Thread(new Runnable() {

			@Override
			public void run() {
				// 访问服务器url
				String url=HttpUtil.BASE_URL+"servlet/MainServlet?requestStr=orderTwiceReq&userid="+getSharedPreferences("config", MODE_PRIVATE).getInt("id", 0);
				// 获得响应结果
				String result= HttpUtil.queryStringForPost(url);
				
				checkStr=result;
				if(result.equals("0")) {
					isOrderTwice=true;
				}
				if(!result.equals("0")) {
					isOrderTwice=false;
				}
			}
			
		});
		
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return isOrderTwice;
	}
	
	// 访问服务器查询当前用户是否存在未完成的订单
	// 主要目的在于用户注销后，能正常处理转、并桌和查单的操作（因为注销会重置本地配置文件中的参数）
	private void updateAfterLogout() {
		// 开启一个子线程访问服务器
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// 访问服务器url
				String url=HttpUtil.BASE_URL+"servlet/MainServlet?requestStr=logout4ActReq&userid="+getSharedPreferences("config", MODE_PRIVATE).getInt("id", 0);
				// 获得响应结果
				String result= HttpUtil.queryStringForPost(url);
				
				if(null==result||result.equals("null")) {
					// 不做处理，说明当前没有未完成的订单，本地配置文件保持重置状态没什么影响
				}
				else {
					String[] res=result.split("\\|");
					
					// 更新当前本地配置文件
			        SharedPreferences pres = getSharedPreferences("config", MODE_PRIVATE);
			        SharedPreferences.Editor editor = pres.edit();
			        
			        // 保存参数
			        editor.putInt("DealedOrderId", Integer.parseInt(res[0]));
			        editor.putInt("StartedTableId", Integer.parseInt(res[1]));
			        editor.putInt("StartedCusNum", Integer.parseInt(res[2]));
			        // 提交
			        editor.commit();
				}
			}
			
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}