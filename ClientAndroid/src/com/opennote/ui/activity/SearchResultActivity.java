package com.opennote.ui.activity;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;
import com.opennote.model.provider.RestContact.User;
import com.opennote.ui.fragment.FindUserFragment;

public class SearchResultActivity extends Activity {
	private String mLogin;
	private String mFullName;
	
	private static final int LOADER_ID = 1;
	private final String[] PROJECTION = { 
			User._ID,
			User.LOGIN,
			User.FULL_NAME,
			User.DATE
	    };
	
	private SimpleCursorAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		
		ListView listView = (ListView) findViewById(R.id.searchListView);
		
		// Get the message from the intent
	    Intent intent = getIntent();
	    mLogin = intent.getStringExtra(FindUserFragment.LOGIN_VALUE_MESSAGE);
		mFullName = intent.getStringExtra(FindUserFragment.FULL_NAME_VALUE_MESSAGE);
		
		// DataDroid-lib RequestManager
		RestRequestManager requestManager = RestRequestManager.from(this);
		// Integrate session hash and group slug into request
		Request request = RequestFactory.getFindUsersRequest(MainActivity.instance.getSessionHash(), mLogin, mFullName);
		// Add RequestListener
		requestManager.execute(request, mRequestListener);

	}
	
	private RequestListener mRequestListener = new RequestListener() {
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			// Set list adapter
			mAdapter = new SimpleCursorAdapter(
					SearchResultActivity.this,
		            R.layout.item_search, 
		            null, 
		            new String[]{ User.LOGIN, User.FULL_NAME, User.DATE },
		            new int[]{ R.id.searchLoginTextView, R.id.searchFullNameTextView , R.id.searchDateTextView}, 
		            0);
	        ListView listView = (ListView) findViewById(R.id.searchListView);
	        listView.setAdapter(mAdapter);
	        getLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
	        
	        Toast.makeText(SearchResultActivity.this, "onRequestFinished", 5).show();
		}

		@Override
		public void onRequestConnectionError(Request request, int statusCode) {
			Toast.makeText(SearchResultActivity.this, "onRequestConnectionError", 5).show();
		}

		@Override
		public void onRequestDataError(Request request) {
			Toast.makeText(SearchResultActivity.this, "onRequestDataError", 5).show();
		}

		@Override
		public void onRequestCustomError(Request request, Bundle resultData) {
			Toast.makeText(SearchResultActivity.this, "onRequestCustomError", 5).show();
		}
		
		private LoaderCallbacks<Cursor> loaderCallbacks = new LoaderCallbacks<Cursor>() {

	        @Override
	        public Loader<Cursor> onCreateLoader(int loaderId, Bundle arg1) {
	            return new CursorLoader(
	            	SearchResultActivity.this,
	                User.CONTENT_URI,
	                PROJECTION,
	                null,
	                null,
	                User._ID + " DESC"
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
		
	};
	
}
