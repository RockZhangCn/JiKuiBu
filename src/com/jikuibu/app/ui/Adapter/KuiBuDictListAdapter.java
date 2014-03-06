package com.jikuibu.app.ui.Adapter;

import com.jikuibu.app.R;
import com.jikuibu.app.bean.Directory;
import com.jikuibu.app.bean.KuiBuDict;
import com.jikuibu.app.bean.KuiBuDictList;
import com.jikuibu.app.ui.Adapter.TreeViewAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class KuiBuDictListAdapter extends BaseAdapter 
{
	private Context context;
	private KuiBuDictList kuibuList;
	
	private int listItemViewRes;
	private LayoutInflater inflater;
	
	public KuiBuDictListAdapter(Context context, KuiBuDictList kuibuList, int resourceId)
	{
		this.context  = context;
		this.kuibuList =  kuibuList;
		this.listItemViewRes = resourceId;
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return kuibuList.getKuiBulist().size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return kuibuList.getKuiBulist().get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(listItemViewRes, null);
			//holder.disclosureImg = (ImageView) convertView.findViewById(R.id.disclosureImg);
			holder.contentText = (TextView) convertView.findViewById(R.id.kuibucontentText);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		KuiBuDict kuibu = kuibuList.getKuiBulist().get(position);

		holder.contentText.setText(kuibu.getTitle());
		
		return convertView;
	}
	
	static class ViewHolder
	{
		//ImageView disclosureImg;
		TextView contentText;
	}

}
