package com.jikuibu.app;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * åº”ç”¨ç¨‹åº�Activityç®¡ç�†ç±»ï¼šç”¨äºŽActivityç®¡ç�†å’Œåº”ç”¨ç¨‹åº�é€€å‡º
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class AppManager {
	
	private static Stack<Activity> activityStack;
	private static AppManager instance;
	
	private AppManager(){}
	/**
	 * å�•ä¸€å®žä¾‹
	 */
	public static AppManager getAppManager(){
		if(instance==null){
			instance=new AppManager();
		}
		return instance;
	}
	/**
	 * æ·»åŠ Activityåˆ°å †æ ˆ
	 */
	public void addActivity(Activity activity){
		if(activityStack==null){
			activityStack=new Stack<Activity>();
		}
		activityStack.add(activity);
	}
	/**
	 * èŽ·å�–å½“å‰�Activityï¼ˆå †æ ˆä¸­æœ€å�Žä¸€ä¸ªåŽ‹å…¥çš„ï¼‰
	 */
	public Activity currentActivity(){
		Activity activity=activityStack.lastElement();
		return activity;
	}
	/**
	 * ç»“æ�Ÿå½“å‰�Activityï¼ˆå †æ ˆä¸­æœ€å�Žä¸€ä¸ªåŽ‹å…¥çš„ï¼‰
	 */
	public void finishActivity(){
		Activity activity=activityStack.lastElement();
		finishActivity(activity);
	}
	/**
	 * ç»“æ�ŸæŒ‡å®šçš„Activity
	 */
	public void finishActivity(Activity activity){
		if(activity!=null){
			activityStack.remove(activity);
			activity.finish();
			activity=null;
		}
	}
	/**
	 * ç»“æ�ŸæŒ‡å®šç±»å��çš„Activity
	 */
	public void finishActivity(Class<?> cls){
		for (Activity activity : activityStack) {
			if(activity.getClass().equals(cls) ){
				finishActivity(activity);
			}
		}
	}
	/**
	 * ç»“æ�Ÿæ‰€æœ‰Activity
	 */
	public void finishAllActivity(){
		for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
            	activityStack.get(i).finish();
            }
	    }
		activityStack.clear();
	}
	/**
	 * é€€å‡ºåº”ç”¨ç¨‹åº�
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {	}
	}
}