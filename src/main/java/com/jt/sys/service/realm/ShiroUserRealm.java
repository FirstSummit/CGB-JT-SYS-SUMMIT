package com.jt.sys.service.realm;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.jt.sys.dao.SysUserDao;
import com.jt.sys.entity.SysUser;
import com.jt.sys.token.DefindToken;

import mybatis.po.SysUsers;
import mybatis.po.SysUsersExample;
import mybatis.po.SysUsersExample.Criteria;
/***
 * 借助此realm类型的对象实现用户认证及权限
 * 相关数据的获取
 * FAQ?
 * 1)谁来调用此realm对象?(认证管理器，授权管理器)
 *
 */
public class ShiroUserRealm  extends AuthorizingRealm {
	@Autowired
	private SysUserDao sysUserDao;
	/**
	 * 在此方法中完成权限数据的获取以及封装
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
	    PrincipalCollection principals) {
		//1.获取用户信息
		SysUser sysUser=(SysUser)principals.getPrimaryPrincipal();
		System.out.println("AuthorizationInfo.user="+sysUser);
		//2.获取用户权限信息
		List<String> list=
	    sysUserDao.findUserPermissions(sysUser.getUsername());
		//3.去除用户重复或值为空的权限信息(一个用户可以有多个角色，一个角色可以对应多个权限)
		Set<String> permissionSet=new HashSet<>();
		for(String permission:list){
			if(!StringUtils.isEmpty(permission)){
				permissionSet.add(permission);
			}
		}
		
		
	
		System.out.println("permissionSet="+permissionSet);
		//4.对用户权限信息进行封装
		SimpleAuthorizationInfo info=
		new SimpleAuthorizationInfo();
		info.setStringPermissions(permissionSet);
		return info;
	}
	/**
	 * 在此方法中完成认证数据的获取以及封装
	 * @param token 封装了主体(Subject)的身份信息
	 * Subject.login(token)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		//1.从token中获取Subject身份信息
		DefindToken upToken=
		(DefindToken)token;
		if(!("userAndpwd".equals(upToken.getMethod()))){
			return null;
		}
		String username=upToken.getUsername();
		//2.根据用户信息进行数据库查询
		//SysUser user=
		//sysUserDao.findUserByUserName(username);
		SysUsersExample example=new SysUsersExample();
		Criteria criteria=example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<SysUsers> selectByExample =sysUserDao.selectByExample(example);
		SysUsers user=selectByExample.get(0);
		//3.对查询结果进行验证(例如这个用户是否已经被禁用了)
		if(user==null)
		throw new AuthenticationException("此用户不存在");
		if(user.getValid()==0)
		throw new AuthenticationException("用户已被禁用，请联系客服");
		//4.对查询结果相关数据进行封装
		ByteSource credentialsSalt=
		ByteSource.Util.bytes(user.getSalt());
		
		SimpleAuthenticationInfo info=
		new SimpleAuthenticationInfo(
				user,//principal (主体用户身份)
				user.getPassword(),//hashedCredentials
				credentialsSalt,//credentialsSalt
				this.getName());//realmName
		//5.返回封装结果
		return info;
	}

}
