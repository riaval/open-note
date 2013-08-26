package com.opennote.model.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.opennote.R;
import com.opennote.model.LocalItem;

public class LocalArrayAdapter extends ArrayAdapter<LocalItem> {
	private static final String tag = "LocalArrayAdapter";
	private Context context;
	private List<LocalItem> items = new ArrayList<LocalItem>();
	
	public LocalArrayAdapter(Context context, int textViewResourceId, List<LocalItem> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.items = objects;
	}

	// public int getCount() {
	// return this.countries.size();
	// }
	//
	// public Country getItem(int index) {
	// return this.countries.get(index);
	// }

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			// ROW INFLATION
			Log.d(tag, "Starting XML Row Inflation ... ");
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.simple_note_item, parent, false);
			Log.d(tag, "Successfully completed XML Row Inflation!");
		}

		// Get item
		LocalItem item = getItem(position);
		
		// Set date format
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM. HH:mm", Locale.US);
		
		// Set country name
		TextView textItem = (TextView) row.findViewById(R.id.local_title);
		TextView bodyItem = (TextView) row.findViewById(R.id.local_body);
		TextView dateItem = (TextView) row.findViewById(R.id.local_date);
		
		textItem.setText(item.getTitle());
		bodyItem.setText(item.getBody());
		dateItem.setText(dateFormat.format(item.getDate()));
		
		return row;
	}
	
}
