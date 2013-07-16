package com.opennote.model;

import android.content.Context;

import com.foxykeep.datadroid.requestmanager.RequestManager;
import com.opennote.model.service.RestService;

public final class RestRequestManager extends RequestManager {
	private RestRequestManager(Context context) {
		super(context, RestService.class);
	}
	
	private static RestRequestManager sInstance;
	
	public static RestRequestManager from(Context context) {
		if (sInstance == null) {
			sInstance = new RestRequestManager(context);
		}
		return sInstance;
	}
}
