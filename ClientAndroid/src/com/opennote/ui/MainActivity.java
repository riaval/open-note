package com.opennote.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
	    try {
	    	File sdcard = Environment.getExternalStorageDirectory();
	    	File file = new File(sdcard,"data/OpenNote/Session.txt");
	    	BufferedReader br = new BufferedReader(new FileReader(file));
			Context context = getApplicationContext();
			Toast.makeText(context, br.readLine(), 20).show();
//			FileWriter f = new FileWriter("/sdcard/data/OpenNote/Session.txt");
			//f.append("long hash number");
//			f.close();
		} catch (IOException e) {
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
