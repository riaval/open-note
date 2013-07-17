package com.opennote.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.widget.Toast;

import com.opennote.R;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//		SharedPreferences.Editor editor = sharedPref.edit();
//		editor.clear();
//		editor.commit();
		String value = sharedPref.getString("session_hash", null);
		
		if(value == null){
			Intent intent = new Intent(this, OpeningActivity.class);
			startActivity(intent);
			this.finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
