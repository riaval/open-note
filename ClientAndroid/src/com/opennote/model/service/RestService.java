package com.opennote.model.service;

import com.foxykeep.datadroid.service.RequestService;
import com.opennote.model.RequestFactory;
import com.opennote.model.operation.AcceptInvitationOperation;
import com.opennote.model.operation.AddNoteOperation;
import com.opennote.model.operation.CreateGroupOperation;
import com.opennote.model.operation.CreateInvitationOperation;
import com.opennote.model.operation.DeleteInvitationOperation;
import com.opennote.model.operation.FindUsersOperation;
import com.opennote.model.operation.LoadGroupsOperation;
import com.opennote.model.operation.LoadInvitationsOperation;
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
		case RequestFactory.INVITE_USER:
			return new FindUsersOperation();
		case RequestFactory.LOAD_INVITATIONS:
			return new LoadInvitationsOperation();
		case RequestFactory.DELETE_INVITATION:
			return new DeleteInvitationOperation();
		case RequestFactory.CREATE_INVITATION:
			return new CreateInvitationOperation();
		case RequestFactory.ACCEPT_INVITATION:
			return new AcceptInvitationOperation();
		default:
			return null;
		}
	}

}
