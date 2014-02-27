package com.jikuibu.app.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import com.jikuibu.app.AppContext;
import com.jikuibu.app.R;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.jikuibu.app.api.ApiClient;
import com.jikuibu.app.bean.DirectoryList;
import com.jikuibu.app.ui.Adapter.TreeViewAdapter;
import com.jikuibu.app.utils.*;

public class MainActivity extends Activity {
	private Context context;
	private AppContext appContext;
	private DirectoryList dirList;
	private long exitTime = 0; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = MainActivity.this;
		appContext = (AppContext)getApplication();
		FileUtils.setGlobalContext(context);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		BeanParseData();
		//parseConfigDirectoryData();
		Button testButton = (Button)findViewById(R.id.buttonTest);
		testButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				downloadTest();
				//requestDirectory();
			}
		});
		ListView treeview = (ListView) findViewById(R.id.treeview);
		TreeViewAdapter treeViewAdapter = new TreeViewAdapter(appContext, dirList, inflater);
		treeview.setOnItemClickListener(treeViewAdapter);
		treeview.setAdapter(treeViewAdapter);	
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{ 
			if((System.currentTimeMillis()- exitTime) > 2000)
			{ 
				UIHelper.ToastMessage(appContext, "再按一次退出程序");
				exitTime = System.currentTimeMillis(); 
			} else 
			{ 
				finish(); 
				System.exit(0); 
			} 
			return true; 
		} 
		
		return super.onKeyDown(keyCode, event);
	}


	private void BeanParseData()
	{
		String testUrl = "http://10.88.23.170/directory.xml";
		dirList = appContext.getDirectoryList(testUrl);	
	}
	
	private void requestDirectory() 
	{
		if(FileUtils.getSingleInstance().isExist(FileUtils.DIRPATH) == false)
    		return ;
		
		File dirFile = new File(FileUtils.getSingleInstance().getInternalPATH()+ FileUtils.DIRPATH);
		
		FileInputStream fis;
		
		try {
			fis = new FileInputStream(dirFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
	    
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
        Document doc;
		try {
			doc = db.parse( fis );
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
        
		org.w3c.dom.Element ele = doc.getDocumentElement();
		NodeList nodeList  = doc.getElementsByTagName("node");
		for(int i=0;i<nodeList.getLength();i++){  
            
			org.w3c.dom.Element el=(org.w3c.dom.Element) nodeList.item(i);  
           
            String nodeName = el.getAttribute("name");  
        }  

	}
	
	private void downloadTest()
	{
		try  
        {  
            HttpDownloader httpDownloader = new HttpDownloader(context);    
            httpDownloader.download("http://192.168.1.33/directory.xml","/dirconfig/","directory.xml");  
        }  
        catch(Exception e)  
        {  
            e.printStackTrace();  
        }          
	}
	
	private void parseConfigDirectoryData() 
	{/*
		elementsData = new ArrayList<Element>();
		int idIndex = 0;
		
		final int MAXDEPTH = 16; 
		int[] depthParent = new int[MAXDEPTH];
		for(int i = 0 ; i < MAXDEPTH ; i++)
			depthParent[i] = -1;
		
        XmlResourceParser xrp = getResources().getXml(R.xml.directory);

        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) 
                {
                    String tagName = xrp.getName();
                    if (tagName.equals("node")) 
                    {
                        String nodeName = xrp.getAttributeValue(null, "name");// 
                      
                        int depth = xrp.getDepth() - 2;//start at 0. First is 0, second is 1.
                        boolean hasChildren = true;
                        
                        String type = xrp.getAttributeValue(null, "type");
                        
                        if(type != null && type.equalsIgnoreCase("leaf"))
                        	hasChildren = false;
    
                        Element ele = new Element(nodeName, depth, idIndex, depth == 0? -1:depthParent[depth -1], hasChildren, false);
                        
                        elementsData.add(ele);

                        Log.e("MainActivity", nodeName + "[" + idIndex +"]\t\t depth[" + depth + "]\t\t" + "parentId[" + (depth == 0? -1:depthParent[depth -1]) + 
                        		"]\t\tHasChildren[" + hasChildren +"]\n");
                        
                        depthParent[depth] = idIndex;
                        idIndex++;
                    }
                }
                xrp.next();// èŽ·å�–è§£æž�ä¸‹ä¸€ä¸ªäº‹ä»¶
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
