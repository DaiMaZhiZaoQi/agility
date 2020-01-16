package com.hunt.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
	
	
	/*private String principal;
	private String longtitude;
	private List<Object> ibeaconsignars;
	
	public static class Object{
		private String uuid;
		private String signar;
	}
	*/
	
	public static Logger lgLogger=LoggerFactory.getLogger(Log.class);
	public static<T> Logger l(Class<T> clazz) {
		lgLogger.debug("rizhi");
		return LoggerFactory.getLogger(clazz);
	}
}
