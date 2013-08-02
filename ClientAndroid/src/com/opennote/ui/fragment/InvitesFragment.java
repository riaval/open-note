package com.opennote.ui.fragment;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;
import com.opennote.model.provider.RestContact.Invitation;
import com.opennote.ui.activity.MainActivity;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class InvitesFragment extends Fragment {
	private View mRootView;
	private SimpleCursorAdapter mAdapter;
	private static final int LOADER_ID = 1;
	private final String[] PROJECTION = { 
			Invitation._ID,
			Invitation.USER_LOGIN,
			Invitation.USER_NAME,
			Invitation.GROUP_SLUG,
			Invitation.GROUP_NAME
	    };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.activity_search_result, container, false);

		// DataDroid-lib RequestManager
		RestRequestManager requestManager = RestRequestManager.from(getActivity());
		// Integrate session hash and group slug into request
		Request request = RequestFactory.getLoadInvitationsRequest(MainActivity.mainActivity.getSessionHash());
		// Add RequestListener
		requestManager.execute(request, mRequestListener);
		
		return mRootView;
	}
	
	private RequestListener mRequestListener = new RequestListener() {
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			mAdapter = new SimpleCursorAdapter(
	        		getActivity(),
		            R.layout.item_invitation, 
		            null, 
		            new String[]{ Invitation.USER_LOGIN, Invitation.USER_NAME, Invitation.GROUP_SLUG, Invitation.GROUP_NAME },
		            new int[]{ R.id.invitationLogin, R.id.invitationFullName , R.id.invitationSlug, R.id.invitationGroupName}, 
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
                Invitation.CONTENT_URI,
                PROJECTION,
                null,
                null,
                Invitation._ID + " DESC"
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

}
