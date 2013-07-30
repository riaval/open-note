package com.opennote.ui.local;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opennote.R;

public class CreateGroupFragment extends Fragment {
	private View mRootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_create_group, container, false);
		
		return mRootView;
	}
	
}
