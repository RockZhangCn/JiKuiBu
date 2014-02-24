package com.jikuibu.app.bean;

import java.io.IOException;
import java.io.InputStream;

import com.jikuibu.app.AppException;
import com.jikuibu.app.bean.Comment.Refer;
import com.jikuibu.app.bean.Comment.Reply;
import com.jikuibu.app.utils.StringUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * æ•°æ�®æ“�ä½œç»“æžœå®žä½“ç±»
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Result extends Base {

	private int errorCode;
	private String errorMessage;
	
	private Comment comment;
	
	public boolean OK() {
		return errorCode == 1;
	}

	/**
	 * è§£æž�è°ƒç”¨ç»“æžœ
	 * 
	 * @param stream
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public static Result parse(InputStream stream) throws IOException, AppException {
		Result res = null;
		Reply reply = null;
		Refer refer = null;        
		// èŽ·å¾—XmlPullParserè§£æž�å™¨
		XmlPullParser xmlParser = Xml.newPullParser();
		try {
			xmlParser.setInput(stream, Base.UTF8);
			// èŽ·å¾—è§£æž�åˆ°çš„äº‹ä»¶ç±»åˆ«ï¼Œè¿™é‡Œæœ‰å¼€å§‹æ–‡æ¡£ï¼Œç»“æ�Ÿæ–‡æ¡£ï¼Œå¼€å§‹æ ‡ç­¾ï¼Œç»“æ�Ÿæ ‡ç­¾ï¼Œæ–‡æœ¬ç­‰ç­‰äº‹ä»¶ã€‚
			int evtType = xmlParser.getEventType();
			// ä¸€ç›´å¾ªçŽ¯ï¼Œç›´åˆ°æ–‡æ¡£ç»“æ�Ÿ
			while (evtType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch (evtType) {

				case XmlPullParser.START_TAG:
					// å¦‚æžœæ˜¯æ ‡ç­¾å¼€å§‹ï¼Œåˆ™è¯´æ˜Žéœ€è¦�å®žä¾‹åŒ–å¯¹è±¡äº†
					if (tag.equalsIgnoreCase("result")) 
					{
						res = new Result();
					} 
					else if (res != null) 
					{ 
						if (tag.equalsIgnoreCase("errorCode")) 
						{
							res.errorCode = StringUtils.toInt(xmlParser.nextText(), -1);
						} 
						else if (tag.equalsIgnoreCase("errorMessage")) 
						{
							res.errorMessage = xmlParser.nextText().trim();
						}
						else if(tag.equalsIgnoreCase("comment"))
			    		{
							res.comment = new Comment();
			    		}
			            else if(res.comment != null)
			    		{
				            if(tag.equalsIgnoreCase("id"))
				            {			      
				            	res.comment.id = StringUtils.toInt(xmlParser.nextText(),0);
				            }
				            else if(tag.equalsIgnoreCase("portrait"))
				            {			            	
				            	res.comment.setFace(xmlParser.nextText());
				            }
				            else if(tag.equalsIgnoreCase("author"))
				            {			            	
				            	res.comment.setAuthor(xmlParser.nextText());		            	
				            }
				            else if(tag.equalsIgnoreCase("authorid"))
				            {			            	
				            	res.comment.setAuthorId(StringUtils.toInt(xmlParser.nextText(),0));		            	
				            }
				            else if(tag.equalsIgnoreCase("content"))
				            {			            	
				            	res.comment.setContent(xmlParser.nextText());
				            }
				            else if(tag.equalsIgnoreCase("pubDate"))
				            {			            	
				            	res.comment.setPubDate(xmlParser.nextText());	
				            }
				            else if(tag.equalsIgnoreCase("appclient"))
				            {			            	
				            	res.comment.setAppClient(StringUtils.toInt(xmlParser.nextText(),0));			            	
				            }
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
			    		}
			            //é€šçŸ¥ä¿¡æ�¯
						/*
			            else if(tag.equalsIgnoreCase("notice"))
			    		{
			            	res.setNotice(new Notice());
			    		}
			            else if(res.getNotice() != null)
			    		{
			    			if(tag.equalsIgnoreCase("atmeCount"))
				            {			      
			    				res.getNotice().setAtmeCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
				            else if(tag.equalsIgnoreCase("msgCount"))
				            {			            	
				            	res.getNotice().setMsgCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
				            else if(tag.equalsIgnoreCase("reviewCount"))
				            {			            	
				            	res.getNotice().setReviewCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
				            else if(tag.equalsIgnoreCase("newFansCount"))
				            {			            	
				            	res.getNotice().setNewFansCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
			    		}
			    		*/
					}
					break;
				case XmlPullParser.END_TAG:
					//å¦‚æžœé�‡åˆ°æ ‡ç­¾ç»“æ�Ÿï¼Œåˆ™æŠŠå¯¹è±¡æ·»åŠ è¿›é›†å�ˆä¸­
			       	if (tag.equalsIgnoreCase("reply") && res.comment!=null && reply!=null) { 
			       		res.comment.getReplies().add(reply);
			       		reply = null; 
			       	}
			       	else if(tag.equalsIgnoreCase("refer") && res.comment!=null && refer!=null) {
			       		res.comment.getRefers().add(refer);
			       		refer = null;
			       	}
					break;
				}
				// å¦‚æžœxmlæ²¡æœ‰ç»“æ�Ÿï¼Œåˆ™å¯¼èˆªåˆ°ä¸‹ä¸€ä¸ªèŠ‚ç‚¹
				evtType = xmlParser.next();
			}

		} catch (XmlPullParserException e) {
			throw AppException.xml(e);
		} finally {
			stream.close();
		}

		return res;

	}

	public int getErrorCode() {
		return errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}

	@Override
	public String toString(){
		return String.format("RESULT: CODE:%d,MSG:%s", errorCode, errorMessage);
	}

}
