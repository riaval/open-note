package com.opennote.model.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.opennote.R;

public class NoteGroupAdapter extends SimpleCursorAdapter{

	boolean mCreator;
	String mCurrentLogin;
	
	public NoteGroupAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = null;
		View view = super.getView(position, convertView, parent);
		
		// If body is empty set GONE visibility
		TextView bodyTextView = (TextView) view.findViewById(R.id.local_body);
		if(bodyTextView.getText().toString().isEmpty()){
			bodyTextView.setVisibility(View.GONE);
		}
		
		// set item color
		Cursor cursor = getCursor();
		int colorIndex = cursor.getColumnCount() - 2;
		int color = cursor.getInt(colorIndex);
		view.setBackgroundColor(color);
		
		return view;
	}

	@Override
	public boolean isEnabled(int position){
		if(mCreator)
			return true;
		Cursor cursor = (Cursor) getItem(position);
		String login = cursor.getString(6);
		if( login.equals(mCurrentLogin) )
			return true;
		
		return false;
	}
	
	public boolean ismCreator() {
		return mCreator;
	}
	public void setmCreator(boolean creator) {
		this.mCreator = creator;
	}

	public String getmCurrentLogin() {
		return mCurrentLogin;
	}
	public void setmCurrentLogin(String currentLogin) {
		this.mCurrentLogin = currentLogin;
	}
	
}
