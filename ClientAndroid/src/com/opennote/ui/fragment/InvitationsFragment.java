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
import com.opennote.model.provider.RestContact;
import com.opennote.model.provider.RestContact.Invitation;
import com.opennote.ui.activity.MainActivity;

public class InvitationsFragment extends Fragment {
	private View mRootView;
	ListView mListView;
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
		mRootView = inflater.inflate(R.layout.fragment_invitations, container, false);

		// DataDroid
		RestRequestManager requestManager = RestRequestManager.from(getActivity());
		Request request = RequestFactory.getLoadInvitationsRequest(MainActivity.instance.getSessionHash());
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
			mListView = (ListView) mRootView.findViewById(R.id.searchListView);
	        mListView.setAdapter(mAdapter);
	        mListView.setOnItemClickListener(new InvitationClickListener());
	        getLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
		}

		@Override
		public void onRequestConnectionError(Request request, int statusCode) {
			mRootView.findViewById(R.id.connectionError).setVisibility(View.VISIBLE);
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
        	mRootView.findViewById(R.id.connectionError).setVisibility(View.GONE);
        	mRootView.findViewById(R.id.progressBarLayout).setVisibility(View.GONE);
        	if(cursor.getCount() != 0){
        		mListView.setVisibility(View.VISIBLE);
        	} else {
        		mListView.setVisibility(View.GONE);
        		mRootView.findViewById(R.id.nothing).setVisibility(View.VISIBLE);
        	}
            mAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> arg0) {
            mAdapter.swapCursor(null);
        }
    };
    
    private String mGroupSlug;
    private String mGroupName;
    
    private class InvitationClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
			Cursor cursor = mAdapter.getCursor();
			cursor.moveToPosition(position);
			final String inviteId = cursor.getString(0);
			
			AlertDialog.Builder alertDialog;
			alertDialog = new AlertDialog.Builder(getActivity());
			mGroupSlug = ((TextView) view.findViewById(R.id.invitationSlug)).getText().toString();
			mGroupName = ((TextView) view.findViewById(R.id.invitationGroupName)).getText().toString();
			String invitationAuthor = ((TextView) view.findViewById(R.id.invitationFullName)).getText().toString();
			alertDialog.setTitle("Accept invitation");
			alertDialog.setMessage("Group: " + mGroupName + "\n" + "From: " + invitationAuthor);
			
			String joinToGroup = "Join to group";
			String deleteInvitation = "Delete invitation";
			alertDialog.setPositiveButton(joinToGroup, new OnClickListener() {
				public void onClick(DialogInterface dialog, int arg1) {
					RestRequestManager requestManager = RestRequestManager.from(getActivity());
					Request request = RequestFactory.getAcceptInvitationsRequest(MainActivity.instance.getSessionHash(), mGroupSlug, mGroupName);
					requestManager.execute(request, invitationAcceptListener);
				}
			});
			alertDialog.setNegativeButton(deleteInvitation, new OnClickListener() {
				public void onClick(DialogInterface dialog, int arg1) {
					RestRequestManager requestManager = RestRequestManager.from(getActivity());
					Request request = RequestFactory.getDeleteInvitationsRequest(MainActivity.instance.getSessionHash(), inviteId);
					requestManager.execute(request, invitationDeleteListener);
					
				}
			});
			alertDialog.show();
		}
	}
    
    private RequestListener invitationAcceptListener = new RequestListener() {
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			getActivity().getContentResolver().notifyChange(RestContact.Invitation.CONTENT_URI, null);
			MainActivity mainActivity = (MainActivity) getActivity();
			mainActivity.loadGroups();
			mainActivity.updateGroups();
		}

		@Override
		public void onRequestConnectionError(Request request, int statusCode) {
			mRootView.findViewById(R.id.connectionError).setVisibility(View.VISIBLE);
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
	private RequestListener invitationDeleteListener = new RequestListener() {
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			getActivity().getContentResolver().notifyChange(RestContact.Invitation.CONTENT_URI, null);
		}

		@Override
		public void onRequestConnectionError(Request request, int statusCode) {
			mRootView.findViewById(R.id.connectionError).setVisibility(View.VISIBLE);
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

}
