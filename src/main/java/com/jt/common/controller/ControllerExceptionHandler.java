package com.jt.common.controller;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jt.common.exception.ServiceException;
import com.jt.common.vo.JsonResult;
/**Spring MVC 鍩轰簬娉ㄨВ鏂瑰紡瀹炵幇缁熶竴寮傚父澶勭悊鏂瑰紡
 * 瑙勫垯
 * 1)controller鍐呴儴-->鐖剁被-->@ControllerAdvice
 * */
@ControllerAdvice
public class ControllerExceptionHandler {
	
	  @ExceptionHandler(ServiceException.class)
	  @ResponseBody
	  public JsonResult handleServiceException(
			  RuntimeException e){
		  System.out.println("handleServiceException");
		  e.printStackTrace();
		  //灏佽閿欒淇℃伅
		  return new JsonResult(e);
	  }
	  @ExceptionHandler(AuthorizationException.class)
	  @ResponseBody
	  public JsonResult handleAuthorizationeException(
			  AuthorizationException e){
		  System.out.println("handleAuthorizationeException");
		  e.printStackTrace();
		  //灏佽閿欒淇℃伅
		  return new JsonResult(new ServiceException("娌℃湁姝ゆ潈闄�"));
	  }
	  //....... 
}
