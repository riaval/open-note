package com.opennote.model.operation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.foxykeep.datadroid.exception.ConnectionException;
import com.foxykeep.datadroid.exception.CustomRequestException;
import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.network.NetworkConnection;
import com.foxykeep.datadroid.network.NetworkConnection.ConnectionResult;
import com.foxykeep.datadroid.network.NetworkConnection.Method;
import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.service.RequestService.Operation;
import com.opennote.R;
import com.opennote.model.provider.RestContact;
import com.opennote.model.provider.RestContact.Note;

public class AddNoteOperation implements Operation {

	@Override
	public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
		String host = context.getString(R.string.host_name);
		String slug = request.getString("slug");
		String address = "http://" + host + "/api/groups/" + slug + "/snote/";

		NetworkConnection connection = new NetworkConnection(context, address);
		HashMap<String, String> params = new HashMap<String, String>();
		String title = request.getString("title");
		String body = request.getString("body");
		String sessionHash = request.getString("session_hash");
		params.put("title", title);
		params.put("body", body);
		params.put("session_hash", sessionHash);
		connection.setParameters(params);

		connection.setMethod(Method.POST);
		ConnectionResult result = connection.execute();

		SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		String login = sharedPref.getString(context.getString(R.string.user_login), null);
		String fullName = sharedPref.getString(context.getString(R.string.user_full_name), null);
		String color = sharedPref.getString(context.getString(R.string.user_color), null);
		
		// Generate SQLite insert 
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM. HH:mm", Locale.US);
		ContentValues note = new ContentValues();
		note.put(Note.TITLE, title);
		note.put(Note.BODY, body);
		note.put(Note.DATE, sdf.format(Calendar.getInstance().getTime()));
		note.put(Note.FULL_NAME, fullName);
		note.put(Note.LOGIN, login);
		note.put(Note.GROUP, slug);
		note.put(Note.COLOR, color);
		
		context.getContentResolver().insert(RestContact.Note.CONTENT_URI, note);

		return null;
	}

}
