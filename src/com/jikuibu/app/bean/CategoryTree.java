package com.jikuibu.app.bean;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import org.json.JSONObject;

import com.jikuibu.app.utils.StringUtils;

public class CategoryTree extends Entity {

	private static final long serialVersionUID = 1L;

	private static final String TAG = "CategoryTree";
	
	private List<Directory> directoryList;
	private List<Directory> displayDirectories = new ArrayList<Directory>();
		
	public CategoryTree(List<Directory> dirList)
	{
		directoryList = dirList;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder rest = new StringBuilder();
		for(Directory dir: directoryList)
		{
			rest.append(dir.getContentText());
			rest.append("[");
			rest.append(dir.getId());
			rest.append("]\t");
		}
		return rest.toString();
	}

	public boolean isEmpty()
	{
		if(directoryList != null)
			return directoryList.isEmpty();
		else
			return true;
	}
	
	public CategoryTree()
	{

	}
	
	public void generateDisplayDirectoryList()
	{
		if(directoryList != null)
		{
			for(Directory directory : directoryList)
			{
				if(directory.getLevel() == 0)
					displayDirectories.add(directory);
			}
		}
	}
	
	public List<Directory> getDisplayDirectoryList() {
		return displayDirectories;
	}
	
	public List<Directory> getDirectoryList() {
		return directoryList;
	}

	public void setDirectoryList(List<Directory> dirList) {
		displayDirectories.clear();
		
		if(directoryList != null)
			directoryList.clear();
		
		directoryList = dirList;
		generateDisplayDirectoryList();
	}
	
	public CategoryTree parse(InputStream inputstream)
	{	
		
		try {
			String ret = StringUtils.InputStreamTOString(inputstream, UTF8);
			inputstream.reset();
			Log.e(TAG, "we get file content is " + ret );
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		directoryList = new ArrayList<Directory>();

		XmlPullParser xmlParser = Xml.newPullParser();  
		int idIndex = 0;
		
		final int MAXDEPTH = 16; 
		int[] depthParent = new int[MAXDEPTH];

        //depthParent[i] save the latest Directory id for level/depth i.
        //level base on zero.
		for(int i = 0 ; i < MAXDEPTH ; i++)
        {
			depthParent[i] = -1;
        }
		
		try 
		{
			xmlParser.setInput(inputstream, UTF8);
			int eventType = xmlParser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT)
			{
				if(eventType == XmlPullParser.START_TAG)
				{
					String tagName = xmlParser.getName();
                    if (tagName.equals("node")) 
                    {
                        String nodeName = xmlParser.getAttributeValue(null, "name");
                        int depth = xmlParser.getDepth() - 2;//start at 0. First is 0, second is 1.
                        boolean hasChildren = true;
                        String type = xmlParser.getAttributeValue(null, "type");
                        
                        if(type != null && type.equalsIgnoreCase("leaf"))
                        {
                        	hasChildren = false;
                        }
						Directory dir = new Directory(nodeName, depth, idIndex, depth == 0 ? Directory.NO_PARENT : depthParent[depth -1], 0/*default TODO  */, hasChildren, false);

						directoryList.add(dir);
						        
                        depthParent[depth] = idIndex;
                        
                        idIndex++;
                    }
				}
				eventType = xmlParser.next();
			}
		} catch (XmlPullParserException e) 
		{
           // TODO Auto-generated catch block
           e.printStackTrace();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this;
	}


	public int getPageSize() {
		// TODO Auto-generated method stub
		return 0;
	}
}
