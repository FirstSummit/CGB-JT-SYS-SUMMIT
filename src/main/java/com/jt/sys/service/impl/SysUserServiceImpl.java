package com.jt.sys.service.impl;

import java.io.OutputStream;
import java.lang.reflect.Field;
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
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.jt.common.exception.ServiceException;
import com.jt.common.vo.CheckBox;
import com.jt.common.vo.PageObject;
import com.jt.sys.dao.SysRoleDao;
import com.jt.sys.dao.SysUserDao;
import com.jt.sys.dao.SysUserRoleDao;
import com.jt.sys.entity.SysUser;
import com.jt.sys.service.SysUserService;
@Service
public class SysUserServiceImpl implements SysUserService {
	
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Override
	public void login(String username,String password) {
		System.out.println(sysRoleDao.getClass().getName());
		//0.参数合法性验证
		if(StringUtils.isEmpty(username))
		throw new ServiceException("用户名不能为空");
		if(StringUtils.isEmpty(password))
		throw new ServiceException("密码不能为空");
		//1.获取Subject(主体)对象
		Subject subject=SecurityUtils.getSubject();
		//2.封装用户名和密码(封装到一个令牌对象)
		UsernamePasswordToken token=
		new UsernamePasswordToken(
		username, password);
	    //3.执行身份认证
		try {
		subject.login(token);
		//此请求会提交给SecurityManager
		//SecurityManager会调用认证处理器Authenticator
		//认证处理器会去访问相关Realm对象获取认证信息
		} catch (AuthenticationException e) {
		e.printStackTrace();
		throw new ServiceException("用户名或密码不正确");
		}
	}
	
	
	@Override
	public void updateObject(SysUser entity,String roleIds) {
        
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
		if(user==null)
		throw new ServiceException("用户已经不存在");
		//3.查询用户角色
		List<Integer> roleIds=
		sysUserRoleDao.findRoleIdsByUserId(id);
		//4.封装数据
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("user", user);
		map.put("roleIds", roleIds);
		return map;
	}
	
	@Override
	public int saveObject(SysUser entity, 
			String roleIds) {
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
	public PageObject<SysUser> findPageObjects(
			Integer pageCurrent, 
			String username) {
		//1.查询总记录数
		int rowCount=
		sysUserDao.getRowCount(username);
		//2.查询当前页记录
		int pageSize=3;
		if(pageCurrent==null||pageCurrent<1)
		throw new ServiceException("当前页码不正确");
		int startIndex=(pageCurrent-1)*pageSize;
		List<SysUser> list=sysUserDao.findPageObjects(
				startIndex,
				pageSize, username);
		//3.封装数据
		PageObject<SysUser> pageObject=new PageObject<>();
		pageObject.setRecords(list);
		pageObject.setRowCount(rowCount);
		pageObject.setPageSize(pageSize);
		pageObject.setPageCurrent(pageCurrent);
		//4.返回数据
		return pageObject;
	}
	
	@Override
	public Workbook findObjects(OutputStream out) throws Exception {
		//查询获取所有的用户信息
		List<SysUser> sysUsers = sysUserDao.findObjects();
		System.out.println(sysUsers);
		//创建一个workbook
		Workbook workbook = new XSSFWorkbook();	
		//得到一个POI工具类
		//CreationHelper helper = workbook.getCreationHelper();
		// 在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称
        Sheet sheet = workbook.createSheet("userdata");
        // 用于格式化单元格的数据
        DataFormat format = workbook.createDataFormat();
        
        // 设置字体
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 20); // 字体高度
        font.setColor(Font.COLOR_NORMAL); // 字体颜色
        font.setFontName("黑体"); // 字体
        font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 宽度
        //font.setItalic(true); // 是否使用斜体
        // font.setStrikeout(true); //是否使用划线
        
        // 设置单元格类型保存文字
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中
        cellStyle.setWrapText(true);  // 自动换行?
        
        //设置单元格格式保存数字?
        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setDataFormat(format.getFormat("＃,##0.0"));
        
        //设置单元格格式保存日期
        CellStyle cellStyle3 = workbook.createCellStyle();
        cellStyle3.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));
        //创建表头
        Cell head = sheet.createRow(0).createCell(0);
        head.setCellValue("user list");
        head.setCellStyle(cellStyle);
        Row row = sheet.createRow(1);
        Field[] fields = sysUsers.get(0).getClass().getDeclaredFields();
       for(int i=1; i<fields.length;i++){
    	   System.out.println(fields[i]);
    	   Cell cell = row.createCell(i-1);
    	   String name = fields[i].getName();
    	   System.out.println(name);
    	   cell.setCellValue(name);
    	   cell.setCellStyle(cellStyle);
       }
       
       //插入数据
       for(int i =0;i<sysUsers.size();i++){
    	   SysUser sysUser = sysUsers.get(i);
    	   row = sheet.createRow(2+i);
    	   for(int j=1; j<fields.length;j++){
    		   Cell cell = row.createCell(j-1);
    		   Field field = fields[j];
    		   String name = field.getName();
    		   name ="get"+name.substring(0, 1).toUpperCase().concat(name.substring(1));
    		   Object value = sysUser.getClass().getMethod(name).invoke(sysUser);
    		   System.out.println(field.getType());
    		   if(field.getType()==String.class && value!=null){
    			   cell.setCellValue((String)value);
    		   }else if(field.getType()==Integer.class && value!=null){
    			   cell.setCellValue((Integer)value);
    			   cell.setCellStyle(cellStyle2);
    		   }else if(field.getType()==Date.class && value!=null){
    			   System.out.println(value);
    			   cell.setCellValue((Date)value);
    			   cell.setCellStyle(cellStyle3);
    		   }
    	   }
       }
       workbook.write(out);
       out.close();
       return workbook;
	}
}
