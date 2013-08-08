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

public class LoadGroupsOperation implements Operation {

	@Override
	public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
		String host = context.getString(R.string.host_name);
		String adress = "http://" + host + "/api/groups/";
		NetworkConnection connection = new NetworkConnection(context, adress);
		
		HashMap<String, String> params = new HashMap<String, String>();
		String sessionHash = request.getString("session_hash");
		params.put("session_hash", sessionHash);
		connection.setParameters(params);
		connection.setMethod(Method.GET);
		ConnectionResult result = connection.execute();
		ContentValues[] groupsValues;
		try {
			JSONArray userGroupsJson = new JSONArray(result.body);
			groupsValues = new ContentValues[userGroupsJson.length()];
			for (int i = 0; i < userGroupsJson.length(); i++) {
				JSONObject groupsJson = userGroupsJson.getJSONObject(i).getJSONObject("group");
				String groupSlug = groupsJson.getString("slug");
				String groupName = groupsJson.getString("name");
				String groupRole = userGroupsJson.getJSONObject(i).getString("group_role");
				
				ContentValues group = new ContentValues();
				group.put("slug", groupSlug);
				group.put("name", groupName);
				group.put("role", groupRole);

				groupsValues[i] = group;
			}
			
		} catch (JSONException e) {
			throw new DataException(e.getMessage());
		}
		context.getContentResolver().delete(RestContact.Group.CONTENT_URI, null, null);
        context.getContentResolver().bulkInsert(RestContact.Group.CONTENT_URI, groupsValues);
        
		return null;
	}

}
