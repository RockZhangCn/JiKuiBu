package com.jikuibu.app.bean;


import java.io.IOException;
import java.io.InputStream;

import com.jikuibu.app.AppException;
import com.jikuibu.app.utils.StringUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * ç™»å½•ç”¨æˆ·å®žä½“ç±»
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class User extends Base {
	
	public final static int	RELATION_ACTION_DELETE = 0x00;//å�–æ¶ˆå…³æ³¨
	public final static int	RELATION_ACTION_ADD = 0x01;//åŠ å…³æ³¨
	
	public final static int	RELATION_TYPE_BOTH = 0x01;//å�Œæ–¹äº’ä¸ºç²‰ä¸�
	public final static int	RELATION_TYPE_FANS_HIM = 0x02;//ä½ å�•æ–¹é�¢å…³æ³¨ä»–
	public final static int	RELATION_TYPE_NULL = 0x03;//äº’ä¸�å…³æ³¨
	public final static int	RELATION_TYPE_FANS_ME = 0x04;//å�ªæœ‰ä»–å…³æ³¨æˆ‘
	
	private int uid;
	private String location;
	private String name;
	private int followers;
	private int fans;
	private int score;
	private String face;
	private String account;
	private String pwd;
	//private Result validate;
	private boolean isRememberMe;
	
	private String jointime;
	private String gender;
	private String devplatform;
	private String expertise;
	private int relation;
	private String latestonline;
	
	
	public boolean isRememberMe() {
		return isRememberMe;
	}
	public void setRememberMe(boolean isRememberMe) {
		this.isRememberMe = isRememberMe;
	}
	public String getJointime() {
		return jointime;
	}
	public void setJointime(String jointime) {
		this.jointime = jointime;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
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
	public int getRelation() {
		return relation;
	}
	public void setRelation(int relation) {
		this.relation = relation;
	}
	public String getLatestonline() {
		return latestonline;
	}
	public void setLatestonline(String latestonline) {
		this.latestonline = latestonline;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFollowers() {
		return followers;
	}
	public void setFollowers(int followers) {
		this.followers = followers;
	}
	public int getFans() {
		return fans;
	}
	public void setFans(int fans) {
		this.fans = fans;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getFace() {
		return face;
	}
	public void setFace(String face) {
		this.face = face;
	}
	/*
	public Result getValidate() {
		return validate;
	}
	public void setValidate(Result validate) {
		this.validate = validate;
	}
	*/
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public static User parse(InputStream stream) throws IOException, AppException {
		User user = new User();
		//Result res = null;
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
					/*
					if (tag.equalsIgnoreCase("result")) {
						res = new Result();
					} else if (tag.equalsIgnoreCase("errorCode")) {
						res.setErrorCode(StringUtils.toInt(xmlParser.nextText(), -1));
					} else if (tag.equalsIgnoreCase("errorMessage")) {
						res.setErrorMessage(xmlParser.nextText().trim());
					} else if (res != null && res.OK()) {
						if(tag.equalsIgnoreCase("uid")){
							user.uid = StringUtils.toInt(xmlParser.nextText(), 0);
						}else if(tag.equalsIgnoreCase("location")){
							user.setLocation(xmlParser.nextText());
						}else if(tag.equalsIgnoreCase("name")){
							user.setName(xmlParser.nextText());
						}else if(tag.equalsIgnoreCase("followers")){
							user.setFollowers(StringUtils.toInt(xmlParser.nextText(), 0));
						}else if(tag.equalsIgnoreCase("fans")){
							user.setFans(StringUtils.toInt(xmlParser.nextText(), 0));
						}else if(tag.equalsIgnoreCase("score")){
							user.setScore(StringUtils.toInt(xmlParser.nextText(), 0));
						}else if(tag.equalsIgnoreCase("portrait")){
							user.setFace(xmlParser.nextText());
						}
			            //é€šçŸ¥ä¿¡æ�¯
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
					}
					*/
					break;
				case XmlPullParser.END_TAG:
					//å¦‚æžœé�‡åˆ°æ ‡ç­¾ç»“æ�Ÿï¼Œåˆ™æŠŠå¯¹è±¡æ·»åŠ è¿›é›†å�ˆä¸­
					/*
			       	if (tag.equalsIgnoreCase("result") && res != null) { 
			       		user.setValidate(res);
			       	}
			       	*/
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
