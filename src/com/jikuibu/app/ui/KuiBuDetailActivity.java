package com.jikuibu.app.ui;

import com.jikuibu.app.R;
import com.jikuibu.app.bean.KuiBuDictList;
import com.jikuibu.app.ui.Adapter.KuiBuSwipeViewAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;



public class KuiBuDetailActivity extends FragmentActivity {

	private static final String TAG = "KuiBuDetailActivity";
	private KuiBuSwipeViewAdapter _kuiBuSwipeViewAdatper;
	private ViewPager _ViewPager;
	
	
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
		KuiBuDictList kuibulist = (KuiBuDictList)bundle.getSerializable("KUIBULIST");
		
		Log.e(TAG, "We receive " + kuibulist);
		_kuiBuSwipeViewAdatper = new KuiBuSwipeViewAdapter(getSupportFragmentManager(), kuibulist);
		
		_ViewPager = (ViewPager) findViewById(R.id.pager);
        _ViewPager.setAdapter(_kuiBuSwipeViewAdatper);
        _ViewPager.setCurrentItem(index -1); //Due to the PullRefresh HeadView position, need to minus one.
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
