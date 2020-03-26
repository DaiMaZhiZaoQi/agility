package com.hunt.system.security.shiro;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.StringUtils;

import com.hunt.util.AesEncryptUtils;

public class ParamFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String parameter = request.getParameter("requestData");
		System.out.println("paramter--->"+parameter);
		if(!StringUtils.isEmpty(parameter)) {
			try {
				String decrypt = AesEncryptUtils.decrypt(parameter);
//				Map<String, String[]> parameterMap = request.getParameterMap();
//				new HttpServletRequestWrapper(request);
				System.out.println("decypt"+decrypt); 
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	/*
	public static class ParameterRequestWrapper extends HttpServletRequestWrapper{

		public ParameterRequestWrapper(HttpServletRequest request) {
			super(request);
			// TODO Auto-generated constructor stub
		}
		
		*//**
		* 处理初始化参数
		* @param request
		* @param extendParams
		*//*
		public Map<String , Object> handleRequestMap(HttpServletRequest request){
			Enumeration enu=request.getParameterNames();
			Map<String , Object> map = new HashMap<>();
			while(enu.hasMoreElements()){
				String paraName=(String)enu.nextElement();
				System.out.println("未解密param "+request.getParameter(paraName));
				String parameter = AESUtils.decrypt(request.getParameter(paraName));
				System.out.println("已解密parameter "+parameter);
				map.put(paraName, parameter);
			}
			return map;
		}
		
	} */

}
