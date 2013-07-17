package com.opennote.model;

import android.os.Bundle;

import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;

public class RestRequestListener implements RequestListener{

	@Override
	public void onRequestFinished(Request request, Bundle resultData) {
	}

	@Override
	public void onRequestConnectionError(Request request, int statusCode) {
		System.out.println("RequestConnectionError");
		showError();
	}

	@Override
	public void onRequestDataError(Request request) {
		System.out.println("RequestDataError");
		System.out.println(request.getString("login"));
		
	}

	@Override
	public void onRequestCustomError(Request request, Bundle resultData) {
		System.out.println("RequestCustomError");
		showError();
	}
	
	void showError() {
//		listView.onRefreshComplete();
//		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//		builder.
//			setTitle(android.R.string.dialog_alert_title).
//			setMessage(getString(R.string.faled_to_load_data)).
//			create().
//			show();
	}

}
