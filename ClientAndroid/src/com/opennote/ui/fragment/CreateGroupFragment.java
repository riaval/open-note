package com.opennote.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.ErrorFactory;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;
import com.opennote.model.service.RestService;
import com.opennote.ui.activity.MainActivity;

public class CreateGroupFragment extends Fragment {
	private View mRootView;
	String mGroupSlug;
	String mGroupName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_create_group, container,false);

		// Get action button
		Button button = (Button) mRootView.findViewById(R.id.createGroupBt);
		// Add onClick listener
		button.setOnClickListener(new CreateGroupAction());
		
		return mRootView;
	}
	
	private class CreateGroupAction implements View.OnClickListener {
		@Override
		public void onClick(View view) {			
			// Get request values
			mGroupSlug = ((EditText) mRootView.findViewById(R.id.groupSlug)).getText().toString();
			mGroupName = ((EditText) mRootView.findViewById(R.id.groupName)).getText().toString();
			String sessionHash = ((MainActivity)getActivity()).getSessionHash();
			
			// DataDroid-lib RequestManager
			RestRequestManager requestManager = RestRequestManager.from(getActivity());
			// Integrate session hash and group slug into request
			Request request = RequestFactory.getCreateGroupRequest(sessionHash, mGroupSlug, mGroupName);
			// Add RequestListener
			requestManager.execute(request, mRequestListener);
		}		
	}
	
	private RequestListener mRequestListener = new RequestListener() {
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			MainActivity mainActivity = (MainActivity) getActivity();
			mainActivity.loadGroups();
			mainActivity.updateGroups(mGroupSlug);
			Toast.makeText(getActivity(), mGroupName + " created", Toast.LENGTH_LONG).show();
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	    	imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS,0);
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
