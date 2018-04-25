package com.jt.sys.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.JsonResult;
import com.jt.sys.service.SysUserService;

@Controller
@RequestMapping("/")
public class SysLoginController {
     @Autowired
	 private SysUserService sysUserService;
     @RequestMapping("loginUI")
     public String loginUI(){
    	 return "login";
     }
     @RequestMapping("doLogin")
     @ResponseBody
     public JsonResult doLogin(
    	String method,String username,String password){
        sysUserService.login(method,username, password);
      return new JsonResult("login ok");
     }
     @RequestMapping("doLogin02")
	  @ResponseBody
	  public JsonResult doLogin02(String method,String mobile,String password){
		  sysUserService.login02(method, mobile, password);
		  return new JsonResult("login ok");
	  }
     
}



