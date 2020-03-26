package com.testpro.test;

import java.util.Scanner;

import com.testpro.test.TestMain.TestClass;

/**
 * 
 * @author williambai
 *
 */
public class TestMain {
	
	public static void main(String[] args) {
		System.out.println("请输入第一个数");
		Scanner scanner = new Scanner(System.in);
		int one = scanner.nextInt();
		System.out.println("请再输入第一个数");
		int two = scanner.nextInt();
		TestClass testClass = new TestClass();
		int caluc = testClass.caluc(one, two); 
		System.out.println("测试混淆"+caluc);
	}

	public static class TestClass{
		public  int caluc(int numA,int numB) {
			int result=numA+numB;
			return result;
		}
	}
	
	
}
