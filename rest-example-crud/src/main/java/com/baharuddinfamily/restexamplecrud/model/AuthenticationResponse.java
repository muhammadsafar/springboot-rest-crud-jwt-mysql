package com.baharuddinfamily.restexamplecrud.model;

public class AuthenticationResponse {

	private String usernameLogged;

	private String passwordLogged;

	private String asRole;

	private final String jwt;

	public AuthenticationResponse(String usernameLogged, String passwordLogged, String asRole, String jwt) {
		super();
		this.usernameLogged = usernameLogged;
		this.passwordLogged = passwordLogged;
		this.asRole = asRole;
		this.jwt = jwt;
	}

	public String getUsernameLogged() {
		return usernameLogged;
	}

	public String getPasswordLogged() {
		return passwordLogged;
	}

	public String getAsRole() {
		return asRole;
	}

	public String getJwt() {
		return jwt;
	}

}
