package com.amaker.wlo;

import com.amaker.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class RegisterActivity extends Activity{
	
	private EditText userEditText,nicknameEditText,pwdEditText,confpwdEditText;
	private Button registerBtn,resetBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		//setTitle("Register");
		
		//��������ؼ�
		userEditText=(EditText)findViewById(R.id.register_account);
		nicknameEditText=(EditText)findViewById(R.id.register_nickname);
		pwdEditText=(EditText)findViewById(R.id.register_password);
		confpwdEditText=(EditText)findViewById(R.id.register_passwordConfirm);
		//��ð����ؼ�
		registerBtn=(Button)findViewById(R.id.register_ok_btn);
		resetBtn=(Button)findViewById(R.id.register_reset_btn);
		
		//Ϊ���еİ�����ӵ���¼�
		registerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//ȷ���������������
				if(validate()) {
					//����һ�����߳�����ִ��ע�����
					new Thread(new Runnable() {

						@Override
						public void run() {
							String nickname=nicknameEditText.getText().toString();
							String account=userEditText.getText().toString();
							String password=pwdEditText.getText().toString();
							
							String queryStr="&account="+account+"&password="+password+"&nickname="+nickname;
							String url=HttpUtil.BASE_URL+"servlet/MainServlet?requestStr=registerReq"+queryStr;
							
							String resQuery=HttpUtil.queryStringForPost(url);
							
							Message msg = new Message();
					        Bundle data = new Bundle();
					        data.putString("value", resQuery);
					        msg.setData(data);
					        registerHandler.sendMessage(msg);
						}
						
					}).start();
				}
			}
			
		});
		
		resetBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userEditText.setText("");
				nicknameEditText.setText("");
				pwdEditText.setText("");
				confpwdEditText.setText("");
			}
			
		});
	}
	
	
	Handler registerHandler=new Handler() {
		@Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String val = data.getString("value");
	        
	        if(null!=val&&val.equals("0")) {
	        	showDialog("�û��Ѵ��ڣ�");
			}
			if(null!=val&&val.equals("1")) 
			{
				System.out.println("REGISTER SUCCESSFUL.......................");
				showDialog("ע��ɹ���");
			}
	    }
	};
	
	//��֤������Ƿ��������Ҫ��
	private boolean validate() {
		String account=userEditText.getText().toString();
		if(account.equals("")) {
			showDialog("�û��˺Ų���Ϊ�գ�");
			return false;
		}
		String password=pwdEditText.getText().toString();
		if(password.equals("")) {
			showDialog("�û����벻��Ϊ�գ�");
			return false;
		}
		String confpwd=confpwdEditText.getText().toString();
		if(confpwd.equals("")||!confpwd.equals(password)) {
			if(confpwd.equals("")) {
				showDialog("�ظ����벻��Ϊ�գ�");
				return false;
			}
			if(!confpwd.equals(password)) {
				showDialog("ǰ���������ò�һ��");
				return false;
			}
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
	
}
