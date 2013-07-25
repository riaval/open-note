package com.opennote.ui.createnote;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.opennote.R;

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
		EditText editText= (EditText) this.findViewById(R.id.note_title);
		System.out.println(editText.getText().toString());
		super.onDestroy();
	}
	
}
