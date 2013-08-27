package com.opennote.model.operation;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Note;

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

public class DeleteNotesOperation implements Operation {

	@Override
	public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
		String host = context.getString(R.string.host_name);
		int length = request.getInt("length");
		String address = "http://" + host + "/api/snote/";

		NetworkConnection connection = new NetworkConnection(context, address);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		String sessionHash = request.getString("session_hash");
		params.add( new BasicNameValuePair("session_hash", sessionHash) );
		List<String> IDs = new ArrayList<String>();
		for(int i=0; i<length; i++){
			String id = String.valueOf( request.getLong("id"+i) );
			IDs.add(id);
			params.add(new BasicNameValuePair("id", id));
		}
		connection.setParameters(params);
		
		String where = "(";
		for (int i=0; i<IDs.size(); i++){
			where += "?,";
		}
		where = where.substring(0, where.length()-1) + ")";

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
				RestContact.Note.CONTENT_URI
				, Note._ID+" in " + where
				, IDs.toArray( new String[IDs.size()] )
		);
		
		return null;
	}
	
}
