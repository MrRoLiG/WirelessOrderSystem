package com.amaker.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	public UploadServlet()
	{
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		PrintWriter out = response.getWriter();
		
		String requestStr=request.getParameter("requestStr");
		switch(requestStr)
		{
			case "upload":
				// �����ļ���Ŀ��������
				DiskFileItemFactory factory = new DiskFileItemFactory();

				// �����ļ��ϴ�·��
				//String upload = this.getServletContext().getRealPath("/");
				String upload="C:/Users/DELL/Desktop/Test/MyServerAndClient/Wireless_Order_Server/WebContent/uploads";
				String path="";
				String urlPath="http://10.0.3.2:8080/Wireless_Order_Server/uploads/";
				
				// ��ȡϵͳĬ�ϵ���ʱ�ļ�����·������·��ΪTomcat��Ŀ¼�µ�temp�ļ���
				String temp = System.getProperty("java.io.tmpdir");
				System.out.println(temp);
				// ���û�������СΪ 5M
				factory.setSizeThreshold(1024 * 1024 * 5);
				// ������ʱ�ļ���Ϊtemp
				factory.setRepository(new File(temp));
				// �ù���ʵ�����ϴ����,ServletFileUpload ���������ļ��ϴ�����
				ServletFileUpload servletFileUpload = new ServletFileUpload(factory);

				// �����������List��
				try {
					List<FileItem> list = servletFileUpload.parseRequest(request);

					for (FileItem item : list) {
						String name = item.getFieldName();
						InputStream is = item.getInputStream();

						if (name.contains("content")) 
						{
							System.out.println(inputStream2String(is));
						} 
						else if (name.contains("img")) 
						{
							try 
							{
								path = upload+"\\"+item.getName();
								urlPath+=item.getName();
								System.out.println(item.getName());
								System.out.println(temp);
								System.out.println(path);
								inputStream2File(is, path);
								break;
							} 
							catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					out.write(urlPath);  //�����Ұѷ���˳ɹ��󣬷��ظ��ͻ��˵����ϴ��ɹ���·��
				} 
				catch (FileUploadException e) {
					e.printStackTrace();
					System.out.println("failure");
					out.write("failure");
				}

				out.flush();
				out.close();
				
				break;
			default:
				break;
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request,response);
    }  
	
	public String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}
	
	public void inputStream2File(InputStream is, String savePath) throws Exception {
		System.out.println("�ļ�����·��Ϊ:" + savePath);
		File file = new File(savePath);
		InputStream inputSteam = is;
		BufferedInputStream fis = new BufferedInputStream(inputSteam);
		FileOutputStream fos = new FileOutputStream(file);
		int f;
		while ((f = fis.read()) != -1) {
			fos.write(f);
		}
		fos.flush();
		fos.close();
		fis.close();
		inputSteam.close();
	}
}