package service;

import java.util.Set;

import domain.UserGroup;

public interface GroupService {

	public void createGroup(String slug, String name, String sessionHash) throws Exception;

	public Set<UserGroup> getGroups(String sessionHash) throws Exception;

	public void deleteGroup(String slug, String sessionHash) throws Exception;

}
