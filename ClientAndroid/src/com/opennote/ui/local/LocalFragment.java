package com.opennote.ui.local;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.opennote.R;
import com.opennote.model.provider.Contract.LocalNotes;
import com.opennote.ui.createnote.CreateNoteActivity;

public class LocalFragment extends Fragment {
	public static final String ID_VALUE_MESSAGE = "id";
	public static final String TITLE_VALUE_MESSAGE = "title";
	public static final String BODY_VALUE_MESSAGE = "body";
	
	private static final int LOADER_ID = 1;
	private final String[] PROJECTION = { 
			LocalNotes._ID,
			LocalNotes.TITLE,
			LocalNotes.BODY,
			LocalNotes.DATE
	    };
	private SimpleCursorAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.note_fragment, container, false);
		
//		LocalItem item1 = new LocalItem("Lorem Ipsum is simply dummy text", "When an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the");
//		LocalItem item2 = new LocalItem("There are many variations of passages", "College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum");
//		List<LocalItem> list = new ArrayList<LocalItem>();
//		list.add(item1);
//		list.add(item2);
//		
//		LocalArrayAdapter accauntAdapter = new LocalArrayAdapter(getActivity(), R.layout.local_item, list);
//
//        ListView listView = (ListView)rootView;
//        listView.setAdapter(accauntAdapter);
        
        adapter = new SimpleCursorAdapter(
        		getActivity(),
	            R.layout.local_item, 
	            null, 
	            new String[]{ LocalNotes.TITLE, LocalNotes.BODY, LocalNotes.DATE },
	            new int[]{ R.id.local_title, R.id.local_body , R.id.local_date}, 
	            0);
        ListView listView = (ListView)rootView;
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new LocalListClickListener());
        
        getLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
        
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
	
    private LoaderCallbacks<Cursor> loaderCallbacks = new LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, Bundle arg1) {
            return new CursorLoader(
                getActivity(),
                LocalNotes.CONTENT_URI,
                PROJECTION,
                null,
                null,
                LocalNotes._ID + " DESC"
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
            adapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> arg0) {
            adapter.swapCursor(null);
        }
    };
    
    private class LocalListClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String title = ((TextView) view.findViewById(R.id.local_title)).getText().toString();
			String body = ((TextView) view.findViewById(R.id.local_body)).getText().toString();
			
			// Starting update activity
			Intent intent = new Intent(getActivity(), CreateNoteActivity.class);
			intent.putExtra(ID_VALUE_MESSAGE, id);
			intent.putExtra(TITLE_VALUE_MESSAGE, title);
			intent.putExtra(BODY_VALUE_MESSAGE, body);
			startActivity(intent);
		}
	}
    
}
