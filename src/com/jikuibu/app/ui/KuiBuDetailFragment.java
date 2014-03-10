package com.jikuibu.app.ui;

import com.jikuibu.app.R;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("NewApi")
public class KuiBuDetailFragment extends Fragment {
	private static final String TAG = KuiBuDetailFragment.class.getSimpleName();
	
	public static final String ARG_STRING_TITILE = "KUIBU_TITLE";
	public static final String ARG_STIRNG_CONTENT = "KUIBU_CONTENT";
	public static final String ARG_STIRNG_KUIBUID = "KUIBU_IDENT";
	

	
	private TextView _contentTextView;
	private String _contentTextData;
	
	private int _kuibuId;
	private Handler _handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			_contentTextView.setText(_contentTextData);
			super.handleMessage(msg);
		}
		
	};

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		Log.e(TAG, "KuiBuDetailFragment onCreateView we begin to create view, and set the data to display");
		View rootView = inflater.inflate(R.layout.kuibudetailfragment, container, false);
	    Bundle args = getArguments();
	    
	    TextView title  = (TextView) rootView.findViewById(R.id.kuibudetail_title);  
        _contentTextView = (TextView) rootView.findViewById(R.id.kuibudetail_content);
        	
        title.getBackground().setAlpha(100);
        _contentTextView.getBackground().setAlpha(100);
	    title.setText(args.getString(ARG_STRING_TITILE));
	    
	    _kuibuId = args.getInt(ARG_STIRNG_KUIBUID);
	    this.initContentData();
		return rootView;
	}

	private void initContentData() {
		// TODO Auto-generated method stub
		new Thread()
		{
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				_contentTextData = "some string get from internet for " + _kuibuId + " KuiBu";
				_handler.sendEmptyMessage(_kuibuId);
				super.run();
			}
			
		}.start();
		
	}

}
