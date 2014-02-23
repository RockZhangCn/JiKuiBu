package com.jikuibu.Mobile.utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;


public class FileUtils 
{
	public static final String  DIRPATH = "/dirconfig/directory.xml";
	

	private static Context context;
	private static FileUtils singleInstance = null;

	private String internalPath=null;
	private File internalPathFile;
	
	public String getInternalPATH()
	{
		return internalPath;
	}
	
	public static void setGlobalContext(Context c)
	{
		context = c;
	}
	
	public static Context getGlobalContext()
	{
		return context;
	}
	
	public static FileUtils getSingleInstance()
	{
		if(singleInstance == null)
		{
			singleInstance =  new FileUtils();
		}
		
		return singleInstance;
	}
	
	private FileUtils()
	{
		internalPathFile = context.getFilesDir();
		internalPath= internalPathFile.getAbsolutePath();
	}
	//创建文件
	public File createFile(String fileName) throws IOException
	{
		File file=new File(internalPath + fileName);
		file.createNewFile();
		return file;
	}
	//创建目录
	public File createDir(String fileName) throws IOException
	{
		File dir=new File(internalPath+fileName);		
		dir.mkdir();
		return dir;
	}
	//判断文件是否存在
	public boolean isExist(String fileName)
	{
		File file = new File(internalPath + fileName);
		return file.exists();
	}
	public File writeToSDPATHFromInput(String path,String fileName,InputStream inputstream)
	{
		File file=null;
		OutputStream outputstream=null;
		//path = internalPath + path;
	    try
		{
	    	createDir(path);
	    	file=createFile(path+fileName);
	    	outputstream=new FileOutputStream(file);
	    	byte buffer[]=new byte[1024];
	    	//将输入流中的内容先输入到buffer中缓存，然后用输出流写到文件中
	    	while((inputstream.read(buffer))!=-1)
	    	{
	    		outputstream.write(buffer);
	    	}
		}
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	try {
				outputstream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    return file;
	}
}
