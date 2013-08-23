package com.opennote.model.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.opennote.R;
import com.opennote.model.provider.LocalContact.LocalNotes;

public class NoteLocalAdapter extends SimpleCursorAdapter {
	
	Context mContext;
//	LayoutInflater mIInflater;
	
	public NoteLocalAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		mContext = context;
//		mIInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		
		Cursor cursor = getCursor();
		int isList = cursor.getInt(
				cursor.getColumnIndex(LocalNotes.LIST)
				);
		if(isList == 1){
			String[] bodyString = bodyTextView.getText().toString().split("\n");
//			String newBody = null;
			LinearLayout listBody = new LinearLayout(mContext);
			listBody.setOrientation(LinearLayout.VERTICAL);
			for(String s : bodyString){
				TextView listBodyItem = new TextView(mContext);
				listBodyItem.setTextSize(16);
				listBodyItem.setText(s.substring(1));
				if(s.substring(0, 1).equals("#")){
//					newBody += s.substring(1) + "<br>";
//					tv.setText(s.substring(1) + "\n");
//					listBodyItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.feedback, 0, 0, 0);
				} else {
					listBodyItem.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//					newBody += "<b>" + s.substring(1) + "</b>" + "<br>";
				}
				listBody.addView(listBodyItem);
			}
//			bodyTextView.setText(Html.fromHtml(newBody.substring(4, newBody.length()-1)));
//			View C = view.findViewById(R.id.local_body);
			((ViewGroup) bodyTextView.getParent()).removeView(bodyTextView);
			((ViewGroup) view.findViewById(R.id.selectableLocalItem)).addView(listBody, 1);
			
		}
		
		// set item color
		String color = cursor.getString(
				cursor.getColumnIndex(LocalNotes.COLOR)
				);
//		System.out.println(cursor.getColumnCount());
		view.setBackgroundColor(Color.parseColor(color));
		
		return view;
	}

}
