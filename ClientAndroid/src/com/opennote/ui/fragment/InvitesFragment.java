package com.opennote.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;
import com.opennote.model.provider.RestContact.Invitation;
import com.opennote.ui.activity.MainActivity;

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
		Request request = RequestFactory.getLoadInvitationsRequest(MainActivity.instance.getSessionHash());
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
//			System.out.println();
	        listView.setAdapter(mAdapter);
	        listView.setOnItemClickListener(new InvitationClickListener());
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
    
    private class InvitationClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
			Cursor cursor = mAdapter.getCursor();
			cursor.moveToPosition(position);
			final String inviteId = cursor.getString(0);
			
			AlertDialog.Builder alertDialog;
			alertDialog = new AlertDialog.Builder(getActivity());
			String groupName = ((TextView) view.findViewById(R.id.invitationGroupName)).getText().toString();
			String invitationAuthor = ((TextView) view.findViewById(R.id.invitationFullName)).getText().toString();
			alertDialog.setTitle("Accept invitation");
			alertDialog.setMessage("Group: " + groupName + "\n" + "From: " + invitationAuthor);
			
			String joinToGroup = "Join to group";
			String deleteInvitation = "Delete invitation";
			alertDialog.setPositiveButton(joinToGroup, new OnClickListener() {
				public void onClick(DialogInterface dialog, int arg1) {
					Toast.makeText(getActivity(), "joinToGroup", Toast.LENGTH_LONG).show();
				}
			});
			alertDialog.setNegativeButton(deleteInvitation, new OnClickListener() {
				public void onClick(DialogInterface dialog, int arg1) {
					// DataDroid-lib RequestManager
					RestRequestManager requestManager = RestRequestManager.from(getActivity());
					// Integrate session hash and group slug into request
					Request request = RequestFactory.getDeleteInvitationsRequest(MainActivity.instance.getSessionHash(), inviteId);
					// Add RequestListener
					requestManager.execute(request, mRequestListener);
					Toast.makeText(getActivity(), "deleteInvitation", Toast.LENGTH_LONG).show();
				}
			});
			alertDialog.show();
		}
	}

}
