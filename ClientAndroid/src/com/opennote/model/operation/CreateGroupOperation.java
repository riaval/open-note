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

public class CreateGroupOperation implements Operation {

	@Override
	public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
		String host = context.getString(R.string.host_name);
		String slug = request.getString("slug");
		String address = "http://" + host + "/api/groups/" + slug;

		NetworkConnection connection = new NetworkConnection(context, address);
		HashMap<String, String> params = new HashMap<String, String>();
		String groupName = request.getString("name");
		String sessionHash = request.getString("session_hash");
		params.put("name", groupName);
		params.put("session_hash", sessionHash);
		connection.setParameters(params);
		
		connection.setMethod(Method.POST);
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
		
		ContentValues group = new ContentValues();
		group.put("slug", slug);
		group.put("name", groupName);
		group.put("role", "creator");
		
		context.getContentResolver().insert(RestContact.Group.CONTENT_URI, group);
		
		return null;
	}

}
