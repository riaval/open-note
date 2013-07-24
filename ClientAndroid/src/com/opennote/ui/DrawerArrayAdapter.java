package com.opennote.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.opennote.R;

public class DrawerArrayAdapter extends ArrayAdapter<DrawerListItem> {
	private static final String tag = "CountryArrayAdapter";
	private Context context;
	private List<DrawerListItem> items = new ArrayList<DrawerListItem>();

	public DrawerArrayAdapter(Context context, int textViewResourceId, List<DrawerListItem> objects) {
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
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.drawer_list_item, parent, false);
			Log.d(tag, "Successfully completed XML Row Inflation!");
		}

		// Get item
		DrawerListItem item = getItem(position);

		// Set country name
		TextView textItem = (TextView) row.findViewById(R.id.list_item_title);
		ImageView iconItem = (ImageView) row.findViewById(R.id.list_item_icon);
		textItem.setText(item.getText());
		iconItem.setImageResource(item.getIcon());
//		listItem.setCompoundDrawablesWithIntrinsicBounds(item.getIcon(), 0, 0, 0); // R.drawable.ic_action_attach

		return row;
	}
}