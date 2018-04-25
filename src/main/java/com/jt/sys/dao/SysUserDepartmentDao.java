package com.jt.sys.dao;

import org.apache.ibatis.annotations.Param;

public interface SysUserDepartmentDao {
	int insertObject(@Param("user_id")Integer user_id,@Param("department_id")Integer department_id);	
	
	int findDepartmentIdByUserId(@Param("user_id")Integer user_id);
	int deleteObjectById(Integer id);
	
	String findDepartmentNameByUserId(@Param("user_id")Integer user_id);
}
