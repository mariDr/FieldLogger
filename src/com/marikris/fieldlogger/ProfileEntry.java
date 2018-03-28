package com.marikris.fieldlogger;

/**
 * The class defines an array of two profile entry
 * where each profile contains name and password
 * 
 */
public class ProfileEntry{
	// constructor
	String name;
	String password;

	public ProfileEntry() {
	}

	public ProfileEntry(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public String getUsername() {
		return name;
	}

	public void setUsername(String username) {
		this.name = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
