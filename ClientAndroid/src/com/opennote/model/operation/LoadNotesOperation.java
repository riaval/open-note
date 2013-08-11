package com.opennote.model.operation;

import java.util.HashMap;

import org.json.JSONArray;
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

public class LoadNotesOperation implements Operation{

	@Override
	public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
		String host = context.getString(R.string.host_name);
		String slug = request.getString("slug");
		String address = "http://" + host + "/api/groups/" + slug + "/snote/";

		NetworkConnection connection = new NetworkConnection(context, address);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("session_hash", request.getString("session_hash"));
		connection.setParameters(params);

		connection.setMethod(Method.GET);
		ConnectionResult result = connection.execute();
		
		ContentValues[] notesValues;
        try {
        	JSONArray groupsJson = new JSONArray(result.body);
        	notesValues = new ContentValues[groupsJson.length()];

			for (int i = 0; i < groupsJson.length(); i++) {
				ContentValues note = new ContentValues();
				note.put(Note.TITLE, groupsJson.getJSONObject(i).getString("title"));
				note.put(Note.BODY, groupsJson.getJSONObject(i).getString("body"));
				note.put(Note.DATE, groupsJson.getJSONObject(i).getString("date"));
				JSONObject user = groupsJson.getJSONObject(i).getJSONObject("user");

				note.put("user", user.getString("fullName"));
				
				note.put(Note._ID, groupsJson.getJSONObject(i).getString("id"));
				note.put(Note.GROUP, slug);
				note.put(Note.COLOR, user.getInt("color"));
				notesValues[i] = note;
			}
		} catch (JSONException e) {
			throw new DataException(e.getMessage());
		}
        context.getContentResolver().delete(RestContact.Note.CONTENT_URI, Note.GROUP + " = ?", new String[]{slug});
        context.getContentResolver().bulkInsert(RestContact.Note.CONTENT_URI, notesValues);
        
		return null;
	}

}
