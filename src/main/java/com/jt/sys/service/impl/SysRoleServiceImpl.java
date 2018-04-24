package com.jt.sys.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jt.common.exception.ServiceException;
import com.jt.sys.dao.SysRoleDao;
import com.jt.sys.dao.SysRoleMenuDao;
import com.jt.sys.dao.SysUserRoleDao;
import com.jt.sys.entity.SysRole;
import com.jt.sys.service.SysRoleService;
@Service
public class SysRoleServiceImpl implements SysRoleService{
    @Autowired
	private SysRoleDao sysRoleDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Override
    public int updateObject(SysRole entity,String menuIds) {
    	//1.合法性验证
    	if(entity==null)
        throw new ServiceException("更新的对象不能为空");
    	if(entity.getId()==null)
    	throw new ServiceException("id的值不能为空");
    	if(StringUtils.isEmpty(entity.getName()))
    	throw new ServiceException("角色名不能为空");
    	if(StringUtils.isEmpty(menuIds))
    	throw new ServiceException("必须为角色指定一个权限");
    	//2.判定对象是否还存在
    	SysRole role=
    	sysRoleDao.findObjectById(entity.getId());
    	if(role==null)
        throw new ServiceException("对象可能已经不存在");
    	//3.更新数据
    	int rows;
    	try{
    	
    	rows=sysRoleDao.updateObject(entity);
    	sysRoleMenuDao.deleteObject(entity.getId());
    	sysRoleMenuDao.insertObject(entity.getId(),
    			menuIds.split(","));
    	
    	}catch(Exception e){
    	e.printStackTrace();
    	//系统报警，给运维人员发短信
    	throw new ServiceException("更新失败，服务端忙");
    	}
    	//4.返回结果
    	return rows;
    }
    
    @Override
    public Map<String,Object> findObjectById(Integer id) {
    	//1.合法性验证
    	if(id==null||id<=0)
    	throw new ServiceException("id的值不合法");
    	//2.执行查询
    	SysRole role=sysRoleDao.findObjectById(id);
    	List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleId(id);
    	//3.验证结果并返回
    	if(role==null)
    	throw new ServiceException("此记录已经不存在");
    	if(menuIds==null)
    	throw new ServiceException("角色与菜单记录不存在");
    	Map<String,Object> map=new HashMap<String, Object>();
    	map.put("role", role);
    	map.put("menuIds", menuIds);
    	return map;
    }
    
    @Override
    public int saveObject(SysRole entity,String menuIds) {
    	//1.合法性验证
    	if(entity==null)
        throw new ServiceException("保存数据不能为空");
    	if(StringUtils.isEmpty(entity.getName()))
    	throw new ServiceException("角色名不能为空");
    	//2.保存数据
    	int rows;
    	try{
    	rows=sysRoleDao.insertObject(entity);
    	sysRoleMenuDao.insertObject(
    			entity.getId(),
    			menuIds.split(","));
    	}catch(Exception e){
    	//给系统维护人员发短信(出现问题了)
    	e.printStackTrace();
    	throw new ServiceException("保存失败,系统正维护");
    	}
    	//3.返回结果
    	return rows;
    }
	@Override
	public PageInfo<SysRole> findPageObjects(
			Integer pageCurrent,String name) {
	      PageHelper.startPage(pageCurrent, 5);
	      List<SysRole> list = sysRoleDao.findPageObjects(pageCurrent, name);
		PageInfo<SysRole> info=new PageInfo<>(list,5);
		return info;
	}
	@RequiresPermissions("sys:role:delete")
	@Override
	public int deleteObject(String idStr) {
		//1.参数合法性验证
				if(StringUtils.isEmpty(idStr))
				throw new ServiceException("必须选中才能删除");
				//2.解析字符串
				String[] ids=idStr.split(",");
				//3.调用数据层方法执行删除操作
				int rows=sysRoleDao.deleteObject(ids);
				
				for(String id:ids){
					sysRoleMenuDao.deleteObject(Integer.valueOf(id));
				    sysUserRoleDao.deleteObject(null, Integer.valueOf(id));
				}
				//4.返回处理结果
				if(rows==0)
				throw new ServiceException("数据已经不存在");
				return rows;
			}
}
