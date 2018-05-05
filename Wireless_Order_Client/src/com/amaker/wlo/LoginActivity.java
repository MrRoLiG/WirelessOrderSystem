package com.amaker.wlo;

import com.amaker.provider.User;
import com.amaker.util.HttpUtil;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private Button loginBtn,resetBtn,exitBtn,registerBtn;
	private EditText userEditText,pwdEditText;
	private boolean bIsExit=false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		
		//��ø����������
		loginBtn=(Button)findViewById(R.id.login_ok_btn);
		resetBtn=(Button)findViewById(R.id.login_reset_btn);
		exitBtn=(Button)findViewById(R.id.login_exit_btn);
		registerBtn=(Button)findViewById(R.id.login_register_btn);
		
		//�����������
		userEditText=(EditText)findViewById(R.id.login_account);
		pwdEditText=(EditText)findViewById(R.id.login_password);
		
		//Ϊ����������ӵ���¼�
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(validate()) {
					new Thread(new Runnable() {
						//�������߳̽��е�¼����
						@Override
						public void run() {
							try {
								String username=userEditText.getText().toString();
								String password=pwdEditText.getText().toString();
								
								String queryStr="&account="+username+"&password="+password;
								String url=HttpUtil.BASE_URL+"servlet/MainServlet?requestStr=loginReq"+queryStr;
								
								String resQuery=HttpUtil.queryStringForPost(url);
								
								Message msg = new Message();
						        Bundle data = new Bundle();
						        data.putString("value", resQuery);
						        msg.setData(data);
						        loginHandler.sendMessage(msg);
							}
							catch(Exception e) {
								e.printStackTrace();
							}
						}
						
					}).start();
				}
				
			}
		});
		
		resetBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userEditText.setText("");
				pwdEditText.setText("");
			}
			
		});
		exitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				android.os.Process.killProcess(android.os.Process.myPid());     //��ȡPID 
				System.exit(0);   												//����java�ı�׼�˳���������ֵΪ0���������˳�
			}
			
		});
		registerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
			
		});
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(KeyEvent.KEYCODE_BACK == keyCode) {
			if(bIsExit) {
				this.finish();
			}
			else {
				Toast.makeText(this, "�ٰ�һ���˳�", Toast.LENGTH_SHORT).show();
				bIsExit=true;
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						bIsExit=false;
					}
					
				}, 2000);
			}
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}

	Handler loginHandler=new Handler() {
		@Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String val = data.getString("value");
	        
	        if(null!=val&&val.equals("0")) {
	        	showDialog("�û������ڻ����û���������");
			}
			else {
				Toast.makeText(LoginActivity.this, "��¼�ɹ���", Toast.LENGTH_SHORT).show();
				
				Gson gson=new Gson();
				User user=gson.fromJson(val, User.class);
				
				// ȡ�õ�¼�ɹ��û���permission
				// ��ͨ����û�
				if(2==user.getPermission()) {
					Toast.makeText(LoginActivity.this, "��ӭ����û�:"+user.getName(), Toast.LENGTH_LONG).show();
					// ��ת���û����������
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					startActivity(intent);
				}
				// ����Ա
				if(1==user.getPermission()) {
					Toast.makeText(LoginActivity.this, "��ӭ����Ա:"+user.getName(), Toast.LENGTH_LONG).show();
					// ��ת������Ա�������
					Intent intent = new Intent(LoginActivity.this,AdminActivity.class);
					startActivity(intent);
				}
				
				// �����û���¼������Ϣ
				saveUserInfoMsg(user);
			}
	    }
	};
	
	//��֤��������ݺ���
	private boolean validate() {
		String username=userEditText.getText().toString();
		if(username.equals("")) {
			showDialog("�û��˺Ų���Ϊ�գ�");
			return false;
		}
		String password=pwdEditText.getText().toString();
		if(password.equals("")) {
			showDialog("���벻��Ϊ�գ�");
			return false;
		}
		return true;
	}
	//��ʾ�Ի�����
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
	
	//�����û���¼��Ϣ
	private void saveUserInfoMsg(User user) {
		//��ȡSharedPreferences����
        SharedPreferences sharedPre=getSharedPreferences("config", MODE_PRIVATE);
        //��ȡEditor����
        Editor editor=sharedPre.edit();
        //���ò���
        editor.putInt("id", user.getId());
        editor.putString("name", user.getName());
        editor.putString("account", user.getAccount());
        editor.putString("password", user.getPassword());
        //�ύ
        editor.commit();
	}
}