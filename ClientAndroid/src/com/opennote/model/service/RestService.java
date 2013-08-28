package com.opennote.model.service;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.foxykeep.datadroid.exception.CustomRequestException;
import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.service.RequestService;
import com.opennote.model.RequestFactory;
import com.opennote.model.operation.AcceptInvitationOperation;
import com.opennote.model.operation.CreateGroupOperation;
import com.opennote.model.operation.CreateInvitationOperation;
import com.opennote.model.operation.CreateNoteOperation;
import com.opennote.model.operation.DeleteGroupOperation;
import com.opennote.model.operation.DeleteInvitationOperation;
import com.opennote.model.operation.DeleteNotesOperation;
import com.opennote.model.operation.EditNoteOperation;
import com.opennote.model.operation.EditPasswordOperation;
import com.opennote.model.operation.EditSimpleDataOperation;
import com.opennote.model.operation.FindUsersOperation;
import com.opennote.model.operation.LoadAllNotesOperation;
import com.opennote.model.operation.LoadGroupsOperation;
import com.opennote.model.operation.LoadInvitationsOperation;
import com.opennote.model.operation.LoadNotesOperation;
import com.opennote.model.operation.RemoveUserFromGroupOperation;
import com.opennote.model.operation.SignInOperation;
import com.opennote.model.operation.SignUpOperation;

public class RestService extends RequestService{
	
	public static String STATUS_CODE = "RestService.statusCode";
	public static String MESSAGE = "RestService.message";
	public static String COMMENT = "RestService.comment";

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
			return new CreateNoteOperation();
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
		case RequestFactory.DELETE_GROUP:
			return new DeleteGroupOperation();
		case RequestFactory.REMOVE_USER:
			return new RemoveUserFromGroupOperation();
		case RequestFactory.EDIT_NOTE:
			return new EditNoteOperation();
		case RequestFactory.LOAD_ALL_NOTES:
			return new LoadAllNotesOperation();
		case RequestFactory.DELETE_NOTES:
			return new DeleteNotesOperation();
		case RequestFactory.EDIT_SIMPLE_DATA:
			return new EditSimpleDataOperation();
		case RequestFactory.EDIT_PASSWORD:
			return new EditPasswordOperation();
		default:
			return null;
		}
	}
	
	@Override
	protected Bundle onCustomRequestException(Request request, CustomRequestException exception) {
		String body = exception.getMessage();
		
		try {
			// parse JSON
			JSONObject JSONRoot = new JSONObject(body);
			JSONObject JSONStatus = JSONRoot.getJSONObject("status");
			
			int code = JSONStatus.getInt("code");
			String message = JSONStatus.getString("message");
			String comment = JSONRoot.getString("comment");
			
			// Create bandle
			Bundle bundle = new Bundle();
			bundle.putInt(STATUS_CODE, code);
			bundle.putString(MESSAGE, message);
			bundle.putString(COMMENT, comment);
			
			return bundle;
		} catch (JSONException e) {
			e.printStackTrace();
		}

        return null;
    }

}
