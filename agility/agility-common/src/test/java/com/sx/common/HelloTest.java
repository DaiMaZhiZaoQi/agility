package com.sx.common;

import org.junit.Test;

import junit.framework.Assert;

public class HelloTest {

	
	@Test
	public void testMaven(){
		HelloMaven helloMaven = new HelloMaven();
		String testMaven = helloMaven.testMaven("testMaven"); 
		Assert.assertEquals("test", testMaven);
	}
	
	@Test
	public void testMaven2() {
		HelloMaven helloMaven = new HelloMaven();
		String testMaven = helloMaven.testMaven("testMaven"); 
		Assert.assertEquals("test2", testMaven);
	}
	
}
