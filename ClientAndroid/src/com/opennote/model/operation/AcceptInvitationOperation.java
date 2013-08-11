package com.opennote.model.operation;

import java.util.HashMap;

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

public class AcceptInvitationOperation implements Operation {

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
		
		connection.setMethod(Method.POST);
		ConnectionResult result = connection.execute();
		
		context.getContentResolver().delete(
				RestContact.Invitation.CONTENT_URI
				, Invitation.GROUP_SLUG+"=?"
				, new String[]{groupSlug}
		);
		
		String groupName = request.getString("name");
		ContentValues group = new ContentValues();
		group.put("slug", groupSlug);
		group.put("name", groupName);
		group.put("role", "member");
		
		context.getContentResolver().insert(RestContact.Group.CONTENT_URI, group);
		
		return null;
	}

}
