package com.jikuibu.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/** 
 * å­—ç¬¦ä¸²æ“�ä½œå·¥å…·åŒ…
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class StringUtils 
{
	private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	//private final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
	
	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	
	/**
	 * å°†å­—ç¬¦ä¸²è½¬ä½�æ—¥æœŸç±»åž‹
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * ä»¥å�‹å¥½çš„æ–¹å¼�æ˜¾ç¤ºæ—¶é—´
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if(time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();
		
		//åˆ¤æ–­æ˜¯å�¦æ˜¯å�Œä¸€å¤©
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if(curDate.equals(paramDate)){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"åˆ†é’Ÿå‰�";
			else 
				ftime = hour+"å°�æ—¶å‰�";
			return ftime;
		}
		
		long lt = time.getTime()/86400000;
		long ct = cal.getTimeInMillis()/86400000;
		int days = (int)(ct - lt);		
		if(days == 0){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"åˆ†é’Ÿå‰�";
			else 
				ftime = hour+"å°�æ—¶å‰�";
		}
		else if(days == 1){
			ftime = "æ˜¨å¤©";
		}
		else if(days == 2){
			ftime = "å‰�å¤©";
		}
		else if(days > 2 && days <= 10){ 
			ftime = days+"å¤©å‰�";			
		}
		else if(days > 10){			
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}
	
	/**
	 * åˆ¤æ–­ç»™å®šå­—ç¬¦ä¸²æ—¶é—´æ˜¯å�¦ä¸ºä»Šæ—¥
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate){
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if(time != null){
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if(nowDate.equals(timeDate)){
				b = true;
			}
		}
		return b;
	}
	
	/**
	 * åˆ¤æ–­ç»™å®šå­—ç¬¦ä¸²æ˜¯å�¦ç©ºç™½ä¸²ã€‚
	 * ç©ºç™½ä¸²æ˜¯æŒ‡ç”±ç©ºæ ¼ã€�åˆ¶è¡¨ç¬¦ã€�å›žè½¦ç¬¦ã€�æ�¢è¡Œç¬¦ç»„æˆ�çš„å­—ç¬¦ä¸²
	 * è‹¥è¾“å…¥å­—ç¬¦ä¸²ä¸ºnullæˆ–ç©ºå­—ç¬¦ä¸²ï¼Œè¿”å›žtrue
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty( String input ) 
	{
		if ( input == null || "".equals( input ) )
			return true;
		
		for ( int i = 0; i < input.length(); i++ ) 
		{
			char c = input.charAt( i );
			if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * åˆ¤æ–­æ˜¯ä¸�æ˜¯ä¸€ä¸ªå�ˆæ³•çš„ç”µå­�é‚®ä»¶åœ°å�€
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email){
		if(email == null || email.trim().length()==0) 
			return false;
	    return emailer.matcher(email).matches();
	}
	/**
	 * å­—ç¬¦ä¸²è½¬æ•´æ•°
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try{
			return Integer.parseInt(str);
		}catch(Exception e){}
		return defValue;
	}
	/**
	 * å¯¹è±¡è½¬æ•´æ•°
	 * @param obj
	 * @return è½¬æ�¢å¼‚å¸¸è¿”å›ž 0
	 */
	public static int toInt(Object obj) {
		if(obj==null) return 0;
		return toInt(obj.toString(),0);
	}
	/**
	 * å¯¹è±¡è½¬æ•´æ•°
	 * @param obj
	 * @return è½¬æ�¢å¼‚å¸¸è¿”å›ž 0
	 */
	public static long toLong(String obj) {
		try{
			return Long.parseLong(obj);
		}catch(Exception e){}
		return 0;
	}
	/**
	 * å­—ç¬¦ä¸²è½¬å¸ƒå°”å€¼
	 * @param b
	 * @return è½¬æ�¢å¼‚å¸¸è¿”å›ž false
	 */
	public static boolean toBool(String b) {
		try{
			return Boolean.parseBoolean(b);
		}catch(Exception e){}
		return false;
	}
}
