package com.opennote.ui.local;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.opennote.R;
import com.opennote.ui.createnote.CreateNoteActivity;

public class LocalFragment extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.note_fragment, container, false);
		
		LocalItem item1 = new LocalItem("Lorem Ipsum is simply dummy text", "When an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the");
		LocalItem item2 = new LocalItem("There are many variations of passages", "College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum");
		List<LocalItem> list = new ArrayList<LocalItem>();
		list.add(item1);
		list.add(item2);
		
		LocalArrayAdapter accauntAdapter = new LocalArrayAdapter(getActivity(), R.layout.local_item, list);

        ListView listView = (ListView)rootView;
        listView.setAdapter(accauntAdapter);
		
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.action_new);
		item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(getActivity(), CreateNoteActivity.class);
				startActivity(intent);
				return true;
			}
		});
	}

}
