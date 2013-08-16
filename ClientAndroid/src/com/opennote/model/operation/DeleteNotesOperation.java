package com.opennote.model.operation;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

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
import com.opennote.model.provider.RestContact.Invitation;

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
		

		connection.setMethod(Method.DELETE);
		ConnectionResult result = connection.execute();
		
		context.getContentResolver().delete(
				RestContact.Note.CONTENT_URI
				, Note._ID+"=?"
				, IDs.toArray( new String[IDs.size()] )
		);
		
		return null;
	}
	
}
