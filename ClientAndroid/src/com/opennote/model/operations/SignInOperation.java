package com.opennote.model.operations;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public final class SignInOperation implements Operation {
	
	private String mHost;
	private String mLogin;
	
	@Override
	public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
		mHost = context.getString(R.string.host_name);
		mLogin = request.getString("login");
		String address = "http://" + mHost + "/api/sessions/" + mLogin;

		NetworkConnection connection = new NetworkConnection(context, address);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("password", request.getString("password"));
		connection.setParameters(params);

		connection.setMethod(Method.POST);
		ConnectionResult result = connection.execute();
		
        try {
			JSONObject jsonBbject = new JSONObject(result.body);
			SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			
			String sessionHash = jsonBbject.get("session_hash").toString();
			editor.putString(context.getString(R.string.session_hash), sessionHash);
			editor.putString(context.getString(R.string.user_login), mLogin);
			editor.commit();
			// Load group list
			setGroups(context, sessionHash);
        } catch (JSONException e) {
            throw new DataException(e.getMessage());
        }
      
		return null;
	}
	
	private void setGroups(Context context, String sessionHash) throws ConnectionException, DataException, CustomRequestException{
		String adress = "http://" + mHost + "/api/groups/";
		NetworkConnection connection = new NetworkConnection(context, adress);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("session_hash", sessionHash);
		connection.setParameters(params);
		connection.setMethod(Method.GET);
		ConnectionResult result = connection.execute();
		ContentValues[] groupsValues;
		try {
			JSONArray groupsJson = new JSONArray(result.body);
			groupsValues = new ContentValues[groupsJson.length()];

			for (int i = 0; i < groupsJson.length(); i++) {
				ContentValues group = new ContentValues();
				group.put("slug", groupsJson.getJSONObject(i).getString("slug"));
				group.put("name", groupsJson.getJSONObject(i).getString("name"));
				groupsValues[i] = group;
			}
			
		} catch (JSONException e) {
			throw new DataException(e.getMessage());
		}
        context.getContentResolver().bulkInsert(RestContact.Group.CONTENT_URI, groupsValues);
	}
	
}
