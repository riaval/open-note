package com.opennote.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.ErrorFactory;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;
import com.opennote.model.provider.RestContact;
import com.opennote.model.service.RestService;
import com.opennote.ui.fragment.GroupFragment;

public class CreateGroupNoteActivity extends Activity {

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
	    mTitle = intent.getStringExtra(GroupFragment.TITLE_VALUE_MESSAGE);
	    mBody = intent.getStringExtra(GroupFragment.BODY_VALUE_MESSAGE);
		
	    // Preload
	    final EditText titleEditText = ((EditText) findViewById(R.id.note_title));
	    final EditText bodyEditText = ((EditText) findViewById(R.id.note_body));
	    titleEditText.setText(mTitle);
	    bodyEditText.setText(mBody);
	}
	
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
			MainActivity.instance.getContentResolver().notifyChange(RestContact.Note.CONTENT_URI, null);
//			MainActivity mainActivity = MainActivity.instance;
//			mainActivity.updateGroups(mGroupSlug);
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
