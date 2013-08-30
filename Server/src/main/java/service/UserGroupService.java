package service;

public interface UserGroupService {

	public void deleteUserFromGroup(String sessionHash, String groupSlug) throws Exception;

}
