package com.amaker.wlo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amaker.util.HttpUtil;
import com.amaker.util.UploadUtil;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddDishActivity extends Activity{
	
	private static final int SELECT_PHOTO=102;
	private static final int TAKE_PHOTO=202;
	
	private String imageFilePath="";
	private String picPath="";
	private String tipsStr="";
	private String str="";
	
	private static String szAdminRequestStr="dishAddReq";
	private static String szQueryStr="";
	
	private EditText dishNameET,dishTypeEt,dishPriceET,dishRemarkET;
	private ImageView uploadPicIV;
	private Button localPhotoBtn,takePhotoBtn,confirmBtn,cancelBtn;
	
	private Intent intent=null;
	private Uri imageFileUri=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_add_dish);
		
		dishNameET=(EditText)findViewById(R.id.admin_add_dish_name);
		dishTypeEt=(EditText)findViewById(R.id.admin_add_dish_type);
		dishPriceET=(EditText)findViewById(R.id.admin_add_dish_price);
		dishRemarkET=(EditText)findViewById(R.id.admin_add_dish_remark);
		
		uploadPicIV=(ImageView)findViewById(R.id.admin_add_dish_upload_iv);
		
		localPhotoBtn=(Button)findViewById(R.id.admin_add_dish_upload_pic_btn);
		localPhotoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ���ѡȡ
				intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, SELECT_PHOTO);
			}
			
		});
		
		takePhotoBtn=(Button)findViewById(R.id.admin_add_dish_take_picture_upload);
		takePhotoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����
				//����ͼƬ�ı���·��,��Ϊȫ�ֱ���
				Date date=new Date(System.currentTimeMillis());
				SimpleDateFormat dateFormat=new SimpleDateFormat("'IMG'_yyyyMMddHHmmss");
				String timeStr=dateFormat.format(date);
				imageFilePath = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/"+timeStr+".jpg";
				File temp = new File(imageFilePath);
				
				imageFileUri = Uri.fromFile(temp);//��ȡ�ļ���Uri	
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//��ת�����Activity
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);//�����������������ͼƬ��ָ����Uri
				startActivityForResult(intent, TAKE_PHOTO);
			}
			
		});
		
		confirmBtn=(Button)findViewById(R.id.admin_add_dish_comfirm_btn);
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String dishName=dishNameET.getText().toString().trim();
        	    String dishType=dishTypeEt.getText().toString().trim();
        	    String dishPrice=dishPriceET.getText().toString().trim();
        	    String dishRemark=dishRemarkET.getText().toString().trim();
        	    String picPath=upLoadPicture(str).toString().trim();
        	    
        	    if((dishName.equals(""))||(dishType.equals(""))||(dishPrice.equals(""))||(picPath.equals(""))) {
	        		   Toast.makeText(AddDishActivity.this, "��������Ҫ���ӵĲ�Ʒ��Ϣ!", Toast.LENGTH_LONG).show();
	        	   }
        	    else {
        	    	   szQueryStr="&name="+dishName+"&type="+dishType+"&price="+dishPrice+"&picture="+picPath+"&remark="+dishRemark;
		        	   
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
		       		        handler.sendMessage(msg);
						}
		        		   
		        	   }).start();
        	    }
			}
			
		});
		
		cancelBtn=(Button)findViewById(R.id.admin_add_dish_cancel_btn);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddDishActivity.this,AdminActivity.class);
				startActivity(intent);
			}
			
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==SELECT_PHOTO)
		{
			if(data!=null&&resultCode!=0)
			{
				Bitmap bm = null;
				 // ���ĳ������ContentProvider���ṩ���� ����ͨ��ContentResolver�ӿ�
				 ContentResolver resolver = getContentResolver();

				 try {
					Uri originalUri = data.getData(); // ���ͼƬ��uri

					bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // �Եõ�bitmapͼƬ

					// ���￪ʼ�ĵڶ����֣���ȡͼƬ��·����

					String[] proj = { MediaStore.Images.Media.DATA };

					// ������android��ý�����ݿ�ķ�װ�ӿڣ�����Ŀ�Android�ĵ�
					@SuppressWarnings("deprecation")
					Cursor cursor = managedQuery(originalUri, proj, null, null, null);
					if(cursor!=null)
					{
						// ���Ҹ������� ����ǻ���û�ѡ���ͼƬ������ֵ
						int column_index = cursor.getColumnIndexOrThrow(proj[0]);
						// �����������ͷ ���������Ҫ����С�ĺ���������Խ��
						cursor.moveToFirst();
						// ����������ֵ��ȡͼƬ·��
						picPath = cursor.getString(column_index);
						//cursor.close();
					}
					else {
						picPath=originalUri.getPath();
					}
					
					uploadPicIV.setImageURI(originalUri);
					str=picPath;
					
				 } catch (IOException e) {
					e.printStackTrace();
				 }
			}
		}
		
		if(requestCode==TAKE_PHOTO) {
			if(resultCode!=0/*&&data.hasExtra("data")*/) {
				if(data!=null)
				{
					Bitmap bmp = BitmapFactory.decodeFile(imageFilePath);
					str=imageFilePath;
					uploadPicIV.setImageBitmap(bmp);
				}
				else {
					str=imageFileUri.getPath();
					Bitmap bmp = BitmapFactory.decodeFile(str);
					uploadPicIV.setImageBitmap(bmp);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	Handler handler=new Handler() {
		@Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String val = data.getString("value");
	        
	        Toast.makeText(AddDishActivity.this, val, 1).show();
		}
	};
	
	private String upLoadPicture(final String str)
	{
		Thread thread=new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				File file=new File(str);
				//ģ���
				tipsStr=UploadUtil.uploadFile(file, "http://10.0.3.2:8080/Wireless_Order_Server/servlet/UploadServlet?requestStr=upload");
				//ʵ������
				//temp=UploadUtil.uploadFile(file, "http://192.168.191.1:8080/Wireless_Order_Server/servlet/UploadServlet?upload=true");
			}
			
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(tipsStr==""&&tipsStr.equals("Not Uploaded"))
		{
			Toast.makeText(AddDishActivity.this, "ͼƬ�ϴ�ʧ��", 1);
			return "";
		}
		else {
			Toast.makeText(AddDishActivity.this, "ͼƬ�ϴ��ɹ�", 1);
			return tipsStr;
		}
		
		
	}
}