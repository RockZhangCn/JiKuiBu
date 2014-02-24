package com.jikuibu.app.bean;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.jikuibu.app.AppException;
//import com.jikuibu.app.bean.Active.ObjectReply;
import com.jikuibu.app.utils.StringUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * ç”¨æˆ·ä¸“é¡µä¿¡æ�¯å®žä½“ç±»
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class UserInformation extends Entity{
	
	private int pageSize;
	private User user = new User();
	//private List<Active> activelist = new ArrayList<Active>();	

	public int getPageSize() {
		return pageSize;
	}
	public User getUser() {
		return user;
	}
	/*
	public List<Active> getActivelist() {
		return activelist;
	}
	*/

	public static UserInformation parse(InputStream inputStream) throws IOException, AppException {
		UserInformation uinfo = new UserInformation();
		User user = null;
		//Active active = null;
        //èŽ·å¾—XmlPullParserè§£æž�å™¨
        XmlPullParser xmlParser = Xml.newPullParser();
        try {        	
            xmlParser.setInput(inputStream, UTF8);
            //èŽ·å¾—è§£æž�åˆ°çš„äº‹ä»¶ç±»åˆ«ï¼Œè¿™é‡Œæœ‰å¼€å§‹æ–‡æ¡£ï¼Œç»“æ�Ÿæ–‡æ¡£ï¼Œå¼€å§‹æ ‡ç­¾ï¼Œç»“æ�Ÿæ ‡ç­¾ï¼Œæ–‡æœ¬ç­‰ç­‰äº‹ä»¶ã€‚
            int evtType=xmlParser.getEventType();
			//ä¸€ç›´å¾ªçŽ¯ï¼Œç›´åˆ°æ–‡æ¡£ç»“æ�Ÿ    
			while(evtType!=XmlPullParser.END_DOCUMENT){ 
				String tag = xmlParser.getName(); 
	    		int depth = xmlParser.getDepth();
			    switch(evtType){ 
			    	case XmlPullParser.START_TAG:
			    		if(tag.equalsIgnoreCase("user")) 
			    		{
			    			user = new User();
			    		}
			    		else if(tag.equalsIgnoreCase("pageSize")) 
			    		{
			    			uinfo.pageSize = StringUtils.toInt(xmlParser.nextText(),0);
			    		}
			    		/*
			    		else if (tag.equalsIgnoreCase("active")) 
			    		{
			    			active = new Active();
			    		}
			    		*/
			    		else if (user != null)
			    		{
			    			if(tag.equalsIgnoreCase("uid")){
								user.setUid(StringUtils.toInt(xmlParser.nextText(), 0));
							}else if(tag.equalsIgnoreCase("from")){
								user.setLocation(xmlParser.nextText());
							}else if(tag.equalsIgnoreCase("name")){
								user.setName(xmlParser.nextText());
							}else if(depth==3 && tag.equalsIgnoreCase("portrait")){
								user.setFace(xmlParser.nextText());
							}else if(tag.equalsIgnoreCase("jointime")){
								user.setJointime(xmlParser.nextText());
							}else if(tag.equalsIgnoreCase("gender")){
								user.setGender(xmlParser.nextText());
							}else if(tag.equalsIgnoreCase("devplatform")){
								user.setDevplatform(xmlParser.nextText());
							}else if(tag.equalsIgnoreCase("expertise")){
								user.setExpertise(xmlParser.nextText());
							}else if(tag.equalsIgnoreCase("relation")){
								user.setRelation(StringUtils.toInt(xmlParser.nextText(), 0));
							}else if(tag.equalsIgnoreCase("latestonline")){
								user.setLatestonline(xmlParser.nextText());
							}
			    		}
			    		/*
			    		else if (active != null)
			    		{	
				            if(tag.equalsIgnoreCase("id"))
				            {			      
				            	active.id = StringUtils.toInt(xmlParser.nextText(),0);
				            }
				            else if(depth==4 && tag.equalsIgnoreCase("portrait"))
				            {			            	
				            	active.setFace(xmlParser.nextText());
				            }
				            else if(tag.equalsIgnoreCase("message"))
				            {	
				            	active.setMessage(xmlParser.nextText());		            	
				            }
				            else if(tag.equalsIgnoreCase("author"))
				            {
				            	active.setAuthor(xmlParser.nextText());		            	
				            }
				            else if(tag.equalsIgnoreCase("authorid"))
				            {			            	
				            	active.setAuthorId(StringUtils.toInt(xmlParser.nextText(),0));		            	
				            }
				            else if(tag.equalsIgnoreCase("catalog"))
				            {			            	
				            	active.setActiveType(StringUtils.toInt(xmlParser.nextText(),0));			            	
				            }
				            else if(tag.equalsIgnoreCase("objectID"))
				            {			            	
				            	active.setObjectId(StringUtils.toInt(xmlParser.nextText(),0));			            	
				            }
				            else if(tag.equalsIgnoreCase("objecttype"))
				            {			            	
				            	active.setObjectType(StringUtils.toInt(xmlParser.nextText(),0));			            	
				            }
				            else if(tag.equalsIgnoreCase("objectcatalog"))
				            {			            	
				            	active.setObjectCatalog(StringUtils.toInt(xmlParser.nextText(),0));			            	
				            }
				            else if(tag.equalsIgnoreCase("objecttitle"))
				            {			            	
				            	active.setObjectTitle(xmlParser.nextText());			            	
				            }
				            else if(tag.equalsIgnoreCase("objectreply"))
				            {			            	
				            	active.setObjectReply(new ObjectReply());	            	
				            }
				            else if(active.getObjectReply()!=null && tag.equalsIgnoreCase("objectname"))
				            {			            	
				            	active.getObjectReply().objectName = xmlParser.nextText();		            	
				            }
				            else if(active.getObjectReply()!=null && tag.equalsIgnoreCase("objectbody"))
				            {			            	
				            	active.getObjectReply().objectBody = xmlParser.nextText();		            	
				            }
				            else if(tag.equalsIgnoreCase("commentCount"))
				            {			            	
				            	active.setCommentCount(StringUtils.toInt(xmlParser.nextText(),0));			            	
				            }
				            else if(tag.equalsIgnoreCase("pubDate"))
				            {			            	
				            	active.setPubDate(xmlParser.nextText());	            	
				            }
				            else if(tag.equalsIgnoreCase("tweetimage"))
				            {			            	
				            	active.setTweetimage(xmlParser.nextText());			            	
				            }
				            else if(tag.equalsIgnoreCase("appclient"))
				            {			            	
				            	active.setAppClient(StringUtils.toInt(xmlParser.nextText(),0));			            	
				            }
				            else if(tag.equalsIgnoreCase("url"))
				            {			            	
				            	active.setUrl(xmlParser.nextText());			            	
				            }
				          
			    		}  
			            //é€šçŸ¥ä¿¡æ�¯
			            else if(tag.equalsIgnoreCase("notice"))
			    		{
			            	uinfo.setNotice(new Notice());
			    		}
			            else if(uinfo.getNotice() != null)
			    		{
			    			if(tag.equalsIgnoreCase("atmeCount"))
				            {			      
			    				uinfo.getNotice().setAtmeCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
				            else if(tag.equalsIgnoreCase("msgCount"))
				            {			            	
				            	uinfo.getNotice().setMsgCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
				            else if(tag.equalsIgnoreCase("reviewCount"))
				            {			            	
				            	uinfo.getNotice().setReviewCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
				            else if(tag.equalsIgnoreCase("newFansCount"))
				            {			            	
				            	uinfo.getNotice().setNewFansCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
			    		}
			    		  */
			    		break;
			    	case XmlPullParser.END_TAG:	
					   	//å¦‚æžœé�‡åˆ°æ ‡ç­¾ç»“æ�Ÿï¼Œåˆ™æŠŠå¯¹è±¡æ·»åŠ è¿›é›†å�ˆä¸­
			    		if (tag.equalsIgnoreCase("user") && user != null) {
			    			uinfo.user = user;
			    			user = null;
			    		}
			    		/*
			    		else if (tag.equalsIgnoreCase("active") && active != null) { 
				       		uinfo.getActivelist().add(active); 
				       		active = null; 
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
        return uinfo;       
	}
}
