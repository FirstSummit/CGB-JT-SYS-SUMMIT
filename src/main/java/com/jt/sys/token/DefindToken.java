package com.jt.sys.token;

import org.apache.shiro.authc.UsernamePasswordToken;

public class DefindToken extends UsernamePasswordToken{

	private static final long serialVersionUID = 1L;
	
	 private String loginType;

	    public DefindToken(final String username, final String password,String loginType) {
	        super(username,password);
	        this.loginType = loginType;
	    }

		
		public String getLoginType() {
	        return loginType;
	    }

	    public void setLoginType(String loginType) {
	        this.loginType = loginType;
	    }

	

}
