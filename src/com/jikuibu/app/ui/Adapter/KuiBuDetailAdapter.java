package com.jikuibu.app.ui.Adapter;

import com.jikuibu.app.ui.KuiBuDetailFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class KuiBuDetailAdapter extends FragmentStatePagerAdapter {

	public KuiBuDetailAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		Fragment fragment = new KuiBuDetailFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putString(KuiBuDetailFragment.ARG_STRING_TITILE, "Passed titile");
        args.putString(KuiBuDetailFragment.ARG_STIRNG_CONTENT, "Passed content");
        fragment.setArguments(args);
        return fragment;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

}
