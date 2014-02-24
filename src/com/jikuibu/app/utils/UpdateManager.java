package com.jikuibu.app.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import com.jikuibu.app.AppContext;
import com.jikuibu.app.AppException;

import com.jikuibu.app.api.ApiClient;
import com.jikuibu.app.bean.Update;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jikuibu.app.R;

/**
 * åº”ç”¨ç¨‹åº�æ›´æ–°å·¥å…·åŒ…
 * @author liux (http://my.oschina.net/liux)
 * @version 1.1
 * @created 2012-6-29
 */
public class UpdateManager {

	private static final int DOWN_NOSDCARD = 0;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
	
    private static final int DIALOG_TYPE_LATEST = 0;
    private static final int DIALOG_TYPE_FAIL   = 1;
    
	private static UpdateManager updateManager;
	
	private Context mContext;
	//é€šçŸ¥å¯¹è¯�æ¡†
	private Dialog noticeDialog;
	//ä¸‹è½½å¯¹è¯�æ¡†
	private Dialog downloadDialog;
	//'å·²ç»�æ˜¯æœ€æ–°' æˆ–è€… 'æ— æ³•èŽ·å�–æœ€æ–°ç‰ˆæœ¬' çš„å¯¹è¯�æ¡†
	private Dialog latestOrFailDialog;
    //è¿›åº¦æ�¡
    private ProgressBar mProgress;
    //æ˜¾ç¤ºä¸‹è½½æ•°å€¼
    private TextView mProgressText;
    //æŸ¥è¯¢åŠ¨ç”»
    private ProgressDialog mProDialog;
    //è¿›åº¦å€¼
    private int progress;
    //ä¸‹è½½çº¿ç¨‹
    private Thread downLoadThread;
    //ç»ˆæ­¢æ ‡è®°
    private boolean interceptFlag;
	//æ��ç¤ºè¯­
	private String updateMsg = "";
	//è¿”å›žçš„å®‰è£…åŒ…url
	private String apkUrl = "";
	//ä¸‹è½½åŒ…ä¿�å­˜è·¯å¾„
    private String savePath = "";
	//apkä¿�å­˜å®Œæ•´è·¯å¾„
	private String apkFilePath = "";
	//ä¸´æ—¶ä¸‹è½½æ–‡ä»¶è·¯å¾„
	private String tmpFilePath = "";
	//ä¸‹è½½æ–‡ä»¶å¤§å°�
	private String apkFileSize;
	//å·²ä¸‹è½½æ–‡ä»¶å¤§å°�
	private String tmpFileSize;
	
	private String curVersionName = "";
	private int curVersionCode;
	private Update mUpdate;
    
