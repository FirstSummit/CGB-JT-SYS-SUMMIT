package com.jt.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jt.common.vo.Node;
import com.jt.sys.entity.SysDepartment;

public interface SysDepartmentDao {
	/**获取菜单信息及对应的上级菜单的名称
	 * Map<String,Object>：一行记录对应一个map
	 * key:为字段名
	 * value:为字段对应值
	 * 多行记录对应多个map，多个map存储到list集合
	 * */
	List<Map<String,Object>> findObjects();
	
	List<Node> findZTreeNodes();
	
	
	/**
	 * 在用户添加部门或者修改部门时，根据用户输入的部门名或排序号查找部门数据库，判断此部门是否已经存在
	 * @param name 用户输入的部门名
	 * @return
	 */
	List<SysDepartment> findDepartmentByName(@Param("name")String name,@Param("sort")Integer sort);
	
	int insertObject(SysDepartment entity);
	
	int deleteDepartmentById(Integer id);
	
	Map<String, Object> findDepartmentById(Integer id);
	
	int getChildCount(Integer id);
	
	int updateDepartment(SysDepartment entity);
	
	int findCountById(Integer id);
	
	int updateCount(@Param("department_id")Integer department_id,@Param("count")Integer count);
}
