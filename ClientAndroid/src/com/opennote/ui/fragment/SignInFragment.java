package com.opennote.ui.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;

public class SignInFragment extends Fragment{

	private View mRootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_signin, container, false);
		
		Button button = (Button) mRootView.findViewById(R.id.signInBt);
		CheckBox checkBox = (CheckBox) mRootView.findViewById(R.id.passInChBx);
		// Add onClick listener
		button.setOnClickListener(new SignInAction());
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				EditText passEdit = (EditText) mRootView.findViewById(R.id.passInEdit);
				if (isChecked) {
					passEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					passEdit.setTypeface( Typeface.SANS_SERIF );
				} else {
					passEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
		
		return mRootView;
	}

	private class SignInAction implements View.OnClickListener {
		@Override
		public void onClick(View view) {			
			// Get login and password from TextEdit
			EditText loginInEdit   = (EditText) mRootView.findViewById(R.id.loginInEdit);
			EditText passInEdit   = (EditText) mRootView.findViewById(R.id.passInEdit);
			String login = loginInEdit.getText().toString();
			String password = passInEdit.getText().toString();
			
			// DataDroid-lib RequestManager
			RestRequestManager requestManager = RestRequestManager.from(getActivity());
			// Integrate login and password into request
			Request request = RequestFactory.getSignInRequest(login, password);
			// Add RequestListener
			requestManager.execute(request, mRequestListener);
		}		
	}
	
	private RequestListener mRequestListener = new RequestListener(){
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			//reboot activity
			getActivity().finish();
			startActivity(getActivity().getIntent());
//			Toast.makeText(getActivity(), "onRequestFinished", 5).show();
		}

		@Override
		public void onRequestConnectionError(Request request, int statusCode) {
			Toast.makeText(getActivity(), "Connection error.", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onRequestDataError(Request request) {
			Toast.makeText(getActivity(), "Wrong data.", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onRequestCustomError(Request request, Bundle resultData) {
		}
		
	};
	
}
