package service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dao.DAOFactory;
import dao.HibernateUtil;
import domain.Session;
import domain.User;

public class UserService {

	public long createUser(String login, String fullName, String password, String email, String hostIp) throws Exception {
		if(login.length() < 6){
			throw new IllegalArgumentException("Login length < 6 characters.");
		}
		if(password.length() < 6){
			throw new IllegalArgumentException("Password length < 6 characters.");
		}
		
		User user = new User(login, fullName, passwordHash(password), email);
		Session session = new Session(hostIp);
		session.setUser(user);
		
		HibernateUtil.beginTransaction();
		DAOFactory.getUserDAO().save(user);
		DAOFactory.getSessionDAO().save(session);
		HibernateUtil.commitTransaction();
		
		return session.getId();
	}
	
	
	private String passwordHash(String password) throws NoSuchAlgorithmException{
		final String SALT = "sflprt49fhi2";
		String hash1 = null;
		String hash2 = null;
		try {
			hash1 = getHash(password);
			hash2 = getHash(hash1 + SALT);
		} catch (NoSuchAlgorithmException e) {
			throw new NoSuchAlgorithmException("Could not get hash", e);
		}
		
		return hash2;
	}
	
	private String getHash(String str) throws NoSuchAlgorithmException{
		 
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes());
		
		byte byteData[] = md.digest();
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		
		return sb.toString();
	}
}
