package com.opennote.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.opennote.R;
import com.opennote.ui.activity.CreateNoteActivity;
import com.opennote.ui.activity.SearchResultActivity;

public class FindUserFragment extends Fragment {
	public static final String LOGIN_VALUE_MESSAGE = "login";
	public static final String FULL_NAME_VALUE_MESSAGE = "full_name";
	
	EditText mLoginEdit;
	EditText mFullName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_find_user, container, false);

		mLoginEdit = (EditText) rootView.findViewById(R.id.searchLoginEdit);
		mFullName = (EditText) rootView.findViewById(R.id.searchFullNameEdit);
		
		// Find view buttons
		Button search = (Button) rootView.findViewById(R.id.searchSearchBt);
		Button clear = (Button) rootView.findViewById(R.id.searchClearBt);
		// Add onClick listeners
		search.setOnClickListener(new FindUsersAction());
		clear.setOnClickListener(new ClearAction());
		return rootView;
	}
	
	private class FindUsersAction implements View.OnClickListener {
		@Override
		public void onClick(View view) {			
			Intent intent = new Intent(getActivity(), SearchResultActivity.class);
			String login = mLoginEdit.getText().toString();
			String fullName = mFullName.getText().toString();
			intent.putExtra(LOGIN_VALUE_MESSAGE, login);
			intent.putExtra(FULL_NAME_VALUE_MESSAGE, fullName);
			startActivity(intent);
		}		
	}
	
	private class ClearAction implements View.OnClickListener {
		@Override
		public void onClick(View view) {			
			mLoginEdit.getText().clear();
			mFullName.getText().clear();
		}		
	}

}
