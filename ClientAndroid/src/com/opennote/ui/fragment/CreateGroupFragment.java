package com.opennote.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.opennote.R;
import com.opennote.model.RequestFactory;
import com.opennote.model.RestRequestManager;
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
			mainActivity.addGroup(mGroupSlug, mGroupName);
			mainActivity.updateGroups(mGroupSlug);
			Toast.makeText(getActivity(), "onRequestFinished", 5).show();
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
