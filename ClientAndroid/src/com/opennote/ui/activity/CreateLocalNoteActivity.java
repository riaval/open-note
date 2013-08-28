package com.opennote.ui.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.ErrorFactory;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;
import com.opennote.model.adapter.ColorSpinnerAdapter;
import com.opennote.model.provider.LocalContact;
import com.opennote.model.provider.RestContact;
import com.opennote.model.provider.LocalContact.LocalNotes;
import com.opennote.model.provider.RestContact.Group;
import com.opennote.model.service.RestService;
import com.opennote.ui.fragment.LocalFragment;

public class CreateLocalNoteActivity extends Activity {
	private long mId;
	private String mTitle;
	private String mBody;
	
	private String mBackgroundColor;
	private View mRootView;
	private boolean isShared;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_note);
		mRootView = getWindow().getDecorView().findViewById(android.R.id.content);
		
		// Get the message from the intent
	    Intent intent = getIntent();
	    mId = intent.getLongExtra(LocalFragment.ID_VALUE_MESSAGE, -1);
	    mTitle = intent.getStringExtra(LocalFragment.TITLE_VALUE_MESSAGE);
	    mBody = intent.getStringExtra(LocalFragment.BODY_VALUE_MESSAGE);
	    String bkColorMessage = intent.getStringExtra(LocalFragment.COLOR_VALUE_MESSAGE);
	    
		// Preload
	    final EditText titleEditText = ((EditText) findViewById(R.id.note_title));
	    final EditText bodyEditText = ((EditText) findViewById(R.id.note_body));
	    titleEditText.setText(mTitle);
	    bodyEditText.setText(mBody);
    	
        // Configure dropdown menu
	    ColorSpinnerAdapter adapter = new ColorSpinnerAdapter(getBaseContext(), R.layout.color);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(adapter, new OnNavigationListener() {
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
	protected void onDestroy(){
		if (isShared) {
			super.onDestroy();
		}
		
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
			note.put(LocalContact.LocalNotes.TITLE, title);
			note.put(LocalContact.LocalNotes.BODY, body);
			note.put(LocalContact.LocalNotes.DATE, date);
			note.put(LocalContact.LocalNotes.COLOR, mBackgroundColor);
			note.put(LocalContact.LocalNotes.LIST, 0);
			
			if(mId == -1) {
				this.getContentResolver().insert(LocalContact.LocalNotes.CONTENT_URI, note);
			} else {
				this.getContentResolver().update(LocalContact.LocalNotes.CONTENT_URI, note, LocalNotes._ID + " = ?", new String[]{String.valueOf(mId)});
				this.getContentResolver().notifyChange(LocalContact.LocalNotes.CONTENT_URI, null);
			}
		}
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (MainActivity.instance.getSessionHash() != null){
			MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.single_note, menu);
		    return true;
		}
	    return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_share:
	            shareItem();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void shareItem(){
		Cursor cursor = this
				.getApplicationContext()
				.getContentResolver()
				.query(RestContact.Group.CONTENT_URI, null, Group.ROLE + "=?", new String[]{"creator"}, Group.NAME);
		List<String> groupSlugsList = new ArrayList<String>();
		List<String> groupNamesList = new ArrayList<String>();
		while (cursor.moveToNext()) {
			groupSlugsList.add(cursor.getString(1));
			groupNamesList.add(cursor.getString(2));
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Share note");
		
		final Object[] groupSlugs = groupSlugsList.toArray();
		builder.setItems(groupSlugsList.toArray(new String[groupSlugsList.size() - 1]),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int index) {
						RestRequestManager requestManager = RestRequestManager.from(CreateLocalNoteActivity.this);
						EditText editTextTitle= (EditText) CreateLocalNoteActivity.this.findViewById(R.id.note_title);
						EditText editTextBody= (EditText) CreateLocalNoteActivity.this.findViewById(R.id.note_body);
						String sessionHash = MainActivity.instance.getSessionHash();
						String groupSlug = groupSlugs[index].toString();
						Request request = RequestFactory.getAddNoteRequest(
								sessionHash
								, groupSlug
								, editTextTitle.getText().toString()
								, editTextBody.getText().toString());
						requestManager.execute(request, shareNoteRequestListener);
						isShared = true;
						CreateLocalNoteActivity.this.finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private RequestListener shareNoteRequestListener = new RequestListener() {
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
            AlertDialog.Builder builderInner = new AlertDialog.Builder(MainActivity.instance);
            builderInner.setTitle("Success");
            builderInner.setMessage("Note created.");
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
			ErrorFactory.showConnectionErrorMessage(MainActivity.instance);
		}

		@Override
		public void onRequestDataError(Request request) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void onRequestCustomError(Request request, Bundle resultData) {
			int code = resultData.getInt(RestService.STATUS_CODE);
			String comment = resultData.getString(RestService.COMMENT);
			ErrorFactory.doError(MainActivity.instance, code, comment);
		}
	};
	
}
