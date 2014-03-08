package com.jikuibu.app.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.jikuibu.app.AppException;
import com.jikuibu.app.utils.StringUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * å�šå®¢åˆ—è¡¨å®žä½“ç±»
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21s
 */
public class KuiBuList extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int CATALOG_USER = 1;
	public static final int CATALOG_LATEST = 2;
	public static final int CATALOG_RECOMMEND = 3;
	
	public static final String TYPE_LATEST = "latest";
	public static final String TYPE_RECOMMEND = "recommend";
	
	private int blogsCount;
	private int pageSize;
	
	private List<KuiBuDict> kuibulist = new ArrayList<KuiBuDict>();
	
	public int getBlogsCount() {
		return blogsCount;
	}
	public int getPageSize() {
		return 0;//pageSize;
	}
	public List<KuiBuDict> getKuiBulist() {
		return kuibulist;
	}
	
	public void setKuiBulist(List<KuiBuDict> kuibulist)
	{
		this.kuibulist.clear();
		this.kuibulist = kuibulist;		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		for(KuiBuDict dict : this.kuibulist)
		{
			builder.append(dict.getTitle());
			builder.append("[" + dict.getKuibuid() +"]");
			builder.append("\t");
		}
		return builder.toString();
	}
	
	
	public  KuiBuList parse(InputStream inputStream) throws IOException, AppException {
        XmlPullParser xmlParser = Xml.newPullParser();
        try {        	
            xmlParser.setInput(inputStream, UTF8);
            int evtType=xmlParser.getEventType();
			
            while(evtType!=XmlPullParser.END_DOCUMENT){ 
	    		String tag = xmlParser.getName(); 
			    switch(evtType){ 
			    	case XmlPullParser.START_TAG:
			    		if(tag.equalsIgnoreCase("item")) 
			    		{
			    			KuiBuDict kuibuDict = new KuiBuDict();
			    			String name = xmlParser.getAttributeValue(null, "name");
			    			kuibuDict.setKuibuid(StringUtils.toInt(xmlParser.getAttributeValue(null, "id"),0));
			    			kuibuDict.setTitle(name);
			    			kuibulist.add(kuibuDict);
			    		}
			    	
			    		break;
			    	/*
			    	case XmlPullParser.END_TAG:
				       	if (tag.equalsIgnoreCase("blog") && blog != null) { 
				       		bloglist.getBloglist().add(blog); 
				       		blog = null; 
				       	}
				       	break;
				    */  	 
			    }
			   
			    evtType=xmlParser.next();
			}		
        } catch (XmlPullParserException e) {
			throw AppException.xml(e);
        } finally {
        	inputStream.close();	
        }      
        return this;       
	}
}
