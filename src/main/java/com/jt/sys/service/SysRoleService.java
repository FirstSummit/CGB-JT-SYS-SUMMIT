package com.jt.sys.service;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jt.sys.entity.SysRole;
public interface SysRoleService {
	 /**
	  * 查询当前页数据
	  * @param pageCurrent (当前页码)
	  * @return
	  */
	 PageInfo<SysRole> findPageObjects(
			 Integer pageCurrent, String name);

	 int deleteObject(String idStr);
	 int saveObject(SysRole entity,String menuIds);
	 
	 Map<String,Object> findObjectById(Integer id);
	 
	 int updateObject(SysRole entity,String menuIds);

	
	 
}
