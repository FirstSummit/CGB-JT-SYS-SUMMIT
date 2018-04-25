package com.jt.sys.service.impl;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.View;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jt.common.exception.ServiceException;
import com.jt.common.util.ExcelUtil;
import com.jt.common.util.ShiroUtils;
import com.jt.common.vo.CheckBox;
import com.jt.sys.dao.SysRoleDao;
import com.jt.sys.dao.SysUserDao;
import com.jt.sys.dao.SysUserDepartmentDao;
import com.jt.sys.dao.SysUserRoleDao;
import com.jt.sys.entity.SysUser;
import com.jt.sys.service.SysUserService;
import com.jt.sys.service.UserPdfView;
import com.jt.sys.token.DefindToken;
@Service
public class SysUserServiceImpl implements SysUserService {
	@Autowired
	private SysUserDepartmentDao sysUserDepartmentDao;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Override
	public void login(String method,String username,String password) {
		System.out.println(sysRoleDao.getClass().getName());
		System.out.println("service.login");
		//0.参数合法性验证
		
		if(StringUtils.isEmpty(username))
			throw new ServiceException("用户名不能为空");
		if(StringUtils.isEmpty(password))
			throw new ServiceException("密码不能为空");
		//1.获取Subject(主体)对象
		Subject subject=SecurityUtils.getSubject();
		//2.封装用户名和密码
		DefindToken token=
				new DefindToken(
						username, password);
		//3.执行身份认证
		token.setMethod(method);
		try {
			subject.login(token);
			//此请求会提交给SecurityManager
			//SecurityManager会调用认证处理器Authenticator
			//认证处理器会去访问相关Realm对象获取认证信息
		} catch (AuthenticationException e) {
			e.printStackTrace();
			throw new ServiceException("用户名或密码不正确");
		}
		//4.记录用户信息
		Session session=
				SecurityUtils.getSubject().getSession();
		session.setAttribute("user", username);
	}
	
	@Override
	public void login02(String method,String mobile, String password) {
		if(StringUtils.isEmpty(mobile))
			throw new ServiceException("电话号码不能为空");
		if(StringUtils.isEmpty(password))
			throw new ServiceException("密码不能为空");
		Subject subject=SecurityUtils.getSubject();
		//2.封装用户名和密码
			System.out.println(mobile+"   /"+password);
		DefindToken token=
				new DefindToken(
						mobile,password);
		token.setMethod(method);
		//3.执行身份认证
		try {
		subject.login(token);
		//此请求会提交给SecurityManager
		//SecurityManager会调用认证处理器Authenticator
		//认证处理器会去访问相关Realm对象获取认证信息
		} catch (AuthenticationException e) {
		e.printStackTrace();
		throw new ServiceException("电话号码和验证码不匹配");
		}
		
	}
	
	
	@Override
	public void updateObject(SysUser entity,String roleIds,Integer departmentId,Integer oldDepartmentId) {
        
		//1.对数据进行合法验证
		if (entity == null)
		throw new ServiceException("保存对象不能为空");
		if (StringUtils.isEmpty(entity.getUsername()))
		throw new ServiceException("用户名不能为空");
		if (StringUtils.isEmpty(roleIds))
		throw new ServiceException("必须要选择一个角色");
		//2.更新数据
		//2.1设置新密码
		System.out.println("entity.getPassword()="+entity.getPassword());
		if(!StringUtils.isEmpty(entity.getPassword())){
		String pwd=entity.getPassword();
		String salt=UUID.randomUUID().toString();
		SimpleHash sHash=//Shiro中的一个类
		new SimpleHash("MD5",pwd,salt);
		String newPwd=sHash.toString();
		entity.setSalt(salt);
		entity.setPassword(newPwd);
		entity.setModifiedUser(ShiroUtils.getPrincipal().getUsername());
		}
		//2.2更新数据
		try{
		sysUserDao.updateObject(entity);
		sysUserRoleDao.deleteObject(entity.getId(),null);
		sysUserRoleDao.insertObject(
			entity.getId(), roleIds.split(","));
		}catch(Exception e){
		e.printStackTrace();
		throw new ServiceException("系统维护中");
		}
	}
	@Override
	public Map<String, Object> findObjectById(Integer id) {
		//1.合法性验证
		if(id==null||id<1)
		throw new ServiceException("数据不合法,id="+id);
		//2.查询用户信息
		SysUser user=sysUserDao.findObjectById(id);
		int departmentId = sysUserDepartmentDao.findDepartmentIdByUserId(id);
		if(user==null)
		throw new ServiceException("用户已经不存在");
		//3.查询用户角色
		List<Integer> roleIds=
		sysUserRoleDao.findRoleIdsByUserId(id);
		//4.封装数据
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("departmentId",departmentId );
		map.put("user", user);
		map.put("roleIds", roleIds);
		return map;
	}
	
