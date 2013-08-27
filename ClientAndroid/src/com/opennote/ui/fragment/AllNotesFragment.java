package com.opennote.ui.fragment;

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

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.ErrorFactory;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;
import com.opennote.model.adapter.NoteGroupAdapter;
import com.opennote.model.provider.RestContact.Note;
import com.opennote.model.service.RestService;
import com.opennote.ui.activity.MainActivity;

public class AllNotesFragment extends Fragment {

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
	private ListView mListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		mRootView = inflater.inflate(R.layout.fragment_note, container, false);
		mAdapter = new NoteGroupAdapter(
        		getActivity(),
	            R.layout.simple_note_item, 
	            null, 
	            new String[]{ Note.TITLE, Note.BODY, Note.DATE, Note.FULL_NAME, Note.COLOR, Note.LOGIN },
	            new int[]{ R.id.local_title, R.id.local_body , R.id.local_date, R.id.local_author}, 
	            0);
		mListView = (ListView) mRootView.findViewById(R.id.noteList);;
        mListView.setAdapter(mAdapter);
		
		// DataDroid RequestManager
		Request request = RequestFactory.getLoadAllNoteRequest(MainActivity.instance.getSessionHash());
		mRequestManager.execute(request, mLoadRequestListener);
		
		return mRootView;
	}

	
	private RequestListener mLoadRequestListener = new RequestListener() {
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
	        getLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
		}

		@Override
		public void onRequestConnectionError(Request request, int statusCode) {
			mRootView.findViewById(R.id.connectionError).setVisibility(View.VISIBLE);
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
                Note.CONTENT_URI,
                PROJECTION,
                null,
                null,
                Note._ID + " DESC"
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
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> arg0) {
            mAdapter.swapCursor(null);
        }
    };
	
}
