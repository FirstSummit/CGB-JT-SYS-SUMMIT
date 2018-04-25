package com.jt.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.JsonResult;
import com.jt.sys.entity.SysDepartment;
import com.jt.sys.service.SysDepartmentService;

@Controller
@RequestMapping("/department/")
public class SysDepartmentController {
	@Autowired
	private SysDepartmentService sysDepartmentService;
	@RequestMapping("listUI")
	public String listUI() {
		return"sys/department_list";
	}
	
	@RequestMapping("editUI")
	public String editUI() {
		return"sys/department_edit";
	}
	
	@RequestMapping("doFindObjects")
	@ResponseBody
	public JsonResult doFindObjects(){
		return new JsonResult(
			sysDepartmentService.findObjects());
	}
	
	@RequestMapping("doFindZTreeNodes")
	@ResponseBody
	public JsonResult doFindZTreeNodes(){
		return new JsonResult(
		sysDepartmentService.findZTreeNodes());
	}
	
	@RequestMapping("doDepartmentIsExists")
	@ResponseBody
	public JsonResult doDepartmentIsExists(String name,Integer sort) {
		return new JsonResult(sysDepartmentService.findDepartmentByName(name,sort));
	}
	@RequestMapping("doManagerIsExists")
	@ResponseBody
	public JsonResult doManagerIsExists(String username) {
		return new JsonResult(sysDepartmentService.findManegerByUserName(username));
	}
	
	@RequestMapping("doSaveDepartment")
	@ResponseBody
	public JsonResult doSaveDepartment(SysDepartment entity) {
		if(entity==null) {
			return null;
		}
		sysDepartmentService.saveObject(entity);
		return new JsonResult("save ok");
	}
	
	
	@RequestMapping("doDeleteDepartmentById")
	@ResponseBody
	public JsonResult doDeleteDepartmentById(Integer id) {
		sysDepartmentService.deleteDepartmentById(id);
		return new JsonResult("delete ok");
	}
	
	@RequestMapping("doFindDepartmentById")
	@ResponseBody
	public JsonResult doFindDepartmentById(Integer id) {
		return new JsonResult(sysDepartmentService.findDepartmentById(id));
	}
	
	@RequestMapping("doUpdateDepartment")
	@ResponseBody
	public JsonResult doUpdateDepartment(SysDepartment entity) {
		sysDepartmentService.updateDepartments(entity);
		return new JsonResult("update ok");
	}
	
	@RequestMapping("doFindDepartmentNameByUserId")
	@ResponseBody
	public JsonResult doFindDepartmentNameByUserId(Integer id) {
		//System.out.println(sysDepartmentService.findDepartmentNameByUserId(id));
		return new JsonResult( sysDepartmentService.findDepartmentNameByUserId(id));
	}
}
