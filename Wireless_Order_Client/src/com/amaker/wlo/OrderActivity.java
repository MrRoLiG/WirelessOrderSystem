package com.amaker.wlo;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.amaker.adapter.MyAddedDishesLVAdapter;
import com.amaker.provider.Dishes;
import com.amaker.provider.User;
import com.amaker.util.CheckDish;
import com.amaker.util.CheckTable;
import com.amaker.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//
public class OrderActivity extends Activity {
	// ���������б���ؿؼ�
	private Spinner tableNoSpinner;
	private Button confirmDealBtn;
	private Button startATableBtn;
	private Button addDishesBtn;
	private EditText tableNoCustNumET;
	private EditText remarkET;
	
	// �˵������б���ؿؼ�
	private Spinner menuSpinner;
	private EditText dishesNumET;
	private EditText dishRemarkET;
	private ImageView dishImageIV;
	private TextView dishInfoNameTV;
	private TextView dishInfoPriceTV;
	private TextView dishInfoTypeTV;
	private TextView dishInfoRemarkTV;
	
	// ���ȷ����Ʒ��Ϣ��ؿؼ�
	private ListView addedDishesLV;
	
	private static ArrayList<Integer> tableDataList=new ArrayList<Integer>();
	private ArrayAdapter<Integer> arrTableAdapter;
	private static ArrayList<String> dishDataList=new ArrayList<String>();
	private ArrayAdapter<String> arrDishAdapter;
 	
	private ArrayList<CheckTable> checkTableList=new ArrayList<CheckTable>();
	private static ArrayList<CheckDish> checkDishList=new ArrayList<CheckDish>();
	private ArrayList<Dishes> dishesListViewList=new ArrayList<Dishes>();
	
	private UrlEncodedFormEntity startTableEntity=null;
	private UrlEncodedFormEntity confirmDealEntity=null;
	private ArrayList<UrlEncodedFormEntity> entityList=new ArrayList<UrlEncodedFormEntity>();

	private int addedDishIdTemp=0;
	private String addedDishNameTemp="";
	
	private MyAddedDishesLVAdapter addedListViewadapter;
	// �µ��Ƿ�ɹ���־��Ĭ�ϳɹ�
	private boolean bIsDealSuccess=true;
	// �µ�ʱ��ѡ�Ĳ�Ʒ������
	private int addedDishesNum=0;
	// �µ����ɶ�����,��ʼֵΪ0
	private static int nOrderId=0;
	// ���������Ӻ�
	private static int nTableId=0;
	// �����������Ϲ˿�����
	private static int nCustomerNum=0;
	// �û��Ƿ��Ѿ������Ҷ���δ��ɱ�־λ
	private boolean isOrdered=false;
	private String checkStr="";
	
	private URL getPicURL=null;
	private Bitmap picBitmap=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		
		confirmDealBtn=(Button)findViewById(R.id.order_confirmDeal_btn);
		startATableBtn=(Button)findViewById(R.id.order_startATable_btn);
		addDishesBtn=(Button)findViewById(R.id.order_addDishes_btn);
		tableNoCustNumET=(EditText)findViewById(R.id.order_tableNo_customerNumber);
		remarkET=(EditText)findViewById(R.id.order_remark);
		
		addedDishesLV=(ListView) findViewById(R.id.added_dishes_lv);
		
