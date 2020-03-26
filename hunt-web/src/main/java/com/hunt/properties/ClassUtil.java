package com.hunt.properties;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.google.gson.Gson;
import com.hunt.controller.JsonUtils;
import com.hunt.dynamicclass.ChineseToSpell;


public class ClassUtil {
	 private String filePath = "/config/"; //配置文件路径

	    public String getFilePath() {
	        return filePath;
	    }

	    public void setFilePath(String filePath) {
	        this.filePath = filePath;
	    }

	    public Object dynamicClass(Object object) throws Exception {
	        HashMap returnMap = new HashMap();
	        HashMap typeMap = new HashMap();
	        //读取配置文件
	        Properties prop = new Properties();
	        String sourcepackage = object.getClass().getName();
	        String classname = sourcepackage.substring(sourcepackage.lastIndexOf(".") + 1);
//	        InputStream in = ClassUtil.class.getResourceAsStream(filePath + classname + ".properties");
//	        prop.load(in);
	        prop.load(new InputStreamReader(ClassUtil.class.getResourceAsStream(filePath + classname + ".properties"), "UTF-8"));

	        Set<String> keylist = prop.stringPropertyNames();

	        Class type = object.getClass();
	        BeanInfo beanInfo = Introspector.getBeanInfo(type);
	        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
	        for(int i = 0; i < propertyDescriptors.length; i++) {
	            PropertyDescriptor descriptor = propertyDescriptors[i];
	            String propertyName = descriptor.getName();
	            if(!propertyName.equals("class")) {
	                Method readMethod = descriptor.getReadMethod();
	                Object result = readMethod.invoke(object, new Object[0]);
	                if(result != null) {
	                    returnMap.put(propertyName, result);
	                } else {
	                    returnMap.put(propertyName, "");
	                }
	                typeMap.put(propertyName, descriptor.getPropertyType());
	            }
	        }
	        //加载配置文件中的属性
	        Iterator<String> iterator = keylist.iterator();
	        while(iterator.hasNext()) {
	            String key = iterator.next();
	            returnMap.put(key, prop.getProperty(key));
	            typeMap.put(key, Class.forName("java.lang.String"));
	        }
	        //map转换成实体对象
	        DynamicBean bean = new DynamicBean(typeMap);
	        //赋值
	        Set keys = typeMap.keySet();
	        for(Iterator it = keys.iterator(); it.hasNext(); ) {
	            String key = (String) it.next();
	            bean.setValue(key, returnMap.get(key));
	        }
	        Object obj = bean.getObject();
	        return obj;
	    }

	 
	    
	    public static void main(String[] args) throws Exception {
//	    	String st="姓名,电话号码,住址,籍贯,其他";
//	    	try {
//	    		String jsonStr = getJsonStr(st);
//	    		System.out.println("json-->"+jsonStr);
//				
//			} catch (Exception e) {
//				System.out.println("表格列格式错误，请检查");
//			}
	        Object dynamicClass = new ClassUtil().dynamicClass(new LeapRole()/*LeapRole是个普通类，未贴源码*/);
	        String jSon = JsonUtils.toJSon(dynamicClass);
	        System.out.println("动态生成--->"+jSon);
	    	
	    }
}
