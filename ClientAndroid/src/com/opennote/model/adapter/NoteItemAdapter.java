package com.opennote.model.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.opennote.R;
import com.opennote.model.provider.LocalContract.LocalNotes;

public class NoteItemAdapter extends CursorAdapter{

	public NoteItemAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);

	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView title = (TextView)view.findViewById(R.id.local_title);
		TextView body = (TextView)view.findViewById(R.id.local_body);
		TextView date = (TextView)view.findViewById(R.id.local_date);
		
		// Attach layout with database columns
		title.setText(cursor.getString(cursor.getColumnIndex(LocalNotes.TITLE)));
		body.setText(cursor.getString(cursor.getColumnIndex(LocalNotes.BODY)));
		date.setText(cursor.getString(cursor.getColumnIndex(LocalNotes.DATE)));
		
		// Set gone visibility for empty body
		if(body.getText().toString().isEmpty()){
			body.setVisibility(View.GONE);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.local_item, parent, false);
		bindView(v, context, cursor);
		return v;
	}


}
