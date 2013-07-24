package com.opennote.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.res.TypedArray;

public class DrawerListItem {
	private String text;
	private int icon;
	private int value;

	public final static int ICON_ITEM = 0;
	public final static int TEXT_ITEM = 1;
	public final static int VALUE_ITEM = 2;
	
	public DrawerListItem() {
	}

	public DrawerListItem(String text, int icon) {
		this.text = text;
		this.icon = icon;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	static List<DrawerListItem> generateItems(String[] texts, TypedArray icons) {
		List<DrawerListItem> list = new ArrayList<DrawerListItem>();
		for (int i = 0; i < texts.length; i++) {
			DrawerListItem item = new DrawerListItem(texts[i], icons.getResourceId(i, -1));
			list.add(item);
		}
		
		return list;
	}

}
