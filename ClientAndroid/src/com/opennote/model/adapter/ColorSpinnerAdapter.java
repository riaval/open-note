package com.opennote.model.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ColorSpinnerAdapter extends ArrayAdapter<String>{
	
	final public static String[] NAMES = {
		  "White"
		, "Cyan"
		, "Light Green"
		, "Yellow"
		, "Pink"
		, "Magenta"
	};
	
	final public static String[] COLORS = {
		  "#FFFFFF"
		, "#b1ede2"
		, "#cef4ac"
		, "#FCFDB1"
		, "#fbc2b7"
		, "#efb8dc"
	};
	
	public ColorSpinnerAdapter(Context context, int resource) {
		super(context, resource, NAMES);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return inflateView(position, convertView, parent);
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return inflateView(position, convertView, parent);
	}
	
	private View inflateView(int position, View convertView, ViewGroup parent){
		TextView view = (TextView) super.getView(position, convertView, parent);
		view.setText(NAMES[position]);
		view.setTextColor(Color.parseColor(COLORS[position]));
		return view;
	}

}
