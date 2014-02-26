package com.jikuibu.app.bean;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Xml;

public class DirectoryList extends Entity {

	private static final long serialVersionUID = 8512622247803777462L;
	List<Directory> directoryList = new ArrayList<Directory>();
	
	public List<Directory> getDirectoryList() {
		return directoryList;
	}

	public void setDirectoryList(List<Directory> directoryList) {
		this.directoryList = directoryList;
	}
	
	public List<Directory> parse(InputStream inputstream)
	{
		directoryList.clear();
		XmlPullParser xmlParser = Xml.newPullParser();  
		int idIndex = 0;
		
		final int MAXDEPTH = 16; 
		int[] depthParent = new int[MAXDEPTH];
		for(int i = 0 ; i < MAXDEPTH ; i++)
			depthParent[i] = -1;
		
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
                        	hasChildren = false;
                        
						Directory ele = new Directory(nodeName, depth, idIndex, depth == 0? -1:depthParent[depth -1], hasChildren, false);
						directoryList.add(ele);
						        
                        depthParent[depth] = idIndex;
                        
                        idIndex++;
                    }
				}
			}
		} catch (XmlPullParserException e) 
		{
           // TODO Auto-generated catch block
           e.printStackTrace();
	    }

		return directoryList;
	}

	public class Directory implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -164708515566412051L;
		private String contentText;
		private int level;
		private int id;
		private int parendId;
		private boolean hasChildren;
		private boolean isExpanded;

		public static final int NO_PARENT = -1;
		
		public static final int TOP_LEVEL = 0;
		
		public Directory(String contentText, int level, int id, int parendId,
				boolean hasChildren, boolean isExpanded) {
			super();
			this.contentText = contentText;
			this.level = level;
			this.id = id;
			this.parendId = parendId;
			this.hasChildren = hasChildren;
			this.isExpanded = isExpanded;
		}

		public boolean isExpanded() {
			return isExpanded;
		}

		public void setExpanded(boolean isExpanded) {
			this.isExpanded = isExpanded;
		}

		public String getContentText() {
			return contentText;
		}

		public void setContentText(String contentText) {
			this.contentText = contentText;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getParendId() {
			return parendId;
		}

		public void setParendId(int parendId) {
			this.parendId = parendId;
		}

		public boolean isHasChildren() {
			return hasChildren;
		}

		public void setHasChildren(boolean hasChildren) {
			this.hasChildren = hasChildren;
		}
	}
}