    private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mProgressText.setText(tmpFileSize + "/" + apkFileSize);
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				installApk();
				break;
			case DOWN_NOSDCARD:
				downloadDialog.dismiss();
				Toast.makeText(mContext, "æ— æ³•ä¸‹è½½å®‰è£…æ–‡ä»¶ï¼Œè¯·æ£€æŸ¥SDå�¡æ˜¯å�¦æŒ‚è½½", 3000).show();
				break;
			}
    	};
    };
    
	public static UpdateManager getUpdateManager() {
		if(updateManager == null){
			updateManager = new UpdateManager();
		}
		updateManager.interceptFlag = false;
		return updateManager;
	}
	
	/**
	 * æ£€æŸ¥Appæ›´æ–°
	 * @param context
	 * @param isShowMsg æ˜¯å�¦æ˜¾ç¤ºæ��ç¤ºæ¶ˆæ�¯
	 */
	public void checkAppUpdate(Context context, final boolean isShowMsg){
		this.mContext = context;
		getCurrentVersion();
		if(isShowMsg){
			if(mProDialog == null)
				mProDialog = ProgressDialog.show(mContext, null, "æ­£åœ¨æ£€æµ‹ï¼Œè¯·ç¨�å�Ž...", true, true);
			else if(mProDialog.isShowing() || (latestOrFailDialog!=null && latestOrFailDialog.isShowing()))
				return;
		}
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				//è¿›åº¦æ�¡å¯¹è¯�æ¡†ä¸�æ˜¾ç¤º - æ£€æµ‹ç»“æžœä¹Ÿä¸�æ˜¾ç¤º
				if(mProDialog != null && !mProDialog.isShowing()){
					return;
				}
				//å…³é—­å¹¶é‡Šæ”¾é‡Šæ”¾è¿›åº¦æ�¡å¯¹è¯�æ¡†
				if(isShowMsg && mProDialog != null){
					mProDialog.dismiss();
					mProDialog = null;
				}
				//æ˜¾ç¤ºæ£€æµ‹ç»“æžœ
				if(msg.what == 1){
					mUpdate = (Update)msg.obj;
					if(mUpdate != null){
						if(curVersionCode < mUpdate.getVersionCode()){
							apkUrl = mUpdate.getDownloadUrl();
							updateMsg = mUpdate.getUpdateLog();
							showNoticeDialog();
						}else if(isShowMsg){
							showLatestOrFailDialog(DIALOG_TYPE_LATEST);
						}
					}
				}else if(isShowMsg){
					showLatestOrFailDialog(DIALOG_TYPE_FAIL);
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg = new Message();
				try {					
					Update update = ApiClient.checkVersion((AppContext)mContext.getApplicationContext());
					msg.what = 1;
					msg.obj = update;
				} catch (AppException e) {
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}			
		}.start();		
	}	
	
	/**
	 * æ˜¾ç¤º'å·²ç»�æ˜¯æœ€æ–°'æˆ–è€…'æ— æ³•èŽ·å�–ç‰ˆæœ¬ä¿¡æ�¯'å¯¹è¯�æ¡†
	 */
	private void showLatestOrFailDialog(int dialogType) {
		if (latestOrFailDialog != null) {
			//å…³é—­å¹¶é‡Šæ”¾ä¹‹å‰�çš„å¯¹è¯�æ¡†
			latestOrFailDialog.dismiss();
			latestOrFailDialog = null;
		}
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("ç³»ç»Ÿæ��ç¤º");
		if (dialogType == DIALOG_TYPE_LATEST) {
			builder.setMessage("æ‚¨å½“å‰�å·²ç»�æ˜¯æœ€æ–°ç‰ˆæœ¬");
		} else if (dialogType == DIALOG_TYPE_FAIL) {
			builder.setMessage("æ— æ³•èŽ·å�–ç‰ˆæœ¬æ›´æ–°ä¿¡æ�¯");
		}
		builder.setPositiveButton("ç¡®å®š", null);
		latestOrFailDialog = builder.create();
		latestOrFailDialog.show();
	}

	/**
	 * èŽ·å�–å½“å‰�å®¢æˆ·ç«¯ç‰ˆæœ¬ä¿¡æ�¯
	 */
	private void getCurrentVersion(){
        try { 
        	PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        	curVersionName = info.versionName;
        	curVersionCode = info.versionCode;
        } catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
	}
	
	/**
	 * æ˜¾ç¤ºç‰ˆæœ¬æ›´æ–°é€šçŸ¥å¯¹è¯�æ¡†
	 */
	private void showNoticeDialog(){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("è½¯ä»¶ç‰ˆæœ¬æ›´æ–°");
		builder.setMessage(updateMsg);
		builder.setPositiveButton("ç«‹å�³æ›´æ–°", new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();			
			}
		});
		builder.setNegativeButton("ä»¥å�Žå†�è¯´", new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();				
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}
	
	/**
	 * æ˜¾ç¤ºä¸‹è½½å¯¹è¯�æ¡†
	 */
	private void showDownloadDialog(){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("æ­£åœ¨ä¸‹è½½æ–°ç‰ˆæœ¬");
		
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.update_progress);
		mProgressText = (TextView) v.findViewById(R.id.update_progress_text);
		
		builder.setView(v);
		builder.setNegativeButton("å�–æ¶ˆ", new OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();
		
		downloadApk();
	}
	
	private Runnable mdownApkRunnable = new Runnable() {	
		@Override
		public void run() {
			try {
				String apkName = "OSChinaApp_"+mUpdate.getVersionName()+".apk";
				String tmpApk = "OSChinaApp_"+mUpdate.getVersionName()+".tmp";
				//åˆ¤æ–­æ˜¯å�¦æŒ‚è½½äº†SDå�¡
				String storageState = Environment.getExternalStorageState();		
				if(storageState.equals(Environment.MEDIA_MOUNTED)){
					savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/OSChina/Update/";
					File file = new File(savePath);
					if(!file.exists()){
						file.mkdirs();
					}
					apkFilePath = savePath + apkName;
					tmpFilePath = savePath + tmpApk;
				}
				
				//æ²¡æœ‰æŒ‚è½½SDå�¡ï¼Œæ— æ³•ä¸‹è½½æ–‡ä»¶
				if(apkFilePath == null || apkFilePath == ""){
					mHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}
				
				File ApkFile = new File(apkFilePath);
				
				//æ˜¯å�¦å·²ä¸‹è½½æ›´æ–°æ–‡ä»¶
				if(ApkFile.exists()){
					downloadDialog.dismiss();
					installApk();
					return;
				}
				
				//è¾“å‡ºä¸´æ—¶ä¸‹è½½æ–‡ä»¶
				File tmpFile = new File(tmpFilePath);
				FileOutputStream fos = new FileOutputStream(tmpFile);
				
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				
				//æ˜¾ç¤ºæ–‡ä»¶å¤§å°�æ ¼å¼�ï¼š2ä¸ªå°�æ•°ç‚¹æ˜¾ç¤º
		    	DecimalFormat df = new DecimalFormat("0.00");
		    	//è¿›åº¦æ�¡ä¸‹é�¢æ˜¾ç¤ºçš„æ€»æ–‡ä»¶å¤§å°�
		    	apkFileSize = df.format((float) length / 1024 / 1024) + "MB";
				
				int count = 0;
				byte buf[] = new byte[1024];
				
				do{   		   		
		    		int numread = is.read(buf);
		    		count += numread;
		    		//è¿›åº¦æ�¡ä¸‹é�¢æ˜¾ç¤ºçš„å½“å‰�ä¸‹è½½æ–‡ä»¶å¤§å°�
		    		tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
		    		//å½“å‰�è¿›åº¦å€¼
		    	    progress =(int)(((float)count / length) * 100);
		    	    //æ›´æ–°è¿›åº¦
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){	
		    			//ä¸‹è½½å®Œæˆ� - å°†ä¸´æ—¶ä¸‹è½½æ–‡ä»¶è½¬æˆ�APKæ–‡ä»¶
						if(tmpFile.renameTo(ApkFile)){
							//é€šçŸ¥å®‰è£…
							mHandler.sendEmptyMessage(DOWN_OVER);
						}
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}while(!interceptFlag);//ç‚¹å‡»å�–æ¶ˆå°±å�œæ­¢ä¸‹è½½
				
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
			
		}
	};
	
	/**
	* ä¸‹è½½apk
	* @param url
	*/	
	private void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	
	/**
    * å®‰è£…apk
    * @param url
    */
	private void installApk(){
		File apkfile = new File(apkFilePath);
        if (!apkfile.exists()) {
            return;
        }    
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        mContext.startActivity(i);
	}
}
