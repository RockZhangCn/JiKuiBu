package com.jikuibu.app.utils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jikuibu.app.AppException;
import com.jikuibu.app.api.ApiClient;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
/**
 * å¼‚æ­¥çº¿ç¨‹åŠ è½½å›¾ç‰‡å·¥å…·ç±»
 * ä½¿ç”¨è¯´æ˜Žï¼š
 * BitmapManager bmpManager;
 * bmpManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.loading));
 * bmpManager.loadBitmap(imageURL, imageView);
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-6-25
 */
public class BitmapManager {  
	  
    private static HashMap<String, SoftReference<Bitmap>> cache;  
    private static ExecutorService pool;  
    private static Map<ImageView, String> imageViews;  
    private Bitmap defaultBmp;  
    
    static {  
        cache = new HashMap<String, SoftReference<Bitmap>>();  
        pool = Executors.newFixedThreadPool(5);  //å›ºå®šçº¿ç¨‹æ± 
        imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    }  
    
    public BitmapManager(){}
    
    public BitmapManager(Bitmap def) {
    	this.defaultBmp = def;
    }
    
    /**
     * è®¾ç½®é»˜è®¤å›¾ç‰‡
     * @param bmp
     */
    public void setDefaultBmp(Bitmap bmp) {  
    	defaultBmp = bmp;  
    }   
  
    /**
     * åŠ è½½å›¾ç‰‡
     * @param url
     * @param imageView
     */
    public void loadBitmap(String url, ImageView imageView) {  
    	loadBitmap(url, imageView, this.defaultBmp, 0, 0);
    }
	
    /**
     * åŠ è½½å›¾ç‰‡-å�¯è®¾ç½®åŠ è½½å¤±è´¥å�Žæ˜¾ç¤ºçš„é»˜è®¤å›¾ç‰‡
     * @param url
     * @param imageView
     * @param defaultBmp
     */
    public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp) {  
    	loadBitmap(url, imageView, defaultBmp, 0, 0);
    }
    
    /**
     * åŠ è½½å›¾ç‰‡-å�¯æŒ‡å®šæ˜¾ç¤ºå›¾ç‰‡çš„é«˜å®½
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp, int width, int height) {  
        imageViews.put(imageView, url);  
        Bitmap bitmap = getBitmapFromCache(url);  
   
        if (bitmap != null) {  
			//æ˜¾ç¤ºç¼“å­˜å›¾ç‰‡
            imageView.setImageBitmap(bitmap);  
        } else {  
        	//åŠ è½½SDå�¡ä¸­çš„å›¾ç‰‡ç¼“å­˜
        	String filename = FileUtils.getFileName(url);
        	String filepath = imageView.getContext().getFilesDir() + File.separator + filename;
    		File file = new File(filepath);
    		if(file.exists()){
				//æ˜¾ç¤ºSDå�¡ä¸­çš„å›¾ç‰‡ç¼“å­˜
    			Bitmap bmp = ImageUtils.getBitmap(imageView.getContext(), filename);
        		imageView.setImageBitmap(bmp);
        	}else{
				//çº¿ç¨‹åŠ è½½ç½‘ç»œå›¾ç‰‡
        		imageView.setImageBitmap(defaultBmp);
        		queueJob(url, imageView, width, height);
        	}
        }  
    }  
  
    /**
     * ä»Žç¼“å­˜ä¸­èŽ·å�–å›¾ç‰‡
     * @param url
     */
    public Bitmap getBitmapFromCache(String url) {  
    	Bitmap bitmap = null;
        if (cache.containsKey(url)) {  
            bitmap = cache.get(url).get();  
        }  
        return bitmap;  
    }  
    
    /**
     * ä»Žç½‘ç»œä¸­åŠ è½½å›¾ç‰‡
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public void queueJob(final String url, final ImageView imageView, final int width, final int height) {  
        /* Create handler in UI thread. */  
        final Handler handler = new Handler() {  
            public void handleMessage(Message msg) {  
                String tag = imageViews.get(imageView);  
                if (tag != null && tag.equals(url)) {  
                    if (msg.obj != null) {  
                        imageView.setImageBitmap((Bitmap) msg.obj);  
                        try {
                        	//å�‘SDå�¡ä¸­å†™å…¥å›¾ç‰‡ç¼“å­˜
							ImageUtils.saveImage(imageView.getContext(), FileUtils.getFileName(url), (Bitmap) msg.obj);
						} catch (IOException e) {
							e.printStackTrace();
						}
                    } 
                }  
            }  
        };  
  
        pool.execute(new Runnable() {   
            public void run() {  
                Message message = Message.obtain();  
                message.obj = downloadBitmap(url, width, height);  
                handler.sendMessage(message);  
            }  
        });  
    } 
  
    /**
     * ä¸‹è½½å›¾ç‰‡-å�¯æŒ‡å®šæ˜¾ç¤ºå›¾ç‰‡çš„é«˜å®½
     * @param url
     * @param width
     * @param height
     */
    private Bitmap downloadBitmap(String url, int width, int height) {   
        Bitmap bitmap = null;
        try {
			//httpåŠ è½½å›¾ç‰‡
			bitmap = ApiClient.getNetBitmap(url);
			if(width > 0 && height > 0) {
				//æŒ‡å®šæ˜¾ç¤ºå›¾ç‰‡çš„é«˜å®½
				bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
			} 
			//æ”¾å…¥ç¼“å­˜
			cache.put(url, new SoftReference<Bitmap>(bitmap));
		} catch (AppException e) {
			e.printStackTrace();
		}
        return bitmap;  
    }  
}