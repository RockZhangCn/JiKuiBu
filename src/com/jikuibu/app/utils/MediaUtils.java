package com.jikuibu.app.utils;

import java.util.HashMap;
import java.util.Map;

/** 
 * åª’ä½“ç±»åž‹å·¥å…·åŒ…
 * @author  @Cundong
 * @weibo   http://weibo.com/liucundong
 * @blog    http://www.liucundong.com
 * @date    Apr 29, 2011 2:50:48 PM
 * @version 1.0
 */
public class MediaUtils
{
	private static Map<String, String> FORMAT_TO_CONTENTTYPE = new HashMap<String, String>();
	
	static
	{
		//éŸ³é¢‘
		FORMAT_TO_CONTENTTYPE.put( "mp3", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "mid", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "midi", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "asf", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "wm", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "wma", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "wmd", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "amr", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "wav", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "3gpp", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "mod", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "mpc", "audio" );
		
		//è§†é¢‘
		FORMAT_TO_CONTENTTYPE.put( "fla", "video" );
		FORMAT_TO_CONTENTTYPE.put( "flv", "video" );
		FORMAT_TO_CONTENTTYPE.put( "wav", "video" );
		FORMAT_TO_CONTENTTYPE.put( "wmv", "video" );
		FORMAT_TO_CONTENTTYPE.put( "avi", "video" );
		FORMAT_TO_CONTENTTYPE.put( "rm", "video" );
		FORMAT_TO_CONTENTTYPE.put( "rmvb", "video" );
		FORMAT_TO_CONTENTTYPE.put( "3gp", "video" );
		FORMAT_TO_CONTENTTYPE.put( "mp4", "video" );
		FORMAT_TO_CONTENTTYPE.put( "mov", "video" );
		
		//flash
		FORMAT_TO_CONTENTTYPE.put( "swf", "video" );
		
		FORMAT_TO_CONTENTTYPE.put( "null", "video" );
		
		//å›¾ç‰‡
		FORMAT_TO_CONTENTTYPE.put( "jpg", "photo" );
		FORMAT_TO_CONTENTTYPE.put( "jpeg", "photo" );
		FORMAT_TO_CONTENTTYPE.put( "png", "photo" );
		FORMAT_TO_CONTENTTYPE.put( "bmp", "photo" );
		FORMAT_TO_CONTENTTYPE.put( "gif", "photo" );
	}
	
	/**
	 * æ ¹æ�®æ ¹æ�®æ‰©å±•å��èŽ·å�–ç±»åž‹
	 * @param attFormat
	 * @return
	 */
	public static String getContentType( String attFormat )
	{
		String contentType = FORMAT_TO_CONTENTTYPE.get("null");
		
		if ( attFormat != null ) 
		{
			contentType = (String)FORMAT_TO_CONTENTTYPE.get( attFormat.toLowerCase() );
		}
		return contentType;
	}
	
	/**
	 * åˆ¤æ–­æ–‡ä»¶MimeTypeçš„method
	 * @param f
	 * @return
	 */
    public static String getMIMEType(String filePath)
    {
        String type = "";
        String fName = FileUtils.getFileName(filePath);
        /* å�–å¾—æ‰©å±•å�� */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        
        /* æŒ‰æ‰©å±•å��çš„ç±»åž‹å†³å®šMimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg")
            || end.equals("wav"))
        {
            type = "audio";
        }
        else if (end.equals("3gp") || end.equals("mp4"))
        {
            type = "video";
        }
        else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp"))
        {
            type = "image";
        }
        else if(end.equals("doc") || end.equals("docx"))
        {
            type = "application/msword";
        }
        else if(end.equals("xls"))
        {
            type = "application/vnd.ms-excel";
        }
        else if(end.equals("ppt") || end.equals("pptx") || end.equals("pps") || end.equals("dps"))
        {
            type = "application/vnd.ms-powerpoint";
        }
        else
        {
            type = "*";
        }
        /* å¦‚æžœæ— æ³•ç›´æŽ¥æ‰“å¼€ï¼Œå°±å¼¹å‡ºè½¯ä»¶åˆ—è¡¨ç»™ç”¨æˆ·é€‰æ‹© */
        type += "/*";
        return type;
    }

}