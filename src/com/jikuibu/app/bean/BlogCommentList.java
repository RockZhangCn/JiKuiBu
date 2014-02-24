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
 * å�šå®¢è¯„è®ºåˆ—è¡¨å®žä½“ç±»
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */

public class BlogCommentList extends Entity{
	
	private int pageSize;
	private int allCount;
	private List<Comment> commentlist = new ArrayList<Comment>();
	
	public int getPageSize() {
		return pageSize;
	}
	public int getAllCount() {
		return allCount;
	}
	public List<Comment> getCommentlist() {
		return commentlist;
	}

	public static BlogCommentList parse(InputStream inputStream) throws IOException, AppException {
		BlogCommentList commlist = new BlogCommentList();
		Comment comm = null;
		//Reply reply = null;
		//Refer refer = null;
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
			    		if(tag.equalsIgnoreCase("allCount")) 
			    		{
			    			commlist.allCount = StringUtils.toInt(xmlParser.nextText(),0);
			    		}
			    		else if(tag.equalsIgnoreCase("pageSize")) 
			    		{
			    			commlist.pageSize = StringUtils.toInt(xmlParser.nextText(),0);
			    		}
			    		else if (tag.equalsIgnoreCase("comment")) 
			    		{ 
			    			comm = new Comment();
			    		}
			    		else if(comm != null)
			    		{	
				            if(tag.equalsIgnoreCase("id"))
				            {			      
				            	comm.id = StringUtils.toInt(xmlParser.nextText(),0);
				            }
				            else if(tag.equalsIgnoreCase("portrait"))
				            {			            	
				            	comm.setFace(xmlParser.nextText());
				            }
				            else if(tag.equalsIgnoreCase("author"))
				            {			            	
				            	comm.setAuthor(xmlParser.nextText());		            	
				            }
				            else if(tag.equalsIgnoreCase("authorid"))
				            {			            	
				            	comm.setAuthorId(StringUtils.toInt(xmlParser.nextText(),0));		            	
				            }
				            else if(tag.equalsIgnoreCase("content"))
				            {			            	
				            	comm.setContent(xmlParser.nextText());
				            }
				            else if(tag.equalsIgnoreCase("pubDate"))
				            {			            	
				            	comm.setPubDate(xmlParser.nextText());		            	
				            }
				            else if(tag.equalsIgnoreCase("appclient"))
				            {			            	
				            	comm.setAppClient(StringUtils.toInt(xmlParser.nextText(),0));			            	
				            }
				            /*
				            else if(tag.equalsIgnoreCase("reply"))
				            {			            	
				            	reply = new Reply();         	
				            }
				            else if(reply!=null && tag.equalsIgnoreCase("rauthor"))
				            {
				            	reply.rauthor = xmlParser.nextText();
				            }
				            else if(reply!=null && tag.equalsIgnoreCase("rpubDate"))
				            {
				            	reply.rpubDate = xmlParser.nextText();
				            }
				            else if(reply!=null && tag.equalsIgnoreCase("rcontent"))
				            {
				            	reply.rcontent = xmlParser.nextText();
				            }
				            else if(tag.equalsIgnoreCase("refer"))
				            {			            	
				            	refer = new Refer();         	
				            }
				            else if(refer!=null && tag.equalsIgnoreCase("refertitle"))
				            {
				            	refer.refertitle = xmlParser.nextText();
				            }
				            else if(refer!=null && tag.equalsIgnoreCase("referbody"))
				            {
				            	refer.referbody = xmlParser.nextText();
				            }
				            */
			    		}
			    		/*
			            //é€šçŸ¥ä¿¡æ�¯
			            else if(tag.equalsIgnoreCase("notice"))
			    		{
			            	commlist.setNotice(new Notice());
			    		}
			            else if(commlist.getNotice() != null)
			    		{
			    			if(tag.equalsIgnoreCase("atmeCount"))
				            {			      
			    				commlist.getNotice().setAtmeCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
				            else if(tag.equalsIgnoreCase("msgCount"))
				            {			            	
				            	commlist.getNotice().setMsgCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
				            else if(tag.equalsIgnoreCase("reviewCount"))
				            {			            	
				            	commlist.getNotice().setReviewCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
				            else if(tag.equalsIgnoreCase("newFansCount"))
				            {			            	
				            	commlist.getNotice().setNewFansCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
			    		}
			    		*/
			    		break;
			    	case XmlPullParser.END_TAG:	
					   	//å¦‚æžœé�‡åˆ°æ ‡ç­¾ç»“æ�Ÿï¼Œåˆ™æŠŠå¯¹è±¡æ·»åŠ è¿›é›†å�ˆä¸­
				       	if (tag.equalsIgnoreCase("comment") && comm != null) { 
				       		commlist.getCommentlist().add(comm); 
				       		comm = null; 
				       	}
				       	/*
				       	else if (tag.equalsIgnoreCase("reply") && comm!=null && reply!=null) { 
				       		comm.getReplies().add(reply);
				       		reply = null; 
				       	}
				       	else if(tag.equalsIgnoreCase("refer") && comm!=null && refer!=null) {
				       		comm.getRefers().add(refer);
				       		refer = null;
				       	}
				       	*/
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
        return commlist;       
	}
}
