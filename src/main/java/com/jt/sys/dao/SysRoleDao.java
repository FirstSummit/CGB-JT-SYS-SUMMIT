package com.jt.sys.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jt.common.vo.CheckBox;
import com.jt.sys.entity.SysRole;
public interface SysRoleDao {
	List<CheckBox> findObjects();
	/**获取当前页数据*/
	List<SysRole> findPageObjects(
			@Param("pageCurrent") int pageCurrent,
			@Param("name") String name);
	
	/**添加删除功能*/
	int deleteObject(@Param("ids") String[] ids);
	int insertObject(SysRole entity);
	SysRole findObjectById(Integer id);
	int updateObject(SysRole entity);
}
