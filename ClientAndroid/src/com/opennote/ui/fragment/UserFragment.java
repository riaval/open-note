package com.opennote.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.opennote.R;

public class UserFragment extends Fragment {

	View mRootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_user, container, false);

		// Get action button
		Button button = (Button) mRootView.findViewById(R.id.userLogOutBt);
		// Add onClick listener
		button.setOnClickListener(new LogOutAction());
		
		return mRootView;
	}
	
	private class LogOutAction implements View.OnClickListener {
		@Override
		public void onClick(View view) {	
			SharedPreferences sharedPref = getActivity().getSharedPreferences(getActivity().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			
			editor.putString(getActivity().getString(R.string.session_hash), null);
			editor.commit();
			
			getActivity().finish();
			startActivity(getActivity().getIntent());
		}		
	}
	
}
