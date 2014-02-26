package com.jikuibu.app.bean;

import java.io.Serializable;

public class Directory implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -164708515566412051L;
	private String contentText;
	private int level;
	private int id;
	private int parendId;
	private boolean hasChildren;
	private boolean isExpanded;

	public static final int NO_PARENT = -1;
	
	public static final int TOP_LEVEL = 0;
	
	public Directory(String contentText, int level, int id, int parendId,
			boolean hasChildren, boolean isExpanded) {
		super();
		this.contentText = contentText;
		this.level = level;
		this.id = id;
		this.parendId = parendId;
		this.hasChildren = hasChildren;
		this.isExpanded = isExpanded;
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
}