package com.jt.sys.service;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.jt.common.vo.CheckBox;
import com.jt.common.vo.PageObject;
import com.jt.sys.entity.SysUser;

public interface SysUserService {
	   /**
	    * 执行登录业务处理
	    * @param username
	    * @param password
	    */
	   void login(String username,String password);
	
	   /**
	    * 根据用户id查找用户信息以及对应的角色
	    * @param id
	    * @return 封装用户以及用户对应的角色ID
	    */
	   Map<String,Object> findObjectById(Integer id);
	   
	   void updateObject(SysUser entity,String roleIds);
	
	   int saveObject(SysUser entity,String roleIds);
	
	   List<CheckBox> findRoles();
	
	   PageObject<SysUser> findPageObjects(
			   Integer pageCurrent,
			   String username);
	   
	   int validById(Integer id,Integer valid);
	   
	   Workbook findObjects(OutputStream out) throws Exception;
	   
}
