package com.opennote.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.opennote.R;

public class UserFragment extends Fragment {

	View mRootView;
	String mFullName;
	String mEmail;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_user, container, false);

		// Get Preferences values
		SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		String login = sharedPref.getString(getString(R.string.user_login), null);
		String fullName = sharedPref.getString(getString(R.string.user_full_name), null);
		String email = sharedPref.getString(getString(R.string.user_email), null);
		
		// Get EtitText
		EditText loginEdit = (EditText) mRootView.findViewById(R.id.userLoginEdit);
		EditText fullNameEdit = (EditText) mRootView.findViewById(R.id.userFullNameEdit);
		EditText emailEdit = (EditText) mRootView.findViewById(R.id.userEmailEdit);
		
		// Swap EtitText values
		mFullName = fullNameEdit.getText().toString();
		mEmail = emailEdit.getText().toString();
		
		// Set Preferences values to EditTexts
		loginEdit.setText(login);
		fullNameEdit.setText(fullName);
		emailEdit.setText(email);
		
		ImageButton button = (ImageButton) mRootView.findViewById(R.id.userLogOutBt);
		button.setOnClickListener(new LogOutAction());
		
		return mRootView;
	}
	
	private class LogOutAction implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
            builderInner.setTitle("Alert");
            builderInner.setMessage("Logout?");
            builderInner.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int index) {
                        	SharedPreferences sharedPref = getActivity().getSharedPreferences(getActivity().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                			SharedPreferences.Editor editor = sharedPref.edit();
                			
                			editor.putString(getActivity().getString(R.string.session_hash), null);
                			editor.commit();
                			
                			getActivity().finish();
                			startActivity(getActivity().getIntent());
                        }
                    });
            builderInner.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int index) {
                            dialog.dismiss();
                        }
                    });
            builderInner.show();
		}		
	}
	
}
