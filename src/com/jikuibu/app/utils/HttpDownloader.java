package com.jikuibu.app.utils;

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
			//åˆ›å»ºä¸€ä¸ªURLå¯¹è±¡
			url=new URL(urlStr);			
			//å¾—åˆ°ä¸€ä¸ªHttpURLConnectionå¯¹è±¡
			HttpURLConnection httpUrlConnection=(HttpURLConnection) url.openConnection();	
			// å¾—åˆ°IOæµ�ï¼Œä½¿ç�?¨IOæµ�è¯»å�–æ•°æ�®
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
	// è¯¥å‡½æ•°è¿�?å›žæ•´å½¢ -1ï¼šä»£è¡¨ä¸‹è½½æ–‡ä»¶å‡ºé�?™ ;0ï¼šä»£è¡¨ä¸‹è½½æ–‡ä»¶æˆ�åŠŸ; 1ï¼šä»£è¡¨æ–‡ä»¶å·²ç»�å­˜åœ¨
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
    //æ ¹æ�®urlå­—ç¬¦ä¸²å¾—åˆ°è¾“å…¥æµ�
    public InputStream getFromUrl(String urlStr) throws IOException
    {    	
		url = new URL(urlStr);			
		HttpURLConnection httpUrlConnection = (HttpURLConnection)url.openConnection();
		InputStream input = httpUrlConnection.getInputStream();	
		return input;
    }
}
