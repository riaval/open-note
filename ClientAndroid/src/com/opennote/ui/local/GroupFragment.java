package com.opennote.ui.local;

import android.app.ListFragment;
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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;
import com.opennote.model.provider.RestContact.Note;
import com.opennote.ui.createnote.CreateNoteActivity;

public class GroupFragment extends ListFragment {
	private SimpleCursorAdapter mAdapter;
	private static final int LOADER_ID = 1;
	private final String[] PROJECTION = { 
			Note._ID,
			Note.TITLE,
			Note.BODY,
			Note.DATE,
			Note.USER
	    };
	
	private View mRootView;
	private String mSessionHash;
	private String mGroupSlug;
	
	public static final String NOT_LOCAL = "not_local";
	public static final String GROUP_SLUG = "group_slug";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_note_list, container, false);
		
		loadNotes();
		
		return mRootView;
	}
	
	public void setValues(String sessionHash, String groupSlug){
		mSessionHash = sessionHash;
		mGroupSlug = groupSlug;
	}
	
	public void loadNotes(){
		// DataDroid-lib RequestManager
		RestRequestManager requestManager = RestRequestManager.from(getActivity());
		// Integrate session hash and group slug into request
		Request request = RequestFactory.getLoadNotesRequest(mSessionHash, mGroupSlug);
		// Add RequestListener
		requestManager.execute(request, mRequestListener);
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
				intent.putExtra(NOT_LOCAL, true);
				intent.putExtra(GROUP_SLUG, mGroupSlug);
				startActivity(intent);
				return true;
			}
		});
	}
	
	private RequestListener mRequestListener = new RequestListener() {
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			mAdapter = new SimpleCursorAdapter(
	        		getActivity(),
		            R.layout.local_item, 
		            null, 
		            new String[]{ Note.TITLE, Note.BODY, Note.DATE, Note.USER },
		            new int[]{ R.id.local_title, R.id.local_body , R.id.local_date, R.id.local_author}, 
		            0);
			ListView listView = (ListView) mRootView;
	        listView.setAdapter(mAdapter);
//	        listView.setOnItemClickListener(new LocalListClickListener());
	        getLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
			Toast.makeText(getActivity(), "onRequestFinished", 5).show();
		}

		@Override
		public void onRequestConnectionError(Request request, int statusCode) {
			Toast.makeText(getActivity(), "onRequestConnectionError", 5).show();
		}

		@Override
		public void onRequestDataError(Request request) {
			Toast.makeText(getActivity(), "onRequestDataError", 5).show();
		}

		@Override
		public void onRequestCustomError(Request request, Bundle resultData) {
			Toast.makeText(getActivity(), "onRequestCustomError", 5).show();
		}
		
	};
	
	private LoaderCallbacks<Cursor> loaderCallbacks = new LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, Bundle arg1) {
            return new CursorLoader(
                getActivity(),
                Note.CONTENT_URI,
                PROJECTION,
                Note.GROUP + " = ?",
                new String[]{mGroupSlug},
                Note._ID + " DESC"
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
            mAdapter.swapCursor(cursor);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> arg0) {
            mAdapter.swapCursor(null);
        }
    };
	
//	private class LocalListClickListener implements ListView.OnItemClickListener {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//			String title = ((TextView) view.findViewById(R.id.local_title)).getText().toString();
//			String body = ((TextView) view.findViewById(R.id.local_body)).getText().toString();
//			
//			// Starting update activity
//			Intent intent = new Intent(getActivity(), CreateNoteActivity.class);
//			intent.putExtra(ID_VALUE_MESSAGE, id);
//			intent.putExtra(TITLE_VALUE_MESSAGE, title);
//			intent.putExtra(BODY_VALUE_MESSAGE, body);
//			startActivity(intent);
//		}
//	}

}                                                                                                                                                                           