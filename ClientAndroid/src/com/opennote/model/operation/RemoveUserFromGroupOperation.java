package com.opennote.model.operation;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.opennote.model.provider.RestContact.Group;

public class RemoveUserFromGroupOperation implements Operation {

	@Override
	public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
		String host = context.getString(R.string.host_name);
		String groupSlug = request.getString("slug");
		String address = "http://" + host + "/api/groups/" + groupSlug + "/users";

		NetworkConnection connection = new NetworkConnection(context, address);
		HashMap<String, String> params = new HashMap<String, String>();
		String sessionHash = request.getString("session_hash");
		params.put("session_hash", sessionHash);
		connection.setParameters(params);

		connection.setMethod(Method.DELETE);
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
		
		context.getContentResolver().delete(
				RestContact.Group.CONTENT_URI
				, Group.SLUG+"=?"
				, new String[]{groupSlug}
		);
		return null;
	}

}
