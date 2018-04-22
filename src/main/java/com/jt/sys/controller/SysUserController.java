package com.jt.sys.controller;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jt.common.vo.JsonResult;
import com.jt.common.vo.PageObject;
import com.jt.sys.entity.SysUser;
import com.jt.sys.service.SysUserService;

@RequestMapping("/user/")
@Controller
public class SysUserController {
	@Autowired
    private SysUserService sysUserService;
	@RequestMapping("listUI")
	public String listUI(){
		return "sys/user_list";
	}
	@RequestMapping("editUI")
	public String editUI(){
		return "sys/user_edit";
	}
	@RequestMapping("doUpdateObject")
	@ResponseBody
	public JsonResult doUpdateObject(SysUser entity,String roleIds){
		sysUserService.updateObject(entity,
				roleIds);
		return new JsonResult();
	}
	
	@RequestMapping("doFindObjectById")
	@ResponseBody
	public JsonResult doFindObjectById(
			Integer id){
		Map<String,Object> map=
		sysUserService.findObjectById(id);
		return new JsonResult(map);
	}
	
	
	@RequestMapping("doSaveObject")
	@ResponseBody
	public JsonResult doSaveObject(
			SysUser entity,String roleIds){
		sysUserService.saveObject(entity,
				roleIds);
		JsonResult r=new JsonResult();
		r.setMessage("save ok");
		return r;
	}
	
	@RequestMapping("doFindRoles")
	@ResponseBody
	public JsonResult doFindRoles(){
		return new JsonResult(
				sysUserService.findRoles());
	}
	
	@RequestMapping("doValidById")
	@ResponseBody
	public JsonResult doValidById(
		Integer id,Integer valid){
		sysUserService.validById(id, valid);
		return new JsonResult();
	}
	@RequestMapping("doFindPageObjects")
	@ResponseBody
	public JsonResult doFindPageObjects(
			Integer pageCurrent,String username){
		PageObject<SysUser> pageObject=
		sysUserService.findPageObjects(pageCurrent, username);
		return new JsonResult(pageObject);
	}
	
	@RequestMapping("/export")
	@ResponseBody
	public JsonResult doExportUser(HttpServletResponse response) throws Exception{
		response.setContentType("application/binary;charset=UTF-8");
		String fileName="userInfo"+new String(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()).getBytes(), "utf-8");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
		ServletOutputStream out = response.getOutputStream();
		Workbook workbook = sysUserService.findObjects(out);
	    return new JsonResult("export success");
	}
	
}
