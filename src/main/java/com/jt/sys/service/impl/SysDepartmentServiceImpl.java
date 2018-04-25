package com.jt.sys.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jt.common.exception.ServiceException;
import com.jt.common.util.ShiroUtils;
import com.jt.common.vo.Node;
import com.jt.sys.dao.SysDepartmentDao;
import com.jt.sys.dao.SysUserDao;
import com.jt.sys.dao.SysUserDepartmentDao;
import com.jt.sys.entity.SysDepartment;
import com.jt.sys.entity.SysUser;
import com.jt.sys.service.SysDepartmentService;
/**负责菜单业务处理
 * @Transactional 注解可以应用在类上，也可以应用在方法上
 * 1)当应用在类上时，表示类中所有方法都采用默认事务处理策略
 * 2)当类上和方法上都有这个注解时，默认采用方法上定义的事务
 * 处理策略。
 * */
@Service
public class SysDepartmentServiceImpl implements SysDepartmentService {
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysDepartmentDao sysDepartmentDao;
	@Autowired
	private SysUserDepartmentDao sysUserDepartmentDao;
	@Override
	public List<Map<String, Object>> findObjects() {
		return sysDepartmentDao.findObjects();
	}
	@Override
	public List<Node> findZTreeNodes() {
		return sysDepartmentDao.findZTreeNodes();
	}
	
	@Override
	public List<SysDepartment> findDepartmentByName(String name,Integer sort) {
		List<SysDepartment>list=null;
		try {
			 list= sysDepartmentDao.findDepartmentByName(name,sort);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new ServiceException("服务器维护中");
		}
		
		return list;
	}
	@Override
	public SysUser findManegerByUserName(String name) {
		SysUser sysUser=null;
		try {
			sysUser = sysUserDao.findUserByUserName(name);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new ServiceException("服务器维护中");
		}
		
		return sysUser;
	}
	@Override
	public int saveObject(SysDepartment entity) {
		
		//1.合法验证
		if(entity==null)
			throw new ServiceException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new ServiceException("部门名不能为空");
		entity.setCreatedUser(ShiroUtils.getPrincipal().getUsername());
		entity.setCreatedTime(new Date());
		int rows;
		//2.保存数据
		try{
		rows=sysDepartmentDao.insertObject(entity);
		}catch(Exception e){
		e.printStackTrace();
		throw new ServiceException("保存失败");
		}
		//3.返回数据
		return rows;
	}
	@Override
	public int deleteDepartmentById(Integer id) {
		findDepartmentById(id);
		int childCount = sysDepartmentDao.getChildCount(id);
		if(childCount>0)
			throw new ServiceException("此元素有子元素，不允许删除");
		int rows = 0;
		try {
			rows = sysDepartmentDao.deleteDepartmentById(id);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new ServiceException("服务器维护中");
		}
		return rows;
	}
	@Override
	public Map<String, Object> findDepartmentById(Integer id) {
		Map<String, Object> map= sysDepartmentDao.findDepartmentById(id);
		if(StringUtils.isEmpty(map)) 
			throw new ServiceException("该部门可能已被删除");
		return map;
	}
	@Override
	public int updateDepartments(SysDepartment entity) {
		//1.合法验证
		if(entity==null)
		throw new ServiceException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
		throw new ServiceException("菜单名不能为空");
		entity.setModifiedUser(ShiroUtils.getPrincipal().getUsername());
		entity.setModifiedTime(new Date());
		int rows;
		//2.更新数据
		try{
		rows=sysDepartmentDao.updateDepartment(entity);
		}catch(Exception e){
		e.printStackTrace();
		throw new ServiceException("更新失败");
		}
		//3.返回数据
		return rows;
	}
	@Override
	public String findDepartmentNameByUserId(Integer userId) {
		String departmentName = sysUserDepartmentDao.findDepartmentNameByUserId(userId);
		System.out.println(departmentName);
		return departmentName;
	}
	
}






