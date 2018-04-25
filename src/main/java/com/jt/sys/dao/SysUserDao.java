package com.jt.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jt.sys.entity.SysUser;

public interface SysUserDao {
	
	
	
	/**
	 * 根据用户名查找用户信息
	 * @param username
	 * @return
	 */
	SysUser findUserByUserName(String username);
	/**
	 * 根据用户id查找用户权限标识信息
	 * 例如：sys:role:view,sys:role:add
	 * @param userId
	 * @return
	 * ArrayList 数组(线性结构)
	 * 
	 */
	List<String> findUserPermissions(String username);
	
	
	int updateObject(SysUser entity);
	/**
	 * 根据用户id查找用户对象
	 * @param id 为用户id
	 * @return
	 */
	SysUser findObjectById(Integer id);
	int insertObject(SysUser entity);
	
	List<SysUser> findPageObjects(
			@Param("username")String username,
			@Param("pageCurrent")Integer startIndex);
	
	int validById(@Param("id")Integer id,
			      @Param("valid")Integer valid);
	
	// 查询所有的用户信息
	List<SysUser> findObjects();
	SysUser findUserByPhone(String mobile);
}
