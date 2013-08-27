package com.opennote.model.operation;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

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

public class FindUsersOperation implements Operation {

	@Override
	public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
		String host = context.getString(R.string.host_name);
		String adress = "http://" + host + "/api/users/";
		NetworkConnection connection = new NetworkConnection(context, adress);
		
		HashMap<String, String> params = new HashMap<String, String>();
		String sessionHash = request.getString("session_hash");
		String searchValue = request.getString("search");

		params.put("session_hash", sessionHash);
		params.put("search", searchValue);

		connection.setParameters(params);
		connection.setMethod(Method.GET);
		ConnectionResult result = connection.execute();
		ContentValues[] usersValues;
		try {
			JSONArray groupsJson = new JSONArray(result.body);
			usersValues = new ContentValues[groupsJson.length()];
			for (int i = 0; i < groupsJson.length(); i++) {
				ContentValues user = new ContentValues();
				user.put("login", groupsJson.getJSONObject(i).getString("login"));
				user.put("fullName", groupsJson.getJSONObject(i).getString("full_name"));
				user.put("date", groupsJson.getJSONObject(i).getString("date"));
				usersValues[i] = user;
			}
			
		} catch (JSONException e) {
			throw new DataException(e.getMessage());
		}
		context.getContentResolver().delete(RestContact.User.CONTENT_URI, null, null);
        context.getContentResolver().bulkInsert(RestContact.User.CONTENT_URI, usersValues);
        
		return null;
	}

}
