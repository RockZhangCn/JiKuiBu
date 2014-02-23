package com.example.androidtreeviewdemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import com.example.androidtreeviewdemo.treeview.Element;
import com.example.androidtreeviewdemo.treeview.TreeView;
import com.example.androidtreeviewdemo.treeview.TreeViewAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.jikuibu.Mobile.utils.*;

public class MainActivity extends Activity {
	private ArrayList<Element> elements;
	private ArrayList<Element> elementsData;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = MainActivity.this;
		FileUtils.setGlobalContext(context);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//init();
		getData();
		Button testButton = (Button)findViewById(R.id.buttonTest);
		testButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getData();
				//downloadTest();
				//requestDirectory();
			}
		});
		TreeView treeview = (TreeView) findViewById(R.id.treeview);
		TreeViewAdapter treeViewAdapter = new TreeViewAdapter(
				elements, elementsData, inflater);
		treeview.setOnItemClickListener(treeViewAdapter);
		treeview.setAdapter(treeViewAdapter);	
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
	    
    	// 1.首先得到文档生成器工厂的实例
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // 2.然后通过文档生成器工厂产生一个文档生成器
        DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
        // 3.最后通过文档生成器分析 HTTP URL 连接的输入流得到文档接口
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
        //   　　通过文档接口得到元素接口
		org.w3c.dom.Element ele = doc.getDocumentElement();
		NodeList nodeList  = doc.getElementsByTagName("node");
		for(int i=0;i<nodeList.getLength();i++){  
            //提取Person元素  
			org.w3c.dom.Element el=(org.w3c.dom.Element) nodeList.item(i);  
           
            String nodeName = el.getAttribute("name");  

          /*
            Element myele = new Element(nodeName, depth, i, lastId, true, false);
            elementsData.add(ele);
           
           */
           // Log.e("ZHANG", "name is " + name);
        }  

	}
	
	private void downloadTest()
	{
		try  
        {  
            HttpDownloader httpDownloader=new HttpDownloader(context);  
            //调用httpDownloader对象的重载方法download下载mp3文件  
            httpDownloader.download("http://192.168.1.33/directory.xml","/dirconfig/","directory.xml");  
        }  
        catch(Exception e)  
        {  
            e.printStackTrace();  
        }          
	}
	
	private void getData() {
		elements = new ArrayList<Element>();
		elementsData = new ArrayList<Element>();
		
		int lastDepth = -100;
		int idIndex = 0;
		
		final int MAXDEPTH = 16; 
		int[] depthParent = new int[MAXDEPTH];
		for(int i = 0 ; i < MAXDEPTH ; i++)
			depthParent[i] = -1;
		
        XmlResourceParser xrp = getResources().getXml(R.xml.directory);

        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                // 如果遇到了开始标签
                if (xrp.getEventType() == XmlResourceParser.START_TAG) 
                {
                    String tagName = xrp.getName();// 获取标签的名字
                    if (tagName.equals("node")) 
                    {
                        String nodeName = xrp.getAttributeValue(null, "name");// 通过属性名来获取属性值
                      
                        int depth = xrp.getDepth() - 2;//start at 0. First is 0, second is 1.
                        boolean hasChildren = true;
                        
                        String type = xrp.getAttributeValue(null, "type");
                        
                        if(type != null && type.equalsIgnoreCase("leaf"))
                        	hasChildren = false;
    
                        Element ele = new Element(nodeName, depth, idIndex, depth == 0? -1:depthParent[depth -1], hasChildren, false);
                        
                        elementsData.add(ele);
                        
                        if(depth == 0)
                        	elementsData.add(ele);
                        
                        Log.e("MainActivity", nodeName + "[" + idIndex +"]\t\t depth[" + depth + "]\t\t" + "parentId[" + (depth == 0? -1:depthParent[depth -1]) + 
                        		"]\t\tHasChildren[" + hasChildren +"]\n");
                        depthParent[depth] = idIndex;
                        lastDepth = depth;
                        
                        idIndex++;
                    }
                }
                xrp.next();// 获取解析下一个事件
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
	private void init() {
		elements = new ArrayList<Element>();
		elementsData = new ArrayList<Element>();
	
		Element e1 = new Element("山东省", Element.TOP_LEVEL, 0, Element.NO_PARENT/*-1*/, true, false);
		
		Element e2 = new Element("青岛市", Element.TOP_LEVEL + 1, 1, e1.getId(), true, false);

		Element e3 = new Element("市南区", Element.TOP_LEVEL + 2, 2, e2.getId(), true, false);
		Element e4 = new Element("香港中路", Element.TOP_LEVEL + 3, 3, e3.getId(), false, false);
		
		Element e5 = new Element("烟台市", Element.TOP_LEVEL + 1, 4, e1.getId(), true, false);

		Element e6 = new Element("֥芝罘区", Element.TOP_LEVEL + 2, 5, e5.getId(), true, false);
		Element e7 = new Element("凤凰台街道", Element.TOP_LEVEL + 3, 6, e6.getId(), false, false);

		Element e8 = new Element("威海市", Element.TOP_LEVEL + 1, 7, e1.getId(), false, false);

		Element e9 = new Element("广东省", Element.TOP_LEVEL, 8, Element.NO_PARENT, true, false);

		Element e10 = new Element("深圳市", Element.TOP_LEVEL + 1, 9, e9.getId(), true, false);

		Element e11 = new Element("南山区", Element.TOP_LEVEL + 2, 10, e10.getId(), true, false);

		Element e12 = new Element("深南大道", Element.TOP_LEVEL + 3, 11, e11.getId(), true, false);
	
		Element e13 = new Element("10000号", Element.TOP_LEVEL + 4, 12, e12.getId(), false, false);
		
		elements.add(e1);
		elements.add(e9);

		elementsData.add(e1);
		elementsData.add(e2);
		elementsData.add(e3);
		elementsData.add(e4);
		elementsData.add(e5);
		elementsData.add(e6);
		elementsData.add(e7);
		elementsData.add(e8);
		elementsData.add(e9);
		elementsData.add(e10);
		elementsData.add(e11);
		elementsData.add(e12);
		elementsData.add(e13);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
