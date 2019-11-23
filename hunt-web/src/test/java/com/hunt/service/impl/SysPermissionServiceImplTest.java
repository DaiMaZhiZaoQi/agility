package com.hunt.service.impl;

import com.alibaba.druid.support.logging.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hunt.controller.JsonUtils;
import com.hunt.dao.SysContactUserMapper;
import com.hunt.dao.SysDeviceCallLogMapper;
import com.hunt.dao.SysDeviceMapper;
import com.hunt.dao.SysDeviceRoleOrgMapper;
import com.hunt.dao.SysDeviceTotalMapper;
import com.hunt.dao.SysLoginStatusMapper;
import com.hunt.dao.SysOrganizationMapper;
import com.hunt.dao.SysPermissionGroupMapper;
import com.hunt.dao.SysPermissionGroupTestMapper;
import com.hunt.dao.SysUserMapper;
import com.hunt.model.dto.PageDto;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysCallLogDeviceRecoDto;
import com.hunt.model.dto.SysDeviceAndCallDto;
import com.hunt.model.dto.SysDeviceOrgDto;
import com.hunt.model.dto.SysUserOrgDto;
import com.hunt.model.entity.SysContactUser;
import com.hunt.model.entity.SysDevice;
import com.hunt.model.entity.SysDeviceRoleOrg;
import com.hunt.model.entity.SysDeviceTotal;
import com.hunt.model.entity.SysOrganization;
import com.hunt.model.entity.SysPermission;
import com.hunt.model.entity.SysPermissionGroup;
import com.hunt.model.entity.SysPermissionGroupTest;
import com.hunt.model.entity.SysUser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.hunt.service.SysOrganizationService;
import com.hunt.service.SysPermissionService;
import com.hunt.service.SysUserService;
import com.hunt.service.SystemDeviceService;
import com.hunt.util.AmrToMP3Utils;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import junit.extensions.TestDecorator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

