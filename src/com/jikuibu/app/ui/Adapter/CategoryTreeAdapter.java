package com.jikuibu.app.ui.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jikuibu.app.R;
import com.jikuibu.app.bean.CategoryTree;
import com.jikuibu.app.bean.Directory;


public class CategoryTreeAdapter extends BaseAdapter 
{
	private Context activityContext;
	CategoryTree directoryList;
	private LayoutInflater inflater;
	private int listitemResourceId;
	private static final int indentionBase = 70;
	private static final String TAG = "TreeViewAdapter";
	
	public CategoryTreeAdapter(Context context, CategoryTree directoryList, int resourceId) {
		this.activityContext = context; 
		this.directoryList = directoryList;
		this.listitemResourceId  = resourceId;
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		if(directoryList == null)
			return 0;
		else
			return directoryList.getDisplayDirectoryList().size();
	}

	@Override
	public Object getItem(int position) {
		if(directoryList == null)
			return null;
		else
			return directoryList.getDisplayDirectoryList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(listitemResourceId, null);
			holder.disclosureImg = (ImageView) convertView.findViewById(R.id.disclosureImg);
			holder.contentText = (TextView) convertView.findViewById(R.id.contentText);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Directory element = directoryList.getDisplayDirectoryList().get(position);
		
		int level = element.getLevel();
		holder.disclosureImg.setPadding(
				indentionBase * level, 
				holder.disclosureImg.getPaddingTop(), 
				holder.disclosureImg.getPaddingRight(), 
				holder.disclosureImg.getPaddingBottom());
		holder.contentText.setText(element.getContentText());
		if (element.isHasChildren() && !element.isExpanded()) {
			holder.disclosureImg.setImageResource(R.drawable.close);
			holder.disclosureImg.setVisibility(View.VISIBLE);
		} else if (element.isHasChildren() && element.isExpanded()) {
			holder.disclosureImg.setImageResource(R.drawable.open);
			holder.disclosureImg.setVisibility(View.VISIBLE);
		} else if (!element.isHasChildren()) {
			holder.disclosureImg.setImageResource(R.drawable.nomore);
			holder.disclosureImg.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	
	static class ViewHolder
	{
		ImageView disclosureImg;
		TextView contentText;
	}
}
