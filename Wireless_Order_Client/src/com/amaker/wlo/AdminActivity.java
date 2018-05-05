package com.amaker.wlo;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.amaker.adapter.MyAdminDishAdapter;
import com.amaker.adapter.MyAdminOrderDetailAdapter;
import com.amaker.adapter.MyAdminUserAdapter;
import com.amaker.adapter.MyMarqueeTextView;
import com.amaker.provider.DishUpOrders;
import com.amaker.provider.Dishes;
import com.amaker.provider.User;
import com.amaker.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends Activity{
	
	private Button userAddBtn,userDeleteBtn,dishAddBtn,dishDeleteBtn,dishUpdateBtn,clearTableBtn,dishUpBtn,idDoneBtn,listenOrderBtn,logoutBtn;
	private MyMarqueeTextView orderMarqueeET;
	private MyMarqueeTextView orderDetailMarqueeET;
	
	// ����һ��ȫ�ֱ������洢�������ش����ϲ˶�������
	private List<DishUpOrders> dishUpOrdersList;
	
	// �����������ַ���������˸��ݿͻ��˷��͵������ַ�����Ӧ��Ӧ�ͻ���
	private static String szAdminRequestStr="";
	private static String szQueryStr="";

	// ���ڹ����������û�
	private static String szGenderSpinnerStr="";
	private static String szPermissionSpinnerStr="";
	
	// ��ѯ�������û���Ϣjson�ַ���
	private String szCheckUsersStr="";
	private String szCheckDishesStr="";
	private String szCheckOrdersStr="";
	
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = getApplicationContext();
		
		setContentView(R.layout.acticity_admin);
		// ��ø�����ť
		userAddBtn=(Button) findViewById(R.id.admin_add_user_btn);
		userDeleteBtn=(Button) findViewById(R.id.admin_delete_user_btn);
		dishAddBtn=(Button) findViewById(R.id.admin_add_dish_btn);
		dishDeleteBtn=(Button) findViewById(R.id.admin_delete_dish_btn);
		dishUpdateBtn=(Button) findViewById(R.id.admin_update_dish_btn);
		clearTableBtn=(Button) findViewById(R.id.admin_clear_table_btn);
		idDoneBtn=(Button) findViewById(R.id.admin_order_is_done_btn);
		listenOrderBtn=(Button) findViewById(R.id.admin_listen_order_btn);
		dishUpBtn=(Button) findViewById(R.id.admin_dish_up_btn);
		logoutBtn=(Button) findViewById(R.id.admin_logout_btn);
		
		orderMarqueeET=(MyMarqueeTextView) findViewById(R.id.admin_listen_order_marquee);
		orderDetailMarqueeET=(MyMarqueeTextView) findViewById(R.id.admin_listen_order_detail_marquee);
		
		// Ϊ������ť���ӵ���¼�
		userAddBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Ϊ�����ַ�����ֵ
				szAdminRequestStr="userAddReq";
				getAddUserQuery();
			}
			
		});
		userDeleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Ϊ�����ַ�����ֵ
				szAdminRequestStr="userDeleteReq";
				getAllUsers();
				getDeleteUserQuery();
			}
			
		});
		dishAddBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Ϊ�����ַ�����ֵ
				//szAdminRequestStr="dishAddReq";
				//getAddDishQuery();
				Intent intent = new Intent(AdminActivity.this,AddDishActivity.class);
				startActivity(intent);
			}
			
		});
		dishDeleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Ϊ�����ַ�����ֵ
				szAdminRequestStr="dishDeleteReq";
				getAllDishes();
				getDeleteDishQuery();
			}
			
		});
		dishUpdateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Ϊ�����ַ�����ֵ
				szAdminRequestStr="dishUpdateReq";
				getAllDishes();
				getUpdateDishesQuery();
			}
			
		});
		dishUpBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Ϊ�����ַ�����ֵ
				szAdminRequestStr="dishUpReq";
				getAllPaidOrder();
				getDishUpQuery();
			}
			
		});
		idDoneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Ϊ�����ַ�����ֵ
				szAdminRequestStr="isDoneReq";
				getIsDoneQuery();
			}
			
		});
		clearTableBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Ϊ�����ַ�����ֵ
				szAdminRequestStr="clearTableReq";
				getClearTableQuery();
			}
			
		});
		// �������֮�󣬷��ʷ�������ѯ���ݿ⿴��ǰ�Ƿ��ж�������µ�������ж����µ����������ش�������Ϣ���ͻ�������������Ϣ��ʾ��ʾ����
		listenOrderBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Ϊ�����ַ�����ֵ
				szAdminRequestStr="listenOrderReq";
				getListenOrderQuery();
			}
			
		});
		logoutBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����ע������
				logout();
			}
			
		});
	}

	
	private void getAddUserQuery() {
		final String[] genders= {"��","Ů"};
		final String[] gendersVal= {"1","0"}; 
		final String[] permissions= {"��ͨ�û�","����Ա"};
		final int[] permissionVal= {2,1};
		
		// ���LayoutInflaterʵ��
		LayoutInflater inflater = LayoutInflater.from(this);
		// ���LinearLayout��ͼʵ��
		View v =inflater.inflate(R.layout.admin_add_user, null);
		// ��ø�������ʵ��
		final EditText nameET=(EditText) v.findViewById(R.id.admin_add_user_name);
		final EditText accountET=(EditText) v.findViewById(R.id.admin_add_user_account);
		final EditText passwordET=(EditText) v.findViewById(R.id.admin_add_user_password);
		final EditText remarkET=(EditText) v.findViewById(R.id.admin_add_user_remark);
		final Spinner genderSP=(Spinner) v.findViewById(R.id.admin_add_user_gender);
		final Spinner permissionSP=(Spinner) v.findViewById(R.id.admin_add_user_permission);

		ArrayAdapter<String> genderAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,genders);
		genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		genderSP.setAdapter(genderAdapter);
		genderSP.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				szGenderSpinnerStr = ("&gender=" + gendersVal[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				szGenderSpinnerStr = ("&gender=1");
			}
			
		});
		
		ArrayAdapter<String> permissionAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,permissions);
		permissionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		permissionSP.setAdapter(permissionAdapter);
		permissionSP.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				szPermissionSpinnerStr = ("&permission=" + permissionVal[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				szPermissionSpinnerStr = ("&permission=2 ");
			}
			
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(" �����û�  ")
		       .setCancelable(false)
		       .setView(v)
		       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {

		       		String name=nameET.getText().toString().trim();
		       		String account=accountET.getText().toString().trim();
		       		String password=passwordET.getText().toString().trim();
		       		String remark=remarkET.getText().toString().trim();
		       		
		       		if(name.equals("")) {
		       			Toast.makeText(AdminActivity.this, "�û�������Ϊ��!", Toast.LENGTH_LONG).show();
		       		}
		       		else if(account.equals("")) {
		       			Toast.makeText(AdminActivity.this, "�˺Ų���Ϊ��!", Toast.LENGTH_LONG).show();
		       		}
		       		else if(password.equals("")) {
		       			Toast.makeText(AdminActivity.this, "���벻��Ϊ��!", Toast.LENGTH_LONG).show();
		       		}
		       		else {
			       		szQueryStr += ("&name="+name+"&account="+account+"&password="+password+"&remark="+remark);
			       		
			       		// ����һ�����߳̽��з���������
			       		new Thread(new Runnable() {
	
			       			@Override
			       			public void run() {
			       				// ���ʷ�����url
			       				String url=HttpUtil.BASE_URL+"servlet/AdminServlet?requestStr="+szAdminRequestStr+szQueryStr+szGenderSpinnerStr+szPermissionSpinnerStr;
			       				// ��÷��ʽ��
			       				String result=HttpUtil.queryStringForPost(url);
			       				
			       				Message msg = new Message();
			       		        Bundle data = new Bundle();
			       		        data.putString("value", result);
			       		        msg.setData(data);
			       		        adminHandler.sendMessage(msg);
			       			}
			       			
			       		}).start();
		       		 }
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
	
	private void getAllUsers() {
		// ����һ�����̷߳��ʷ�������õ�ǰ�����û�
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// ���ʷ�����url
				String url=HttpUtil.BASE_URL+"servlet/AdminServlet?requestStr=allUserReq";
   				// ��÷��ʽ��
   				String result=HttpUtil.queryStringForPost(url);
   				
   				szCheckUsersStr=result;
			}
			
		});
		thread.start();
		try {
			thread.join();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	private void getDeleteUserQuery() {
		// ���LayoutInflaterʵ��
		LayoutInflater inflater = LayoutInflater.from(this);
		// ���LinearLayout��ͼʵ��
		View v =inflater.inflate(R.layout.admin_delete_user, null);
		// ��ø�������ʵ��
		final ListView usersLV=(ListView) v.findViewById(R.id.admin_delete_user_show_allUsers);
		final EditText deleteUserNameET=(EditText) v.findViewById(R.id.admin_delete_user_name);
		
		Gson gson=new Gson();
		List<User> userList=new ArrayList<User>();
		userList = gson.fromJson(szCheckUsersStr, new TypeToken<List<User>>() {  
        }.getType());
		
		MyAdminUserAdapter adpater=new MyAdminUserAdapter(userList,mContext);
		usersLV.setAdapter(adpater);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("ɾ���û�  ")
		       .setCancelable(false)
		       .setView(v)
		       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
		        	   String szUserName=deleteUserNameET.getText().toString().trim();
		        	   
		        	   if(szUserName.equals("")) {
		        		   Toast.makeText(AdminActivity.this, "ɾ���û�������Ϊ��!", Toast.LENGTH_LONG).show();
		        	   }
		        	   else {
			        	   szQueryStr="&name="+szUserName;
			        	   
			        	   new Thread(new Runnable() {
	
							@Override
							public void run() {
								// ���ʷ�����url
			       				String url=HttpUtil.BASE_URL+"servlet/AdminServlet?requestStr="+szAdminRequestStr+szQueryStr;
			       				// ��÷��ʽ��
			       				String result=HttpUtil.queryStringForPost(url);
			       				
			       				Message msg = new Message();
			       		        Bundle data = new Bundle();
			       		        data.putString("value", result);
			       		        msg.setData(data);
			       		        adminHandler.sendMessage(msg);
							}
			        		   
			        	   }).start();
		        	   }
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
	
	/*
	private void getAddDishQuery() {
		// ���LayoutInflaterʵ��
		LayoutInflater inflater = LayoutInflater.from(this);
		// ���LinearLayout��ͼʵ��
		View v =inflater.inflate(R.layout.admin_add_dish, null); 
		// ��ø�������ʵ��
		final EditText dishNameET=(EditText) v.findViewById(R.id.admin_add_dish_name);
		final EditText dishTypeET=(EditText) v.findViewById(R.id.admin_add_dish_type);
		final EditText dishPriceET=(EditText) v.findViewById(R.id.admin_add_dish_price);
		final EditText dishPicturePathET=(EditText) v.findViewById(R.id.admin_add_dish_picture_path);
		final EditText dishRemarkET=(EditText) v.findViewById(R.id.admin_add_dish_remark);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(" �����û�  ")
		       .setCancelable(false)
		       .setView(v)
		       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   String dishName=dishNameET.getText().toString().trim();
		        	   String dishType=dishTypeET.getText().toString().trim();
		        	   String dishPrice=dishPriceET.getText().toString().trim();
		        	   String dishPicturePath=dishPicturePathET.getText().toString().trim();
		        	   String dishRemark=dishRemarkET.getText().toString().trim();
		        	   
		        	   if((dishName.equals(""))||(dishType.equals(""))||(dishPrice.equals(""))||(dishPicturePath.equals(""))) {
		        		   Toast.makeText(AdminActivity.this, "��������Ҫ���ӵĲ�Ʒ��Ϣ!", Toast.LENGTH_LONG).show();
		        	   }
		        	   else{
			        	   szQueryStr="&name="+dishName+"&type="+dishType+"&price="+dishPrice+"&picture="+dishPicturePath+"&remark="+dishRemark;
			        	   
			        	   // ����һ�����̷߳��ʷ�����
			        	   new Thread(new Runnable() {
	
							@Override
							public void run() {
								// ���ʷ�����url
								String url=HttpUtil.BASE_URL+"servlet/AdminServlet?requestStr="+szAdminRequestStr+szQueryStr;
								// ���ط��������ʽ��
								String result=HttpUtil.queryStringForPost(url);
								
								Message msg = new Message();
			       		        Bundle data = new Bundle();
			       		        data.putString("value", result);
			       		        msg.setData(data);
			       		        adminHandler.sendMessage(msg);
							}
			        		   
			        	   }).start();
		        	   }
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
	*/
	
	private void getAllDishes() {
		// ����һ�����̷߳��ʷ�������õ�ǰ�����û�
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// ���ʷ�����url
				String url=HttpUtil.BASE_URL+"servlet/AdminServlet?requestStr=allDishesReq";
   				// ��÷��ʽ��
   				String result=HttpUtil.queryStringForPost(url);
   				
   				szCheckDishesStr=result;
			}
			
		});
		thread.start();
		try {
			thread.join();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void getDeleteDishQuery() {
		// ���LayoutInflaterʵ��
		LayoutInflater inflater = LayoutInflater.from(this);
		// ���LinearLayout��ͼʵ��
		View v =inflater.inflate(R.layout.admin_delete_dish, null);
		// ��ø�������ʵ��
		final ListView dishesLV=(ListView) v.findViewById(R.id.admin_delete_dish_show_allDishes);
		final EditText deleteDishNameET=(EditText) v.findViewById(R.id.admin_delete_dish_name);
		
		Gson gson=new Gson();
		List<Dishes> dishList=new ArrayList<Dishes>();
		dishList = gson.fromJson(szCheckDishesStr, new TypeToken<List<Dishes>>() {  
        }.getType());
		
		MyAdminDishAdapter adapter=new MyAdminDishAdapter(dishList,mContext);
		dishesLV.setAdapter(adapter);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("ɾ����Ʒ  ")
		       .setCancelable(false)
		       .setView(v)
		       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
		        	   String szDishName=deleteDishNameET.getText().toString().trim();
		        	   
		        	   if(szDishName.equals("")) {
		        		   Toast.makeText(AdminActivity.this, "��������Ҫɾ���Ĳ�Ʒ��!", Toast.LENGTH_LONG).show();
		        	   }
		        	   else {
			        	   szQueryStr="&name="+szDishName;
			        	   
			        	   new Thread(new Runnable() {
	
							@Override
							public void run() {
								// ���ʷ�����url
			       				String url=HttpUtil.BASE_URL+"servlet/AdminServlet?requestStr="+szAdminRequestStr+szQueryStr;
			       				// ��÷��ʽ��
			       				String result=HttpUtil.queryStringForPost(url);
			       				
			       				Message msg = new Message();
			       		        Bundle data = new Bundle();
			       		        data.putString("value", result);
			       		        msg.setData(data);
			       		        adminHandler.sendMessage(msg);
							}
			        		   
			        	   }).start();
		        	   }
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
	
	private void getUpdateDishesQuery() {
		// ���LayoutInflaterʵ��
		LayoutInflater inflater = LayoutInflater.from(this);
		// ���LinearLayout��ͼʵ��
		View v =inflater.inflate(R.layout.admin_update_dish, null);
		// ��ø�������ʵ��
		final ListView updateDishLV=(ListView) v.findViewById(R.id.admin_update_dish_show_allDishes);
		final EditText updateDishNameET=(EditText) v.findViewById(R.id.admin_update_dish_name);
		final EditText updateDishPriceET=(EditText) v.findViewById(R.id.admin_update_dish_price);
		final EditText updateDishRemarkET=(EditText) v.findViewById(R.id.admin_update_dish_remark);
		
		Gson gson=new Gson();
		List<Dishes> dishList=new ArrayList<Dishes>();
		dishList = gson.fromJson(szCheckDishesStr, new TypeToken<List<Dishes>>() {  
        }.getType());
		
		MyAdminDishAdapter adapter=new MyAdminDishAdapter(dishList,mContext);
		updateDishLV.setAdapter(adapter);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("���²�Ʒ  ")
		       .setCancelable(false)
		       .setView(v)
		       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
		        	   String szUpdateDishName=updateDishNameET.getText().toString().trim();
		        	   String szUpdateDishPrice=updateDishPriceET.getText().toString().trim();
		        	   String szUpdateDishRemark=updateDishRemarkET.getText().toString().trim();
		        	   
		        	   if((szUpdateDishName.equals(""))||(szUpdateDishPrice.equals(""))||(szUpdateDishRemark.equals(""))) {
		        		   Toast.makeText(AdminActivity.this, "��������Ҫ���µĲ�Ʒ��Ϣ!", Toast.LENGTH_LONG).show();
		        	   }
		        	   else {
			        	   szQueryStr="&name="+szUpdateDishName+"&price="+szUpdateDishPrice+"&remark="+szUpdateDishRemark;
			        	   
			        	   new Thread(new Runnable() {

							@Override
							public void run() {
								// ���ʷ�����url
			       				String url=HttpUtil.BASE_URL+"servlet/AdminServlet?requestStr="+szAdminRequestStr+szQueryStr;
			       				// ��÷��ʽ��
			       				String result=HttpUtil.queryStringForPost(url);
			       				
			       				Message msg = new Message();
			       		        Bundle data = new Bundle();
			       		        data.putString("value", result);
			       		        msg.setData(data);
			       		        adminHandler.sendMessage(msg);
							}
			        		   
			        	   }).start();
		        	   }
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
	
	private void getClearTableQuery() {
		// ���LayoutInflaterʵ��
		LayoutInflater inflater = LayoutInflater.from(this);
		// ���LinearLayout��ͼʵ��
		View v =inflater.inflate(R.layout.admin_clear_tables, null);
		// ��ø�������ʵ��
		final EditText tableIdET=(EditText) v.findViewById(R.id.admin_clear_table_tableid);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("��������  ")
		       .setCancelable(false)
		       .setView(v)
		       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   String szTableId=tableIdET.getText().toString().trim();
		        	   
		        	   if(szTableId.equals("")) {
		        		   Toast.makeText(AdminActivity.this, "���ӺŲ���Ϊ��!", Toast.LENGTH_LONG).show();
		        	   }
		        	   else {
			        	   szQueryStr="&tableid="+szTableId;
			        	   
			        	   new Thread(new Runnable() {
	
							@Override
							public void run() {
								// ���ʷ�����url
			       				String url=HttpUtil.BASE_URL+"servlet/AdminServlet?requestStr="+szAdminRequestStr+szQueryStr;
			       				// ��÷��ʽ��
			       				String result=HttpUtil.queryStringForPost(url);
			       				
			       				Message msg = new Message();
			       		        Bundle data = new Bundle();
			       		        data.putString("value", result);
			       		        msg.setData(data);
			       		        adminHandler.sendMessage(msg);
							}
			        		   
			        	   }).start();
		        	   }
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
	
	private void getIsDoneQuery() {
		// ���LayoutInflaterʵ��
		LayoutInflater inflater = LayoutInflater.from(this);
		// ���LinearLayout��ͼʵ��
		View v =inflater.inflate(R.layout.admin_dish_is_done, null);
		// ��ø�������ʵ��
		final EditText orderIdET=(EditText) v.findViewById(R.id.admin_dish_is_done_orderid_et);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("���㶩��  ")
		       .setCancelable(false)
		       .setView(v)
		       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   String szOrderId=orderIdET.getText().toString().trim();
		        	   
		        	   if(szOrderId.equals("")) {
		        		   Toast.makeText(AdminActivity.this, "�����Ų���Ϊ��!", Toast.LENGTH_LONG).show();
		        	   }
		        	   else {
			        	   szQueryStr="&orderid="+szOrderId;
			        	   
			        	   new Thread(new Runnable() {
	
							@Override
							public void run() {
								// ���ʷ�����url
			       				String url=HttpUtil.BASE_URL+"servlet/AdminServlet?requestStr="+szAdminRequestStr+szQueryStr;
			       				// ��÷��ʽ��
			       				String result=HttpUtil.queryStringForPost(url);
			       				
			       				Message msg = new Message();
			       		        Bundle data = new Bundle();
			       		        data.putString("value", result);
			       		        msg.setData(data);
			       		        adminHandler.sendMessage(msg);
							}
			        		   
			        	   }).start();
		        	   }
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
	
	private void getListenOrderQuery() {
		// ����һ�����̷߳��ʷ�����
		new Thread(new Runnable() {

			@Override
			public void run() {
				// ���ʷ�����url
				String url=HttpUtil.BASE_URL+"servlet/AdminServlet?requestStr="+szAdminRequestStr;
				// ���ط��ʽ��
				String result=HttpUtil.queryStringForPost(url);
				
				Message msg = new Message();
   		        Bundle data = new Bundle();
   		        data.putString("value", result);
   		        msg.setData(data);
   		        checkOrdersHandler.sendMessage(msg);
			}
			
		}).start();
	}
	
	private void getAllPaidOrder() {
		// ����һ�����̷߳��ʷ�������õ�ǰ�����û�
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// ���ʷ�����url
				String url=HttpUtil.BASE_URL+"servlet/AdminServlet?requestStr=allOrdersReq";
   				// ��÷��ʽ��
   				String result=HttpUtil.queryStringForPost(url);
   				
   				szCheckOrdersStr=result;
			}
			
		});
		thread.start();
		try {
			thread.join();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void getDishUpQuery() {
		// ���LayoutInflaterʵ��
		LayoutInflater inflater = LayoutInflater.from(this);
		// ���LinearLayout��ͼʵ��
		View v =inflater.inflate(R.layout.admin_dish_up, null);
		// ��ø�������ʵ��
		final ListView orderInfoLV=(ListView) v.findViewById(R.id.admin_dish_up_show_allOrders);
		final EditText dishUpOrderIdET=(EditText) v.findViewById(R.id.admin_dish_up_orderid);
		
		Gson gson=new Gson();
		dishUpOrdersList = new ArrayList<DishUpOrders>();
		dishUpOrdersList = gson.fromJson(szCheckOrdersStr, new TypeToken<List<DishUpOrders>>() {  
        }.getType());
		
		if(null==dishUpOrdersList) {
			Toast.makeText(AdminActivity.this, "��ǰû�ж���!", Toast.LENGTH_LONG).show();
		}
		else {
			MyAdminOrderDetailAdapter adapter=new MyAdminOrderDetailAdapter(dishUpOrdersList,mContext);
			orderInfoLV.setAdapter(adapter);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("ȷ���ϲ�����  ")
			       .setCancelable(false)
			       .setView(v)
			       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   
			        	   String szDishUpOrderId=dishUpOrderIdET.getText().toString().trim();
			        	   
			        	   if(szDishUpOrderId.equals("")) {
			        		   Toast.makeText(AdminActivity.this, "��������Ҫ�ϲ˵Ķ�����!", Toast.LENGTH_LONG).show();
			        	   }
			        	   else {
				        	   szQueryStr="&orderid="+szDishUpOrderId;
				        	   // ����һ�����߳����ڸ��¶������ϲ�״̬
				        	   new Thread(new Runnable() {
		
								@Override
								public void run() {
									// ���ʷ�����url
				       				String url=HttpUtil.BASE_URL+"servlet/AdminServlet?requestStr="+szAdminRequestStr+szQueryStr;
				       				// ��÷��ʽ��
				       				String result=HttpUtil.queryStringForPost(url);
				       				
				       				Message msg = new Message();
				       		        Bundle data = new Bundle();
				       		        data.putString("value", result);
				       		        msg.setData(data);
				       		        checkOrdersDetailHandler.sendMessage(msg);
								}
				        		   
				        	   }).start();
			        	   }
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
	}
	
	Handler adminHandler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		    Bundle data = msg.getData();
		    String temp = data.getString("value").toString();
		    
		    // ��ʾ������
			Toast.makeText(AdminActivity.this, temp, Toast.LENGTH_LONG).show();
		};
	};
	Handler checkUsersHandler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		    Bundle data = msg.getData();
		    String temp = data.getString("value").toString();

			szCheckUsersStr=temp;
		};
	};
	Handler checkOrdersHandler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		    Bundle data = msg.getData();
		    String temp = data.getString("value").toString();
		    // ����ӷ������õ��Ľ����Ϣ
		    String resStr="";

		    Gson gson=new Gson();
			List<String> orderInfoList=new ArrayList<String>();
			orderInfoList = gson.fromJson(temp, new TypeToken<List<String>>() {  
	        }.getType());
			
			for(int i=0;i<orderInfoList.size();i++) {
				resStr += (orderInfoList.get(i)+"                            ");
			}
			
			orderMarqueeET.setText(resStr);
		};
	};
	Handler checkOrdersDetailHandler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		    Bundle data = msg.getData();
		    String temp = data.getString("value").toString();
		    
		    String[] res=temp.split("\\|");
		    String marqueeStr="";
		    
 			// ��������ʾ��ǰ�ϲ˶�������
 			int orderid=Integer.parseInt(res[0]);
 			marqueeStr += "����"+orderid+"��ʼ�ϲ�~~ ";
 			for(int i=0;i<dishUpOrdersList.size();i++) {
 				if(orderid == dishUpOrdersList.get(i).getOrderId()) {
 					marqueeStr += (dishUpOrdersList.get(i).getName()+dishUpOrdersList.get(i).getDishNum()+"��~  ��ע:"+dishUpOrdersList.get(i).getRemark()+"~                ");
 				}
 			}
 			
 			orderDetailMarqueeET.setText(marqueeStr);
		    // ��ʾ������
 			Toast.makeText(AdminActivity.this, res[1], Toast.LENGTH_LONG).show();
		};
	};
	
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
		        	  intent.setClass(AdminActivity.this, LoginActivity.class);
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
}