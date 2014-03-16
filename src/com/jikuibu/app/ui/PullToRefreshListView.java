package com.jikuibu.app.ui;


import com.jikuibu.app.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * ä¸‹æ‹‰åˆ·æ–°æŽ§ä»¶
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class PullToRefreshListView extends ListView implements OnScrollListener {  
	   
    private final static String TAG = "PullToRefreshListView";  
    
    // ä¸‹æ‹‰åˆ·æ–°æ ‡å¿—   
    private final static int PULL_To_REFRESH = 0; 
    // æ�¾å¼€åˆ·æ–°æ ‡å¿—   
    private final static int RELEASE_To_REFRESH = 1; 
    // æ­£åœ¨åˆ·æ–°æ ‡å¿—   
    private final static int REFRESHING = 2;  
    // åˆ·æ–°å®Œæˆ�æ ‡å¿—   
    private final static int DONE = 3;  
  
    private LayoutInflater inflater;  
  
    private LinearLayout headView;  
    private TextView tipsTextview;  
    private TextView lastUpdatedTextView;  
    private ImageView arrowImageView;  
    private ProgressBar progressBar;  
    // ç”¨æ�¥è®¾ç½®ç®­å¤´å›¾æ ‡åŠ¨ç”»æ•ˆæžœ   
    private RotateAnimation animation;  
    private RotateAnimation reverseAnimation;  
  
    // ç”¨äºŽä¿�è¯�startYçš„å€¼åœ¨ä¸€ä¸ªå®Œæ•´çš„touchäº‹ä»¶ä¸­å�ªè¢«è®°å½•ä¸€æ¬¡   
    private boolean isRecored;  
  
    private int headContentWidth;  
    private int headContentHeight;  
    private int headContentOriginalTopPadding;
  
    private int startY;  
    private int firstItemIndex;  
    private int currentScrollState;
  
    private int state;  
  
    private boolean isBack;  
  
    public OnRefreshListener refreshListener;  
    
    public PullToRefreshListView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        init(context);  
    }  
    
    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        init(context);  
    }  
  
    private void init(Context context) {   
    	//è®¾ç½®æ»‘åŠ¨æ•ˆæžœ
        animation = new RotateAnimation(0, -180,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);  
        animation.setInterpolator(new LinearInterpolator());  
        animation.setDuration(100);  
        animation.setFillAfter(true);  
  
        reverseAnimation = new RotateAnimation(-180, 0,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);  
        reverseAnimation.setInterpolator(new LinearInterpolator());  
        reverseAnimation.setDuration(100);  
        reverseAnimation.setFillAfter(true);  
        
        inflater = LayoutInflater.from(context);  
        headView = (LinearLayout) inflater.inflate(R.layout.pull_to_refresh_head, null);  
  
        arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);  
        arrowImageView.setMinimumWidth(50);  
        arrowImageView.setMinimumHeight(50);  
        progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);  
        tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);  
        lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);  
        
        headContentOriginalTopPadding = headView.getPaddingTop();  
        
        measureView(headView);  
        headContentHeight = headView.getMeasuredHeight();  
        headContentWidth = headView.getMeasuredWidth(); 
        
        headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(), headView.getPaddingBottom());  
        headView.invalidate();  

        //System.out.println("åˆ�å§‹é«˜åº¦ï¼š"+headContentHeight); 
        //System.out.println("åˆ�å§‹TopPadï¼š"+headContentOriginalTopPadding);
        
        addHeaderView(headView);        
        setOnScrollListener(this); 
    }  
  
    public void onScroll(AbsListView view, int firstVisiableItem, int visibleItemCount,  int totalItemCount) {  
        firstItemIndex = firstVisiableItem;  
    }  
  
    public void onScrollStateChanged(AbsListView view, int scrollState) {  
    	currentScrollState = scrollState;
    }  
  
    public boolean onTouchEvent(MotionEvent event) {  
        switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            if (firstItemIndex == 0 && !isRecored) {  
                startY = (int) event.getY();  
                isRecored = true;  
                //System.out.println("å½“å‰�-æŒ‰ä¸‹é«˜åº¦-ACTION_DOWN-Yï¼š"+startY);
            }  
            break;  
        
        case MotionEvent.ACTION_CANCEL://å¤±åŽ»ç„¦ç‚¹&å�–æ¶ˆåŠ¨ä½œ
        case MotionEvent.ACTION_UP:  
  
            if (state != REFRESHING) {  
                if (state == DONE) {  
                    //System.out.println("å½“å‰�-æŠ¬èµ·-ACTION_UPï¼šDONEä»€ä¹ˆéƒ½ä¸�å�š");
                }  
                else if (state == PULL_To_REFRESH) {  
                    state = DONE;  
                    changeHeaderViewByState();                      
                    //System.out.println("å½“å‰�-æŠ¬èµ·-ACTION_UPï¼šPULL_To_REFRESH-->DONE-ç”±ä¸‹æ‹‰åˆ·æ–°çŠ¶æ€�åˆ°åˆ·æ–°å®Œæˆ�çŠ¶æ€�");
                }  
                else if (state == RELEASE_To_REFRESH) {  
                    state = REFRESHING;  
                    changeHeaderViewByState();  
                    onRefresh();                      
                    //System.out.println("å½“å‰�-æŠ¬èµ·-ACTION_UPï¼šRELEASE_To_REFRESH-->REFRESHING-ç”±æ�¾å¼€åˆ·æ–°çŠ¶æ€�ï¼Œåˆ°åˆ·æ–°å®Œæˆ�çŠ¶æ€�");
                }  
            }  
  
            isRecored = false;  
            isBack = false;  
  
            break;  
  
        case MotionEvent.ACTION_MOVE:  
            int tempY = (int) event.getY(); 
            //System.out.println("å½“å‰�-æ»‘åŠ¨-ACTION_MOVE Yï¼š"+tempY);
            if (!isRecored && firstItemIndex == 0) {  
                //System.out.println("å½“å‰�-æ»‘åŠ¨-è®°å½•æ‹–æ‹½æ—¶çš„ä½�ç½® Yï¼š"+tempY);
                isRecored = true;  
                startY = tempY;  
            }  
            if (state != REFRESHING && isRecored) {  
                // å�¯ä»¥æ�¾å¼€åˆ·æ–°äº†   
                if (state == RELEASE_To_REFRESH) {  
                    // å¾€ä¸ŠæŽ¨ï¼ŒæŽ¨åˆ°å±�å¹•è¶³å¤ŸæŽ©ç›–headçš„ç¨‹åº¦ï¼Œä½†è¿˜æ²¡æœ‰å…¨éƒ¨æŽ©ç›–   
                    if ((tempY - startY < headContentHeight+20)  
                            && (tempY - startY) > 0) {  
                        state = PULL_To_REFRESH;  
                        changeHeaderViewByState();                          
                        //System.out.println("å½“å‰�-æ»‘åŠ¨-ACTION_MOVEï¼šRELEASE_To_REFRESH--ã€‹PULL_To_REFRESH-ç”±æ�¾å¼€åˆ·æ–°çŠ¶æ€�è½¬å�˜åˆ°ä¸‹æ‹‰åˆ·æ–°çŠ¶æ€�");
                    }  
                    // ä¸€ä¸‹å­�æŽ¨åˆ°é¡¶   
                    else if (tempY - startY <= 0) {  
                        state = DONE;  
                        changeHeaderViewByState();                         
                        //System.out.println("å½“å‰�-æ»‘åŠ¨-ACTION_MOVEï¼šRELEASE_To_REFRESH--ã€‹DONE-ç”±æ�¾å¼€åˆ·æ–°çŠ¶æ€�è½¬å�˜åˆ°doneçŠ¶æ€�");
                    }  
                    // å¾€ä¸‹æ‹‰ï¼Œæˆ–è€…è¿˜æ²¡æœ‰ä¸ŠæŽ¨åˆ°å±�å¹•é¡¶éƒ¨æŽ©ç›–head   
                    else {  
                        // ä¸�ç”¨è¿›è¡Œç‰¹åˆ«çš„æ“�ä½œï¼Œå�ªç”¨æ›´æ–°paddingTopçš„å€¼å°±è¡Œäº†   
                    }  
                }  
                // è¿˜æ²¡æœ‰åˆ°è¾¾æ˜¾ç¤ºæ�¾å¼€åˆ·æ–°çš„æ—¶å€™,DONEæˆ–è€…æ˜¯PULL_To_REFRESHçŠ¶æ€�   
                else if (state == PULL_To_REFRESH) {  
                    // ä¸‹æ‹‰åˆ°å�¯ä»¥è¿›å…¥RELEASE_TO_REFRESHçš„çŠ¶æ€�   
                    if (tempY - startY >= headContentHeight+20 &&((currentScrollState == SCROLL_STATE_IDLE) || (currentScrollState == SCROLL_STATE_TOUCH_SCROLL))) {  
                        state = RELEASE_To_REFRESH;  
                        isBack = true;  
                        changeHeaderViewByState();  
                        //System.out.println("å½“å‰�-æ»‘åŠ¨-PULL_To_REFRESH--ã€‹RELEASE_To_REFRESH-ç”±doneæˆ–è€…ä¸‹æ‹‰åˆ·æ–°çŠ¶æ€�è½¬å�˜åˆ°æ�¾å¼€åˆ·æ–°");
                    }  
                    // ä¸ŠæŽ¨åˆ°é¡¶äº†   
                    else if (tempY - startY <= 0) {  
                        state = DONE;  
                        changeHeaderViewByState();   
                        //System.out.println("å½“å‰�-æ»‘åŠ¨-PULL_To_REFRESH--ã€‹DONE-ç”±Doneæˆ–è€…ä¸‹æ‹‰åˆ·æ–°çŠ¶æ€�è½¬å�˜åˆ°doneçŠ¶æ€�");
                    }  
                }  
                // doneçŠ¶æ€�ä¸‹   
                else if (state == DONE) {  
                    if (tempY - startY > 0) {  
                        state = PULL_To_REFRESH;  
                        changeHeaderViewByState(); 
                        //System.out.println("å½“å‰�-æ»‘åŠ¨-DONE--ã€‹PULL_To_REFRESH-ç”±doneçŠ¶æ€�è½¬å�˜åˆ°ä¸‹æ‹‰åˆ·æ–°çŠ¶æ€�");
                    }  
                }  
                
                // æ›´æ–°headViewçš„size   
                if (state == PULL_To_REFRESH) { 
                	int topPadding = (int)((-1 * headContentHeight + (tempY - startY)));
                	headView.setPadding(headView.getPaddingLeft(), topPadding, headView.getPaddingRight(), headView.getPaddingBottom());   
                    headView.invalidate();  
                    //System.out.println("å½“å‰�-ä¸‹æ‹‰åˆ·æ–°PULL_To_REFRESH-TopPadï¼š"+topPadding);
                }  
  
                // æ›´æ–°headViewçš„paddingTop   
                if (state == RELEASE_To_REFRESH) {  
                	int topPadding = (int)((tempY - startY - headContentHeight));
                	headView.setPadding(headView.getPaddingLeft(), topPadding, headView.getPaddingRight(), headView.getPaddingBottom());    
                    headView.invalidate();  
                    //System.out.println("å½“å‰�-é‡Šæ”¾åˆ·æ–°RELEASE_To_REFRESH-TopPadï¼š"+topPadding);
                }  
            }  
            break;  
        }  
        return super.onTouchEvent(event);  
    }  
  
    // å½“çŠ¶æ€�æ”¹å�˜æ—¶å€™ï¼Œè°ƒç”¨è¯¥æ–¹æ³•ï¼Œä»¥æ›´æ–°ç•Œé�¢   
    private void changeHeaderViewByState() {  
        switch (state) {  
        case RELEASE_To_REFRESH:  
        	
            arrowImageView.setVisibility(View.VISIBLE);  
            progressBar.setVisibility(View.GONE);  
            tipsTextview.setVisibility(View.VISIBLE);  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
  
            arrowImageView.clearAnimation();  
            arrowImageView.startAnimation(animation);  
  
            tipsTextview.setText(R.string.pull_to_refresh_release_label);  
  
            //Log.v(TAG, "å½“å‰�çŠ¶æ€�ï¼Œæ�¾å¼€åˆ·æ–°");  
            break;  
        case PULL_To_REFRESH:
        	
            progressBar.setVisibility(View.GONE);  
            tipsTextview.setVisibility(View.VISIBLE);  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
            arrowImageView.clearAnimation();  
            arrowImageView.setVisibility(View.VISIBLE);  
            if (isBack) {  
                isBack = false;  
                arrowImageView.clearAnimation();  
                arrowImageView.startAnimation(reverseAnimation);  
            } 
            tipsTextview.setText(R.string.pull_to_refresh_pull_label);  

            //Log.v(TAG, "å½“å‰�çŠ¶æ€�ï¼Œä¸‹æ‹‰åˆ·æ–°");  
            break;  
  
        case REFRESHING:   
        	//System.out.println("åˆ·æ–°REFRESHING-TopPadï¼š"+headContentOriginalTopPadding);
        	headView.setPadding(headView.getPaddingLeft(), headContentOriginalTopPadding, headView.getPaddingRight(), headView.getPaddingBottom());   
            headView.invalidate();  
  
            progressBar.setVisibility(View.VISIBLE);  
            arrowImageView.clearAnimation();  
            arrowImageView.setVisibility(View.GONE);  
            tipsTextview.setText(R.string.pull_to_refresh_refreshing_label);  
            lastUpdatedTextView.setVisibility(View.GONE);  
  
            //Log.v(TAG, "å½“å‰�çŠ¶æ€�,æ­£åœ¨åˆ·æ–°...");  
            break;  
        case DONE:  
        	//System.out.println("å®Œæˆ�DONE-TopPadï¼š"+(-1 * headContentHeight));
        	headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(), headView.getPaddingBottom());  
            headView.invalidate();  
  
            progressBar.setVisibility(View.GONE);  
            arrowImageView.clearAnimation();  
            // æ­¤å¤„æ›´æ�¢å›¾æ ‡   
            arrowImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);  
  
            tipsTextview.setText(R.string.pull_to_refresh_pull_label);  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
  
            //Log.v(TAG, "å½“å‰�çŠ¶æ€�ï¼Œdone");  
            break;  
        }  
    }  
  
    public void clickRefresh() {
    	setSelection(0);
    	state = REFRESHING;  
        changeHeaderViewByState();  
        onRefresh(); 
    }
    
    public void setOnRefreshListener(OnRefreshListener refreshListener) {  
        this.refreshListener = refreshListener;  
    }  
  
    public interface OnRefreshListener {  
        public void onRefresh();  
    }  
  
    public void onRefreshComplete(String update) {  
        lastUpdatedTextView.setText(update);  
        onRefreshComplete();
    } 
    
    public void onRefreshComplete() {  
        state = DONE;  
        changeHeaderViewByState();  
    }  
  
    private void onRefresh() {  
        if (refreshListener != null) {  
            refreshListener.onRefresh();  
        }  
    }  
  
    // è®¡ç®—headViewçš„widthå�Šheightå€¼  
    private void measureView(View child) {  
        ViewGroup.LayoutParams p = child.getLayoutParams();  
        if (p == null) {  
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,  
                    ViewGroup.LayoutParams.WRAP_CONTENT);  
        }  
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);  
        int lpHeight = p.height;  
        int childHeightSpec;  
        if (lpHeight > 0) {  
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,  
                    MeasureSpec.EXACTLY);  
        } else {  
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,  
                    MeasureSpec.UNSPECIFIED);  
        }  
        child.measure(childWidthSpec, childHeightSpec);  
    }  
	  
}
