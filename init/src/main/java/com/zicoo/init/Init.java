package com.zicoo.init;

import cn.zicoo.tele.TeleManager;

/**
 * @author 铱方科技 
 * @version 1.2
 * 
 */

public class Init {
	
	public static void main(String[] args) {
		initData();
	}

	/**
	 * <p>init初始化</p>
	 * @see #startTest(int,String)
	 * 
	 */
	public static void initData() {
		System.out.println("初始化数据库");
		InitSuper initSuper = new InitSuper();
		initSuper.initPermission();
		TeleManager.getInstance();
	}
	
	/**
	 * 测试方法{@link String} 类型 {@code String}
	 * 
	 * <p>测试方法</p>
	 * @param args1 参数一 1:状态1,2:状态2 
	 * @param args2 参数二
	 * 
	 * @return 
	 * 返回信息
	 */
	public static String startTest(int args1,String args2) {
		return args2+args1;
	}
	
}
