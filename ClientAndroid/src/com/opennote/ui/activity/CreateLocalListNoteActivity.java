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
import android.widget.ListView;

import com.opennote.R;
import com.opennote.model.adapter.ColorSpinnerAdapter;
import com.opennote.model.adapter.ListNoteAddapter;
import com.opennote.model.provider.LocalContact;
import com.opennote.model.provider.LocalContact.LocalNotes;
import com.opennote.ui.fragment.LocalFragment;

public class CreateLocalListNoteActivity extends Activity {

	private long mId;
	private String mTitle;
	private String mBody;
	
	private String mBackgroundColor;
	private View mRootView;
	
	ListNoteAddapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_list_note);
		mRootView = getWindow().getDecorView().findViewById(android.R.id.content);

		// Get the message from the intent
	    Intent intent = getIntent();
	    mId = intent.getLongExtra(LocalFragment.ID_VALUE_MESSAGE, -1);
	    mTitle = intent.getStringExtra(LocalFragment.TITLE_VALUE_MESSAGE);
	    mBody = intent.getStringExtra(LocalFragment.BODY_VALUE_MESSAGE);
	    String bkColorMessage = intent.getStringExtra(LocalFragment.COLOR_VALUE_MESSAGE);

	    // Preload
	    final EditText titleEditText = ((EditText) findViewById(R.id.note_title));
	    titleEditText.setText(mTitle);
	    
		mAdapter = new ListNoteAddapter(this);
		ListView list = (ListView) findViewById(R.id.list_note_list);
		list.setItemsCanFocus(true);
		list.setAdapter(mAdapter);
		
		if(mId != -1){
			String[] values = mBody.split("\n");
			for (String s : values){
				boolean state = false;
				String value = null;
				if (s.substring(0, 1).equals("@")){
					state = true;
				}
				value = s.substring(1, s.length());
				mAdapter.add(state, value);
			}
		}
		mAdapter.add();

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
		EditText editTextTitle= (EditText) this.findViewById(R.id.note_title);
		
		// Set date format
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM. HH:mm", Locale.US);
		
		// Set database fields
		String title = editTextTitle.getText().toString();
		String date = dateFormat.format(Calendar.getInstance().getTime());

		String data = "";
		for (int i=0; i<mAdapter.getCount()-1; i++){
			data = data.toString() + (mAdapter.getState(i) ? "@" : "#");
			data += mAdapter.getValue(i) + "\n";
		}
		
		if(!title.isEmpty()){
			ContentValues note = new ContentValues();
			note.put(LocalContact.LocalNotes.TITLE, title);
			note.put(LocalContact.LocalNotes.BODY, data.substring(0, data.length()-1));
			note.put(LocalContact.LocalNotes.DATE, date);
			note.put(LocalContact.LocalNotes.COLOR, mBackgroundColor);
			note.put(LocalContact.LocalNotes.LIST, 1);
			
			if(mId == -1) {
				this.getContentResolver().insert(LocalContact.LocalNotes.CONTENT_URI, note);
			} else {
				this.getContentResolver().update(LocalContact.LocalNotes.CONTENT_URI, note, LocalNotes._ID + " = ?", new String[]{String.valueOf(mId)});
				this.getContentResolver().notifyChange(LocalContact.LocalNotes.CONTENT_URI, null);
			}
		}
		super.onDestroy();
	}
	
}
