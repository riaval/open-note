package com.opennote.ui.fragment;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;
import com.opennote.model.adapter.NoteGroupAdapter;
import com.opennote.model.provider.RestContact.Note;
import com.opennote.ui.activity.MainActivity;

public class AllNotesFragment extends ListFragment {

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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		mRootView = inflater.inflate(R.layout.fragment_note_list, container, false);

		// DataDroid RequestManager
		Request request = RequestFactory.getLoadAllNoteRequest(MainActivity.instance.getSessionHash());
		mRequestManager.execute(request, mLoadRequestListener);
		
		return mRootView;
	}

	
	private RequestListener mLoadRequestListener = new RequestListener() {
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			mAdapter = new NoteGroupAdapter(
	        		getActivity(),
		            R.layout.local_item, 
		            null, 
		            new String[]{ Note.TITLE, Note.BODY, Note.DATE, Note.FULL_NAME, Note.COLOR, Note.LOGIN },
		            new int[]{ R.id.local_title, R.id.local_body , R.id.local_date, R.id.local_author}, 
		            0);
			ListView listView = (ListView) mRootView;
	        listView.setAdapter(mAdapter);
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
                null,
                null,
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
	
}
