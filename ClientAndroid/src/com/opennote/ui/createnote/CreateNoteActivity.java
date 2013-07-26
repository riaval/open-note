package com.opennote.ui.createnote;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.opennote.R;
import com.opennote.model.provider.Contract;

public class CreateNoteActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_note);
		
		// Show keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	}
	
	@Override
	protected void onDestroy(){
		EditText editTextTitle= (EditText) this.findViewById(R.id.note_title);
		EditText editTextBody= (EditText) this.findViewById(R.id.note_title);
//		System.out.println(editTextTitle.getText().toString());
		
		String title = editTextTitle.getText().toString();
		String body = editTextBody.getText().toString();
		
		ContentValues[] localNotesValues = new ContentValues[1];
		
		
		ContentValues note = new ContentValues();
		
		note.put("title", title);
		note.put("body", body);
		note.put("date", "simple_date");
		localNotesValues[0] = note;
		
		
		this.getContentResolver().bulkInsert(Contract.LocalNotes.CONTENT_URI, localNotesValues);
		
		super.onDestroy();
	}
	
}
