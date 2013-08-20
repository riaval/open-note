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

public final class SignInOperation implements Operation {
	
	@Override
	public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
		String host = context.getString(R.string.host_name);
		String login = request.getString("login");
		String address = "http://" + host + "/api/sessions/" + login;

		NetworkConnection connection = new NetworkConnection(context, address);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("password", request.getString("password"));
		connection.setParameters(params);
		connection.setMethod(Method.POST);
		ConnectionResult result = connection.execute();
		
		String sessionHash;
        try {
			JSONObject jsonBbject = new JSONObject(result.body);
//			SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//			SharedPreferences.Editor editor = sharedPref.edit();
			
			sessionHash = jsonBbject.get("session_hash").toString();
//			editor.putString(context.getString(R.string.session_hash), sessionHash);
//			editor.putString(context.getString(R.string.user_login), login);
//			editor.commit();
        } catch (JSONException e) {
            throw new DataException(e.getMessage());
        }
        
		params.clear();
		params.put("session_hash", sessionHash);
		address = "http://" + host + "/api/users/";
		connection = new NetworkConnection(context, address);
		connection.setParameters(params);
		connection.setMethod(Method.GET);
		result = connection.execute();
		
		String fullName;
        String email;
        
        try {
			JSONObject jsonBbject = new JSONObject(result.body);
			fullName = jsonBbject.get("full_name").toString();	
			email = jsonBbject.get("email").toString();
		} catch (JSONException e) {
			throw new DataException(e.getMessage());
		}
        
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		
		editor.putString(context.getString(R.string.session_hash), sessionHash);
		editor.putString(context.getString(R.string.user_login), login);
		editor.putString(context.getString(R.string.user_full_name), fullName);
		editor.putString(context.getString(R.string.user_email), email);
		
		editor.commit();
      
		return null;
	}
	
}
