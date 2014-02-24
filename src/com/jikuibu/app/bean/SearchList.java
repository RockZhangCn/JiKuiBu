package com.jikuibu.app.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jikuibu.app.AppException;
import com.jikuibu.app.utils.StringUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * æ�œç´¢åˆ—è¡¨å®žä½“ç±»
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class SearchList extends Entity{

	public final static String CATALOG_ALL = "all";
	public final static String CATALOG_NEWS = "news";
	public final static String CATALOG_POST = "post";
	public final static String CATALOG_SOFTWARE = "software";
	public final static String CATALOG_BLOG = "blog";
	public final static String CATALOG_CODE = "code";
	
	private int pageSize;
	private List<Result> resultlist = new ArrayList<Result>();
	
	/**
	 * æ�œç´¢ç»“æžœå®žä½“ç±»
	 */
	public static class Result implements Serializable {
		private int objid;
		private int type;
		private String title;
		private String url;
		private String pubDate;
		private String author;
		public int getObjid() {return objid;}
		public void setObjid(int objid) {this.objid = objid;}
		public int getType() {return type;}
		public void setType(int type) {this.type = type;}
		public String getTitle() {return title;}
		public void setTitle(String title) {this.title = title;}
		public String getUrl() {return url;}
		public void setUrl(String url) {this.url = url;}
		public String getPubDate() {return pubDate;}
		public void setPubDate(String pubDate) {this.pubDate = pubDate;}
		public String getAuthor() {return author;}
		public void setAuthor(String author) {this.author = author;}
	}

	public int getPageSize() {
		return pageSize;
	}
	public List<Result> getResultlist() {
		return resultlist;
	}
	public void setResultlist(List<Result> resultlist) {
		this.resultlist = resultlist;
	}
	
	public static SearchList parse(InputStream inputStream) throws IOException, AppException {
		SearchList searchList = new SearchList();
		Result res = null;
        //èŽ·å¾—XmlPullParserè§£æž�å™¨
        XmlPullParser xmlParser = Xml.newPullParser();
        try {        	
            xmlParser.setInput(inputStream, UTF8);
            //èŽ·å¾—è§£æž�åˆ°çš„äº‹ä»¶ç±»åˆ«ï¼Œè¿™é‡Œæœ‰å¼€å§‹æ–‡æ¡£ï¼Œç»“æ�Ÿæ–‡æ¡£ï¼Œå¼€å§‹æ ‡ç­¾ï¼Œç»“æ�Ÿæ ‡ç­¾ï¼Œæ–‡æœ¬ç­‰ç­‰äº‹ä»¶ã€‚
            int evtType=xmlParser.getEventType();
			//ä¸€ç›´å¾ªçŽ¯ï¼Œç›´åˆ°æ–‡æ¡£ç»“æ�Ÿ    
			while(evtType!=XmlPullParser.END_DOCUMENT){ 
	    		String tag = xmlParser.getName(); 
			    switch(evtType){ 
			    	case XmlPullParser.START_TAG:
			    		if(tag.equalsIgnoreCase("pageSize")) 
			    		{
			    			searchList.pageSize = StringUtils.toInt(xmlParser.nextText(),0);
			    		}
			    		else if (tag.equalsIgnoreCase("result")) 
			    		{ 
			    			res = new Result();
			    		}
			    		else if(res != null)
			    		{	
				            if(tag.equalsIgnoreCase("objid"))
				            {			      
				            	res.objid = StringUtils.toInt(xmlParser.nextText(),0);
				            }
				            else if(tag.equalsIgnoreCase("type"))
				            {			            	
				            	res.type = StringUtils.toInt(xmlParser.nextText(),0);
				            }
				            else if(tag.equalsIgnoreCase("title"))
				            {			            	
				            	res.title = xmlParser.nextText();		            	
				            }
				            else if(tag.equalsIgnoreCase("url"))
				            {			            	
				            	res.url = xmlParser.nextText();		            	
				            }
				            else if(tag.equalsIgnoreCase("pubDate"))
				            {			            	
				            	res.pubDate = xmlParser.nextText();		            	
				            }
				            else if(tag.equalsIgnoreCase("author"))
				            {			            	
				            	res.author = xmlParser.nextText();		            	
				            }
			    		}
			            //é€šçŸ¥ä¿¡æ�¯
			    		/*
			            else if(tag.equalsIgnoreCase("notice"))
			    		{
			            	searchList.setNotice(new Notice());
			    		}
			            else if(searchList.getNotice() != null)
			    		{
			    			if(tag.equalsIgnoreCase("atmeCount"))
				            {			      
			    				searchList.getNotice().setAtmeCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
				            else if(tag.equalsIgnoreCase("msgCount"))
				            {			            	
				            	searchList.getNotice().setMsgCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
				            else if(tag.equalsIgnoreCase("reviewCount"))
				            {			            	
				            	searchList.getNotice().setReviewCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
				            else if(tag.equalsIgnoreCase("newFansCount"))
				            {			            	
				            	searchList.getNotice().setNewFansCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
			    		}
			    		*/
			    		break;
			    	case XmlPullParser.END_TAG:	
					   	//å¦‚æžœé�‡åˆ°æ ‡ç­¾ç»“æ�Ÿï¼Œåˆ™æŠŠå¯¹è±¡æ·»åŠ è¿›é›†å�ˆä¸­
				       	if (tag.equalsIgnoreCase("result") && res != null) { 
				       		searchList.getResultlist().add(res); 
				       		res = null; 
				       	}
				       	break; 
			    }
			    //å¦‚æžœxmlæ²¡æœ‰ç»“æ�Ÿï¼Œåˆ™å¯¼èˆªåˆ°ä¸‹ä¸€ä¸ªèŠ‚ç‚¹
			    evtType=xmlParser.next();
			}		
        } catch (XmlPullParserException e) {
			throw AppException.xml(e);
        } finally {
        	inputStream.close();	
        }      
        return searchList;       
	}
}
