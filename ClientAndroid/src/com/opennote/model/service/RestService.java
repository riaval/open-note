package com.opennote.model.service;

import com.foxykeep.datadroid.service.RequestService;
import com.opennote.model.RequestFactory;
import com.opennote.model.operation.AddNoteOperation;
import com.opennote.model.operation.CreateGroupOperation;
import com.opennote.model.operation.LoadGroupsOperation;
import com.opennote.model.operation.LoadNotesOperation;
import com.opennote.model.operation.SignInOperation;
import com.opennote.model.operation.SignUpOperation;

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
		case RequestFactory.CREATE_GROUP:
			return new CreateGroupOperation();
		case RequestFactory.LOAD_GROUPS:
			return new LoadGroupsOperation();
		default:
			return null;
		}
	}

}
