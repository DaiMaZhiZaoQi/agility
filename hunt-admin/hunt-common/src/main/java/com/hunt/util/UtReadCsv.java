package com.hunt.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

import com.hunt.util.UtReadCsv.TTSecond;
import com.hunt.util.UtReadCsv.TTestChild;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
//import com.sun.glass.ui.Timer;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

public class UtReadCsv/*<T extends TTest>*/{
	
	static class TTestChild extends TTest{

		@Override
		public String getName() {
			return super.getName();
		}
		
	}
	
	static	class TTSecond extends TTest{

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return super.getName();
		}
		
	}
	
	@Test
	public void testStr() {
		String machex="^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";
//		String string="ʤ�����͹���";
		String string="safd432s找那个243�";
		boolean matches = string.matches(machex);
		System.out.println("matches-->"+matches);
	}

    /**
     * 方法三：比较准确，解决了实际问题
     * @param filePath
     * @return
     */
    public static String getFileEncode(String filePath) {
        String charsetName = null;
        try {
            File file = new File(filePath);
            CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
            detector.add(new ParsingDetector(false));
            detector.add(ASCIIDetector.getInstance());
            detector.add(JChardetFacade.getInstance());
            detector.add(UnicodeDetector.getInstance());
            java.nio.charset.Charset charset = null;
            charset = detector.detectCodepage(file.toURI().toURL());
            if (charset != null) {
                charsetName = charset.name();
            } else {
                charsetName = "UTF-8";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return charsetName;
    }

    public static String getFileEncode2(String fr) {
        String encode = "utf-8";
        try {
            InputStream is = new FileInputStream(new File(fr));
            byte[] buff = new byte[2048];
            int retn = is.read(buff, 0, 2048);
            if (retn>0) {
                encode = getEncode(buff, retn);
            }
            is.close();
        }
        catch (IOException e) {}
        return encode;
    }
    
    public static String getEncode(byte[] buff, int length) {
        try {
            int len = (length==0 || length>buff.length) ? buff.length : length;
            String gbk = new String(buff, 0, len, "gbk");
            String utf = new String(buff, 0, len, "utf-8");
            if (gbk.length()<utf.length()) return "gbk";
        }
        catch (IOException e) {}
        return "utf-8";
    }

	
	
	public static void main(String[] args) {
		File file=new File("C:\\Users\\williambai\\Desktop\\candelete\\qytxlmbtest.csv");
		String[] strArr = isCorrectFileVersion(file.getAbsolutePath());
//		StringUtil.getSRange(sTimeType)
		boolean strCorrect = StringUtil.isStrCorrect(strArr[0]); 
		System.out.println("--->"+(strCorrect==true?strArr[0]:"不符规则")+"--->"+strArr[1]);
		
		
//		String name = file.getName();
//		System.out.println("fileName-->"+name); 
//		UtReadCsv<TTest> utReadCsv = new UtReadCsv<>();
//		TTestChild tTestChild = new TTestChild(); 
//		tTestChild.setName("张三");
//		utReadCsv.show(tTestChild);
//		TTSecond ttSecond = new TTSecond(); 
//		ttSecond.setName("李四");
//		utReadCsv.show(ttSecond);
//		pareType(file);
		
	}
	
	
	
	public static void pareType(Object object) {
		Class<? extends Object> class1 = object.getClass();
		String name = class1.getName();
		System.out.println("name-->"+name);
	}
	
/*	public void show(T t) {
		String name = t.getName();
		System.out.println("名称--》"+name);
		
	}*/

	/**
	 * 获得当前时间与这个月的第一天相隔多少天
	 */
	@Test
	public void testTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date time = calendar.getTime();
	 
		long timeInMillis = calendar.getTimeInMillis(); 
		System.out.println("testTime-->"+timeInMillis);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = sdf.format(timeInMillis);
		System.out.println("format-->"+format);
	}

	/**
	 * 判断文件版本是否正确
	 * @param filePath
	 * @return csv文件版本
	 */
	public static String[] isCorrectFileVersion(String filePath) {
		
//		File file=new File("C:\\Users\\williambai\\Desktop\\candelete\\qytxlmbtest.csv");
		File file=new File(filePath);
		String[] strArr=new String[2];
		if(!file.exists()) {
			return strArr;
		}
		BufferedReader reader=null;
		try {
			reader=new BufferedReader(new FileReader(file));
		
			String str="";
			int i=0;
			while((str=reader.readLine())!=null) {	//  读到第三行 判断是否为 YYYYMMDD类型
				i++;
				if(i==2) {							// 机构名称
					String[] split = str.split(",");
					if(split.length>=1) {
						strArr[0]=split[0];
					}
				}
				if(i==3) {							// 文件版本
					 String[] split = str.split(",");
					 if(split.length>=1) {
						 if(isCorrect(split[0])) {
							 strArr[1]=split[0];
							 return strArr;
//							 return split[0];
						 }
						 
					 }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return strArr;
	}
	
	/**
	 * 判断时间是否符合 yyyyMMdd
	 * @param time
	 * @return
	 */
	public static boolean isCorrect(String time) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			Date parse = sdf.parse(time);
			String fTime = sdf.format(parse); 
			return fTime.equals(time);
		} catch (ParseException e) {
			return false;
		}
	}
	
	
	@Test
	public void testDate() {
		String time="20190932";
		boolean correct = isCorrect(time);
		System.out.println("时间格式-->"+correct);
	}
	
}
