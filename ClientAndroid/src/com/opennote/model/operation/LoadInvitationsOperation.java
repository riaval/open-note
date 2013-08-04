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
import com.opennote.model.provider.RestContact.Invitation;

public class LoadInvitationsOperation implements Operation {

	@Override
	public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
		String host = context.getString(R.string.host_name);
		String adress = "http://" + host + "/api/invitations";
		NetworkConnection connection = new NetworkConnection(context, adress);
		System.out.println(adress);
		HashMap<String, String> params = new HashMap<String, String>();
		String sessionHash = request.getString("session_hash");
		params.put("session_hash", sessionHash);
		connection.setParameters(params);
		connection.setMethod(Method.GET);
		ConnectionResult result = connection.execute();
		ContentValues[] groupsValues;
		try {
			JSONArray groupsJson = new JSONArray(result.body);
			groupsValues = new ContentValues[groupsJson.length()];
			
			for (int i = 0; i < groupsJson.length(); i++) {
				ContentValues invitation = new ContentValues();
				JSONObject user = groupsJson.getJSONObject(i).getJSONObject("user");
				JSONObject group = groupsJson.getJSONObject(i).getJSONObject("group");
				
				invitation.put(Invitation._ID, groupsJson.getJSONObject(i).getString("id"));
				invitation.put(Invitation.USER_LOGIN, user.getString("login"));
				invitation.put(Invitation.USER_NAME, user.getString("fullName"));
				invitation.put(Invitation.GROUP_SLUG, group.getString("slug"));
				invitation.put(Invitation.GROUP_NAME, group.getString("name"));
				groupsValues[i] = invitation;
			}
			
		} catch (JSONException e) {
			throw new DataException(e.getMessage());
		}
		context.getContentResolver().delete(RestContact.Invitation.CONTENT_URI, null, null);
        context.getContentResolver().bulkInsert(RestContact.Invitation.CONTENT_URI, groupsValues);
		return null;
	}

}
