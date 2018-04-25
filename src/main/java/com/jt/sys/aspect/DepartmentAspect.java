package com.jt.sys.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jt.sys.dao.SysDepartmentDao;
import com.jt.sys.dao.SysUserDepartmentDao;
import com.jt.sys.entity.SysUser;

@Component
@Aspect
public class DepartmentAspect {
	@Autowired
	private SysUserDepartmentDao sysUserDepartmentDao;
	@Autowired
	private SysDepartmentDao sysDepartmentDao;
	@Pointcut("execution(* com.jt.sys.service.impl.SysUserServiceImpl.saveObject(..))")
	public void pointCutSave() {
		
	}
	
	@Pointcut("execution(* com.jt.sys.service.impl.SysUserServiceImpl.updateObject(..))")
	public void pointCutUpdate() {
		
	}
	
	@AfterReturning("pointCutUpdate()")
	public void updateDepartment(JoinPoint jp) {
		Object[] object = jp.getArgs();
		SysUser user = (SysUser) object[0];
		int user_id = user.getId();
		int department_id = (int) object[2];
		int oldDepartmentId = (int) object[3];
		sysUserDepartmentDao.deleteObjectById(user_id);
		sysUserDepartmentDao.insertObject(user_id, department_id);
		int oldCount = sysDepartmentDao.findCountById(oldDepartmentId);
		oldCount--;
		sysDepartmentDao.updateCount(oldDepartmentId, oldCount);
		int count = sysDepartmentDao.findCountById(department_id);
		count++;
		sysDepartmentDao.updateCount(department_id, count);
	}
	
	@AfterReturning("pointCutSave()")
	public void saveDepartment(JoinPoint jp) {
		Object[] object = jp.getArgs();
		SysUser user = (SysUser) object[0];
		int user_id = user.getId();
		int department_id = (int) object[2];
		sysUserDepartmentDao.insertObject(user_id, department_id);
		int count = sysDepartmentDao.findCountById(department_id);
		count++;
		sysDepartmentDao.updateCount(department_id,count);
	}
}
