package com.opennote.model.service;

import com.foxykeep.datadroid.service.RequestService;
import com.opennote.model.RequestFactory;
import com.opennote.model.operations.AddNoteOperation;
import com.opennote.model.operations.LoadNotesOperation;
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
		case RequestFactory.LOAD_NOTES:
			return new LoadNotesOperation();
		case RequestFactory.ADD_NOTE:
			return new AddNoteOperation();
		default:
			return null;
		}
	}

}
