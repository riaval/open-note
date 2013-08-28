package com.opennote.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.ErrorFactory;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;
import com.opennote.model.service.RestService;
import com.opennote.ui.activity.MainActivity;

public class UserFragment extends Fragment {

	View mRootView;
	EditText mFullNameEdit;
	EditText mEmailEdit;
//	EditText mPassEdit;
	
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
		mFullNameEdit = (EditText) mRootView.findViewById(R.id.userFullNameEdit);
		mEmailEdit = (EditText) mRootView.findViewById(R.id.userEmailEdit);
		
//		// Swap EtitText values
//		fullName = mFullNameEdit.getText().toString();
//		email = mEmailEdit.getText().toString();
		
		// Set Preferences values to EditTexts
		loginEdit.setText(login);
		mFullNameEdit.setText(fullName);
		mEmailEdit.setText(email);
		
		ImageButton logOutBt = (ImageButton) mRootView.findViewById(R.id.userLogOutBt);
		ImageButton editPassBt = (ImageButton) mRootView.findViewById(R.id.userEditPassBt);
		Button saveChangesBt = (Button) mRootView.findViewById(R.id.userSaveBt);
		
		logOutBt.setOnClickListener(mLogOutAction);
		saveChangesBt.setOnClickListener(mSaveChangesAction);
		editPassBt.setOnClickListener(mEditPassAction);
		
		return mRootView;
	}
	
	private OnClickListener mLogOutAction = new OnClickListener() {
		
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
		
	};
	
	private OnClickListener mSaveChangesAction = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// DataDroid RequestManager
			RestRequestManager requestManager = RestRequestManager.from(getActivity());
			Request request = RequestFactory.getEditSimpleDataOperation(
					  MainActivity.instance.getSessionHash()
					, mFullNameEdit.getText().toString()
					, mEmailEdit.getText().toString());
			requestManager.execute(request, mEditSimpleRequestListener);
		}
		
	};
	
	private OnClickListener mEditPassAction = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    LayoutInflater inflater = getActivity().getLayoutInflater();
		    final View rootPassView = inflater.inflate(R.layout.new_password, null);
		    
		    CheckBox showPassChBx = (CheckBox) rootPassView.findViewById(R.id.passShowPassChBx);
		    showPassChBx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					EditText oldPassEdit = (EditText) rootPassView.findViewById(R.id.passOldPassEdit);
                	EditText newPassEdit = (EditText) rootPassView.findViewById(R.id.passNewPassEdit);
                	
					if (isChecked) {
						oldPassEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						oldPassEdit.setTypeface( Typeface.SANS_SERIF );
						newPassEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						newPassEdit.setTypeface( Typeface.SANS_SERIF );
					} else {
						oldPassEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
						newPassEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
					}
				}
			});
		    
		    builder.setView(rootPassView); 
		    builder.setTitle("Change password");
			builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int index) {
                        	EditText oldPassEdit = (EditText) rootPassView.findViewById(R.id.passOldPassEdit);
                        	EditText newPassEdit = (EditText) rootPassView.findViewById(R.id.passNewPassEdit);
                        	
                        	// DataDroid RequestManager
                			RestRequestManager requestManager = RestRequestManager.from(getActivity());
                			Request request = RequestFactory.getEditPasswordOperation(
                					  MainActivity.instance.getSessionHash()
                					, oldPassEdit.getText().toString()
                					, newPassEdit.getText().toString());
                			requestManager.execute(request, mEditSimpleRequestListener);
                        	
                            dialog.dismiss();
                        }
                    });
		    
		    AlertDialog dialog = builder.create();
		    dialog.show();
			
		}
	};
	
	private RequestListener mEditSimpleRequestListener = new RequestListener() {
		
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			mRootView.requestFocus();
			Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestConnectionError(Request request, int statusCode) {
			ErrorFactory.showConnectionErrorMessage(getActivity());
		}

		@Override
		public void onRequestDataError(Request request) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void onRequestCustomError(Request request, Bundle resultData) {
			int code = resultData.getInt(RestService.STATUS_CODE);
			String comment = resultData.getString(RestService.COMMENT);
			ErrorFactory.doError(getActivity(), code, comment);
		}
	};
	
}
