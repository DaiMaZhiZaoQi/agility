package com.hunt.initListen;

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
//import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hunt.dao.SysDataGroupMapper;
import com.hunt.dao.SysDataItemMapper;
import com.hunt.dao.SysOrganizationMapper;
import com.hunt.dao.SysPermissionGroupTestMapper;
import com.hunt.dao.SysRoleOrganizationMapper;
import com.hunt.dao.SysUserMapper;
import com.hunt.model.entity.SysDataGroup;
import com.hunt.model.entity.SysDataItem;
import com.hunt.model.entity.SysOrganization;
import com.hunt.model.entity.SysPermissionGroupTest;
import com.hunt.model.entity.SysRoleOrganization;
import com.hunt.model.entity.SysUser;
import com.hunt.model.entity.SysUserRoleOrganization;
import com.hunt.util.Result;

/**
 * 初始化
 * @author williambai
 *
 */
@Service
@Transactional
public class InitSuper {
	@Autowired
	private SysDataGroupMapper mSysDataGroupMapper;
	@Autowired
	private SysDataItemMapper mSysDataItemMapper;
	@Autowired
	private SysOrganizationMapper mSysOrganizationMapper;
	@Autowired
	private SysRoleOrganizationMapper mSysRogMapper;
	@Autowired
	private SysUserMapper mSysUserMapper;
	@Autowired
	private SysPermissionGroupTestMapper mPgT;
	
	/**
	 * 1，sys_data_group-->sys_data_item
	 * @return
	 */
	private Integer initSdgSdi() {
//		List<SysDataGroup> list=new ArrayList<SysDataGroup>();
		SysDataGroup sysDataGroup0=new SysDataGroup();
		sysDataGroup0.setDescription("极限验证");
		sysDataGroup0.setParentId(0l);
		sysDataGroup0.setName("极限验证");
		sysDataGroup0.setIsFinal(2);
		 mSysDataGroupMapper.insert(sysDataGroup0);
		 SysDataItem sysDataItem0 = new SysDataItem();
		 sysDataItem0.setSysDataGroupId(sysDataGroup0.getId()); 
		 sysDataItem0.setKeyValue("796c2461adf8051c835e4a758a6091f6");
		 sysDataItem0.setKeyName("geetest_id");
		 sysDataItem0.setIsFinal(2);
		 sysDataItem0.setDescription("geetest_id");
		 mSysDataItemMapper.insert(sysDataItem0);
		 
//		list.add(sysDataGroup0);
		
		SysDataItem sysDataItem1 = new SysDataItem();
		sysDataItem1.setSysDataGroupId(sysDataGroup0.getId());
		sysDataItem1.setKeyValue("0edad631bed761ab039d8391dd3103ff");
		sysDataItem1.setKeyName("geetest_key");
		sysDataItem1.setIsFinal(2);
		sysDataItem1.setDescription("geetest_key");
		mSysDataItemMapper.insert(sysDataItem1);
		
		SysDataGroup sysDataGroup1 = new SysDataGroup();
		sysDataGroup1.setDescription("日志输出控制");
		sysDataGroup1.setParentId(0l);
		sysDataGroup1.setName("日志");
		sysDataGroup1.setIsFinal(2);
		mSysDataGroupMapper.insert(sysDataGroup1);
		SysDataItem sysDataItem2 = new SysDataItem();
		sysDataItem2.setSysDataGroupId(sysDataGroup1.getId());
		sysDataItem2.setKeyValue("true");
		sysDataItem2.setKeyName("error_detail");
		sysDataItem2.setIsFinal(2);
		sysDataItem2.setDescription("是否输出错误日志详情");
		mSysDataItemMapper.insert(sysDataItem2);
		
//		list.add(sysDataGroup1);
		SysDataGroup sysDataGroup2=new SysDataGroup();
		sysDataGroup2.setDescription("是否开启IP拦截");
		sysDataGroup2.setParentId(0l);
		sysDataGroup2.setName("IP拦截");
		sysDataGroup2.setIsFinal(2);
		mSysDataGroupMapper.insert(sysDataGroup2);
		SysDataItem sysDataItem3 = new SysDataItem();
		sysDataItem3.setSysDataGroupId(sysDataGroup2.getId());
		sysDataItem3.setKeyValue("true");
		sysDataItem3.setKeyName("ip_forbidden");
		sysDataItem3.setIsFinal(2);
		sysDataItem3.setDescription("是否开启ip拦截");
		mSysDataItemMapper.insert(sysDataItem3);
		
//		list.add(sysDataGroup2);
		
		
		
		SysDataGroup sysDataGroup3=new SysDataGroup();
		sysDataGroup3.setDescription("系统配置信息");
		sysDataGroup3.setParentId(0l);
		sysDataGroup3.setName("系统");
		sysDataGroup3.setIsFinal(2);
		mSysDataGroupMapper.insert(sysDataGroup3);
		SysDataItem sysDataItem4 = new SysDataItem();
		sysDataItem4.setSysDataGroupId(sysDataGroup3.getId());
		sysDataItem4.setKeyValue("/hunt-admin");
		sysDataItem4.setKeyName("basePath");
		sysDataItem4.setIsFinal(2);
		sysDataItem4.setDescription("系统root路径");
		mSysDataItemMapper.insert(sysDataItem4);
//		list.add(sysDataGroup3);
		
		return 1;
	}
	
