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
		
		//获得各个按键组件
		loginBtn=(Button)findViewById(R.id.login_ok_btn);
		resetBtn=(Button)findViewById(R.id.login_reset_btn);
		exitBtn=(Button)findViewById(R.id.login_exit_btn);
		registerBtn=(Button)findViewById(R.id.login_register_btn);
		
		//获得输入框组件
		userEditText=(EditText)findViewById(R.id.login_account);
		pwdEditText=(EditText)findViewById(R.id.login_password);
		
		//为各个按键添加点击事件
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(validate()) {
					new Thread(new Runnable() {
						//开启子线程进行登录操作
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
				android.os.Process.killProcess(android.os.Process.myPid());     //获取PID 
				System.exit(0);   												//常规java的标准退出法，返回值为0代表正常退出
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
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
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
	        	showDialog("用户不存在或者用户输入有误！");
			}
			else {
				Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
				
				Gson gson=new Gson();
				User user=gson.fromJson(val, User.class);
				
				// 取得登录成功用户的permission
				// 普通点餐用户
				if(2==user.getPermission()) {
					Toast.makeText(LoginActivity.this, "欢迎点餐用户:"+user.getName(), Toast.LENGTH_LONG).show();
					// 跳转到用户点餐主界面
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					startActivity(intent);
				}
				// 管理员
				if(1==user.getPermission()) {
					Toast.makeText(LoginActivity.this, "欢迎管理员:"+user.getName(), Toast.LENGTH_LONG).show();
					// 跳转到管理员管理界面
					Intent intent = new Intent(LoginActivity.this,AdminActivity.class);
					startActivity(intent);
				}
				
				// 保存用户登录配置信息
				saveUserInfoMsg(user);
			}
	    }
	};
	
	//验证输入框内容函数
	private boolean validate() {
		String username=userEditText.getText().toString();
		if(username.equals("")) {
			showDialog("用户账号不能为空！");
			return false;
		}
		String password=pwdEditText.getText().toString();
		if(password.equals("")) {
			showDialog("密码不能为空！");
			return false;
		}
		return true;
	}
	//显示对话框函数
	private void showDialog(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
		       .setCancelable(false)
		       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	//保存用户登录信息
	private void saveUserInfoMsg(User user) {
		//获取SharedPreferences对象
        SharedPreferences sharedPre=getSharedPreferences("config", MODE_PRIVATE);
        //获取Editor对象
        Editor editor=sharedPre.edit();
        //设置参数
        editor.putInt("id", user.getId());
        editor.putString("name", user.getName());
        editor.putString("account", user.getAccount());
        editor.putString("password", user.getPassword());
        //提交
        editor.commit();
	}
}