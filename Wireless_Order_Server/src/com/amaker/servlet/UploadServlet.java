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
				// 创建文件项目工厂对象
				DiskFileItemFactory factory = new DiskFileItemFactory();

				// 设置文件上传路径
				String upload="C:/Users/DELL/Desktop/Test/MyServerAndClient/Wireless_Order_Server/WebContent/uploads";
				String path="";
				String urlPath="http://10.0.3.2:8080/Wireless_Order_Server/uploads/";
				
				// 获取系统默认的临时文件保存路径，该路径为Tomcat根目录下的temp文件夹
				String temp = System.getProperty("java.io.tmpdir");
				System.out.println(temp);
				// 设置缓冲区大小为 5M
				factory.setSizeThreshold(1024 * 1024 * 5);
				// 设置临时文件夹为temp
				factory.setRepository(new File(temp));
				// 用工厂实例化上传组件,ServletFileUpload 用来解析文件上传请求
				ServletFileUpload servletFileUpload = new ServletFileUpload(factory);

				// 解析结果放在List中
				try {
					@SuppressWarnings("unchecked")
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
					out.write(urlPath);  //这里我把服务端成功后，返回给客户端的是上传成功后路径
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
		System.out.println("文件保存路径为:" + savePath);
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
