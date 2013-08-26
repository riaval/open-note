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
import com.opennote.model.provider.RestContact.Note;

public class EditNoteOperation implements Operation {

	@Override
	public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
		String host = context.getString(R.string.host_name);
		long noteID = request.getLong("id");
		String address = "http://" + host + "/api/snote/" + noteID;

		NetworkConnection connection = new NetworkConnection(context, address);
		HashMap<String, String> params = new HashMap<String, String>();
		String sessionHash = request.getString("session_hash");
		String title = request.getString("title");
		String body = request.getString("body");
		params.put("session_hash", sessionHash);
		params.put("title", title);
		params.put("body", body);
		connection.setParameters(params);

		connection.setMethod(Method.PUT);
		ConnectionResult result = connection.execute();
		
		ContentValues note = new ContentValues();
		note.put(Note.TITLE, title);
		note.put(Note.BODY, body);
		
		context.getContentResolver().update(
				  RestContact.Note.CONTENT_URI
				, note
				, Note._ID+"=?"
				, new String[]{String.valueOf(noteID)}
		);
		
		return null;
	}
	
}
