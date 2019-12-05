package sjzc.hf.store.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import sjzc.hf.store.error.BSException;
import sjzc.hf.store.response.R;
@RestControllerAdvice//(添加这个标签时，可以不使用继承，也可对异常进行处理)
public class BaseController{
	public static Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	public static final String CONTENT_TYPE="application/x-www-form-urlencoded";

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
	public Object handlerException(Exception ex,HttpServletRequest request) {
		
		HashMap<String, Object> errorData=new HashMap<String,Object>();
		// 如果抛出的是系统自定义异常则直接转换
		if (ex instanceof BSException) {
			BSException businessException = (BSException) ex;
			errorData.put("errorCode", businessException.getErrorCode());
			errorData.put("errorMsg", businessException.getErrorMsg());
			
		} else {
			// 如果抛出的不是系统自定义异常则重新构造一个未知错误异常。
			errorData.put("errorCode", 500);
			errorData.put("errorMsg", "系统异常");
			
		}
		
		logger.error(errorData.get("errorCode")+":"+ex.getMessage());

		return R.error((int)errorData.get("errorCode"), (String)errorData.get("errorMsg"));

	
	}
	
		
		

}
