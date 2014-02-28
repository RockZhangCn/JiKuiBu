package com.jikuibu.app;

import com.jikuibu.app.utils.StringUtils;
import com.jikuibu.app.ui.MainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import com.jikuibu.app.R;

/**
 * åº”ç”¨ç¨‹åº�å�¯åŠ¨ç±»ï¼šæ˜¾ç¤ºæ¬¢è¿Žç•Œé�¢å¹¶è·³è½¬åˆ°ä¸»ç•Œé�¢
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class AppStart extends Activity {
    private static final String TAG = "AppStart";
	
	@SuppressLint("NewApi")  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.start, null);
		setContentView(view);
        
        //See StrictMode document for details.
		//NetworkOnMainThreadException
		//http://blog.csdn.net/zjtbetter/article/details/12890831
		if (android.os.Build.VERSION.SDK_INT > 9) 
		{
			try{
				//This application developed on old version Android, old code run on new Platform 4.0+ want
				//to access to Internet need to set StrictMode to avoid exception.
				 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				    StrictMode.setThreadPolicy(policy);
				    
		        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		                .detectDiskReads()
		                .detectDiskWrites()
		                .detectNetwork()   // or .detectAll() for all detectable problems
		                .penaltyLog()
		                .build());
		        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		                .detectLeakedSqlLiteObjects()
		                .detectLeakedClosableObjects()
		                .penaltyLog()
		                .penaltyDeath()
		                .build());
			}catch(Throwable e)
			{
				Log.e(TAG, "we must be running on an older devices.");
			}
		}
		
        
		AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener()
		{
			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationStart(Animation animation) {}
			
		});
		
		AppContext appContext = (AppContext)getApplication();
		String cookie = appContext.getProperty("cookie");
		if(StringUtils.isEmpty(cookie)) {
			String cookie_name = appContext.getProperty("cookie_name");
			String cookie_value = appContext.getProperty("cookie_value");
			if(!StringUtils.isEmpty(cookie_name) && !StringUtils.isEmpty(cookie_value)) {
				cookie = cookie_name + "=" + cookie_value;
				appContext.setProperty("cookie", cookie);
				appContext.removeProperty("cookie_domain","cookie_name","cookie_value","cookie_version","cookie_path");
			}
		}
    }
    
 
    private void redirectTo(){        
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}