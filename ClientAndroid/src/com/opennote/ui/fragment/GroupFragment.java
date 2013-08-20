package com.opennote.ui.fragment;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestGroup;
import com.opennote.model.RestRequestManager;
import com.opennote.model.adapter.NoteGroupAdapter;
import com.opennote.model.provider.RestContact;
import com.opennote.model.provider.RestContact.Note;
import com.opennote.ui.activity.CreateGroupNoteActivity;
import com.opennote.ui.activity.MainActivity;

public class GroupFragment extends ListFragment {
	private NoteGroupAdapter mAdapter;
	RestRequestManager mRequestManager = RestRequestManager.from(getActivity());
	private static final int LOADER_ID = 1;
	private final String[] PROJECTION = { 
			Note._ID,
			Note.TITLE,
			Note.BODY,
			Note.DATE,
			Note.FULL_NAME,
			Note.COLOR,
			Note.LOGIN
	    };
	
	private View mRootView;
	ListView mListView;
	private String mSessionHash;
	private RestGroup mCurrentGroup;
	
	public static final String CREATOR = "creator";
	public static final String GROUP_SLUG_MESSAGE = "group_slug";
	public static final String ID_VALUE_MESSAGE = "id";
	public static final String TITLE_VALUE_MESSAGE = "title";
	public static final String BODY_VALUE_MESSAGE = "body";
	public static final String EDITABLE_MESSAGE = "editable";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		mRootView = inflater.inflate(R.layout.fragment_note_list, container, false);
		mAdapter = new NoteGroupAdapter(
        		getActivity(),
	            R.layout.local_item, 
	            null, 
	            new String[]{ Note.TITLE, Note.BODY, Note.DATE, Note.FULL_NAME, Note.COLOR, Note.LOGIN },
	            new int[]{ R.id.local_title, R.id.local_body , R.id.local_date, R.id.local_author}, 
	            0);
		mAdapter.setmCreator( mCurrentGroup.getRole().equals(CREATOR) );
		mAdapter.setmCurrentLogin(MainActivity.instance.getUserLogin());
		mListView = (ListView) mRootView;
        mListView.setAdapter(mAdapter);
        
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            	mode.setTitle( String.valueOf(mListView.getCheckedItemCount()) + " selected");
                // Here you can do something when items are selected/de-selected,
                // such as update the title in the CAB
            }
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.action_delete:
                    	deleteDialogue(mode);
                        return true;
                    default:
                        return false;
                }
            }
            private void deleteDialogue(final ActionMode mode){
            	AlertDialog.Builder alertDialog;
    			alertDialog = new AlertDialog.Builder(getActivity());
    			alertDialog.setTitle("Attention");
    			alertDialog.setMessage("Do you really want to delete selected notes?");
    			String ok = "OK";
    			String cancel = "Cancel";
    			alertDialog.setPositiveButton(ok, new OnClickListener() {
    				public void onClick(DialogInterface dialog, int arg1) {
    					long[] IDs = mListView.getCheckedItemIds();
    	            	Request request = RequestFactory.getDeleteNotesRequest(mSessionHash, IDs);
    					mRequestManager.execute(request, mLoadRequestListener);
    					mode.finish();
    				}
    			});
    			alertDialog.setNegativeButton(cancel, new OnClickListener() {
    				public void onClick(DialogInterface dialog, int arg1) {
    					dialog.dismiss();
    				}
    			});
    			alertDialog.show();
            }
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context, menu);
                return true;
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
            }
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });

		// DataDroid RequestManager
		Request request = RequestFactory.getLoadNotesRequest(mSessionHash, mCurrentGroup.getSlug());
		mRequestManager.execute(request, mLoadRequestListener);
		
		return mRootView;
	}
	
	public void setValues(String sessionHash, RestGroup refGroup){
		mSessionHash = sessionHash;
		mCurrentGroup = refGroup;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.notes, menu);
		menu.removeItem(R.id.action_new_list);
		if(mCurrentGroup.getRole().equals(CREATOR)){
			menu.removeItem(R.id.action_left);
		} else {
			menu.removeItem(R.id.action_discard);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		MenuItem newItem = menu.findItem(R.id.action_new);	
		newItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(getActivity(), CreateGroupNoteActivity.class);
				intent.putExtra(GROUP_SLUG_MESSAGE, mCurrentGroup.getSlug());
				startActivity(intent);
				return true;
			}
		});
		
		if(mCurrentGroup.getRole().equals(CREATOR)){
			MenuItem discardItem = menu.findItem(R.id.action_discard);
			discardItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
	                builderInner.setTitle("Alert");
	                builderInner.setMessage("Are you sure you want to delete the group \"" + mCurrentGroup.getSlug() + "\"?");
	                builderInner.setPositiveButton("OK",
	                        new DialogInterface.OnClickListener() {
	                            @Override
	                            public void onClick(DialogInterface dialog, int index) {
	                                dialog.dismiss();
	                                Request request = RequestFactory.getDeleteGroupRequest(mSessionHash, mCurrentGroup.getSlug());
	            					mRequestManager.execute(request, mDismissRequestListener);
	                            }
	                        });
	                builderInner.setNegativeButton("Cancel",
	                        new DialogInterface.OnClickListener() {
	                            @Override
	                            public void onClick(DialogInterface dialog, int index) {
	                                dialog.dismiss();
	                            }
	                        });
	                builderInner.show();
					return true;
				}
			});
		} else {
			MenuItem discardItem = menu.findItem(R.id.action_left);
			discardItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
	                builderInner.setTitle("Alert");
	                builderInner.setMessage("Left the group: \"" + mCurrentGroup.getSlug() + "\"?");
	                builderInner.setPositiveButton("OK",
	                        new DialogInterface.OnClickListener() {
	                            @Override
	                            public void onClick(DialogInterface dialog, int index) {
	                                dialog.dismiss();
	                                Request request = RequestFactory.getRemoveUserFromGroupRequest(mSessionHash, mCurrentGroup.getSlug());
	            					mRequestManager.execute(request, mDismissRequestListener);
	                            }
	                        });
	                builderInner.setNegativeButton("Cancel",
	                        new DialogInterface.OnClickListener() {
	                            @Override
	                            public void onClick(DialogInterface dialog, int index) {
	                                dialog.dismiss();
	                            }
	                        });
	                builderInner.show();
					return true;
				}
			});
		}
		
	}
	
	private RequestListener mLoadRequestListener = new RequestListener() {
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			mListView.setOnItemClickListener(new GroupListClickListener());
	        getLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
	        getActivity().getContentResolver().notifyChange(RestContact.Note.CONTENT_URI, null);
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
	
	private RequestListener mDismissRequestListener = new RequestListener() {
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			Toast.makeText(getActivity(), "Executed", Toast.LENGTH_SHORT).show();
			MainActivity.instance.loadGroups();
			MainActivity.instance.updateGroups("Local");
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
                new String[]{mCurrentGroup.getSlug()},
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
	
	private class GroupListClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Cursor cursor = (Cursor) mAdapter.getItem(position);
			String login = cursor.getString(6);
			System.out.println(cursor.getString(6));
			if(login.equals(MainActivity.instance.getUserLogin())){
				Intent intent = new Intent(getActivity(), CreateGroupNoteActivity.class);
				intent.putExtra(GROUP_SLUG_MESSAGE, mCurrentGroup.getSlug());
				intent.putExtra(ID_VALUE_MESSAGE, cursor.getLong(0));
				intent.putExtra(TITLE_VALUE_MESSAGE, cursor.getString(1));
				intent.putExtra(BODY_VALUE_MESSAGE, cursor.getString(2));
				startActivity(intent);
			}
		}
	}

}                                                                                                                                                                           