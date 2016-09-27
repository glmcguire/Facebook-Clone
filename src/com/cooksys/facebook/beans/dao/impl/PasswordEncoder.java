package com.cooksys.facebook.beans.dao.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * This class takes a given password, and encodes the password 
 * using Spring's BCryptePasswordEncoder class. This is an algorithm 
 * that hashes and then salts the password for security.
 * 
 * Once the encrypted password is made and stored, it can me compared to 
 * a raw password using BCryptePasswordEncoder's match method. 
 * @author Gary McGuire
 *
 */
public class PasswordEncoder {


	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	String cryptedPassword = null;
	
	public String encryptPassword(String password){
		String cryptedPassword = passwordEncoder.encode(password);
		return cryptedPassword;
	}
	

	public BCryptPasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public String getCryptedPassword() {
		return cryptedPassword;
	}

	public void setCryptedPassword(String cryptedPassword) {
		this.cryptedPassword = cryptedPassword;
	}

}
