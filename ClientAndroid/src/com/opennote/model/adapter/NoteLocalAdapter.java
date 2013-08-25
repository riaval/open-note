package com.opennote.model.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.opennote.R;
import com.opennote.model.provider.LocalContact.LocalNotes;

public class NoteLocalAdapter extends SimpleCursorAdapter {
	
	Context mContext;
	
	public NoteLocalAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		mContext = context;
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
			LinearLayout listBody = new LinearLayout(mContext);
			listBody.setOrientation(LinearLayout.VERTICAL);
			for(String s : bodyString){
				LayoutInflater inflater = LayoutInflater.from(mContext);
				LinearLayout listBodyItem = (LinearLayout) inflater.inflate(R.layout.list_note_view_item, null, false);
				ImageView listBodyImgView = (ImageView) listBodyItem.findViewById(R.id.list_img);
				TextView listBodyTextView = (TextView) listBodyItem.findViewById(R.id.list_text);
				listBodyTextView.setText(s.substring(1));
				if(s.substring(0, 1).equals("#")){
					listBodyImgView.setBackgroundResource(R.drawable.list_item_empty);
				} else {
					listBodyTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
					listBodyImgView.setBackgroundResource(R.drawable.list_item_ok);
				}
				listBody.addView(listBodyItem);
			}
			((ViewGroup) bodyTextView.getParent()).removeView(bodyTextView);
			((ViewGroup) view.findViewById(R.id.selectableLocalItem)).addView(listBody, 1);
			
		}
		
		// set item color
		String color = cursor.getString(
				cursor.getColumnIndex(LocalNotes.COLOR)
				);
		view.setBackgroundColor(Color.parseColor(color));
		
		return view;
	}

}
