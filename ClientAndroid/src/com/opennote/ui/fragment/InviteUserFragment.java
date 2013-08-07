package com.opennote.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;
import com.opennote.model.provider.RestContact.User;
import com.opennote.ui.activity.MainActivity;

public class InviteUserFragment extends Fragment {

	View rootView;
	ListView mListView;
	EditText mSearchEdit;
	
	private static final int LOADER_ID = 1;
	private final String[] PROJECTION = { 
			User._ID,
			User.LOGIN,
			User.FULL_NAME,
			User.DATE
	    };
	
	private SimpleCursorAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_invite_user, container, false);
		mListView = (ListView) rootView.findViewById(R.id.searchListView);
		
		// Set list adapter
		mAdapter = new SimpleCursorAdapter(
				getActivity(),
	            R.layout.item_search, 
	            null, 
	            new String[]{ User.LOGIN, User.FULL_NAME, User.DATE },
	            new int[]{ R.id.searchLoginTextView, R.id.searchFullNameTextView , R.id.searchDateTextView}, 
	            0);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new CreateInvitationClickListener());
		
		mSearchEdit = (EditText) rootView.findViewById(R.id.searchEdit);
		ImageButton searchBt = (ImageButton) rootView.findViewById(R.id.searchBt);

		searchBt.setOnClickListener(new FindUsersAction());
		return rootView;
	}
	
	private class FindUsersAction implements View.OnClickListener {
		@Override
		public void onClick(View view) {			
			String searchValue = mSearchEdit.getText().toString();

			// DataDroid-lib RequestManager
			RestRequestManager requestManager = RestRequestManager.from(getActivity());
			// Integrate session hash and group slug into request
			Request request = RequestFactory.getInviteUserRequest(MainActivity.instance.getSessionHash(), searchValue);
			// Add RequestListener
			requestManager.execute(request, mRequestListener);
		}		
	}
	
	private RequestListener mRequestListener = new RequestListener() {
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
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
        }

        @Override
        public void onLoaderReset(Loader<Cursor> arg0) {
            mAdapter.swapCursor(null);
        }
    };
    
    private class CreateInvitationClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
			final CharSequence[] items = {"Foo", "Bar", "Baz"};

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Select group");
			builder.setItems(items, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			         // Do something with the selection
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

}
