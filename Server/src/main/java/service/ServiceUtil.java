package service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServiceUtil {

	static public String getSaltMD5(String password) throws NoSuchAlgorithmException{
		final String SALT = "sflprt49fhi2";
		String hash1 = null;
		String hash2 = null;
		try {
			hash1 = getMD5(password);
			hash2 = getMD5(hash1 + SALT);
		} catch (NoSuchAlgorithmException e) {
			throw new NoSuchAlgorithmException("Could not get hash", e);
		}
		
		return hash2;
	}
	
	static private String getMD5(String str) throws NoSuchAlgorithmException{
		 
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
