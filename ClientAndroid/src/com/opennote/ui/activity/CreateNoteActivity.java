package com.opennote.ui.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;
import com.opennote.model.adapter.ColorSpinnerAdapter;
import com.opennote.model.provider.LocalContract;
import com.opennote.model.provider.LocalContract.LocalNotes;
import com.opennote.ui.fragment.GroupFragment;
import com.opennote.ui.fragment.LocalFragment;

public class CreateNoteActivity extends Activity implements ActionBar.OnNavigationListener {
	private long mId;
	private String mTitle;
	private String mBody;
	private boolean mNotLocal;
	private String mGroupSlug;
	private String mBackgroundColor;
	private View mRootView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_note);
		mRootView = getWindow().getDecorView().findViewById(android.R.id.content);
		
		// Get the message from the intent
	    Intent intent = getIntent();
	    mNotLocal = intent.getBooleanExtra(GroupFragment.NOT_LOCAL, false);
	    mGroupSlug = intent.getStringExtra(GroupFragment.GROUP_SLUG);
	    mId = intent.getLongExtra(LocalFragment.ID_VALUE_MESSAGE, -1);
	    mTitle = intent.getStringExtra(LocalFragment.TITLE_VALUE_MESSAGE);
	    mBody = intent.getStringExtra(LocalFragment.BODY_VALUE_MESSAGE);
	    String bkColorMessage = intent.getStringExtra(LocalFragment.COLOR_VALUE_MESSAGE);
	    
		// Preload
	    EditText titleEditText = ((EditText) findViewById(R.id.note_title));
	    EditText bodyEditText = ((EditText) findViewById(R.id.note_body));
	    titleEditText.setText(mTitle);
	    bodyEditText.setText(mBody);
	    
		// Show keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    	
        // Configure dropdown menu
	    ColorSpinnerAdapter adapter = new ColorSpinnerAdapter(getBaseContext(), R.layout.color);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getActionBar().setListNavigationCallbacks(adapter, new OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                mBackgroundColor = ColorSpinnerAdapter.COLORS[itemPosition];
                mRootView.setBackgroundColor(Color.parseColor(mBackgroundColor));
                return false;
            }
        });
        if (bkColorMessage != null){
	    	mBackgroundColor = bkColorMessage;
	        for(int i=0; i < ColorSpinnerAdapter.COLORS.length; i++){
	        	int color = Color.parseColor(ColorSpinnerAdapter.COLORS[i]);
	        	if(color == Color.parseColor(mBackgroundColor))
	        		actionBar.setSelectedNavigationItem(i);
	        }
	    }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_create_note, menu);
	    return true;
	}
	
	@Override
	protected void onDestroy(){
		EditText editTextTitle= (EditText) this.findViewById(R.id.note_title);
		EditText editTextBody= (EditText) this.findViewById(R.id.note_body);
		
		// Set date format
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM. HH:mm", Locale.US);
		
		// Set database fields
		String title = editTextTitle.getText().toString();
		String body = editTextBody.getText().toString();
		String date = dateFormat.format(Calendar.getInstance().getTime());
		
		if(!title.isEmpty()){
			ContentValues note = new ContentValues();
			note.put("title", title);
			note.put("body", body);
			note.put("date", date);
			
			// Check where should save
			if (mNotLocal)
				cloudAction(title, body);
			else
				localAction(note);
		}
		
		super.onDestroy();
	}
	
	private void localAction(ContentValues note) {
		note.put("color", mBackgroundColor);
		if(mId == -1) {
			this.getContentResolver().insert(LocalContract.LocalNotes.CONTENT_URI, note);
		} else {
			this.getContentResolver().update(LocalContract.LocalNotes.CONTENT_URI, note, LocalNotes._ID + " = ?", new String[]{String.valueOf(mId)});
			this.getContentResolver().notifyChange(LocalContract.LocalNotes.CONTENT_URI, null);
		}
	}

	private void cloudAction(String title, String body) {
		if(mId == -1) {
			// get Session hash
			SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
			String sessionHash = sharedPref.getString(getString(R.string.session_hash), null);
			
			// DataDroid-lib RequestManager
			RestRequestManager requestManager = RestRequestManager.from(this);
			// Integrate session hash and group slug into request
			Request request = RequestFactory.getAddNoteRequest(sessionHash, mGroupSlug, title, body);
			// Add RequestListener
			requestManager.execute(request, mRequestListener);
		} else {

		}
	}
	
	private RequestListener mRequestListener = new RequestListener() {
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			MainActivity mainActivity = MainActivity.mainActivity;
			mainActivity.updateGroups(mGroupSlug);
		}

		@Override
		public void onRequestConnectionError(Request request, int statusCode) {
//			Toast.makeText(getActivity(), "onRequestConnectionError", 5).show();
		}

		@Override
		public void onRequestDataError(Request request) {
//			Toast.makeText(getActivity(), "onRequestDataError", 5).show();
		}

		@Override
		public void onRequestCustomError(Request request, Bundle resultData) {
//			Toast.makeText(getActivity(), "onRequestCustomError", 5).show();
		}
		
	};

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
