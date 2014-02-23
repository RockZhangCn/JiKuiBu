package com.jikuibu.Mobile.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
public class HttpDownloader {
	private URL url=null;
	
	private Context context;
	
	public HttpDownloader(Context c)
	{
		context = c;
	}
	
	public String download(String urlStr)
	{
		StringBuffer stringbuffer=new StringBuffer();
		String line;
		BufferedReader bufferReader=null;
		try
		{
			//创建一个URL对象
			url=new URL(urlStr);			
			//得到一个HttpURLConnection对象
			HttpURLConnection httpUrlConnection=(HttpURLConnection) url.openConnection();	
			// 得到IO流，使用IO流读取数据
			bufferReader=new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
			while((line=bufferReader.readLine())!=null)
			{				
				stringbuffer.append(line);
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}				
		return stringbuffer.toString();
		
	}
	// 该函数返回整形 -1：代表下载文件出错 ;0：代表下载文件成功; 1：代表文件已经存在
    public int download(String urlStr,String path,String fileName)
    {
    	InputStream inputstream=null;
    	
    	if(FileUtils.getSingleInstance().isExist(path+fileName))
    		return 1;
    	else
    	{
    		try {
				inputstream=getFromUrl(urlStr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
			File file = FileUtils.getSingleInstance().writeToSDPATHFromInput(path, fileName, inputstream);
			
			if(file!=null)
				return 0;
			else 
				return -1;
    	}
    }
    //根据url字符串得到输入流
    public InputStream getFromUrl(String urlStr) throws IOException
    {    	
		url=new URL(urlStr);			
		HttpURLConnection httpUrlConnection=(HttpURLConnection) url.openConnection();
		InputStream input=httpUrlConnection.getInputStream();	
		return input;
    }
}
