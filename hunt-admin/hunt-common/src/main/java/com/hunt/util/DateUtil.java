package com.hunt.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.ws.soap.AddressingFeature;

/**
 * 时间格式化
 * @author williambai
 *
 */
public class DateUtil {

	/**
	 * 获得当前时间
	 * @return yyyyMMddHHmmSSss
	 */
	public static String getCurrTime() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSS");
		return sdf.format(new Date(System.currentTimeMillis()));
	}
	/**
	 * 格式化年月
	 * @return
	 */
	public static String getYearAndMonth() {
	 SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
	 return sdf.format(new Date(System.currentTimeMillis()));
	}
	
	public static String getToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return sdf.format(new Date(System.currentTimeMillis()));
	}
	
	/**
	 * 格式化今天时间
	 * @return
	 */
	public static String getTodayTime(long timeMinnute) {
		SimpleDateFormat sdfHHmm = new SimpleDateFormat("HHmmssSS");
	 return sdfHHmm.format(new Date(timeMinnute));
	}
	/**
	 * 获得唯一时间
	 * @param timeMinnute
	 * @return
	 */
	public static String getUniqueTime(long timeMinnute) {
		SimpleDateFormat sdfHHmm = new SimpleDateFormat("yyyyMMddHHmmssSS");
		return sdfHHmm.format(new Date(timeMinnute));
	}
	
	
	
	public static String createFilePath(long timeMinnute) {
		Date date=new Date(timeMinnute);
		SimpleDateFormat sdfYearMonth=new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdfToday = new SimpleDateFormat("dd");
		
		
		return File.separator+sdfYearMonth.format(date)+File.separator+sdfToday.format(date);
	}
	
}
