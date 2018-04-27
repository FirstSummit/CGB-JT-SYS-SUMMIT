package com.jt.sys.controller;
import java.net.URLEncoder;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.github.pagehelper.PageInfo;
import com.jt.common.vo.JsonResult;
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
	public JsonResult doUpdateObject(SysUser entity,String roleIds,Integer departmentId,Integer oldDepartmentId){
		sysUserService.updateObject(entity,
				roleIds,departmentId,oldDepartmentId);
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
			SysUser entity,String roleIds,Integer departmentId){
		sysUserService.saveObject(entity,
				roleIds,departmentId);
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
		PageInfo<SysUser> info=
		sysUserService.findPageObjects(pageCurrent, username);
		return new JsonResult(info);
	}
	
	@RequestMapping("doDownload")
	@ResponseBody
	public JsonResult doDownload(){
		return new JsonResult("downloading");
	}
	
	@RequestMapping("/export")
	@ResponseBody
	public JsonResult doExportUser(HttpServletResponse response,String fileName) throws Exception{
		System.out.println("filename"+fileName);
		fileName = URLEncoder.encode(fileName, "iso-8859-1");
		if(fileName==""){
			fileName = new String(
					new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(
							new Date()).getBytes(), "iso-8859-1");
		}
		response.setContentType("application/binary;charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename=" + 
				fileName + ".xlsx");
		ServletOutputStream out = response.getOutputStream();
		Workbook workbook = sysUserService.findObjects(out);
	    return new JsonResult("export success");
	}
	
	@RequestMapping("/exportPDF")
	@ResponseBody
	public ModelAndView doExportPdfUser(HttpServletResponse response,String fileName)throws Exception{
		System.out.println(fileName);
		fileName = URLEncoder.encode(fileName, "iso-8859-1");
		if(fileName==""){
			fileName = new String(
					new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(
							new Date()).getBytes(), "iso-8859-1");
		}
		response.setHeader("Content-disposition", "attachment; filename=" + 
						fileName + ".pdf");
		View view = sysUserService.findObjectsPdf();
		ModelAndView mv = new ModelAndView(view);
		return mv;
	}
}
