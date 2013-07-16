package com.opennote.model.service;

import com.foxykeep.datadroid.service.RequestService;
import com.opennote.model.RequestFactory;
import com.opennote.model.operations.SignInOperation;

public class RestService extends RequestService{

	@Override
	public Operation getOperationForType(int requestType) {
		switch (requestType) {
		case RequestFactory.SIGN_IN:
			return new SignInOperation();
		default:
			return null;
		}
	}

}
