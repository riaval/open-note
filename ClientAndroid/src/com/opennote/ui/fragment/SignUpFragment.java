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
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;

public class SignUpFragment extends Fragment {

	private View mRootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_signup, container, false);

		Button button = (Button) mRootView.findViewById(R.id.signUpBt);
		CheckBox checkBox = (CheckBox) mRootView.findViewById(R.id.passUpChBx);
		
		button.setOnClickListener(new SignUpAction());
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				EditText passEdit = (EditText) mRootView.findViewById(R.id.passUpEdit);
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

	private class SignUpAction implements View.OnClickListener {
		@Override
		public void onClick(View view) {			
			// Get login, password and full name from TextEdit
			EditText loginInEdit   = (EditText) mRootView.findViewById(R.id.loginUpEdit);
			EditText fullNameUpEdit   = (EditText) mRootView.findViewById(R.id.fullNameUpEdit);
			EditText passInEdit   = (EditText) mRootView.findViewById(R.id.passUpEdit);
			
			String login = loginInEdit.getText().toString();
			String fullName = fullNameUpEdit.getText().toString();
			String password = passInEdit.getText().toString();
			
			// DataDroid-lib RequestManager
			RestRequestManager requestManager = RestRequestManager.from(getActivity());
			// Integrate login and password into request
			Request request = RequestFactory.getSignUpRequest(login, fullName, password);
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
		}

		@Override
		public void onRequestConnectionError(Request request, int statusCode) {
			Toast.makeText(getActivity(), "onRequestConnectionError", 5).show();
		}

		@Override
		public void onRequestDataError(Request request) {
			Toast.makeText(getActivity(), "onRequestDataError", 5).show();
		}

		@Override
		public void onRequestCustomError(Request request, Bundle resultData) {
			Toast.makeText(getActivity(), "onRequestCustomError", 5).show();
		}
		
	};
	
}
