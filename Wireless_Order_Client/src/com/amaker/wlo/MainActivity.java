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
	
	// ����ƴ�ӷ��ʷ�����url
	private String szQueryStr="";
	private String szRequestStr="";
	private String szMarqueeStr="";
	
	private boolean isOrderTwice=false;
	private String checkStr="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		//��ø�����ť���
		orderBtn=(Button)findViewById(R.id.main_order_btn);
		combineBtn=(Button)findViewById(R.id.main_combine_table_btn);
		changeBtn=(Button)findViewById(R.id.main_change_table_btn);
		checkBtn=(Button)findViewById(R.id.main_check_table_btn);
		dealBtn=(Button)findViewById(R.id.main_deal_btn);
		logoffBtn=(Button)findViewById(R.id.main_logoff_btn);
		
		mMarquee=(MyMarqueeTextView) findViewById(R.id.activity_main_msg_marquee);

		// ��ȡ�����û�������Ϣ��ȡ������
		SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
		String szUserName = sharedPre.getString("name", "");
		szMarqueeStr = "��ӭ"+szUserName+"~                ";
		
		//Ϊ������ť��ӵ���¼�
		orderBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(validate()) {
					// �����˽���
					Intent intent = new Intent(MainActivity.this,OrderActivity.class);
					startActivity(intent);
				}
				else {
					Toast.makeText(MainActivity.this, "��ǰ����"+checkStr+"δ��ɣ��޷���������!", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		combineBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				updateAfterLogout();
				
				// ��ȡ�����û�������Ϣ
				SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
				int nTableIdTemp = sharedPre.getInt("StartedTableId", 0);
				
				// �µ�֧�����ܽ���ý���
				if(validate()) {
					if(0==nTableIdTemp) {
						Toast.makeText(MainActivity.this, "��ǰû�п��������ܽ��в�������!", Toast.LENGTH_LONG).show();
					}
					else {
						szRequestStr="unionTableReq";
						
						// ���в�������
						unionTable();
					}
				}
				else {
					Toast.makeText(MainActivity.this, "��ǰ�����ѽ��㣬�붩����ɺ�����!", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		changeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				updateAfterLogout();
				
				// ��ȡ�����û�������Ϣ
				SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
				int nTableIdTemp = sharedPre.getInt("StartedTableId", 0);
				
				// �µ�֧�����ܽ���ý���
				
				if(validate()) {
					if(0==nTableIdTemp) {
						Toast.makeText(MainActivity.this, "��ǰû�п��������ܽ��л�������!", Toast.LENGTH_LONG).show();
					}
					else {
						szRequestStr="changeTableReq";
						
						// ���л�������
						changeTable();
					}
				}
				else {
					Toast.makeText(MainActivity.this, "��ǰ�����ѽ��㣬�붩����ɺ�����!", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		checkBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// �����������
				Intent intent = new Intent(MainActivity.this,CheckTableActivity.class);
				startActivity(intent);
			}
			
		});
		
		dealBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				updateAfterLogout();
				
				// ����鿴�����������
				// ��ȡ�����û�������Ϣ
				SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
				int nOrderIdTemp = sharedPre.getInt("DealedOrderId", 0);
				int nTableIdTemp = sharedPre.getInt("StartedTableId", 0);
				
				// �µ�֧�����ܽ���ý���
				
				if(validate()) {
					if(0==nOrderIdTemp||0==nTableIdTemp) {
						Toast.makeText(MainActivity.this, "��ǰû�п������µ������ȿ����µ�֮���ٵ��!", Toast.LENGTH_LONG).show();
					}
					else {
						Intent intent = new Intent(MainActivity.this,PayActivity.class);
						startActivity(intent);
					}
				}
				else {
					Toast.makeText(MainActivity.this, "��ǰ�����ѽ��㣬�붩����ɺ�����!", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		logoffBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����ע�������������˳���ʾ��
				logout();
			}
			
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// ��ȡ�����û�������Ϣ��ȡ������
		SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
		int userid = sharedPre.getInt("id", 0);
		szQueryStr="&userid="+userid;
		
		// ����һ�����̷߳��ʷ����ȡ��ǰ�û�״̬(�Ƿ��µ����µ����Ƿ�û�н���ȵ�)
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// ���ʷ�����url
				String url = HttpUtil.BASE_URL+"servlet/MainServlet?requestStr=getCurrentStateReq"+szQueryStr;
				// ��ѯ���ؽ��
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
	
	// ��������
	private void changeTable() {
		// ���LayoutInflaterʵ��
		LayoutInflater inflater = LayoutInflater.from(this);
		// ���LinearLayout��ͼʵ��
		View v =inflater.inflate(R.layout.activity_change_table, null);
		// ��LinearLayout�л��EditTextʵ��
		final EditText change2TableId = (EditText) v.findViewById(R.id.change_table_tableid_change_to);
		// ��LinearLayout�л��TextViewʵ��
		final TextView currentOrderIdTV=(TextView) v.findViewById(R.id.change_table_orderid);
		final TextView currentTableIdTV=(TextView) v.findViewById(R.id.change_table_current_tableid);

    	// ��ȡ�����û�������Ϣ��ȡ������
		SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
		nOrderId = sharedPre.getInt("DealedOrderId", 0);
		nTableId = sharedPre.getInt("StartedTableId", 0);
		nCustNum = sharedPre.getInt("StartedCusNum", 0);
		String szOrderId=nOrderId+"";
		String szTableId=nTableId+"";
		
		// ΪTextView��ֵ
		currentOrderIdTV.setText(szOrderId);
		currentTableIdTV.setText(szTableId);
   		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(" ���Ҫ����λ��")
		       .setCancelable(false)
		       .setView(v)
		       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
			        	// �����Ҫ����������
			       		String szChange2TableId=change2TableId.getText().toString().trim();
			       		if(""!=szChange2TableId) {
			       			nChange2TableId =Integer.parseInt(szChange2TableId);
			       		}
			        	   
			       		// ����һ�����߳����������
			       		new Thread(new Runnable() {
	
							@Override
							public void run() {
								// ���ò�ѯ����
								String queryString = "&orderId="+nOrderId+"&tableId="+nChange2TableId+"&custNum="+nCustNum;
								// ���������url
					       		String url = HttpUtil.BASE_URL+"servlet/MainServlet?requestStr="+szRequestStr+queryString; 
					       		// ��ѯ���ؽ��
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
		       .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	// ��������
	private void unionTable() {
		// ���LayoutInflaterʵ��
		LayoutInflater inflater = LayoutInflater.from(this);
		// ���LinearLayout��ͼʵ��
		View v =inflater.inflate(R.layout.activity_union_table, null);
		// ��LinearLayout�л��EditTextʵ��
		final EditText union2TableId = (EditText) v.findViewById(R.id.union_table_tableid_to_union);
		// ��LinearLayout�л��TextViewʵ��
		final TextView currentOrderIdTV=(TextView) v.findViewById(R.id.union_table_orderid);
		final TextView currentTableIdTV=(TextView) v.findViewById(R.id.union_table_current_tableid);
		
		// ��ȡ�����û�������Ϣ��ȡ������
		SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
		nOrderId = sharedPre.getInt("DealedOrderId", 0);
		nTableId = sharedPre.getInt("StartedTableId", 0);
		nCustNum = sharedPre.getInt("StartedCusNum", 0);
		String szOrderId=nOrderId+"";
		String szTableId=nTableId+"";
		
		// ΪTextView��ֵ
		currentOrderIdTV.setText(szOrderId);
		currentTableIdTV.setText(szTableId);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(" ȷ���ϲ���λ��")
		       .setCancelable(false)
		       .setView(v)
		       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
			        	// �����Ҫ�ϲ�������
			       		String szUnion2TableId=union2TableId.getText().toString().trim();
			       		if(""!=szUnion2TableId) {
			       			nUnion2TableId =Integer.parseInt(szUnion2TableId);
			       		}
			        	   
			       		// ����һ�����߳����������
			       		new Thread(new Runnable() {
	
							@Override
							public void run() {
								// ���ò�ѯ����
								String queryString = "&orderId="+nOrderId+"&originTableId="+nTableId+"&targetTableId="+nUnion2TableId+"&custNum="+nCustNum;
								// ���������url
					       		String url = HttpUtil.BASE_URL+"servlet/MainServlet?requestStr="+szRequestStr+queryString;
					       		// ��ѯ���ؽ��
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
		       .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	// ע������
	private void logout() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("���Ҫ�˳�ϵͳ��")
		       .setCancelable(false)
		       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	  // ȷ��ע���û�ʱ�������������ļ��е�������Ϣ����
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
		       .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
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
	        
	        // ���µ�ǰ���������ļ��е�StartedTableIdΪδ����֮������Ӻ�
	        SharedPreferences pres = getSharedPreferences("config", MODE_PRIVATE);
	        SharedPreferences.Editor editor = pres.edit();
	        
	        editor.putInt("StartedTableId", nChange2TableId);
	        editor.commit();
	        
	        // ��ʾ������
			Toast.makeText(MainActivity.this, temp, Toast.LENGTH_LONG).show();
		};
	};
	Handler UnionTableHandler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String temp = data.getString("value").toString();
	        
	        // ���µ�ǰ���������ļ��е�StartedTableIdΪ�ϲ�֮������Ӻ�
	        SharedPreferences pres = getSharedPreferences("config", MODE_PRIVATE);
	        SharedPreferences.Editor editor = pres.edit();
	        
	        editor.putInt("StartedTableId", nUnion2TableId);
	        editor.commit();
	        
	        // ��ʾ������
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
	
	// �����û���ǰ״̬�ж��û���ǰ�Ƿ��ܽ����˽���
	// �Ѿ��µ�����û����˽���Ŀ����ٴν��룬�Ѿ��µ�������֮����Ҫ�ȵ�������ͬ�����ݿ�֮����ܼ��������µ�
	private boolean validate() {
		// ����һ�����̷߳��ʷ������õ���֤���
		Thread thread=new Thread(new Runnable() {

			@Override
			public void run() {
				// ���ʷ�����url
				String url=HttpUtil.BASE_URL+"servlet/MainServlet?requestStr=orderTwiceReq&userid="+getSharedPreferences("config", MODE_PRIVATE).getInt("id", 0);
				// �����Ӧ���
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
	
	// ���ʷ�������ѯ��ǰ�û��Ƿ����δ��ɵĶ���
	// ��ҪĿ�������û�ע��������������ת�������Ͳ鵥�Ĳ�������Ϊע�������ñ��������ļ��еĲ�����
	private void updateAfterLogout() {
		// ����һ�����̷߳��ʷ�����
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// ���ʷ�����url
				String url=HttpUtil.BASE_URL+"servlet/MainServlet?requestStr=logout4ActReq&userid="+getSharedPreferences("config", MODE_PRIVATE).getInt("id", 0);
				// �����Ӧ���
				String result= HttpUtil.queryStringForPost(url);
				
				if(null==result||result.equals("null")) {
					// ��������˵����ǰû��δ��ɵĶ��������������ļ���������״̬ûʲôӰ��
				}
				else {
					String[] res=result.split("\\|");
					
					// ���µ�ǰ���������ļ�
			        SharedPreferences pres = getSharedPreferences("config", MODE_PRIVATE);
			        SharedPreferences.Editor editor = pres.edit();
			        
			        // �������
			        editor.putInt("DealedOrderId", Integer.parseInt(res[0]));
			        editor.putInt("StartedTableId", Integer.parseInt(res[1]));
			        editor.putInt("StartedCusNum", Integer.parseInt(res[2]));
			        // �ύ
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