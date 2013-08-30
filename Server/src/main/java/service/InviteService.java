package service;

import java.util.Set;

import domain.Invite;

public interface InviteService {

	public void createInvitation(String slug, String login, String sessionHash) throws Exception;

	public Set<Invite> getInvitations(String sessionHash) throws Exception;

	public void deleteInvitation(String sessionHash, long id) throws Exception;

	public void acceptInvitation(String sessionHash, String groupSlug) throws Exception;

}
