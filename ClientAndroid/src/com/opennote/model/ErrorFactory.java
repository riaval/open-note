package com.opennote.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;

import com.opennote.R;
import com.opennote.ui.activity.MainActivity;

public class ErrorFactory {
	
	public static void showConnectionErrorMessage(Context context){
		String title = "Connection error!";
		String message = "Could not connect to server.";
		showError(context, title, message);
		
	}
	
	public static void showClientErrorMessage(Context context, String message) {
		String title = "Request error!";
		showError(context, title, message);
	}

	public static void showServerErrorMessage(Context context, String message) {
		String title = "Server error!";
		showError(context, title, message);

	}
	
	static private void showError(Context context, String title, String message){
		AlertDialog.Builder alertDialog;
		alertDialog = new AlertDialog.Builder(context);		
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setNeutralButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		alertDialog.show();
	}
	
	static public void doError(Context context, int code, String comment){
		if (code == 401){
			Activity main = MainActivity.instance;
			SharedPreferences sharedPref = main.getSharedPreferences(main.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			
			editor.putString(main.getString(R.string.session_hash), null);
			editor.commit();
			
			main.finish();
			main.startActivity(main.getIntent());
			return;
		}
		if (code >= 400 || code < 500){
			ErrorFactory.showClientErrorMessage(context, comment);
			return;
		}
		if (code >= 500){
			ErrorFactory.showServerErrorMessage(context, comment);
			return;
		}
	}

}
