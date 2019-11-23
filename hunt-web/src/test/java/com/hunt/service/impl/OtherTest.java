package com.hunt.service.impl;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hunt.model.entity.SysDevice;
import com.hunt.service.SystemDeviceService;
import com.hunt.util.AmrToMP3Utils;


public class OtherTest {
//	private static Logger log = LoggerFactory.getLogger(SysOrganizationServiceImplTest.class);
	private static Logger log=LoggerFactory.getLogger(OtherTest.class);
	 
	@Test
	public void test1() {
		String string="01";
		int parseInt = Integer.parseInt(string);
		parseInt++;
		String code="";
		if(parseInt<9) {
			code="0"+parseInt;
		}else {
			code=parseInt+"";
		}
		System.out.println("--->"+""+code);
	}
	
	
	@Test
	public void testBeanUtils(){
		Bean1 b1=new Bean1("abc",13);
		Bean2 b2=new Bean2();
		BeanUtils.copyProperties(b1,b2);
		System.out.println(b2.getAge());
		
	}
	
	class Bean1{
		private String fruitName;
		private int age;
		public Bean1(String fruitName, int age) {
			super();
			this.fruitName = fruitName;
			this.age = age;
		}
		public String getFruitName() {
			return fruitName;
		}
		public void setFruitName(String fruitName) {
			this.fruitName = fruitName;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		
	}
	
	class Bean2{
		private String fruitName;
		private int age;
		private String favorite;
		public String getFruitName() {
			return fruitName;
		}
		public void setFruitName(String fruitName) {
			this.fruitName = fruitName;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public String getFavorite() {
			return favorite;
		}
		public void setFavorite(String favorite) {
			this.favorite = favorite;
		}
		
	}

	@Test
	public void testSysDevice() {
		SysDevice sysDevice = new SysDevice();
		sysDevice.setDeviceName("张三"); 
		sysDevice.setDeviceTime(Calendar.getInstance().getTime());
		log.info(sysDevice.toEntityString()); 
		
	}
	
	
  	@Test
   	public void testAmrToMp3() {
   		String sourcePath="G:\\DevelopSoft\\ffmpeg\\resource\\1565064788446.amr";
   		try {
			FileInputStream inputStream = new FileInputStream(new File(sourcePath));
			AmrToMP3Utils.amrToMP3(inputStream, "1565064788446.mp3","");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
   	}
   	
	
  	/**
   	 * 测试输入内容
   	 */
   	@Test
   	public void TestContent() {
   		String str="5425435645364536453645366";
   		String strRegex="\\d+$";
   		Pattern compile = Pattern.compile(strRegex);
   		Matcher matcher = compile.matcher(str);
   		boolean matches = matcher.matches(); 
   		System.out.println("匹配结果-->"+matches);
   	}
   	
   	
   	
   	
   	
   	
	@Test
   	public void testDate() {
		String date="2019/09/18";
   		String regex="\\d{4}/\\d{2}/\\d{2}";
   		boolean matches = date.matches(regex);
   		System.out.println("匹配-->"+matches);
		
   	}
	
	@Test
	public void testTime() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String format = sdf.format(new Date(1514736000000l));
		String format = sdf.format(new Date(1546271000000l));
		System.out.println("--->"+format);
	}
	
	
}
