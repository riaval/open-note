package com.opennote.model.operation;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

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

public class EditSimpleDataOperation implements Operation {

	@Override
	public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
		String host = context.getString(R.string.host_name);
		String address = "http://" + host + "/api/users/";

		NetworkConnection connection = new NetworkConnection(context, address);
		HashMap<String, String> params = new HashMap<String, String>();
		String sessionHash = request.getString("session_hash");
		String fullName = request.getString("full_name");
		String email = request.getString("email");
		params.put("session_hash", sessionHash);
		params.put("full_name", fullName);
		params.put("email", email);
		connection.setParameters(params);

		connection.setMethod(Method.PUT);
		ConnectionResult result = connection.execute();
		
		try {
        	JSONObject JSONRoot = new JSONObject(result.body);
			JSONObject JSONStatus = JSONRoot.getJSONObject("status");
			
			int code = JSONStatus.getInt("code");
			if (code < 200 || code >= 300){
				throw new JSONException("ok message not found");
			}
		} catch (JSONException e) {
			throw new CustomRequestException(result.body) {
				private static final long serialVersionUID = 1L;
			};
		}
		
		SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(context.getString(R.string.user_full_name), fullName);
		editor.putString(context.getString(R.string.user_email), email);
		editor.commit();
		
		return null;
	}
	
}
