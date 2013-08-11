package com.opennote.ui.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.opennote.R;
import com.opennote.model.adapter.ColorSpinnerAdapter;
import com.opennote.model.provider.LocalContract;
import com.opennote.model.provider.LocalContract.LocalNotes;
import com.opennote.ui.fragment.LocalFragment;

public class CreateLocalNoteActivity extends Activity{
	private long mId;
	private String mTitle;
	private String mBody;
	
	private String mBackgroundColor;
	private View mRootView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_note);
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
			
			note.put("color", mBackgroundColor);
			if(mId == -1) {
				this.getContentResolver().insert(LocalContract.LocalNotes.CONTENT_URI, note);
			} else {
				this.getContentResolver().update(LocalContract.LocalNotes.CONTENT_URI, note, LocalNotes._ID + " = ?", new String[]{String.valueOf(mId)});
				this.getContentResolver().notifyChange(LocalContract.LocalNotes.CONTENT_URI, null);
			}
		}
		super.onDestroy();
	}

}