/**
 * @Author: ouyangan
 * @Date : 2016/10/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring.xml"})
@Transactional
@WebAppConfiguration
public class SysPermissionServiceImplTest {
	private static Logger log = LoggerFactory.getLogger(SysOrganizationServiceImplTest.class);
    @Autowired
    private SysPermissionService service;
    @Autowired
    private SysUserService sysUserServiceImpl;
    @Autowired
    private SysDeviceRoleOrgMapper mSysDeviceRoleOrgMapper;
    
    @Autowired
    private SysLoginStatusMapper mSysLoginStatusMapper;
    
    @Autowired
    private SysOrganizationService mSysOrganizationService;
    
	@Autowired
	private SystemDeviceService mSysDeviceService;
	
	@Autowired
	private SysDeviceTotalMapper mSysDeviceTotalMapper;
	
	@Autowired
	private SysDeviceMapper mSysDeviceMapper;
	@Autowired
	SysContactUserMapper mSysContactUserMapper;
	@Autowired
	SysOrganizationMapper mSysOrganizationMapper;
	
	@Autowired
	SysUserMapper mSysUserMapper;
	
	@Autowired
	SysPermissionGroupMapper mSysPermissionGroup;
	@Autowired
	private SysPermissionGroupTestMapper mPgT;
    @Test
    public void isExistName() throws Exception {
        SysPermissionGroup pg = new SysPermissionGroup();
        pg.setName("test pg");
        pg.setDescription("desc");
        service.insertPermissionGroup(pg);
        SysPermission p = new SysPermission();
        p.setName("test");
        p.setSysPermissionGroupId(pg.getId());
        service.insertPermission(p);
        assertTrue(service.isExistName(pg.getId(), "test"));
        assertTrue(!service.isExistName(pg.getId(), "test1"));
    }

    @Test
    public void isExistCode() throws Exception {
        SysPermissionGroup pg = new SysPermissionGroup();
        pg.setName("test pg");
        pg.setDescription("desc");
        service.insertPermissionGroup(pg);
        SysPermission p = new SysPermission();
        p.setName("test");
        p.setCode("code");
        p.setSysPermissionGroupId(pg.getId());
        service.insertPermission(p);
        assertTrue(service.isExistCode(pg.getId(), "code"));
        assertTrue(!service.isExistCode(pg.getId(), "code1"));
    }

    @Test
    public void insertPermission() throws Exception {
        SysPermissionGroup pg = new SysPermissionGroup();
        pg.setName("test pg");
        pg.setDescription("desc");
        service.insertPermissionGroup(pg);
        SysPermission p = new SysPermission();
        p.setName("test");
        p.setCode("code");
        p.setSysPermissionGroupId(pg.getId());
        service.insertPermission(p);
    }

    @Test
    public void selectById() throws Exception {
        SysPermissionGroup pg = new SysPermissionGroup();
        pg.setName("test pg");
        pg.setDescription("desc");
        service.insertPermissionGroup(pg);
        SysPermission p = new SysPermission();
        p.setName("test");
        p.setCode("code");
        p.setSysPermissionGroupId(pg.getId());
        service.insertPermission(p);
        SysPermission sysPermission = service.selectById(p.getId());
        assertTrue(sysPermission.getName().equals("test"));
    }

    @Test
    public void update() throws Exception {
        SysPermissionGroup pg = new SysPermissionGroup();
        pg.setName("test pg");
        pg.setDescription("desc");
        service.insertPermissionGroup(pg);
        SysPermission p = new SysPermission();
        p.setName("test");
        p.setCode("code");
        p.setSysPermissionGroupId(pg.getId());
        service.insertPermission(p);
        p.setName("test update");
        service.update(p);
        SysPermission sysPermission = service.selectById(p.getId());
        assertTrue(sysPermission.getName().equals("test update"));
    }

    @Test
    public void isExistNameExcludeId() throws Exception {
        SysPermissionGroup pg = new SysPermissionGroup();
        pg.setName("test pg");
        pg.setDescription("desc");
        service.insertPermissionGroup(pg);
        SysPermission p = new SysPermission();
        p.setName("test");
        p.setSysPermissionGroupId(pg.getId());
        service.insertPermission(p);
        assertTrue(service.isExistNameExcludeId(pg.getId(), pg.getId(), "test"));
        assertTrue(!service.isExistNameExcludeId(pg.getId(), pg.getId(), "test1"));

    }

    @Test
    public void isExistCodeExcludeId() throws Exception {
        SysPermissionGroup pg = new SysPermissionGroup();
        pg.setName("test pg");
        pg.setDescription("desc");
        service.insertPermissionGroup(pg);
        SysPermission p = new SysPermission();
        p.setName("test");
        p.setCode("code");
        p.setSysPermissionGroupId(pg.getId());
        service.insertPermission(p);
        assertTrue(service.isExistCodeExcludeId(pg.getId(), pg.getId(), "code"));
        assertTrue(!service.isExistCodeExcludeId(pg.getId(), pg.getId(), "code1"));
    }

    @Test
    public void selectPage() throws Exception {
        SysPermissionGroup pg = new SysPermissionGroup();
        pg.setName("test pg");
        pg.setDescription("desc");
        service.insertPermissionGroup(pg);
        for (int i = 0; i < 30; i++) {
            SysPermission p = new SysPermission();
            p.setName("test");
            p.setCode("code");
            p.setSysPermissionGroupId(pg.getId());
            service.insertPermission(p);
        }
        PageInfo pageInfo = service.selectPage(1, 30);
        assertTrue(pageInfo.getTotal() >= 30);
    }

    @Test
    public void isExistGroupName() throws Exception {
        SysPermissionGroup pg = new SysPermissionGroup();
        pg.setName("test pg");
        pg.setDescription("desc");
        service.insertPermissionGroup(pg);
        assertTrue(service.isExistGroupName("test pg"));
    }

    @Test
    public void insertPermissionGroup() throws Exception {
        SysPermissionGroup pg = new SysPermissionGroup();
        pg.setName("test pg");
        pg.setDescription("desc");
        service.insertPermissionGroup(pg);
        assertTrue(pg.getId() != null);
    }

    @Test
    public void selectGroup() throws Exception {
        SysPermissionGroup pg = new SysPermissionGroup();
        pg.setName("test pg");
        pg.setDescription("desc");
        service.insertPermissionGroup(pg);
        List<SysPermissionGroup> sysPermissionGroups = service.selectGroup();
        assertTrue(sysPermissionGroups.size() >= 1);
    }
    @Test
    @Rollback(false)
    public void testInsertPer() {
    	SysUser sysUser = new SysUser();
    	sysUser.setLoginName("eee");
    	sysUser.setId(10l);
    	
    	sysUserServiceImpl.insertUserPermission(sysUser);
//    	sysUserServiceImpl.insertUserPermissionSingle(2);
    	
    }
    
    
    @Autowired
   	private SystemDeviceService mSystemDeviceService;
   	
   	@Test
    @Rollback(false)
   	public void testInsertDevice() {
   		
//   		SysDevice sysDevice = new SysDevice();
//   		sysDevice.setDeviceName("7寸机");
//   		sysDevice.setDeviceMsg("欢迎使用");
//   		sysDevice.setDescription("系统很好用");
//   		sysDevice.setUpdateTime(new Date(System.currentTimeMillis())); 
//   		log.info(sysDevice.toEntityString());
//   		Long id = mSystemDeviceService.insertDeviceRoleOrg(sysDevice,11l,5l);	//  设备id， 角色机构id，sysUserId
//   		log.info(id+"");
   		
   		
//   		boolean existDevice = mSysDeviceService.isExistDevice("FFF017");
//   		System.out.println("existDevice-->"+existDevice);
   		
   	}
   	
   	@Test
   	@Rollback(false)
   	public void testDevOrg() {
   		Set<Long> set=new HashSet();
   		set.add(6l);
   		PageDto pageDto = new PageDto();
   		pageDto.setOrder("desc");
   		pageDto.setSort("create_time");
   		pageDto.setPage(1); 
   		pageDto.setRows(15);
   		List<SysDeviceOrgDto> selectDevRegis = mSysDeviceMapper.selectDevRegis(set, pageDto);  
//   		List<SysDeviceOrgDto> selectDevRegis = mSysDeviceMapper.selectDevRegis(pageDto);  
//   		Long count = mSysDeviceMapper.selectRegisCount(set);
   		String jSon = JsonUtils.toJSon(selectDevRegis);
   		System.out.println("json-->"+jSon);
   		
//   		System.out.println("json-->"+count);
   	}
   	
   	@Test
   	@Rollback(false)
   	public void test2() {
   		SysContactUser sysContactUser = new SysContactUser();
   		sysContactUser.setSysUserId(13l);
   		sysContactUser.setSysContactId(17l);
   	  SysContactUser selectUnAuth = mSysContactUserMapper.selectUnAuth(sysContactUser);
   	  System.out.println("selectUn-->"+selectUnAuth.toEntityString());
   	}
   	
   	@Test
   	@Rollback(false)
   	public void testCallCount() {
   		Set<Long> set=new HashSet<>();
   		set.add(6l);
   		Long selectCallCount = mSysDeviceTotalMapper.selectCallCount(set);
   		System.out.println("testCallCount-->"+selectCallCount);
   	}
   	
   	@Test
   	@Rollback(false)
   	public void test3() {	//  查询上级机构
   		List<SysOrganization> list=new ArrayList<>();
   		List<SysOrganization> selectUpSysOrgList = mSysOrganizationService.selectUpSysOrgList(6l, list);
   		String jSon = JsonUtils.toJSon(selectUpSysOrgList);
   		System.out.println("test3-->"+jSon);
   	}
   	
   	
   	@Test
   	public void testTimeTotal() {
   		Date date = new Date(System.currentTimeMillis()); 
   		SysDeviceTotal to= mSysDeviceTotalMapper.selectByDevIdByCreateTime(30l,date);
   		System.out.println("testTimeTotal-->"+to.toEntityString());
   	}
   	
   	@Test
   	@Rollback(false)
   	public void testSelectCallLog() {
   		Set<Long> set=new HashSet<>();
   		set.add(6l);
   		// 普通搜索
//   		List<SysCallLogDeviceRecoDto> selectCallLogByOrg = mSysDeviceTotalMapper.selectCallLogByOrg(set,"update_time","desc",1,320);
   		// 搜索序列号
//   		List<SysCallLogDeviceRecoDto> selectCallLogByOrg = mSysDeviceTotalMapper.selectSearDevserCallLogByOrg(set,"update_time","desc",1,320,"5037");
   		// 搜索通话号码
//   		List<SysCallLogDeviceRecoDto> selectCallLogByOrg = mSysDeviceTotalMapper.selectSearCallNumCallLogByOrg(set,"update_time","desc",1,320,"8006");
   		// 分时间段查询
   		List<SysCallLogDeviceRecoDto> selectCallLogByOrg = mSysDeviceTotalMapper.selectSearCallDateCallLogByOrg(set,"update_time","desc",1,320,0l,0l,0);
   		
   		System.out.println("selectCallLogByOrg-->"+selectCallLogByOrg.size());
   		for(SysCallLogDeviceRecoDto dto:selectCallLogByOrg) {
   			String devSerial = dto.getDevSerial();
   			System.out.println("testSelectCallLog--->"+devSerial); 
   		}
   	}
   	
   	@Test
   	@Rollback(false)
   	public void testselectUserByContactId() {
   		List<SysUserOrgDto> list = mSysContactUserMapper.selectUserOrgAuthByContactId(21l);
   		String jSon = JsonUtils.toJSon(list);
   		System.out.println("用户所在的机构"+jSon);
   		
   	}
    
  	
	@Test
	@Rollback(false)
   	public void listCallByUserId() {
		List<SysCallLogDeviceRecoDto> selectCallLogByOrg = mSysDeviceTotalMapper.selectCallLogByUser(13l,"update_time","desc",1,320);	
		System.out.println("listCallByUserId--->"+selectCallLogByOrg.size());
		for(SysCallLogDeviceRecoDto dto:selectCallLogByOrg) {
			String devSerial = dto.getDevSerial();
			System.out.println("listCallByUserId--->"+devSerial);
		}
   	}
   	
	@Test
	@Rollback(false)
	public void testRolg() {
		SysCallLogDeviceRecoDto sysCallLog = mSysDeviceTotalMapper.selectRogIdByRoleOrgId(7l);
		System.out.println("sysCallLog--->"+sysCallLog.toEntityString());
	}
	
	@Test
	@Rollback(false)
	public void testSysUser() {
		PageInfo selectUserByUser = sysUserServiceImpl.selectUserByOrgId(3l);
		String jSon = JsonUtils.toJSon(selectUserByUser);
		System.out.println("json--->"+jSon);
	}
	
	
   	@Test
   	@Rollback(false)
   	public void testSysDevice() {
//   		SysDeviceRoleOrg selectByDeviceId = mSysDeviceRoleOrgMapper.selectByDeviceId(11l);
//   		System.out.println("tesstSysDevice-->"+selectByDeviceId.toString()); 
   		List<SysDeviceAndCallDto> listUserAndDevice = sysUserServiceImpl.listUserAndDevice(13l);
   		ObjectMapper objectMapper = new ObjectMapper();
   		try {
			String json = objectMapper.writeValueAsString(listUserAndDevice);
			System.out.println("lisUser-->"+json); 
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   		
   	}
 
   	@Test
   	@Rollback(false)
   	public void test5() { 
   		SysPermissionGroup selectByGroupName = mSysPermissionGroup.selectByGroupName("qytxlmbtest.csv_29_的企业通讯录");
   		System.out.println("test5-->"+selectByGroupName.toString());
   	}
 
   	
	
   	@Test 
   	@Rollback(false)
   	public void testListOrg() {
   		List<SysOrganization> list=new ArrayList<SysOrganization>();
   		List<SysOrganization> selectSysOrgList = mSysOrganizationService.selectSysOrgList(6l,list,false);
   		List<SysUserOrgDto> sysUserOrg = getSysUserOrg(selectSysOrgList,21l);
   		String jSon = JsonUtils.toJSon(sysUserOrg);
   		System.out.println("未绑定-->"+jSon);
//   		ObjectMapper objectMapper = new ObjectMapper();
//   		try {
//			String writeValueAsString = objectMapper.writeValueAsString(selectSysOrgList);
//			System.out.println("testListOrg-->"+writeValueAsString);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
   		
   		
//   		PageInfo selectDeviceFromOrg = mSysOrganizationService.selectDeviceFromOrg(30, 30, 2l);
//   		ObjectMapper objectMapper = new ObjectMapper();
//   		try {
//			String writeValueAsString = objectMapper.writeValueAsString(selectDeviceFromOrg);
//			System.out.println("testListOrg-->"+writeValueAsString); 
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
   	}
   	
   	public List<SysUserOrgDto> getSysUserOrg(List<SysOrganization> listOrg,Long contactId){
   		List<SysUserOrgDto> list = mSysContactUserMapper.selectUserOrgAuthByContactId(contactId);
   		List<Long> listUserId=new ArrayList<>();
   		for(SysUserOrgDto dto:list) {
   			Long userId = dto.getSysUserId();
   			listUserId.add(userId);
   		}
   		List<SysUserOrgDto> listUserOrgDto=new ArrayList<>();
   		for(SysOrganization sysOrg:listOrg) {
   			Long id = sysOrg.getId();
   			
   			List<SysUser> listUser = mSysUserMapper.selectUserByOrgId(id);
   			for(SysUser sysUser:listUser) {
   				Long sysUserId = sysUser.getId();
   				if(!listUserId.contains(sysUserId)) {
   					SysUserOrgDto sysUserOrgDto = new SysUserOrgDto();
   					BeanUtils.copyProperties(sysUser, sysUserOrgDto);
   					sysUserOrgDto.setOrgId(id);
   					sysUserOrgDto.setOrgName(sysOrg.getName());
   					listUserOrgDto.add(sysUserOrgDto);
   				}
   			}
   			
   		}
   		return listUserOrgDto;
   	}
   	
   	@Test
   	@Rollback
   	public void test1() {
   		SysContactUser sysContactUser = new SysContactUser();
   		sysContactUser.setSysUserId(15l);
   		sysContactUser.setSysContactId(21l);
   		sysContactUser.setStatus(1);
   		SysContactUser sysCon = mSysContactUserMapper.select(sysContactUser);
   		System.out.println("sys-->"+sysCon.toEntityString());
   	}
   
   	
   	@Test
   	@Rollback(false)
   	public void testOnLine() {
   		List<Long> list=new ArrayList<>();
   		list.add(15l);
   		list.add(11l);
   		list.add(1l);
   		List<Long> selectOnLine = mSysLoginStatusMapper.selectOnLine(list);
   		System.out.println("在线用户"+selectOnLine.toString());
   	}
    
    
   	@Test
   	public void testOrg() {
//   		Long[] longs=new Long[] {1l,2l};
//   		System.out.println("longs-->"+Arrays.toString(longs));
   		List<Long> list=new ArrayList<>();
   		list.add(1l);
   		list.add(3l);
   		list.add(5l);
   		Long long1=null;
   		boolean contains = list.contains(long1);
   		System.out.println("testOrg");
   	}
    
  
	@Test
	@Rollback(false)
	public void testPg() {
		List<SysPermissionGroupTest> list=new ArrayList<>();
		SysPermissionGroupTest groupTest = new SysPermissionGroupTest();
		groupTest.setName("权限组一"); 
		list.add(groupTest);
		SysPermissionGroupTest groupTest2 = new SysPermissionGroupTest();
		groupTest2.setName("权限组二");
		list.add(groupTest2);
//		Long insert = mPgT.insert(groupTest);
		List<Long> insert = mPgT.insertBatch(list);
		for (Long long1 : insert) {
//			System.out.println("添加权限组成功-->"+insert+"-->id="+groupTest.getId());
			System.out.println("添加权限组成功-->"+insert+"-->id="+long1);
		}
		
	}
	
 
    
}