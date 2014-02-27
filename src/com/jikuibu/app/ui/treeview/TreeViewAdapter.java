package com.jikuibu.app.ui.treeview;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.jikuibu.app.R;
import com.jikuibu.app.bean.DirectoryList;
import com.jikuibu.app.bean.Directory;


public class TreeViewAdapter extends BaseAdapter implements OnItemClickListener 
{
	private List<Directory> allDirectories;
	private List<Directory> displayDirectories;
	private LayoutInflater inflater;
	private int indentionBase;
	
	public TreeViewAdapter(DirectoryList directoryList, LayoutInflater inflater) {
		this.allDirectories = directoryList.getDirectoryList();
		this.inflater = inflater;
		indentionBase = 50;
		initailDisplayDirectories();
	}
	
	
	private void initailDisplayDirectories()
	{
		if(allDirectories == null)
			allDirectories = new ArrayList<Directory>();
		
		displayDirectories =  new ArrayList<Directory>();
		for(Directory directory : allDirectories)
		{
			if(directory.getLevel() == 0)
				displayDirectories.add(directory);
		}
		
	}
	public List<Directory> getElements() {
		return displayDirectories;
	}
	
	public List<Directory> getElementsData() {
		return allDirectories;
	}
	
	@Override
	public int getCount() {
		return displayDirectories.size();
	}

	@Override
	public Object getItem(int position) {
		return displayDirectories.get(position);
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
			convertView = inflater.inflate(R.layout.treeview_item, null);
			holder.disclosureImg = (ImageView) convertView.findViewById(R.id.disclosureImg);
			holder.contentText = (TextView) convertView.findViewById(R.id.contentText);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Directory element = displayDirectories.get(position);
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,long id) 
	{
		// TODO Auto-generated method stub
		Directory element = (Directory) getItem(position);
		if (!element.isHasChildren()) 
		{
            //Should Start another activity to show content lists.
			return;
		}
		
		if (element.isExpanded()) 
		{
			element.setExpanded(false);
			ArrayList<Directory> elementsToDel = new ArrayList<Directory>();
			for (int i = position + 1; i < displayDirectories.size(); i++) {
				if (element.getLevel() >= displayDirectories.get(i).getLevel())
					break;
				elementsToDel.add(displayDirectories.get(i));
			}
			displayDirectories.removeAll(elementsToDel);
			notifyDataSetChanged();
		} else {
			element.setExpanded(true);
			int i = 1;
			for (Directory e : allDirectories) {
				if (e.getParendId() == element.getId()) {
					e.setExpanded(false);
					displayDirectories.add(position + i, e);
					i ++;
				}
			}
			notifyDataSetChanged();
		}
	}
}
