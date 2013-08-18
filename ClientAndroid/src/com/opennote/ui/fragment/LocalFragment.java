package com.opennote.ui.fragment;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.opennote.R;
import com.opennote.model.adapter.NoteLocalAdapter;
import com.opennote.model.provider.LocalContact;
import com.opennote.model.provider.LocalContact.LocalNotes;
import com.opennote.ui.activity.CreateLocalNoteActivity;

import de.timroes.swipetodismiss.SwipeDismissList;
import de.timroes.swipetodismiss.SwipeDismissList.SwipeDirection;

public class LocalFragment extends Fragment {
	private SwipeDismissList mSwipeLis;
	
	public static final String ID_VALUE_MESSAGE = "id";
	public static final String TITLE_VALUE_MESSAGE = "title";
	public static final String BODY_VALUE_MESSAGE = "body";
	public static final String COLOR_VALUE_MESSAGE = "color";
	
	private static final int LOADER_ID = 1;
	private final String[] PROJECTION = { 
			LocalNotes._ID,
			LocalNotes.TITLE,
			LocalNotes.BODY,
			LocalNotes.DATE,
			LocalNotes.COLOR
	    };
	private NoteLocalAdapter mAdapter;
	private MatrixCursor mMatrixCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_note_list, container, false);
		
        mAdapter = new NoteLocalAdapter(
        		getActivity(),
	            R.layout.local_item, 
	            null, 
	            new String[]{ LocalNotes.TITLE, LocalNotes.BODY, LocalNotes.DATE},
	            new int[]{ R.id.local_title, R.id.local_body , R.id.local_date}, 
	            0);
        ListView listView = (ListView) rootView;
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new LocalListClickListener());
        
        SwipeDismissList.OnDismissCallback callback = new SwipeDismissList.OnDismissCallback() {
            // Gets called whenever the user deletes an item.
            public SwipeDismissList.Undoable onDismiss(AbsListView listView, final int position) {
            	if(mMatrixCursor != null)
            		mMatrixCursor.close();
            	mMatrixCursor = new MatrixCursor(PROJECTION);
				final Cursor cursor = mAdapter.getCursor();
				final long deletedId = mAdapter.getItemId(position);;
				for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
					if(cursor.getPosition() == position)
						continue;
					String id = cursor.getString(0);
					String title = cursor.getString(1);
					String body = cursor.getString(2);
					String date = cursor.getString(3);
					String color = cursor.getString(4);
					mMatrixCursor.addRow(new String[]{id, title, body, date, color});
				}
				mAdapter.swapCursor(mMatrixCursor);
                return new SwipeDismissList.Undoable() {

                    // Method is called when user undoes this deletion
                    public void undo() {
                        // Reinsert item to list
                    	mAdapter.changeCursor(cursor);
                    }

                    // Return an undo message for that item
                    public String getTitle() {
                        return "Item deleted";
                    }

                    // Called when user cannot undo the action anymore
                    public void discard() {
                    	getActivity().getContentResolver().delete(LocalContact.LocalNotes.CONTENT_URI, LocalNotes._ID + "=?", new String[]{String.valueOf(deletedId)});
                    	getActivity().getContentResolver().notifyChange(LocalContact.LocalNotes.CONTENT_URI, null);
                    }
                };
            }
        };
        
        mSwipeLis = new SwipeDismissList(listView, callback, SwipeDismissList.UndoMode.SINGLE_UNDO);
        mSwipeLis.setSwipeDirection(SwipeDirection.START);
        mSwipeLis.setAutoHideDelay(1);
        
        getLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
        
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.notes, menu);
		menu.removeItem(R.id.action_discard);
		menu.removeItem(R.id.action_left);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.action_new);
		item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				mSwipeLis.discardUndo();
				Intent intent = new Intent(getActivity(), CreateLocalNoteActivity.class);
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
        	mAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> arg0) {
            mAdapter.swapCursor(null);
        }
    };
    
    private class LocalListClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String title = ((TextView) view.findViewById(R.id.local_title)).getText().toString();
			String body = ((TextView) view.findViewById(R.id.local_body)).getText().toString();
			int backgroundColor = ((ColorDrawable) view.getBackground()).getColor();
			
			// Starting update activity
			Intent intent = new Intent(getActivity(), CreateLocalNoteActivity.class);
			intent.putExtra(ID_VALUE_MESSAGE, id);
			intent.putExtra(TITLE_VALUE_MESSAGE, title);
			intent.putExtra(BODY_VALUE_MESSAGE, body);
			intent.putExtra(COLOR_VALUE_MESSAGE, "#" + Integer.toHexString(backgroundColor));
			startActivity(intent);
		}
	}
    
}
