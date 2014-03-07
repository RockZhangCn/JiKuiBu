package com.jikuibu.app.ui;

import com.jikuibu.app.R;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("NewApi")
public class KuiBuDetailFragment extends Fragment {

	public static final String ARG_STRING_TITILE = "KUIBU_TITLE";
	public static final String ARG_STIRNG_CONTENT = "KUIBU_CONTENT";
	//public static final String ARG_OBJECT = "KUIBU_TITLE";
	//public static final String ARG_OBJECT = "KUIBU_TITLE";
	//public static final String ARG_OBJECT = "KUIBU_TITLE";
	//public static final String ARG_OBJECT = "KUIBU_TITLE";
	

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View rootView = inflater.inflate(R.layout.kuibudetailfragment, container, false);
	    Bundle args = getArguments();
        ((TextView) rootView.findViewById(R.id.kuibudetail_title)).setText
        		("KuiBu titile");
        
        ((TextView) rootView.findViewById(R.id.kuibudetail_content)).setText
				("KuiBu content");
		return super.onCreateView(inflater, container, savedInstanceState);
		
		
	}
	
	

}
