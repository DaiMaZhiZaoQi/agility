package com.hunt.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
	public static Logger lgLogger=LoggerFactory.getLogger(Log.class);
	public static<T> Logger l(Class<T> clazz) {
		lgLogger.debug("rizhi");
		return LoggerFactory.getLogger(clazz);
	}
}
