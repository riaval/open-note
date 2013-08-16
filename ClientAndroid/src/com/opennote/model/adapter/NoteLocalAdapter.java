package com.opennote.model.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.opennote.R;

public class NoteLocalAdapter extends SimpleCursorAdapter {

	public NoteLocalAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = null;
		View view = super.getView(position, convertView, parent);
		((LinearLayout) view.findViewById(R.id.selectableLocalItem)).setBackgroundResource(android.R.color.transparent);
		
		// If body is empty set GONE visibility
		TextView bodyTextView = (TextView) view.findViewById(R.id.local_body);
		if(bodyTextView.getText().toString().isEmpty()){
			bodyTextView.setVisibility(View.GONE);
		}
		
		// set item color
		Cursor cursor = getCursor();
		int lastIndex = cursor.getColumnCount() - 1;
		String color = cursor.getString(lastIndex);
		view.setBackgroundColor(Color.parseColor(color));
		
		return view;
	}

}
