package com.opennote.model.operation;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
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

		ContentValues note = new ContentValues();
		try {
			JSONObject jSONObject = new JSONObject(result.body);
			
			note.put(Note._ID, jSONObject.getString("id"));
			note.put(Note.TITLE, jSONObject.getString("title"));
			note.put(Note.BODY, jSONObject.getString("body"));
			note.put(Note.DATE, jSONObject.getString("date"));
			
			JSONObject JSONUser = jSONObject.getJSONObject("user");
			note.put(Note.LOGIN, JSONUser.getString("login"));
			note.put(Note.FULL_NAME, JSONUser.getString("full_name"));
			note.put(Note.COLOR, JSONUser.getInt("color"));
			
			note.put(Note.GROUP, slug);
		} catch (JSONException e) {
			throw new CustomRequestException(result.body) {
				private static final long serialVersionUID = 1L;
			};
		}
		
		context.getContentResolver().insert(RestContact.Note.CONTENT_URI, note);

		return null;
	}

}
