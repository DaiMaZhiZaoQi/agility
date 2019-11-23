package com.zicoo.init;

public class Init {
	
	public static void main(String[] args) {
		initData();
	}

	public static void initData() {
		System.out.println("初始化数据库");
		InitSuper initSuper = new InitSuper();
		initSuper.initPermission();
	}
	
}
