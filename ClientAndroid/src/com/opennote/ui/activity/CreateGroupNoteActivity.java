package com.opennote.ui.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;
import com.opennote.ui.fragment.GroupFragment;

public class CreateGroupNoteActivity extends Activity implements ActionBar.OnNavigationListener{

	private long mId;
	private String mGroupSlug;
	private String mTitle;
	private String mBody;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_note);
		
		// Get the message from the intent
	    Intent intent = getIntent();
		mGroupSlug = intent.getStringExtra(GroupFragment.GROUP_SLUG_MESSAGE);
	    mId = intent.getLongExtra(GroupFragment.ID_VALUE_MESSAGE, -1);
	    System.out.println(mId);
	    mTitle = intent.getStringExtra(GroupFragment.TITLE_VALUE_MESSAGE);
	    mBody = intent.getStringExtra(GroupFragment.BODY_VALUE_MESSAGE);
		
	    // Preload
	    final EditText titleEditText = ((EditText) findViewById(R.id.note_title));
	    final EditText bodyEditText = ((EditText) findViewById(R.id.note_body));
	    titleEditText.setText(mTitle);
	    bodyEditText.setText(mBody);
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
////	    MenuInflater inflater = getMenuInflater();
////	    inflater.inflate(R.menu.notes, menu);
////	    menu.removeItem(R.id.action_discard);
//	    return true;
//	}
	
	@Override
	protected void onDestroy(){
		EditText editTextTitle= (EditText) this.findViewById(R.id.note_title);
		EditText editTextBody= (EditText) this.findViewById(R.id.note_body);
		
		// Set database fields
		String title = editTextTitle.getText().toString();
		String body = editTextBody.getText().toString();
		
		// DataDroid RequestManager
		RestRequestManager requestManager = RestRequestManager.from(this);
		
		if(!title.isEmpty()){
			if(mId == -1) {
				Request request = RequestFactory.getAddNoteRequest(MainActivity.instance.getSessionHash(), mGroupSlug, title, body);
				requestManager.execute(request, mRequestListener);
			} else {
				if ( !mTitle.equals(title) || !mBody.equals(body) ){
					Request request = RequestFactory.getEditNoteRequest(MainActivity.instance.getSessionHash(), mId, title, body);
					requestManager.execute(request, mRequestListener);
				}
			}
		}
		
		super.onDestroy();
	}
	
	private RequestListener mRequestListener = new RequestListener() {
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			MainActivity mainActivity = MainActivity.instance;
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
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
