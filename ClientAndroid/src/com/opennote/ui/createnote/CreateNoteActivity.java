package com.opennote.ui.createnote;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.opennote.R;
import com.opennote.model.provider.LocalContract;
import com.opennote.model.provider.LocalContract.LocalNotes;
import com.opennote.ui.local.LocalFragment;

public class CreateNoteActivity extends Activity {
	private long mId;
	private String mTitle;
	private String mBody;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_note);
		
		// Get the message from the intent
	    Intent intent = getIntent();
	    mId = intent.getLongExtra(LocalFragment.ID_VALUE_MESSAGE, -1);
	    mTitle = intent.getStringExtra(LocalFragment.TITLE_VALUE_MESSAGE);
	    mBody = intent.getStringExtra(LocalFragment.BODY_VALUE_MESSAGE);
	    
		// Preload
	    EditText titleEditText = ((EditText) findViewById(R.id.note_title));
	    EditText bodyEditText = ((EditText) findViewById(R.id.note_body));
	    titleEditText.setText(mTitle);
	    bodyEditText.setText(mBody);
	    
		// Show keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
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
		
		ContentValues note = new ContentValues();
		
		note.put("title", title);
		note.put("body", body);
		note.put("date", date);
		
		if(mId == -1) {
			this.getContentResolver().insert(LocalContract.LocalNotes.CONTENT_URI, note);
		}
		else {
			this.getContentResolver().update(LocalContract.LocalNotes.CONTENT_URI, note, LocalNotes._ID + " = ?", new String[]{String.valueOf(mId)});
			this.getContentResolver().notifyChange(LocalContract.LocalNotes.CONTENT_URI, null);
		}
		
		super.onDestroy();
	}
	
}
