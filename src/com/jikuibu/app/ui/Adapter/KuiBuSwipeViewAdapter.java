package com.jikuibu.app.ui.Adapter;

import java.util.List;

import com.jikuibu.app.bean.KuiBuDict;
import com.jikuibu.app.bean.KuiBuList;
import com.jikuibu.app.ui.KuiBuDetailFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;


public class KuiBuSwipeViewAdapter extends FragmentStatePagerAdapter {
	
	private static final String TAG = KuiBuSwipeViewAdapter.class.getSimpleName();

	private List<KuiBuDict> _kuiBuList;

	public KuiBuSwipeViewAdapter(FragmentManager fm, KuiBuList kuibulist) {
		super(fm);
		_kuiBuList = kuibulist.getKuiBulist();
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Log.e(TAG, "KuiBuSwipeViewAdapter getItem We request postion : " + position);
		Fragment fragment = new KuiBuDetailFragment();
        Bundle args = new Bundle();
       
        args.putString(KuiBuDetailFragment.ARG_STRING_TITILE,  _kuiBuList.get(position).getTitle());
        args.putString(KuiBuDetailFragment.ARG_STIRNG_CONTENT, _kuiBuList.get(position).getKuibuContent());
        args.putInt(KuiBuDetailFragment.ARG_STIRNG_KUIBUID, _kuiBuList.get(position).getKuibuid());
       
        fragment.setArguments(args);
        return fragment;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _kuiBuList.size();
	}

}
