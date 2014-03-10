package com.jikuibu.app.ui;

import com.jikuibu.app.R;
import com.jikuibu.app.bean.KuiBuList;
import com.jikuibu.app.ui.Adapter.KuiBuSwipeViewAdapter;
import com.jikuibu.app.utils.UIHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class KuiBuDetailActivity extends FragmentActivity {

	private static final String TAG = "KuiBuDetailActivity";
	private KuiBuSwipeViewAdapter _kuiBuSwipeViewAdatper;
	private ViewPager _ViewPager;
	private int _maxPageCount;
	private int _currentPagePos;
	private int __prevPagePos;
	private int _scrollState;
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.kuibudetail_activity);
		
		Intent startIntent = this.getIntent();
		Bundle bundle = startIntent.getExtras();
		int index = bundle.getInt("INDEX");
		KuiBuList kuibulist = (KuiBuList)bundle.getSerializable("KUIBULIST");
		_maxPageCount =  kuibulist.getKuibuCount();
		_kuiBuSwipeViewAdatper = new KuiBuSwipeViewAdapter(getSupportFragmentManager(), kuibulist);
		
		_ViewPager = (ViewPager) findViewById(R.id.pager);
        _ViewPager.setAdapter(_kuiBuSwipeViewAdatper);
        _ViewPager.setCurrentItem(index -1); //Due to the PullRefresh HeadView position, need to minus one.
        
        _ViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				_scrollState = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				_currentPagePos = _ViewPager.getCurrentItem();
				
				if(__prevPagePos == 0 && _currentPagePos == 0 && arg2 == 0)
					UIHelper.ToastMessage(KuiBuDetailActivity.this, R.string.alreadyfirstpage);
		
				if(__prevPagePos == _maxPageCount -1 && _currentPagePos == _maxPageCount -1 && arg2 == 0 )
					UIHelper.ToastMessage(KuiBuDetailActivity.this, R.string.alreadylasttpage);
				
				if(_scrollState == ViewPager.SCROLL_STATE_DRAGGING)
					__prevPagePos = _currentPagePos;
			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				Log.e(TAG, "KuiBuSwipeViewAdapter onPageChangeListener page " + arg0 + " was selected");
			}
        	
        });
        _kuiBuSwipeViewAdatper.notifyDataSetChanged();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
