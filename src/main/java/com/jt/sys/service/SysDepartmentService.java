package com.jt.sys.service;
import java.util.List;
import java.util.Map;

import com.jt.common.vo.Node;
import com.jt.sys.entity.SysDepartment;
import com.jt.sys.entity.SysUser;

public interface SysDepartmentService {
	int saveObject(SysDepartment entity);
	/*int updateObject(SysMenu entity);
	*/
	List<Node> findZTreeNodes();
	
	List<Map<String,Object>> findObjects();
	
	/**
	 * 在用户添加部门或者修改部门时，根据用户输入的部门名或者排序号查找部门数据库，判断此部门是否已经存在
	 * @param name 用户输入的部门名
	 * @return true表示存在 false表示不存在
	 */
	List<SysDepartment> findDepartmentByName(String name,Integer sort);
	
	/**
	 * 根据用户输入的用户姓名判断此用户是否存在，用于用户在添加或修改部门的时候判断用户输入的部门经理是否存在
	 * @param username 用户输入的部门经理姓名
	 * @return
	 */
	SysUser findManegerByUserName(String username);
	
	/*Map<String,Object> findObjectById(Integer id);
	int deleteObject(Integer id);*/
	
	int deleteDepartmentById(Integer id);
	
	Map<String, Object> findDepartmentById(Integer id);
	
	int updateDepartments(SysDepartment entity);
	
	String findDepartmentNameByUserId(Integer userId);
	
}
