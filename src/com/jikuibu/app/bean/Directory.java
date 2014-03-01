package com.jikuibu.app.bean;

import java.io.Serializable;

public class Directory implements Serializable
{
	private static final long serialVersionUID = -164708515566412051L;
	private String contentText;//display name
	private int level; 
	private int id; //sequence number.
	private int parendId; //parent
	private boolean hasChildren; //is leaf? 
	private boolean isExpanded; //display state
	private int actionid;

	public static final int NO_PARENT = -1;
	
	public static final int TOP_LEVEL = 0;
	
	//private static int prevClicked = -1;
	
	public Directory(String contentText, int level, int id, int parendId, int actionid, boolean hasChildren, boolean isExpanded) 
    {
		super();
		this.contentText = contentText;
		this.level = level;
		this.id = id;
		this.parendId = parendId;
		this.actionid = actionid;
		this.hasChildren = hasChildren;
		this.isExpanded = isExpanded;
	}

	public interface OnClickListener
	{
		public void onClick(Directory dir);
	}
	
	private transient Directory.OnClickListener listener;
	
	public void setOnClickListener(Directory.OnClickListener listener)
	{
		this.listener =  listener;
	}
	
	public void OnClick()
	{
		if(listener != null)
			listener.onClick(this);
	}
	
	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	public String getContentText() {
		return contentText;
	}

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParendId() {
		return parendId;
	}

	public void setParendId(int parendId) {
		this.parendId = parendId;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public int getActionid() {
		return actionid;
	}

	public void setActionid(int actionid) {
		this.actionid = actionid;
	}
/*
	public static int getPrevClicked() {
		return prevClicked;
	}

	public static void setPrevClicked(int prevClicked) {
		Directory.prevClicked = prevClicked;
	}
*/
}
