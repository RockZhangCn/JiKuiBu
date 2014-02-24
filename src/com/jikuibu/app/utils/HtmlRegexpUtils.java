package com.jikuibu.app.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title: HTMLç›¸å…³çš„æ­£åˆ™è¡¨è¾¾å¼�å·¥å…·ç±»
 * Description: åŒ…æ‹¬è¿‡æ»¤HTMLæ ‡è®°ï¼Œè½¬æ�¢HTMLæ ‡è®°ï¼Œæ›¿æ�¢ç‰¹å®šHTMLæ ‡è®°
 * Copyright: Copyright (c) 2006
 * @author hejian
 * @version 1.0
 * @createtime 2006-10-16
 */
public class HtmlRegexpUtils {
	private final static String regxpForHtml = "<([^>]*)>"; // è¿‡æ»¤æ‰€æœ‰ä»¥<å¼€å¤´ä»¥>ç»“å°¾çš„æ ‡ç­¾

	private final static String regxpForImgTag = "<\\s*img\\s+([^>]*)\\s*>"; // æ‰¾å‡ºIMGæ ‡ç­¾

	private final static String regxpForImaTagSrcAttrib = "src=\"([^\"]+)\""; // æ‰¾å‡ºIMGæ ‡ç­¾çš„SRCå±žæ€§

	public HtmlRegexpUtils() {}

	/**
	 * åŸºæœ¬åŠŸèƒ½ï¼šæ›¿æ�¢æ ‡è®°ä»¥æ­£å¸¸æ˜¾ç¤º
	 * @param input
	 * @return String
	 */
	public String replaceTag(String input) {
		if (!hasSpecialChars(input)) {
			return input;
		}
		StringBuffer filtered = new StringBuffer(input.length());
		char c;
		for (int i = 0; i <= input.length() - 1; i++) {
			c = input.charAt(i);
			switch (c) {
			case '<':
				filtered.append("&lt;");
				break;
			case '>':
				filtered.append("&gt;");
				break;
			case '"':
				filtered.append("&quot;");
				break;
			case '&':
				filtered.append("&amp;");
				break;
			default:
				filtered.append(c);
			}

		}
		return (filtered.toString());
	}

	/**
	 * åŸºæœ¬åŠŸèƒ½ï¼šåˆ¤æ–­æ ‡è®°æ˜¯å�¦å­˜åœ¨
	 * @param input
	 * @return boolean
	 */
	public boolean hasSpecialChars(String input) {
		boolean flag = false;
		if ((input != null) && (input.length() > 0)) {
			char c;
			for (int i = 0; i <= input.length() - 1; i++) {
				c = input.charAt(i);
				switch (c) {
				case '>':
					flag = true;
					break;
				case '<':
					flag = true;
					break;
				case '"':
					flag = true;
					break;
				case '&':
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * åŸºæœ¬åŠŸèƒ½ï¼šè¿‡æ»¤æ‰€æœ‰ä»¥"<"å¼€å¤´ä»¥">"ç»“å°¾çš„æ ‡ç­¾
	 * @param str
	 * @return String
	 */
	public static String filterHtml(String str) {
		Pattern pattern = Pattern.compile(regxpForHtml);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * åŸºæœ¬åŠŸèƒ½ï¼šè¿‡æ»¤æŒ‡å®šæ ‡ç­¾
	 * @param str
	 * @param tag æŒ‡å®šæ ‡ç­¾
	 * @return String
	 */
	public static String fiterHtmlTag(String str, String tag) {
		String regxp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";
		Pattern pattern = Pattern.compile(regxp);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * åŸºæœ¬åŠŸèƒ½ï¼šæ›¿æ�¢æŒ‡å®šçš„æ ‡ç­¾
	 * @param str
	 * @param beforeTag è¦�æ›¿æ�¢çš„æ ‡ç­¾
	 * @param tagAttrib è¦�æ›¿æ�¢çš„æ ‡ç­¾å±žæ€§å€¼
	 * @param startTag æ–°æ ‡ç­¾å¼€å§‹æ ‡è®°
	 * @param endTag æ–°æ ‡ç­¾ç»“æ�Ÿæ ‡è®°
	 * @return String
	 * @å¦‚ï¼šæ›¿æ�¢imgæ ‡ç­¾çš„srcå±žæ€§å€¼ä¸º[img]å±žæ€§å€¼[/img]
	 */
	public static String replaceHtmlTag(String str, String beforeTag,
			String tagAttrib, String startTag, String endTag) {
		String regxpForTag = "<\\s*" + beforeTag + "\\s+([^>]*)\\s*>";
		String regxpForTagAttrib = tagAttrib + "=\"([^\"]+)\"";
		Pattern patternForTag = Pattern.compile(regxpForTag);
		Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib);
		Matcher matcherForTag = patternForTag.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result = matcherForTag.find();
		while (result) {
			StringBuffer sbreplace = new StringBuffer();
			Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag
					.group(1));
			if (matcherForAttrib.find()) {
				matcherForAttrib.appendReplacement(sbreplace, startTag
						+ matcherForAttrib.group(1) + endTag);
			}
			matcherForTag.appendReplacement(sb, sbreplace.toString());
			result = matcherForTag.find();
		}
		matcherForTag.appendTail(sb);
		return sb.toString();
	}
}