	@Override
	public int saveObject(SysUser entity, 
			String roleIds,Integer departmentId) {
		//1.对数据进行合法验证
		if(entity==null)
		throw new ServiceException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getUsername()))
		throw new ServiceException("用户名不能为空");
		if(StringUtils.isEmpty(entity.getPassword()))
		throw new ServiceException("用户密码不能为空");
		if(StringUtils.isEmpty(roleIds))
		throw new ServiceException("必须要选择一个角色");
		//2.保存用户数据
		//2.1对密码进行加密(后续会采用md5加密算法)
		String salt=UUID.randomUUID().toString();
		String pwd=entity.getPassword();
		SimpleHash sHash=//Shiro中的一个类
		new SimpleHash("MD5",pwd, salt);
		String newPwd=sHash.toString();
		
		
		entity.setSalt(salt);
		entity.setPassword(newPwd);
		entity.setCreatedUser(ShiroUtils.getPrincipal().getUsername());

		//2.2存储用户信息
		int rows;
		try{
		
		rows=sysUserDao.insertObject(entity);
		}catch(Exception e){
		e.printStackTrace();
		//报警
		 if(e instanceof DuplicateKeyException){
		 throw new ServiceException("此用户已存在");
		 }
		 throw new ServiceException("系统维护中");
		}
		//3.保存用户和角色的关系数据
		try{
		sysUserRoleDao.insertObject(
				entity.getId(),
				roleIds.split(","));
		}catch(Exception e){
		e.printStackTrace();
		 //报警
		throw new ServiceException("系统维护中");
		}
		//4.返回结果
		return rows;
	}
	@Override
	public List<CheckBox> findRoles() {
		return sysRoleDao.findObjects();
	}
	//Subject.isPermitted("sys:user:update")
	//当访问此方法时,假如有@RequiresPermissions
	//这个注解,表示此用户必须是授权用户,才能访问
	@RequiresPermissions("sys:user:update")
	@Override
	public int validById(Integer id, 
			Integer valid) {
		//1.合法性验证
		if(id==null||id<1)
		throw new ServiceException("数据不合法,id="+id);
		if(valid==null)
		throw new ServiceException("状态值不能为空");
		if(valid!=0&&valid!=1)
		throw new ServiceException("状态值不正确,valid="+valid);
		//2.执行更新操作
		int rows;
		try{
	    rows=sysUserDao.validById(id, valid);
		}catch(Throwable e){
		e.printStackTrace();
	    //报警
		throw new ServiceException("系统维护中");
		}
		//3.验证结果并处理
		if(rows==0)
		throw new ServiceException("数据可能已经不存在");
		return rows;
	}
	
	@Override
	public PageInfo<SysUser> findPageObjects(
			Integer pageCurrent,String username) {
		PageHelper.startPage(pageCurrent, 5);
		List<SysUser> list = sysUserDao.findPageObjects(username, pageCurrent);
		PageInfo<SysUser> info=new PageInfo<>(list,5);
		return info;
	}
	
	@Override
	public Workbook findObjects(OutputStream out) throws Exception {
		//查询获取所有的用户信息
		List<SysUser> sysUsers = sysUserDao.findObjects();
		if(sysUsers==null || sysUsers.size()==0)
			throw new ServiceException("查询结果为空");
		Workbook workbook = ExcelUtil.creatExcel("userdata", sysUsers);
		workbook.write(out);
		out.close();
		return workbook;
	}

	
	@Override
	public View findObjectsPdf() {
		List<SysUser> sysUsers = sysUserDao.findObjects();
		if(sysUsers==null || sysUsers.size()==0)
			throw new ServiceException("查询结果为空");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("sysUsers", sysUsers);
		UserPdfView pdf = new UserPdfView();
		pdf.setAttributesMap(map);
		return pdf;
	}
}
