package com.opennote.model.operations;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.foxykeep.datadroid.exception.ConnectionException;
import com.foxykeep.datadroid.exception.CustomRequestException;
import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.network.NetworkConnection;
import com.foxykeep.datadroid.network.NetworkConnection.ConnectionResult;
import com.foxykeep.datadroid.network.NetworkConnection.Method;
import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.service.RequestService.Operation;

public final class SignInOperation implements Operation {
	final String HOST = "192.168.0.100:8080";
	
	@Override
	public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
		String login = request.getString("login");
		String address = "http://" + HOST + "/api/sessions/" + login;
		System.out.println(address);
		NetworkConnection connection = new NetworkConnection(context, address);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("password", request.getString("password"));
		connection.setParameters(params);
		
		connection.setMethod(Method.POST);
		ConnectionResult result = connection.execute();
		
		String str = result.body;
		System.out.println(str);
		System.out.println("==========================================================");
		
//		ContentValues[] tweetsValues;
//		try {
//			JSONArray tweetsJson = new JSONArray(result.body);
//			tweetsValues = new ContentValues[tweetsJson.length()];
			
//			for (int i = 0; i < tweetsJson.length(); ++i) {
//				ContentValues tweet = new ContentValues();
//				tweet.put("user_name", tweetsJson.getJSONObject(i).getJSONObject("user").getString("name"));
//				tweet.put("body", tweetsJson.getJSONObject(i).getString("text"));
//				tweetsValues[i] = tweet;
//			}
			
//			tweetsValues = new ContentValues[5];
//			ContentValues tweet = new ContentValues();
//			tweet.put("user_name", "sdgsdg");
//			tweet.put("body", "text");
//			tweetsValues[0] = tweet;
//			tweetsValues[1] = tweet;
//			tweetsValues[2] = tweet;
			
//		} catch (JSONException e) {
//			throw new DataException(e.getMessage());
//		}
		
//		context.getContentResolver().delete(Contract.Tweets.CONTENT_URI, null, null);
//		context.getContentResolver().bulkInsert(Contract.Tweets.CONTENT_URI, tweetsValues);
		return null;
	}
	
}