		// Ϊȷ���µ���ť���ӵ���¼�
		confirmDealBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ��������б�
				for(int i=0;i<dishesListViewList.size();i++) {
					// ���жϴ����Ʒ���µ�Ǯ�Ƿ�ȷ����ѡ�񣬼�checkBox�Ƿ񱻹�ѡ
					if(false!=addedListViewadapter.mChecked.get(i)) {
						// ���ȷ�������Ʒ����Ϣ
						int dishId=dishesListViewList.get(i).getId();
						int number=dishesListViewList.get(i).getNumber();
						String remark=dishesListViewList.get(i).getRemark();
						int orderId=nOrderId;
						
						addedDishesNum++;
						
						// ��װ�����������
						List<NameValuePair> params=new ArrayList<NameValuePair>();
						// ���ӵ����������ȥ
						params.add(new BasicNameValuePair("dishId",dishId+""));
						params.add(new BasicNameValuePair("number",number+""));
						params.add(new BasicNameValuePair("remark",remark));
						params.add(new BasicNameValuePair("orderId",orderId+""));
						// ��ȡ�����û�
						SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
						int nUserId = sharedPre.getInt("id", 0);
						// ���û�id����ȥ���Ա�֤�ش��ĵĶ�����
						params.add(new BasicNameValuePair("userId",nUserId+""));
						
						try {
							confirmDealEntity=new UrlEncodedFormEntity(params,HTTP.UTF_8);
							entityList.add(confirmDealEntity);
						}
						catch(UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					
				}
				// ����һ�����߳����������
				new Thread(new Runnable() {

					@Override
					public void run() {
						 for(int i=0;i<entityList.size();i++) {
							// �����������url
						 	String url= HttpUtil.BASE_URL+"servlet/OrderServlet?requestStr=confirmDealReq";
							// ���HttpPost����
							HttpPost request=HttpUtil.getHttpPost(url);
							// �����������
							request.setEntity(entityList.get(i));
						    // �����Ӧ���
							String result=HttpUtil.queryStringForPost(request);
							
							if("0"==result) {
								bIsDealSuccess=false;
							}
						 }
					}
					
				}).start();
				// ����µ��ɹ�����ʾ
				if(true==bIsDealSuccess && 0!=addedDishesNum) {
					Toast.makeText(OrderActivity.this, "�µ��ɹ���", Toast.LENGTH_LONG).show();
					finish();
				}
				else if(true==bIsDealSuccess && 0==addedDishesNum) {
					showDialog("������ѡ��һ�ֲ�Ʒȷ���µ���");
				}
				else {
					showDialog("�µ�ʧ�ܣ���ǰδ������ǰ������������");
				}
				
			}
			
		});
		
		// Ϊ���˰�ť���ӵ���¼�
		addDishesBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ���õ�˷���
				addDishes();
			}
			
		});
		
		// Ϊ������ť���ӵ���¼�
		startATableBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// �ڿ���֮ǰ�����ʷ�������ѯ��ǰ�û��Ѿ������Ҷ�����δ���
				Thread thread=new Thread(new Runnable() {

					@Override
					public void run() {
						// ���ʷ�����url
						String url=HttpUtil.BASE_URL+"servlet/MainServlet?requestStr=getUserOrderStateReq"
								+ "&userid="+getSharedPreferences("config", MODE_PRIVATE).getInt("id", 0);
						// �����Ӧ���
						String result= HttpUtil.queryStringForPost(url);
						
						if(result.equals("0")) {
							isOrdered=false;
						}
						if(!result.equals("0")) {
							isOrdered=true;
							checkStr=result;
						}
					}
					
				});
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				if(!isOrdered) {
					// ��ȡ����ʱ��
					Date date=new Date();
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					String orderDate=sdf.format(date);
					// ��ȡ�����û�
					SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
					int intUserId = sharedPre.getInt("id", 0);
					final String userId=intUserId+"";
					// ��ȡ����
					TextView tv = (TextView) tableNoSpinner.getSelectedView();
					String tableId = tv.getText().toString().trim();
					nTableId=Integer.parseInt(tableId);
					// ��ȡ�˿�����
					String customerNum=tableNoCustNumET.getText().toString();
					nCustomerNum=Integer.parseInt(customerNum);
					
					// ��������б�
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					// �����������
					params.add(new BasicNameValuePair("userid",userId));
					params.add(new BasicNameValuePair("tableid",tableId));
					params.add(new BasicNameValuePair("customernum",customerNum));
					params.add(new BasicNameValuePair("orderdate",orderDate));
					
					try {
						startTableEntity=new UrlEncodedFormEntity(params,HTTP.UTF_8);
					}
					catch(UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					
					// ����һ�����߳����������
					new Thread(new Runnable() {
	
						@Override
						public void run() {
							// ���������url
							String url = HttpUtil.BASE_URL+"servlet/OrderServlet?requestStr=startTableReq";
							// ����������HttpPost
							HttpPost request = HttpUtil.getHttpPost(url);
							// ���ò�ѯ����
							request.setEntity(startTableEntity);
							// �����Ӧ���
							String result= HttpUtil.queryStringForPost(request);
							// ���������ťʱ��¼��ǰ�����ţ��������һ�����⣺���ͻ�û�е������ʱ���޷�ȡ���������
							nOrderId=Integer.parseInt(result);
							// ���涩��Id������Id�Լ����ӹ˿����������������ļ�
							saveOrderIdConfig(nOrderId,nTableId,nCustomerNum);
							
							Message msg = new Message();
					        Bundle data = new Bundle();
					        data.putString("value", result);
					        msg.setData(data);
					        orderSTHandler.sendMessage(msg);
						}
						
					}).start();
				}
				else {
					// ��ʾ������
					Toast.makeText(OrderActivity.this, "��ǰ�û��ѿ����Ҷ�Ӧ����δ��ɣ������֮ǰ�����ٿ���!������Ϊ��"+checkStr, Toast.LENGTH_LONG).show();
				}
			}
			
			
		});
		
		tableNoSpinner=(Spinner)findViewById(R.id.order_tableNo_spinner);
		setTableAdapter();
		
		tableNoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				//	showDialog("Selected!");
				int sId=tableDataList.get(position);
				int sFlag=getTableFlagById(sId);
				confirmDealBtn.setEnabled(true);
				if(-1==sFlag) {
					showDialog("�����Ų�����!");
				}
				else{
					if(0==sFlag) {
						startATableBtn.setEnabled(true);
						tableNoCustNumET.setText("");
						remarkET.setText("��");
						setEditTextReadOnly(tableNoCustNumET,true);
						setEditTextReadOnly(remarkET,true);
					}
					if(1==sFlag) {
						startATableBtn.setEnabled(false);
						tableNoCustNumET.setText(getNumById(sId)+"");
						remarkET.setText(getRemarkById(sId));
						setEditTextReadOnly(tableNoCustNumET,false);
						setEditTextReadOnly(remarkET,false);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// ��������
			}
			
		});

	}
	
	// Ϊ���Ӻ�Spinner��Adapter
	private void setTableAdapter() {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// ���ʷ�����url
					String url=HttpUtil.BASE_URL+"servlet/OrderServlet?requestStr=checkTableReq";
					// ��ѯ���ؽ��
					String result=(HttpUtil.queryStringForPost(url)).toString();
					
					Gson gson=new Gson();
					List<CheckTable> tablesList=new ArrayList<CheckTable>();
					tablesList = gson.fromJson(result, new TypeToken<List<CheckTable>>(){}.getType());
					
					for(int i=0;i<tablesList.size();i++) {
						int id=tablesList.get(i).getId();
						tableDataList.add(id);
					}
					
					checkTableList=(ArrayList<CheckTable>) tablesList;
					
					Message msg = new Message();
			        Bundle data = new Bundle();
			        data.putString("value", "tableDataList");
			        msg.setData(data);
			        orderHandler.sendMessage(msg);
				}
				catch(Exception e) {
					e.printStackTrace();
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
	
	// ��˷���
	private void addDishes() {
		
		// ���LayoutInflaterʵ��
		LayoutInflater inflater = LayoutInflater.from(this);
		// ʵ������Ҫ�ڶԻ��������ӵ���ͼ
		final View v=inflater.inflate(R.layout.activity_add_dishes, null);
		// �����ͼ�е�Spinner����
		menuSpinner = (Spinner) v.findViewById(R.id.choose_dishes_spinner);
		// �����ͼ�е�EditText��������
		dishesNumET=(EditText) v.findViewById(R.id.choosed_dish_number);
		// �����ͼ�е�EditText��ע����
		dishRemarkET=(EditText) v.findViewById(R.id.choose_dishes_remark);
		// �����ͼ�е�TextView��������չʾ��ѡ��Ʒ��Ϣ
		dishImageIV=(ImageView) v.findViewById(R.id.dishes_info_image_iv);
		dishInfoNameTV=(TextView) v.findViewById(R.id.dishes_info_name_tv);
		dishInfoPriceTV=(TextView) v.findViewById(R.id.dishes_info_price_tv);
		dishInfoRemarkTV=(TextView) v.findViewById(R.id.dishes_info_remark_tv);
		dishInfoTypeTV=(TextView) v.findViewById(R.id.dishes_info_type_tv);
		
		// ����һ�����߳�������������õ���Ʒ��Ϣ
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// ���ʷ�����url
					String url=HttpUtil.BASE_URL+"servlet/OrderServlet?requestStr=orderDishesReq";
					// ��ѯ���ؽ��
					String result=(HttpUtil.queryStringForPost(url)).toString();
					
					Gson gson=new Gson();
					List<CheckDish> dishesList=new ArrayList<CheckDish>();
					dishesList = gson.fromJson(result, new TypeToken<List<CheckDish>>(){}.getType());
					
					for(int i=0;i<dishesList.size();i++) {
						String name=dishesList.get(i).getName();
						dishDataList.add(name);
					}
					
					checkDishList=(ArrayList<CheckDish>) dishesList;
					
					Message msg = new Message();
			        Bundle data = new Bundle();
			        data.putString("value", "dishDataList");
			        msg.setData(data);
			        addDishHandler.sendMessage(msg);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// ΪmenuSpinner�󶨵���¼�
		menuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// ��ѡ��һ���Ʒ��ʱ�򣬹��ڸĲ�Ʒ�������Ϣ����ʾ����ͼ��
				String szName=dishDataList.get(position);
				addedDishNameTemp=szName;
				int dishId=getDishIdByName(addedDishNameTemp);
				addedDishIdTemp=dishId;
				if(dishId<=0) {
					showDialog("�ò�Ʒ�����ڣ�");
				}
				else {
					//setImageByDishId(dishId);
					// ͨ����ȡ���Ķ�ӦͼƬ��Դ���ʷ���������ͼƬ��ʾ
					showDishPictrue(getURLPathById(dishId).toString());
					
					dishInfoNameTV.setText("������"+szName);
					dishInfoPriceTV.setText("�۸�"+getDishPriceById(dishId));
					dishInfoTypeTV.setText("��ϵ��"+getDishTypeById(dishId));
					dishInfoRemarkTV.setText("���ܣ�"+getDishRemarkById(dishId));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// ��������
			}
			
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("���ˣ�").setView(v)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// �����ӵĲ�Ʒ���������Ա�����µ�ʱ��������ɶ���
				String name=addedDishNameTemp.toString();
				String remark="��ע��"+dishRemarkET.getText().toString();
				
				// ����һ����־λ��ʾ��ǰ���ӵĲ�Ʒ�Ƿ��ظ�
				boolean isRepeat=false;
				
				// TODO
				// ���ж����ӵĲ�Ʒ֮ǰ�Ƿ����ӹ���������ӹ�����������������������Ʒ
				for(int i=0;i<dishesListViewList.size();i++) {
					if(addedDishIdTemp==dishesListViewList.get(i).getId()) {
						isRepeat=true;
						// ��������
						int nNum=dishesListViewList.get(i).getNumber()+Integer.parseInt(dishesNumET.getText().toString());
						dishesListViewList.get(i).setNumber(nNum);
						
						break;
					}
				}
				
				if(!isRepeat) {
					Dishes dish=new Dishes();
					dish.setId(addedDishIdTemp);
					dish.setName(name);
					dish.setPrice(getDishPriceById(addedDishIdTemp));
					dish.setNumber(Integer.parseInt(dishesNumET.getText().toString()));
					dish.setRemark(remark);
					
					dishesListViewList.add(dish);
				}
				// Ϊ���ListView��Adapter
				addedListViewadapter=new MyAddedDishesLVAdapter(dishesListViewList,getApplication());
				addedDishesLV.setAdapter(addedListViewadapter);
			}
		}).setNegativeButton("ȡ��", null);
		AlertDialog alert=builder.create();
		alert.show();
	}
	
	Handler orderHandler=new Handler() {
		@Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String temp = data.getString("value").toString();
	        
	        arrTableAdapter=new ArrayAdapter<Integer>(OrderActivity.this,android.R.layout.simple_spinner_item,tableDataList);
	        arrTableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			tableNoSpinner.setAdapter(arrTableAdapter);
	    }
	};
	Handler orderSTHandler=new Handler() {

		@Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String temp = data.getString("value").toString();

			showDialog("�����ɹ��������ţ�"+temp.toString()+"��");
			startATableBtn.setEnabled(false);
	    }
	};
	Handler addDishHandler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String temp = data.getString("value").toString();
	        
	        arrDishAdapter=new ArrayAdapter<String>(OrderActivity.this,android.R.layout.simple_spinner_item,dishDataList);
	        arrDishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        menuSpinner.setAdapter(arrDishAdapter);
		};
	};
	Handler picHandler=new Handler() {
		@Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String temp = data.getString("value").toString();
	        
	        dishImageIV.setImageBitmap(picBitmap);
	    }
	};
	
	// ͨ������id��ȡ���ӵ�״̬
	private int getTableFlagById(int id) {
		for(int i=0;i<checkTableList.size();i++) {
			if(id==checkTableList.get(i).getId()) {
				return checkTableList.get(i).getFlag();
			}
		}
		return -1;
	}
	// ͨ������id��ȡ��������
	private int getNumById(int id) {
		for(int i=0;i<checkTableList.size();i++) {
			if(id==checkTableList.get(i).getId()) {
				return checkTableList.get(i).getCustomerNum();
			}
		}
		return 0;
	}
	// ͨ�����ӺŻ�ȡ���ӵ�remark
	private String getRemarkById(int id) {
		for(int i=0;i<checkTableList.size();i++) {
			if(id==checkTableList.get(i).getId()) {
				return checkTableList.get(i).getRemark();
			}
		}
		return "��";
	}
	
	// ͨ����Ʒ���õ���Ʒid
	private int getDishIdByName(String name) {
		for(int i=0;i<checkDishList.size();i++) {
			if(name.equals(checkDishList.get(i).getName())) {
				return checkDishList.get(i).getId();
			}
		}
		return -1;
	}
	// ͨ����Ʒid��øò�Ʒ��ϵ
	private String getDishTypeById(int id) {
		for(int i=0;i<checkDishList.size();i++) {
			if(id==checkDishList.get(i).getId()) {
				return checkDishList.get(i).getType();
			}
		}
		return null;
	}
	// ͨ����Ʒid��ò�Ʒ�۸�
	private int getDishPriceById(int id) {
		for(int i=0;i<checkDishList.size();i++) {
			if(id==checkDishList.get(i).getId()) {
				return checkDishList.get(i).getPrice();
			}
		}
		return 0;
	}
	// ͨ����Ʒid��øò�Ʒ����
	private String getDishRemarkById(int id) {
		for(int i=0;i<checkDishList.size();i++) {
			if(id==checkDishList.get(i).getId()) {
				return checkDishList.get(i).getRemark();
			}
		}
		return null;
	}
	// ͨ����Ʒid��øò�Ʒ��ӦͼƬ��Դurl·��
	private String getURLPathById(int id)
	{
		for(int i=0;i<checkDishList.size();i++) {
			if(id==checkDishList.get(i).getId()) {
				return checkDishList.get(i).getPicture();
			}
		}
		return null;
	}
	
	// ͨ��URL���ʷ������õ���Ӧ��ͼƬ��Դ
	private void showDishPictrue(final String url)
	{
		//�ӷ���������ͼƬ
		Thread thread=new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					getPicURL=new URL(url);
				}
				catch(MalformedURLException e)
				{
					e.printStackTrace();
				}
				try {
					HttpURLConnection conn=(HttpURLConnection) getPicURL.openConnection();
					conn.setDoInput(true);
					conn.connect();
					InputStream is=conn.getInputStream();
					picBitmap = BitmapFactory.decodeStream(is);
					is.close();
					
					Message msg=new Message();
					Bundle data=new Bundle();
					data.putString("value", "success");
					msg.setData(data);
					picHandler.sendMessage(msg);
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
			}});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// ���������ֻ��
	public void setEditTextReadOnly(TextView view,boolean isReadOnly){ 
      if (view instanceof android.widget.EditText){    
          view.setCursorVisible(isReadOnly);      //����������еĹ�겻�ɼ�    
          view.setFocusable(isReadOnly);           //�޽���    
          view.setFocusableInTouchMode(isReadOnly);     //����ʱҲ�ò�������    
      }    
	}  
	// ��ʾ�Ի�����
	private void showDialog(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
		       .setCancelable(false)
		       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	// ���浱ǰ�����ţ����Ӻţ��˿����������������ļ�
	private void saveOrderIdConfig(int orderid,int tableid,int customernum) {
		// ��ȡSharedPreferences����
        SharedPreferences sharedPre=getSharedPreferences("config", MODE_PRIVATE);
        // ��ȡEditor����
        Editor editor=sharedPre.edit();
        // �������
        editor.putInt("DealedOrderId", orderid);
        editor.putInt("StartedTableId", tableid);
        editor.putInt("StartedCusNum", customernum);
        // �ύ
        editor.commit();
	}
	
}