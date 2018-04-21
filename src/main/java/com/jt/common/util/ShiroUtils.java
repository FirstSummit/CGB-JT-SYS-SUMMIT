package com.jt.common.util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import com.jt.sys.entity.SysUser;
public class ShiroUtils {
	 private ShiroUtils(){}
	 /**获取Subject(主体用户)的身份*/
	 public static SysUser getPrincipal(){
		 return (SysUser)SecurityUtils
		        .getSubject().getPrincipal();
	 }
	 public static Session getSession(){
		 return SecurityUtils
	        .getSubject().getSession();
	 }
	 public static void setSessionAttribute(
			String key,Object value){
		 getSession().setAttribute(key, value);
	 }
	 public static Object getSessionAttribute(String key){
		 return getSession().getAttribute(key);
	 }
}





