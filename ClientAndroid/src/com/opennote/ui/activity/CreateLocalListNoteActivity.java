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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.opennote.R;
import com.opennote.model.adapter.ColorSpinnerAdapter;
import com.opennote.model.adapter.ListNoteAddapter;
import com.opennote.model.provider.LocalContact;
import com.opennote.ui.fragment.LocalFragment;

public class CreateLocalListNoteActivity extends Activity {

	private String mBackgroundColor;
	private View mRootView;
	ListNoteAddapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_list_note);
		mRootView = getWindow().getDecorView().findViewById(android.R.id.content);

		// Get the message from the intent
	    Intent intent = getIntent();
	    String bkColorMessage = intent.getStringExtra(LocalFragment.COLOR_VALUE_MESSAGE);

		mAdapter = new ListNoteAddapter(this);
		mAdapter.add();
		
		ListView list = (ListView) findViewById(R.id.list_note_list);
		list.setAdapter(mAdapter);
		
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

	
	public void click(View view){
		mAdapter.add();
		mAdapter.notifyDataSetChanged();
	}
	
	public void checkAction(View view){
		CheckBox checkBox = (CheckBox) view;
		
		if ( checkBox.isChecked() ){
			System.out.println(view.getId());
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
			note.put(LocalContact.LocalNotes.BODY, data.substring(0, data.length()-2));
			note.put(LocalContact.LocalNotes.DATE, date);
			note.put(LocalContact.LocalNotes.COLOR, mBackgroundColor);
			note.put(LocalContact.LocalNotes.LIST, 1);
			
			this.getContentResolver().insert(LocalContact.LocalNotes.CONTENT_URI, note);
		}
		
		super.onDestroy();
	}
	
}
