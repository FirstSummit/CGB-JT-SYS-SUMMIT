package com.jt.sys.token;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;


public class LoginModularRealmAuthenticator extends ModularRealmAuthenticator{
	@Override
	protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		//判断getRealms是否为空
		 assertRealmsConfigured();
		 //强制类型转换
		DefindToken loginToken = (DefindToken) authenticationToken;
		//获取登录类型
		String loginType = loginToken.getLoginType();
		//获取所有的realms
	        Collection<Realm> realms = getRealms();
	        Collection<Realm> typeRealms = new ArrayList<>();
	        for (Realm realm : realms) {
	        	System.out.println(realm.getName());
	            if (realm.getName().contains(loginType)){
	                typeRealms.add(realm);
	            }
	        }
	        System.out.println(typeRealms.size());
	        if (typeRealms.size() == 1) {
	        	System.out.println("执行一个realm");
	            return doSingleRealmAuthentication(typeRealms.iterator().next(), authenticationToken);
	        } else {
	        	
	            return doMultiRealmAuthentication(realms, authenticationToken);
	        }
			
	} 
	
	
	
	}
		

	
	


