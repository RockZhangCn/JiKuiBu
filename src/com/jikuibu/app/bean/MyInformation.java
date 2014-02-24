package com.jikuibu.app.bean;

import java.io.IOException;
import java.io.InputStream;

import com.jikuibu.app.AppException;
import com.jikuibu.app.utils.StringUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * æˆ‘çš„ä¸ªäººä¿¡æ�¯å®žä½“ç±»
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class MyInformation extends Entity{

	private String name;
	private String face;
	private String jointime;
	private int gender;
	private String from;
	private String devplatform;
	private String expertise;
	private int favoritecount;
	private int fanscount;
	private int followerscount;
	
	public String getJointime() {
		return jointime;
	}
	public void setJointime(String jointime) {
		this.jointime = jointime;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getDevplatform() {
		return devplatform;
	}
	public void setDevplatform(String devplatform) {
		this.devplatform = devplatform;
	}
	public String getExpertise() {
		return expertise;
	}
	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFace() {
		return face;
	}
	public void setFace(String face) {
		this.face = face;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public int getFavoritecount() {
		return favoritecount;
	}
	public void setFavoritecount(int favoritecount) {
		this.favoritecount = favoritecount;
	}
	public int getFanscount() {
		return fanscount;
	}
	public void setFanscount(int fanscount) {
		this.fanscount = fanscount;
	}
	public int getFollowerscount() {
		return followerscount;
	}
	public void setFollowerscount(int followerscount) {
		this.followerscount = followerscount;
	}
	
	public static MyInformation parse(InputStream stream) throws IOException, AppException {
		MyInformation user = null;
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
					if(tag.equalsIgnoreCase("user")){
						user = new MyInformation();
					}else if(user != null) {
						if(tag.equalsIgnoreCase("name")){
							user.setName(xmlParser.nextText());
						}else if(tag.equalsIgnoreCase("portrait")){
							user.setFace(xmlParser.nextText());
						}else if(tag.equalsIgnoreCase("jointime")){
							user.setJointime(xmlParser.nextText());
						}else if(tag.equalsIgnoreCase("gender")){
							user.setGender(StringUtils.toInt(xmlParser.nextText(), 0));
						}else if(tag.equalsIgnoreCase("from")){
							user.setFrom(xmlParser.nextText());
						}else if(tag.equalsIgnoreCase("devplatform")){
							user.setDevplatform(xmlParser.nextText());
						}else if(tag.equalsIgnoreCase("expertise")){
							user.setExpertise(xmlParser.nextText());
						}else if(tag.equalsIgnoreCase("favoritecount")){
							user.setFavoritecount(StringUtils.toInt(xmlParser.nextText(), 0));
						}else if(tag.equalsIgnoreCase("fanscount")){
							user.setFanscount(StringUtils.toInt(xmlParser.nextText(), 0));
						}else if(tag.equalsIgnoreCase("followerscount")){
							user.setFollowerscount(StringUtils.toInt(xmlParser.nextText(), 0));
						}
			            //é€šçŸ¥ä¿¡æ�¯
						/*
			            else if(tag.equalsIgnoreCase("notice"))
			    		{
			            	user.setNotice(new Notice());
			    		}
						
			            else if(user.getNotice() != null)
			    		{
			    			if(tag.equalsIgnoreCase("atmeCount"))
				            {			      
			    				user.getNotice().setAtmeCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
				            else if(tag.equalsIgnoreCase("msgCount"))
				            {			            	
				            	user.getNotice().setMsgCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
				            else if(tag.equalsIgnoreCase("reviewCount"))
				            {			            	
				            	user.getNotice().setReviewCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
				            else if(tag.equalsIgnoreCase("newFansCount"))
				            {			            	
				            	user.getNotice().setNewFansCount(StringUtils.toInt(xmlParser.nextText(),0));
				            }
			    		}
			    		*/
					}
					break;
				case XmlPullParser.END_TAG:
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
		return user;
	}
}