	/**
	 * 初始化机构角色表
	 * @return sys_role_organization_id
	 */
	private Long initOrgRoleOrg(Long sysUserId) {
		SysOrganization sysOrg = new SysOrganization();
		sysOrg.setName("系统");
		sysOrg.setDescription("系统");
		sysOrg.setIsFinal(2);
		sysOrg.setFullName("系统");
		sysOrg.setCreateBy(sysUserId);
		sysOrg.setOrgCode("100");
		mSysOrganizationMapper.insert(sysOrg);
		SysRoleOrganization sysRog = new SysRoleOrganization();
		sysRog.setSysOrganizationId(sysOrg.getId());
		sysRog.setSysRoleId(0l);
		sysRog.setParentId(0l);
		sysRog.setName("系统");
		sysRog.setFullName("系统");
		sysRog.setDescription("系统");
		sysRog.setIsFinal(2); 
		sysOrg.setCreateBy(sysUserId); 
		mSysRogMapper.insert(sysRog);
		return sysRog.getId();
	}
	
	/**
	 * 初始化sys_user_role_organization
	 * @param sysUserId
	 * @param sysRoOId
	 * @return
	 */
	private Integer initSysURO(Long sysUserId,Long sysRoOId) {
		SysUserRoleOrganization sysUsRoO = new SysUserRoleOrganization();
		sysUsRoO.setSysUserId(sysUserId);
		sysUsRoO.setSysRoleOrganizationId(sysRoOId);
		sysUsRoO.setCreateBy(sysUserId);
		sysUsRoO.setIsFinal(1); 
		return 1;
	}
	
	/**
	 * 初始化用户
	 * @return
	 */
	public Integer initSysUser() {
		initPermission();
		initSdgSdi();
		SysUser sysUser = new SysUser();
		sysUser.setLoginName("super");
		sysUser.setZhName("系统");
		sysUser.setEnName("super");
		sysUser.setSex(1);
		sysUser.setPassword("e4725ca36ea5239bb5b203fe2dc1c0df");
		sysUser.setPasswordSalt("109e0d4a996341439bb74898892a1ece");
		sysUser.setIsFinal(1);
		mSysUserMapper.insert(sysUser);
		Long sysROId = initOrgRoleOrg(sysUser.getId());
		initSysURO(sysUser.getId(),sysROId);
		return 1;
	}

	
		
		 
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
	
			   if(url.contains("jdbc:mysql://localhost:3306/hunt")) {
	
				    Class.forName(driver).newInstance();
		
//				    conn= (Connection) DriverManager.getConnection(url, username, password);
				    conn= (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306", username, password);
				    PreparedStatement statement = conn.prepareStatement("SELECT * FROM information_schema.SCHEMATA WHERE SCHEMA_NAME='hunttest'");
				    ResultSet query = statement.executeQuery();
				    while(query.next()) {
				    	String hunttest = query.getString(2);
				    	System.out.println("hunttest-->"+hunttest);
				    	if("hunttest".equals(hunttest)) {
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
