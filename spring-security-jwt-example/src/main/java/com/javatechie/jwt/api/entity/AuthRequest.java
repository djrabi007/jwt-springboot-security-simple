package com.javatechie.jwt.api.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class AuthRequest implements Serializable {
	private static final long serialVersionUID = 5926468583005150707L;


    private String username;
    private String password;
	public String getUserName() {
		return username;
	}
	public void setUserName(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public AuthRequest() {
		
	}
	public AuthRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
    
}
