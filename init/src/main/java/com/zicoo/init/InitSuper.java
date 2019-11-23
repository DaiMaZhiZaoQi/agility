package com.zicoo.init;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.javassist.expr.NewArray;
import org.apache.ibatis.jdbc.ScriptRunner;


/**
 * 初始化
 * @author williambai
 *
 */

public class InitSuper {
	
	
	
		
		 
// /* CREATE DATABASE IF NOT EXISTS `huntTest` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; */

//		 public static void main(String[] args) {
//		@Test
		public void initPermission() {
			InputStreamReader reader=null;
			InputStreamReader sqlReader=null;
			 Connection conn=null;
			  try {
//			   Properties props = Resources.getResourceAsProperties("properties/database.properties");
//				  File file=new File("src/main/resources/env.properties");
//				  if(!file.exists()) {
//					  System.out.println("文件存在");
//					  return ;
//				  }
				  InputStream in = this.getClass().getResourceAsStream("/env.properties");
				  if(in==null) {
					  System.out.println("文件不存在");
					  return;
				  }
				  
				  Properties props = new Properties();
				  reader= new InputStreamReader(in, "UTF-8");
				  props.load(reader);
				  
//			   Properties props = Resources.getResourceAsProperties("src/main/resources/env.properties");
			   
			   String url = props.getProperty("jdbc.url");
	
			   String driver = props.getProperty("jdbc.driverClass");
	
			   String username = props.getProperty("jdbc.username");
	
			   String password = props.getProperty("jdbc.password");
			   System.out.println(url);
	
			   if(url.contains("jdbc:mysql://localhost:3306/zicoo_record")) {
	
				    Class.forName(driver).newInstance();
		
//				    conn= (Connection) DriverManager.getConnection(url, username, password);
				    conn= (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306", username, password);
				    PreparedStatement statement = conn.prepareStatement("SELECT * FROM information_schema.SCHEMATA WHERE SCHEMA_NAME='zicoo_record'");
				    ResultSet query = statement.executeQuery();
				    while(query.next()) {
				    	String hunttest = query.getString(2);
				    	System.out.println("zicoo_record-->"+hunttest);
				    	if("zicoo_record".equals(hunttest)) {
				    		System.out.println("数据库存在");
				    		return;
				    	}
				    	
				    }
//				    DatabaseMetaData dbMetaData = conn.getMetaData();  
//			        String[]   types   =   { "TABLE" };  
//			        ResultSet tabs = dbMetaData.getTables(null, null, "sys_user", types);  
//			        if (tabs.next()) {  
//			        	System.out.println("数据库存在");
//			            return ;  
//			        } 
//				    int i=1/0;
				    ScriptRunner runner = new ScriptRunner(conn);
				    PrintWriter printWriter = new PrintWriter(System.out);
				    
				    runner.setErrorLogWriter(printWriter);
		
				    runner.setLogWriter(printWriter); 
				    System.out.println("userName"+username);
//				    runner.runScript(Resources.getResourceAsReader("ddl/mysql/jpetstore-mysql-schema.sql"));
				    InputStream sqlIn = this.getClass().getResourceAsStream("/initsql.sql");
				    if(sqlIn==null) {
				    	System.out.println("sql文件不存在");
				    	return ;
				    }
				    sqlReader = new InputStreamReader(sqlIn, "UTF-8");
//				    runner.runScript(Resources.getResourceAsReader("/initsql.sql"));
				    runner.runScript(sqlReader);
//		
//				    runner.runScript(Resources.getResourceAsReader("ddl/mysql/jpetstore-mysql-dataload.sql"));
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
				if(sqlReader!=null) {
					try {
						sqlReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(conn!=null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			  }

		 }

		 
	

}
