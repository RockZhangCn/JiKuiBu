package com.jikuibu.app.utils;

//import greendroid.widget.MyQuickAction;
//import greendroid.widget.QuickAction;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jikuibu.app.AppConfig;
import com.jikuibu.app.AppContext;
import com.jikuibu.app.AppException;
import com.jikuibu.app.AppManager;
import com.jikuibu.app.R;

import com.jikuibu.app.api.ApiClient;


import com.jikuibu.app.bean.Comment;

import com.jikuibu.app.ui.MainActivity;


import com.jikuibu.app.bean.Result;

import com.jikuibu.app.bean.URLs;
/*
import net.oschina.app.ui.About;
import net.oschina.app.ui.BaseActivity;
import net.oschina.app.ui.BlogDetail;
import net.oschina.app.ui.CommentPub;
import net.oschina.app.ui.FeedBack;
import net.oschina.app.ui.ImageDialog;
import net.oschina.app.ui.ImageZoomDialog;
import net.oschina.app.ui.LoginDialog;
import net.oschina.app.ui.Main;
import net.oschina.app.ui.MessageDetail;
import net.oschina.app.ui.MessageForward;
import net.oschina.app.ui.MessagePub;
import net.oschina.app.ui.NewsDetail;
import net.oschina.app.ui.QuestionDetail;
import net.oschina.app.ui.QuestionPub;
import net.oschina.app.ui.QuestionTag;
import net.oschina.app.ui.ScreenShotShare;
import net.oschina.app.ui.Search;
import net.oschina.app.ui.Setting;
import net.oschina.app.ui.SoftwareDetail;
import net.oschina.app.ui.SoftwareLib;
import net.oschina.app.ui.TweetDetail;
import net.oschina.app.ui.TweetPub;
import net.oschina.app.ui.UserCenter;
import net.oschina.app.ui.UserFavorite;
import net.oschina.app.ui.UserFriend;
import net.oschina.app.ui.UserInfo;
import net.oschina.app.widget.LinkView;
import net.oschina.app.widget.PathChooseDialog;
import net.oschina.app.widget.LinkView.MyURLSpan;
import net.oschina.app.widget.PathChooseDialog.ChooseCompleteListener;
import net.oschina.app.widget.ScreenShotView;
import net.oschina.app.widget.ScreenShotView.OnScreenShotListener;
*/
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * åº”ç”¨ç¨‹åº�UIå·¥å…·åŒ…ï¼šå°�è£…UIç›¸å…³çš„ä¸€äº›æ“�ä½œ
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class UIHelper {

	public final static int LISTVIEW_ACTION_INIT = 0x01;
	public final static int LISTVIEW_ACTION_REFRESH = 0x02;
	public final static int LISTVIEW_ACTION_SCROLL = 0x03;
	public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 0x04;

	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;

	public final static int LISTVIEW_DATATYPE_NEWS = 0x01;
	public final static int LISTVIEW_DATATYPE_BLOG = 0x02;
	public final static int LISTVIEW_DATATYPE_POST = 0x03;
	public final static int LISTVIEW_DATATYPE_TWEET = 0x04;
	public final static int LISTVIEW_DATATYPE_ACTIVE = 0x05;
	public final static int LISTVIEW_DATATYPE_MESSAGE = 0x06;
	public final static int LISTVIEW_DATATYPE_COMMENT = 0x07;

	public final static int REQUEST_CODE_FOR_RESULT = 0x01;
	public final static int REQUEST_CODE_FOR_REPLY = 0x02;

	/** è¡¨æƒ…å›¾ç‰‡åŒ¹é…� */
	private static Pattern facePattern = Pattern
			.compile("\\[{1}([0-9]\\d*)\\]{1}");

	/** å…¨å±€webæ ·å¼� */
	public final static String WEB_STYLE = "<style>* {font-size:16px;line-height:20px;} p {color:#333;} a {color:#3E62A6;} img {max-width:310px;} "
			+ "img.alignleft {float:left;max-width:120px;margin:0 10px 5px 0;border:1px solid #ccc;background:#fff;padding:2px;} "
			+ "pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;} "
			+ "a.tag {font-size:15px;text-decoration:none;background-color:#bbd6f3;border-bottom:2px solid #3E6D8E;border-right:2px solid #7F9FB6;color:#284a7b;margin:2px 2px 2px 0;padding:2px 4px;white-space:nowrap;}</style>";

	/**
	 * æ˜¾ç¤ºé¦–é¡µ
	 * 
	 * @param activity
	 */
	public static void showHome(Activity activity) {
		Intent intent = new Intent(activity, MainActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}

	/**
	 * æ˜¾ç¤ºç™»å½•é¡µé�¢
	 * 
	 * @param activity
	 */
	public static void showLoginDialog(Context context) {
		/*
		Intent intent = new Intent(context, LoginDialog.class);
		if (context instanceof Main)
			intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_MAIN);
		else if (context instanceof Setting)
			intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_SETTING);
		else
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		*/
	}


	/**
	 * æ˜¾ç¤ºå¸–å­�è¯¦æƒ…
	 * 
	 * @param context
	 * @param postId
	 */
	public static void showQuestionDetail(Context context, int postId) {
		/*
		Intent intent = new Intent(context, QuestionDetail.class);
		intent.putExtra("post_id", postId);
		context.startActivity(intent);
		*/
	}

	
	/**
	 * æ˜¾ç¤ºå�šå®¢è¯¦æƒ…
	 * 
	 * @param context
	 * @param blogId
	 */
	public static void showBlogDetail(Context context, int blogId) {
		/*
		Intent intent = new Intent(context, BlogDetail.class);
		intent.putExtra("blog_id", blogId);
		context.startActivity(intent);
		*/
	}

	

	public static void showBlogOptionDialog(final Context context,
			final Thread thread) {
		new AlertDialog.Builder(context)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(context.getString(R.string.delete_blog))
				.setPositiveButton(R.string.sure,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (thread != null)
									thread.start();
								else
									ToastMessage(context,
											R.string.msg_noaccess_delete);
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.cancle,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create().show();
	}

	/**
	 * åŠ¨å¼¹æ“�ä½œé€‰æ‹©æ¡†
	 * 
	 * @param context
	 * @param thread
	 */
	public static void showTweetOptionDialog(final Context context,
			final Thread thread) {
		new AlertDialog.Builder(context)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(context.getString(R.string.delete_tweet))
				.setPositiveButton(R.string.sure,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (thread != null)
									thread.start();
								else
									ToastMessage(context,
											R.string.msg_noaccess_delete);
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.cancle,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create().show();
	}


	/*	
	public static void showSearch(Context context) {
		Intent intent = new Intent(context, Search.class);
		context.startActivity(intent);
	}

	
	public static void showSoftware(Context context) {
		Intent intent = new Intent(context, SoftwareLib.class);
		context.startActivity(intent);
	}

	
	public static void showUserInfo(Activity context) {
		AppContext ac = (AppContext) context.getApplicationContext();
		if (!ac.isLogin()) {
			showLoginDialog(context);
		} else {
			Intent intent = new Intent(context, UserInfo.class);
			context.startActivity(intent);
		}
	}


	public static void showFilePathDialog(Activity context,
			ChooseCompleteListener listener) {
		new PathChooseDialog(context, listener).show();
	}

	
	public static void showUserCenter(Context context, int hisuid,
			String hisname) {
		Intent intent = new Intent(context, UserCenter.class);
		intent.putExtra("his_id", hisuid);
		intent.putExtra("his_name", hisname);
		context.startActivity(intent);
	}

	
	public static void showUserFavorite(Context context) {
		Intent intent = new Intent(context, UserFavorite.class);
		context.startActivity(intent);
	}

	
	public static void showUserFriend(Context context, int friendType,
			int followers, int fans) {
		Intent intent = new Intent(context, UserFriend.class);
		intent.putExtra("friend_type", friendType);
		intent.putExtra("friend_followers", followers);
		intent.putExtra("friend_fans", fans);
		context.startActivity(intent);
	}

	
	public static void showUserFace(final ImageView imgFace,
			final String faceURL) {
		showLoadImage(imgFace, faceURL,
				imgFace.getContext().getString(R.string.msg_load_userface_fail));
	}

	
	public static void showLoadImage(final ImageView imgView,
			final String imgURL, final String errMsg) {
		// è¯»å�–æœ¬åœ°å›¾ç‰‡
		if (StringUtils.isEmpty(imgURL) || imgURL.endsWith("portrait.gif")) {
			Bitmap bmp = BitmapFactory.decodeResource(imgView.getResources(),
					R.drawable.widget_dface);
			imgView.setImageBitmap(bmp);
			return;
		}

		// æ˜¯å�¦æœ‰ç¼“å­˜å›¾ç‰‡
		final String filename = FileUtils.getFileName(imgURL);
		// Environment.getExternalStorageDirectory();è¿”å›ž/sdcard
		String filepath = imgView.getContext().getFilesDir() + File.separator
				+ filename;
		File file = new File(filepath);
		if (file.exists()) {
			Bitmap bmp = ImageUtils.getBitmap(imgView.getContext(), filename);
			imgView.setImageBitmap(bmp);
			return;
		}

		// ä»Žç½‘ç»œèŽ·å�–&å†™å…¥å›¾ç‰‡ç¼“å­˜
		String _errMsg = imgView.getContext().getString(
				R.string.msg_load_image_fail);
		if (!StringUtils.isEmpty(errMsg))
			_errMsg = errMsg;
		final String ErrMsg = _errMsg;
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1 && msg.obj != null) {
					imgView.setImageBitmap((Bitmap) msg.obj);
					try {
						// å†™å›¾ç‰‡ç¼“å­˜
						ImageUtils.saveImage(imgView.getContext(), filename,
								(Bitmap) msg.obj);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					ToastMessage(imgView.getContext(), ErrMsg);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					Bitmap bmp = ApiClient.getNetBitmap(imgURL);
					msg.what = 1;
					msg.obj = bmp;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
*/
	/**
	 * urlè·³è½¬
	 * 
	 * @param context
	 * @param url
	 */
	public static void showUrlRedirect(Context context, String url) {
		URLs urls = URLs.parseURL(url);
		if (urls != null) {
		//	showLinkRedirect(context, urls.getObjType(), urls.getObjId(),
		//			urls.getObjKey());
		} else {
			openBrowser(context, url);
		}
	}
/*
	public static void showLinkRedirect(Context context, int objType,
			int objId, String objKey) {
		switch (objType) {
		case URLs.URL_OBJ_TYPE_NEWS:
			showNewsDetail(context, objId);
			break;
		case URLs.URL_OBJ_TYPE_QUESTION:
			showQuestionDetail(context, objId);
			break;
		case URLs.URL_OBJ_TYPE_QUESTION_TAG:
			showQuestionListByTag(context, objKey);
			break;
		case URLs.URL_OBJ_TYPE_SOFTWARE:
			showSoftwareDetail(context, objKey);
			break;
		case URLs.URL_OBJ_TYPE_ZONE:
			showUserCenter(context, objId, objKey);
			break;
		case URLs.URL_OBJ_TYPE_TWEET:
			showTweetDetail(context, objId);
			break;
		case URLs.URL_OBJ_TYPE_BLOG:
			showBlogDetail(context, objId);
			break;
		case URLs.URL_OBJ_TYPE_OTHER:
			openBrowser(context, objKey);
			break;
		}
	}
*/

	public static void openBrowser(Context context, String url) {
		try {
			Uri uri = Uri.parse(url);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
			ToastMessage(context, "æ— æ³•æµ�è§ˆæ­¤ç½‘é¡µ", 500);
		}
	}

	
	public static WebViewClient getWebViewClient() {
		return new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				showUrlRedirect(view.getContext(), url);
				return true;
			}
		};
	}

	/**
	 * èŽ·å�–TextWatcherå¯¹è±¡
	 * 
	 * @param context
	 * @param tmlKey
	 * @return
	 */
	public static TextWatcher getTextWatcher(final Activity context,
			final String temlKey) {
		return new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// ä¿�å­˜å½“å‰�EditTextæ­£åœ¨ç¼–è¾‘çš„å†…å®¹
				((AppContext) context.getApplication()).setProperty(temlKey,
						s.toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		};
	}

	/**
	 * ç¼–è¾‘å™¨æ˜¾ç¤ºä¿�å­˜çš„è�‰ç¨¿
	 * 
	 * @param context
	 * @param editer
	 * @param temlKey
	 */
	/*
	public static void showTempEditContent(Activity context, EditText editer,
			String temlKey) {
		String tempContent = ((AppContext) context.getApplication())
				.getProperty(temlKey);
		if (!StringUtils.isEmpty(tempContent)) {
			SpannableStringBuilder builder = parseFaceByText(context,
					tempContent);
			editer.setText(builder);
			editer.setSelection(tempContent.length());// è®¾ç½®å…‰æ ‡ä½�ç½®
		}
	}
	*/

	/**
	 * å°†[12]ä¹‹ç±»çš„å­—ç¬¦ä¸²æ›¿æ�¢ä¸ºè¡¨æƒ…
	 * 
	 * @param context
	 * @param content
	 */
	/*
	public static SpannableStringBuilder parseFaceByText(Context context,
			String content) {
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		Matcher matcher = facePattern.matcher(content);
		while (matcher.find()) {
			// ä½¿ç”¨æ­£åˆ™è¡¨è¾¾å¼�æ‰¾å‡ºå…¶ä¸­çš„æ•°å­—
			int position = StringUtils.toInt(matcher.group(1));
			int resId = 0;
			try {
				if (position > 65 && position < 102)
					position = position - 1;
				else if (position > 102)
					position = position - 2;
				resId = GridViewFaceAdapter.getImageIds()[position];
				Drawable d = context.getResources().getDrawable(resId);
				d.setBounds(0, 0, 35, 35);// è®¾ç½®è¡¨æƒ…å›¾ç‰‡çš„æ˜¾ç¤ºå¤§å°�
				ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
				builder.setSpan(span, matcher.start(), matcher.end(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} catch (Exception e) {
			}
		}
		return builder;
	}
	*/

	
	public static void showClearWordsDialog(final Context cont,
			final EditText editer, final TextView numwords) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setTitle(R.string.clearwords);
		builder.setPositiveButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// æ¸…é™¤æ–‡å­—
						editer.setText("");
						numwords.setText("160");
					}
				});
		builder.setNegativeButton(R.string.cancle,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.show();
	}

	/**
	 * å�‘é€�é€šçŸ¥å¹¿æ’­
	 * 
	 * @param context
	 * @param notice
	 */
	/*
	public static void sendBroadCast(Context context, Notice notice) {
		if (!((AppContext) context.getApplicationContext()).isLogin()
				|| notice == null)
			return;
		Intent intent = new Intent("net.oschina.app.action.APPWIDGET_UPDATE");
		intent.putExtra("atmeCount", notice.getAtmeCount());
		intent.putExtra("msgCount", notice.getMsgCount());
		intent.putExtra("reviewCount", notice.getReviewCount());
		intent.putExtra("newFansCount", notice.getNewFansCount());
		context.sendBroadcast(intent);
	}
	*/


	@SuppressLint("NewApi")
	public static SpannableString parseActiveAction(String author,
			int objecttype, int objectcatalog, String objecttitle) {
		String title = "";
		int start = 0;
		int end = 0;
		if (objecttype == 32 && objectcatalog == 0) {
			title = "åŠ å…¥äº†å¼€æº�ä¸­å›½";
		} else if (objecttype == 1 && objectcatalog == 0) {
			title = "æ·»åŠ äº†å¼€æº�é¡¹ç›® " + objecttitle;
		} else if (objecttype == 2 && objectcatalog == 1) {
			title = "åœ¨è®¨è®ºåŒºæ��é—®ï¼š" + objecttitle;
		} else if (objecttype == 2 && objectcatalog == 2) {
			title = "å�‘è¡¨äº†æ–°è¯�é¢˜ï¼š" + objecttitle;
		} else if (objecttype == 3 && objectcatalog == 0) {
			title = "å�‘è¡¨äº†å�šå®¢ " + objecttitle;
		} else if (objecttype == 4 && objectcatalog == 0) {
			title = "å�‘è¡¨ä¸€ç¯‡æ–°é—» " + objecttitle;
		} else if (objecttype == 5 && objectcatalog == 0) {
			title = "åˆ†äº«äº†ä¸€æ®µä»£ç � " + objecttitle;
		} else if (objecttype == 6 && objectcatalog == 0) {
			title = "å�‘å¸ƒäº†ä¸€ä¸ªè�Œä½�ï¼š" + objecttitle;
		} else if (objecttype == 16 && objectcatalog == 0) {
			title = "åœ¨æ–°é—» " + objecttitle + " å�‘è¡¨è¯„è®º";
		} else if (objecttype == 17 && objectcatalog == 1) {
			title = "å›žç­”äº†é—®é¢˜ï¼š" + objecttitle;
		} else if (objecttype == 17 && objectcatalog == 2) {
			title = "å›žå¤�äº†è¯�é¢˜ï¼š" + objecttitle;
		} else if (objecttype == 17 && objectcatalog == 3) {
			title = "åœ¨ " + objecttitle + " å¯¹å›žå¸–å�‘è¡¨è¯„è®º";
		} else if (objecttype == 18 && objectcatalog == 0) {
			title = "åœ¨å�šå®¢ " + objecttitle + " å�‘è¡¨è¯„è®º";
		} else if (objecttype == 19 && objectcatalog == 0) {
			title = "åœ¨ä»£ç � " + objecttitle + " å�‘è¡¨è¯„è®º";
		} else if (objecttype == 20 && objectcatalog == 0) {
			title = "åœ¨è�Œä½� " + objecttitle + " å�‘è¡¨è¯„è®º";
		} else if (objecttype == 101 && objectcatalog == 0) {
			title = "å›žå¤�äº†åŠ¨æ€�ï¼š" + objecttitle;
		} else if (objecttype == 100) {
			title = "æ›´æ–°äº†åŠ¨æ€�";
		}
		title = author + " " + title;
		SpannableString sp = new SpannableString(title);
		// è®¾ç½®ç”¨æˆ·å��å­—ä½“å¤§å°�ã€�åŠ ç²—ã€�é«˜äº®
		sp.setSpan(new AbsoluteSizeSpan(14, true), 0, author.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
				author.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), 0,
				author.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// è®¾ç½®æ ‡é¢˜å­—ä½“å¤§å°�ã€�é«˜äº®
		if (!StringUtils.isEmpty(objecttitle)) {
			start = title.indexOf(objecttitle);
			if (objecttitle.length() > 0 && start > 0) {
				end = start + objecttitle.length();
				sp.setSpan(new AbsoluteSizeSpan(14, true), start, end,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				sp.setSpan(
						new ForegroundColorSpan(Color.parseColor("#0e5986")),
						start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return sp;
	}

	/**
	 * ç»„å�ˆåŠ¨æ€�çš„å›žå¤�æ–‡æœ¬
	 * 
	 * @param name
	 * @param body
	 * @return
	 */
	public static SpannableString parseActiveReply(String name, String body) {
		SpannableString sp = new SpannableString(name + "ï¼š" + body);
		// è®¾ç½®ç”¨æˆ·å��å­—ä½“åŠ ç²—ã€�é«˜äº®
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
				name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), 0,
				name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}

	/**
	 * ç»„å�ˆæ¶ˆæ�¯æ–‡æœ¬
	 * 
	 * @param name
	 * @param body
	 * @return
	 */
	/*
	public static void parseMessageSpan(LinkView view, String name,
			String body, String action) {
		Spanned span = null;
		SpannableStringBuilder style = null;
		int start = 0;
		int end = 0;
		String content = null;
		if (StringUtils.isEmpty(action)) {
			content = name + "ï¼š" + body;
			span = Html.fromHtml(content);
			view.setText(span);
			end = name.length();
		} else {
			content = action + name + "ï¼š" + body;
			span = Html.fromHtml(content);
			view.setText(span);
			start = action.length();
			end = start + name.length();
		}
		view.setMovementMethod(LinkMovementMethod.getInstance());

		Spannable sp = (Spannable) view.getText();
		URLSpan[] urls = span.getSpans(0, sp.length(), URLSpan.class);

		style = new SpannableStringBuilder(view.getText());
		// style.clearSpans();// è¿™é‡Œä¼šæ¸…é™¤ä¹‹å‰�æ‰€æœ‰çš„æ ·å¼�
		for (URLSpan url : urls) {
			 style.removeSpan(url);// å�ªéœ€è¦�ç§»é™¤ä¹‹å‰�çš„URLæ ·å¼�ï¼Œå†�é‡�æ–°è®¾ç½®
			 MyURLSpan myURLSpan =  view.new MyURLSpan(url.getURL());
			 style.setSpan(myURLSpan, span.getSpanStart(url),
		    		span.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		// è®¾ç½®ç”¨æˆ·å��å­—ä½“åŠ ç²—ã€�é«˜äº®
		style.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start,
				end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")),
				start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(style);
	}
	*/

	/**
	 * ç»„å�ˆå›žå¤�å¼•ç”¨æ–‡æœ¬
	 * 
	 * @param name
	 * @param body
	 * @return
	 */
	public static SpannableString parseQuoteSpan(String name, String body) {
		SpannableString sp = new SpannableString("å›žå¤�ï¼š" + name + "\n" + body);
		// è®¾ç½®ç”¨æˆ·å��å­—ä½“åŠ ç²—ã€�é«˜äº®
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 3,
				3 + name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), 3,
				3 + name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}

	/**
	 * å¼¹å‡ºToastæ¶ˆæ�¯
	 * 
	 * @param msg
	 */
	public static void ToastMessage(Context cont, String msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, int msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, String msg, int time) {
		Toast.makeText(cont, msg, time).show();
	}

	/**
	 * ç‚¹å‡»è¿”å›žç›‘å�¬äº‹ä»¶
	 * 
	 * @param activity
	 * @return
	 */
	public static View.OnClickListener finish(final Activity activity) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				activity.finish();
			}
		};
	}

	/**
	 * æ˜¾ç¤ºå…³äºŽæˆ‘ä»¬
	 * 
	 * @param context
	 */
	/*
	public static void showAbout(Context context) {
		Intent intent = new Intent(context, About.class);
		context.startActivity(intent);
	}
	*/

	/**
	 * æ˜¾ç¤ºç”¨æˆ·å��é¦ˆ
	 * 
	 * @param context
	 */
	/*
	public static void showFeedBack(Context context) {
		Intent intent = new Intent(context, FeedBack.class);
		context.startActivity(intent);
	}
	*/

	/**
	 * è�œå�•æ˜¾ç¤ºç™»å½•æˆ–ç™»å‡º
	 * 
	 * @param activity
	 * @param menu
	 */
	/*
	public static void showMenuLoginOrLogout(Activity activity, Menu menu) {
		if (((AppContext) activity.getApplication()).isLogin()) {
			menu.findItem(R.id.main_menu_user).setTitle(
					R.string.main_menu_logout);
			menu.findItem(R.id.main_menu_user).setIcon(
					R.drawable.ic_menu_logout);
		} else {
			menu.findItem(R.id.main_menu_user).setTitle(
					R.string.main_menu_login);
			menu.findItem(R.id.main_menu_user)
					.setIcon(R.drawable.ic_menu_login);
		}
	}
	*/

	/**
	 * å¿«æ�·æ �æ˜¾ç¤ºç™»å½•ä¸Žç™»å‡º
	 * 
	 * @param activity
	 * @param qa
	 */
	/*
	public static void showSettingLoginOrLogout(Activity activity,
			QuickAction qa) {
		if (((AppContext) activity.getApplication()).isLogin()) {
			qa.setIcon(MyQuickAction.buildDrawable(activity,
					R.drawable.ic_menu_logout));
			qa.setTitle(activity.getString(R.string.main_menu_logout));
		} else {
			qa.setIcon(MyQuickAction.buildDrawable(activity,
					R.drawable.ic_menu_login));
			qa.setTitle(activity.getString(R.string.main_menu_login));
		}
	}
	*/

	/*
	public static void showSettingIsLoadImage(Activity activity, QuickAction qa) {
		if (((AppContext) activity.getApplication()).isLoadImage()) {
			qa.setIcon(MyQuickAction.buildDrawable(activity,
					R.drawable.ic_menu_picnoshow));
			qa.setTitle(activity.getString(R.string.main_menu_picnoshow));
		} else {
			qa.setIcon(MyQuickAction.buildDrawable(activity,
					R.drawable.ic_menu_picshow));
			qa.setTitle(activity.getString(R.string.main_menu_picshow));
		}
	}
	*/

	
	public static void loginOrLogout(Activity activity) {
		AppContext ac = (AppContext) activity.getApplication();
		if (ac.isLogin()) {
			ac.Logout();
			ToastMessage(activity, "å·²é€€å‡ºç™»å½•");
		} else {
			showLoginDialog(activity);
		}
	}

	/**
	 * æ–‡ç« æ˜¯å�¦åŠ è½½å›¾ç‰‡æ˜¾ç¤º
	 * 
	 * @param activity
	 */
	public static void changeSettingIsLoadImage(Activity activity) {
		AppContext ac = (AppContext) activity.getApplication();
		if (ac.isLoadImage()) {
			ac.setConfigLoadimage(false);
			ToastMessage(activity, "å·²è®¾ç½®æ–‡ç« ä¸�åŠ è½½å›¾ç‰‡");
		} else {
			ac.setConfigLoadimage(true);
			ToastMessage(activity, "å·²è®¾ç½®æ–‡ç« åŠ è½½å›¾ç‰‡");
		}
	}

	public static void changeSettingIsLoadImage(Activity activity, boolean b) {
		AppContext ac = (AppContext) activity.getApplication();
		ac.setConfigLoadimage(b);
	}

	/**
	 * æ¸…é™¤appç¼“å­˜
	 * 
	 * @param activity
	 */
	public static void clearAppCache(Activity activity) {
		final AppContext ac = (AppContext) activity.getApplication();
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					ToastMessage(ac, "ç¼“å­˜æ¸…é™¤æˆ�åŠŸ");
				} else {
					ToastMessage(ac, "ç¼“å­˜æ¸…é™¤å¤±è´¥");
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					ac.clearAppCache();
					msg.what = 1;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * å�‘é€�Appå¼‚å¸¸å´©æºƒæŠ¥å‘Š
	 * 
	 * @param cont
	 * @param crashReport
	 */
	public static void sendAppCrashReport(final Context cont,
			final String crashReport) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_error);
		builder.setMessage(R.string.app_error_message);
		builder.setPositiveButton(R.string.submit_report,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// å�‘é€�å¼‚å¸¸æŠ¥å‘Š
						Intent i = new Intent(Intent.ACTION_SEND);
						// i.setType("text/plain"); //æ¨¡æ‹Ÿå™¨
						i.setType("message/rfc822"); // çœŸæœº
						i.putExtra(Intent.EXTRA_EMAIL,
								new String[] { "jxsmallmouse@163.com" });
						i.putExtra(Intent.EXTRA_SUBJECT,
								"å¼€æº�ä¸­å›½Androidå®¢æˆ·ç«¯ - é”™è¯¯æŠ¥å‘Š");
						i.putExtra(Intent.EXTRA_TEXT, crashReport);
						cont.startActivity(Intent.createChooser(i, "å�‘é€�é”™è¯¯æŠ¥å‘Š"));
						// é€€å‡º
						AppManager.getAppManager().AppExit(cont);
					}
				});
		builder.setNegativeButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// é€€å‡º
						AppManager.getAppManager().AppExit(cont);
					}
				});
		builder.show();
	}

	/**
	 * é€€å‡ºç¨‹åº�
	 * 
	 * @param cont
	 */
	public static void Exit(final Context cont) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_menu_surelogout);
		builder.setPositiveButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// é€€å‡º
						AppManager.getAppManager().AppExit(cont);
					}
				});
		builder.setNegativeButton(R.string.cancle,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.show();
	}

	/**
	 * æ·»åŠ æˆªå±�åŠŸèƒ½
	 */
	/*
	@SuppressLint("NewApi")
	public static void addScreenShot(Activity context,
			OnScreenShotListener mScreenShotListener) {
		BaseActivity cxt = null;
		if (context instanceof BaseActivity) {
			cxt = (BaseActivity) context;
			cxt.setAllowFullScreen(false);
			ScreenShotView screenShot = new ScreenShotView(cxt,
					mScreenShotListener);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			context.getWindow().addContentView(screenShot, lp);
		}
	}
	*/

	/**
	 * æ·»åŠ ç½‘é¡µçš„ç‚¹å‡»å›¾ç‰‡å±•ç¤ºæ”¯æŒ�
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public static void addWebImageShow(final Context cxt, WebView wv) {
		wv.getSettings().setJavaScriptEnabled(true);
		wv.addJavascriptInterface(new OnWebViewImageListener() {

			@Override
			public void onImageClick(String bigImageUrl) {
				if (bigImageUrl != null)
					;//UIHelper.showImageZoomDialog(cxt, bigImageUrl);
			}
		}, "mWebViewImageListener");
	}
}
