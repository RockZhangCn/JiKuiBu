package com.jikuibu.app.bean;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Xml;

public class DirectoryOutlineList extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Directory> directoryList;
		
	public DirectoryOutlineList(List<Directory> dirList)
	{
		directoryList = dirList;
	}
	
	
	public boolean isEmpty()
	{
		return directoryList.isEmpty();
	}
	
	public DirectoryOutlineList()
	{
		directoryList = new ArrayList<Directory>();
	}
	
	public List<Directory> getDirectoryList() {
		return directoryList;
	}

	public void setDirectoryList(List<Directory> dirList) {
		directoryList = dirList;
	}
	
	private static  Directory.OnClickListener listener;
	public static void setDirectoryClickListener(Directory.OnClickListener list)
	{
		listener = list;
	}
	
	public void restoreDirectoryListener()
	{
		if(directoryList != null)
		{
			for (Directory dir : directoryList)
			{
				if (dir.isHasChildren() ==  false  && listener != null) // leaf
					dir.setOnClickListener(listener);
			}
		}
		
	}
	
	public DirectoryOutlineList parse(InputStream inputstream)
	{
		directoryList.clear();
		//Directory dirHeaderHodler = new Directory("rock", 0, -2, Directory.NO_PARENT , 0/*default TODO  */, true, false);
		//directoryList.add(dirHeaderHodler);
		
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
						
						if(hasChildren == false)
						{
							dir.setOnClickListener(listener);
							
						}
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
		return 3;
	}
}
