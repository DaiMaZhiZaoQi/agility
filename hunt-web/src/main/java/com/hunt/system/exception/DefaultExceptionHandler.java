package com.hunt.system.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.hunt.util.Result;

public class DefaultExceptionHandler implements HandlerExceptionResolver{

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		boolean contains = request.getHeader("Accept").contains("application/json");
		ModelAndView modelAndView = new ModelAndView();
		if(contains) {			//  移动端请求
			String message = ex.getMessage();
			Throwable cause = ex.getCause();
			 response.setStatus(200); //设置状态码  
	            response.setContentType("text/json;charset=utf-8"); //设置ContentType  
	            response.setCharacterEncoding("UTF-8"); //避免乱码  
	            response.setHeader("Cache-Control", "no-cache, must-revalidate");  
	            try {  
	                response.getWriter().write("{\"success\":false,\"msg\":\"" + ex.getMessage() + "\"}");  
	            } catch (Exception e) {  
	            	
	            }  
		}
		System.out.println("--DefaultExceptionHandler");
		modelAndView.addObject(new Result(201,"参数错误"));
		return modelAndView;
	}

}
