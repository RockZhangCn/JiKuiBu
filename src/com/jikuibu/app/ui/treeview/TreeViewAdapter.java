package com.jikuibu.app.ui.treeview;

import java.util.ArrayList;

import com.jikuibu.app.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class TreeViewAdapter extends BaseAdapter implements OnItemClickListener 
{
	private ArrayList<Element> elementsData;
	private ArrayList<Element> elements;
	private LayoutInflater inflater;
	private int indentionBase;
	
	public TreeViewAdapter(ArrayList<Element> elements, ArrayList<Element> elementsData, LayoutInflater inflater) {
		this.elements = elements;
		this.elementsData = elementsData;
		this.inflater = inflater;
		indentionBase = 50;
	}
	
	public ArrayList<Element> getElements() {
		return elements;
	}
	
	public ArrayList<Element> getElementsData() {
		return elementsData;
	}
	
	@Override
	public int getCount() {
		return elements.size();
	}

	@Override
	public Object getItem(int position) {
		return elements.get(position);
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
		Element element = elements.get(position);
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
		Element element = (Element) getItem(position);
		//ArrayList<Element> elements = getElements();
		//ArrayList<Element> elementsData = getElementsData();

		if (!element.isHasChildren()) 
		{
            //Should Start another activity to show content lists.
			return;
		}
		
		if (element.isExpanded()) 
		{
			element.setExpanded(false);
			ArrayList<Element> elementsToDel = new ArrayList<Element>();
			for (int i = position + 1; i < elements.size(); i++) {
				if (element.getLevel() >= elements.get(i).getLevel())
					break;
				elementsToDel.add(elements.get(i));
			}
			elements.removeAll(elementsToDel);
			notifyDataSetChanged();
		} else {
			element.setExpanded(true);
			int i = 1;
			for (Element e : elementsData) {
				if (e.getParendId() == element.getId()) {
					e.setExpanded(false);
					elements.add(position + i, e);
					i ++;
				}
			}
			notifyDataSetChanged();
		}
	}
}
