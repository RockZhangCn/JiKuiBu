package com.example.androidtreeviewdemo;

import java.util.ArrayList;

import com.example.androidtreeviewdemo.treeview.Element;
import com.example.androidtreeviewdemo.treeview.TreeView;
import com.example.androidtreeviewdemo.treeview.TreeViewAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;

public class MainActivity extends Activity {
	private ArrayList<Element> elements;
	private ArrayList<Element> elementsData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		init();
		
		TreeView treeview = (TreeView) findViewById(R.id.treeview);
		TreeViewAdapter treeViewAdapter = new TreeViewAdapter(
				elements, elementsData, inflater);
		treeview.setOnItemClickListener(treeViewAdapter);
		treeview.setAdapter(treeViewAdapter);	
	}
	
	private void init() {
		elements = new ArrayList<Element>();
		elementsData = new ArrayList<Element>();
	
		Element e1 = new Element("广东省", Element.TOP_LEVEL, 0, Element.NO_PARENT, true, false);
		
		Element e2 = new Element("�ൺ��", Element.TOP_LEVEL + 1, 1, e1.getId(), true, false);

		Element e3 = new Element("������", Element.TOP_LEVEL + 2, 2, e2.getId(), true, false);
		Element e4 = new Element("�����·", Element.TOP_LEVEL + 3, 3, e3.getId(), false, false);
		
		Element e5 = new Element("��̨��", Element.TOP_LEVEL + 1, 4, e1.getId(), true, false);

		Element e6 = new Element("֥���", Element.TOP_LEVEL + 2, 5, e5.getId(), true, false);
		Element e7 = new Element("���̨�ֵ�", Element.TOP_LEVEL + 3, 6, e6.getId(), false, false);

		Element e8 = new Element("������", Element.TOP_LEVEL + 1, 7, e1.getId(), false, false);

		Element e9 = new Element("�㶫ʡ", Element.TOP_LEVEL, 8, Element.NO_PARENT, true, false);

		Element e10 = new Element("������", Element.TOP_LEVEL + 1, 9, e9.getId(), true, false);

		Element e11 = new Element("��ɽ��", Element.TOP_LEVEL + 2, 10, e10.getId(), true, false);

		Element e12 = new Element("���ϴ��", Element.TOP_LEVEL + 3, 11, e11.getId(), true, false);
	
		Element e13 = new Element("10000��", Element.TOP_LEVEL + 4, 12, e12.getId(), false, false);
		
		elements.add(e1);
		elements.add(e9);

		elementsData.add(e1);
		elementsData.add(e2);
		elementsData.add(e3);
		elementsData.add(e4);
		elementsData.add(e5);
		elementsData.add(e6);
		elementsData.add(e7);
		elementsData.add(e8);
		elementsData.add(e9);
		elementsData.add(e10);
		elementsData.add(e11);
		elementsData.add(e12);
		elementsData.add(e13);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
