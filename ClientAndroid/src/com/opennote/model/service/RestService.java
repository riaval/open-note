package com.opennote.model.service;

import com.foxykeep.datadroid.service.RequestService;
import com.opennote.model.RequestFactory;
import com.opennote.model.operations.SignInOperation;
import com.opennote.model.operations.SignUpOperation;

public class RestService extends RequestService{

	@Override
	public Operation getOperationForType(int requestType) {
		switch (requestType) {
		case RequestFactory.SIGN_IN:
			return new SignInOperation();
		case RequestFactory.SIGN_UP:
			return new SignUpOperation();
		default:
			return null;
		}
	}

}
