package com.hunt.initListen;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
 * 项目初始化建表
 * @author williambai
 *
 */
public class InitListen implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("监听器被加载");
		InitSuper initListen = new InitSuper(); 
		initListen.initPermission();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
