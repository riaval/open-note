package com.opennote.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.opennote.R;

public class FeedbackFragment extends Fragment {
	
	private String sRecipient = "riaval@yandex.com";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
		
		final EditText subjectEdit = (EditText) rootView.findViewById(R.id.feedSubjectEdit);
		final EditText bodyEdit = (EditText) rootView.findViewById(R.id.feedBodyEdit);
		Button sendButton = (Button) rootView.findViewById(R.id.feedBt);
		sendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{sRecipient});
				i.putExtra(Intent.EXTRA_SUBJECT, subjectEdit.getText().toString());
				i.putExtra(Intent.EXTRA_TEXT   , bodyEdit.getText().toString());
				try {
				    startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
				    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}
			}	

		});
		
		return rootView;
	}

}
