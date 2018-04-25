package com.jt.sys.token;

import org.apache.shiro.authc.UsernamePasswordToken;

public class DefindToken extends UsernamePasswordToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String method;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public DefindToken() {
		super();
	}

	public DefindToken(String username, char[] password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}

	public DefindToken(String username, char[] password, boolean rememberMe) {
		super(username, password, rememberMe);
	}

	public DefindToken(String username, char[] password, String host) {
		super(username, password, host);
	}

	public DefindToken(String username, char[] password) {
		super(username, password);
	}

	public DefindToken(String username, String password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}

	public DefindToken(String username, String password, boolean rememberMe) {
		super(username, password, rememberMe);
	}

	public DefindToken(String username, String password, String host) {
		super(username, password, host);
	}

	public DefindToken(String username, String password) {
		super(username, password);
	}
	
}
