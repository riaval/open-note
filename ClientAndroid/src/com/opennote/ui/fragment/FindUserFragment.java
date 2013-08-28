package com.opennote.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.ErrorFactory;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;
import com.opennote.model.provider.RestContact;
import com.opennote.model.provider.RestContact.Group;
import com.opennote.model.provider.RestContact.User;
import com.opennote.model.service.RestService;
import com.opennote.ui.activity.MainActivity;

public class FindUserFragment extends Fragment {

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
		
		mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		            performSearch();
		            return true;
		        }
		        return false;
		    }
		});
		
		searchBt.setOnClickListener(new FindUsersAction());
		return rootView;
	}
	
	private class FindUsersAction implements View.OnClickListener {
		@Override
		public void onClick(View view) {			
			performSearch();
		}		
	}
	
	private void performSearch(){
		String searchValue = mSearchEdit.getText().toString();
		
		// DataDroid RequestManager
		RestRequestManager requestManager = RestRequestManager.from(getActivity());
		Request request = RequestFactory.getInviteUserRequest(MainActivity.instance.getSessionHash(), searchValue);
		requestManager.execute(request, mRequestListener);
	}
	
	private RequestListener mRequestListener = new RequestListener() {
		private boolean mark;
		
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			if (!mark){
				getLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
				mark = true;
			} else {
				getActivity().getContentResolver().notifyChange(RestContact.User.CONTENT_URI, null);
			}
		}

		@Override
		public void onRequestConnectionError(Request request, int statusCode) {
			ErrorFactory.showConnectionErrorMessage(getActivity());
		}

		@Override
		public void onRequestDataError(Request request) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void onRequestCustomError(Request request, Bundle resultData) {
			int code = resultData.getInt(RestService.STATUS_CODE);
			String comment = resultData.getString(RestService.COMMENT);
			ErrorFactory.doError(getActivity(), code, comment);
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
            if(cursor.getCount() == 0){
            	Toast.makeText(getActivity(), "Nothing found", Toast.LENGTH_LONG).show();
            }
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
			final String userLogin = ((TextView) view.findViewById(R.id.searchLoginTextView) ).getText().toString();
			
			Cursor cursor = getActivity()
					.getApplicationContext()
					.getContentResolver()
					.query(RestContact.Group.CONTENT_URI, null, Group.ROLE + "=?", new String[]{"creator"}, Group.NAME);
			List<String> groupSlugsList = new ArrayList<String>();
			List<String> groupNamesList = new ArrayList<String>();
			while (cursor.moveToNext()) {
				groupSlugsList.add(cursor.getString(1));
				groupNamesList.add(cursor.getString(2));
			}
			cursor.close();
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Create invitation");
//			builder.setNegativeButton("Cancel",
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int index) {
//							dialog.dismiss();
//						}
//					});
			try {
				final Object[] groupSlugs = groupSlugsList.toArray();
				builder.setItems(groupSlugsList.toArray(new String[groupSlugsList.size() - 1]),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int index) {
								RestRequestManager requestManager = RestRequestManager.from(getActivity());
								String sessionHash = MainActivity.instance.getSessionHash();
								String groupSlug = groupSlugs[index].toString();
								Request request = RequestFactory.getCreateInvitationsRequest(sessionHash, userLogin, groupSlug);
								requestManager.execute(request, createInvitationRequestListener);
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			} catch (Exception e) {
				AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
                builderInner.setTitle("Alert");
                builderInner.setMessage("No groups available for select.\nCreate new group.");
                builderInner.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int index) {
                                dialog.dismiss();
                            }
                        });
                builderInner.show();
                return;
			}
		}
		
		private RequestListener createInvitationRequestListener = new RequestListener() {
			@Override
			public void onRequestFinished(Request request, Bundle resultData) {
                AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
                builderInner.setTitle("Success");
                builderInner.setMessage("Invitation created");
                builderInner.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int index) {
                                dialog.dismiss();
                            }
                        });
                builderInner.show();
			}

			@Override
			public void onRequestConnectionError(Request request, int statusCode) {
				ErrorFactory.showConnectionErrorMessage(getActivity());
			}

			@Override
			public void onRequestDataError(Request request) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void onRequestCustomError(Request request, Bundle resultData) {
				int code = resultData.getInt(RestService.STATUS_CODE);
				String comment = resultData.getString(RestService.COMMENT);
				ErrorFactory.doError(getActivity(), code, comment);
			}
		};
	}

}
