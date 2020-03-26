package com.hunt.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.jta.TransactionFactory;
import org.springframework.transaction.support.TransactionSynchronizationUtils;

import com.hunt.dao.SysTaskGroupMapper;
import com.hunt.dao.SysTaskMapper;
import com.hunt.dao.SysTaskUserMapper;
import com.hunt.dynamicclass.ChineseToSpell;
import com.hunt.model.dto.PageDto;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysTaskAndTaskGroupDto;
import com.hunt.model.entity.SysTask;
import com.hunt.model.entity.SysTaskGroup;
import com.hunt.model.entity.SysTaskUser;
import com.hunt.model.entity.SysTaskWithBLOBs;
import com.hunt.model.entity.SysUser;
import com.hunt.service.SysTaskService;
import com.hunt.util.ListUtil;
import com.hunt.util.ResponseCode;
import com.hunt.util.Result;
import com.hunt.util.StringUtil;
import com.hunt.util.UtReadCsv;
import com.sun.javafx.collections.MappingChange.Map;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

/**
 * 我的任务 测试 事务回滚，查询接口不用考虑食物回滚
 * 
 * @author williambai
 *
 */
@Service
@Transactional
public class SysTaskServiceImp implements SysTaskService {

	@Autowired
	SysTaskGroupMapper mSysTaskGroupMapper;
	
	@Autowired
	SysTaskMapper mSysTaskMapper;
	
	@Autowired
	SysTaskUserMapper mSysTaskUserMapper;
	static Logger log= LoggerFactory.getLogger(SysTaskServiceImp.class);
	@Override
	public PageInfo selectTaskGroup(PageDto pageDto) {
		List<SysTaskGroup> listTaskGroup = mSysTaskGroupMapper.selectByPageDto(pageDto);
		return new PageInfo(listTaskGroup.size(), listTaskGroup);
	}
	
	
	
	
	
	
	@Override
	public SysTaskGroup selectGroupDetail(Long userId, Long sysTaskGroupId) {
		return mSysTaskGroupMapper.selectByIdUserId(userId,sysTaskGroupId);
		
	}






	@Override
	public PageInfo selectTaskList(PageDto pageDto) {
		Long id = pageDto.getId(); 		//  sysTaskGroupId
		Long userId = pageDto.getUserId(); 
		Integer others = pageDto.getOthers();
		List<SysTaskWithBLOBs> listSysTask=null;
		
		if(pageDto.getCurUserId()==null) {		//  查询当前用户的id,在任务派发时
			SysTaskGroup sysTaskGroup = mSysTaskGroupMapper.selectByIdUserIdAuth(userId, id);
			int isAuth=sysTaskGroup==null?0:1;
			   
			listSysTask= mSysTaskMapper.selectSysTask(pageDto,isAuth);
			if(pageDto.getPage()==1) {
				Integer dataCount=mSysTaskMapper.selectCountSysTask(pageDto,isAuth);
				return new PageInfo(dataCount, listSysTask);
			}
		}else {
			
			 listSysTask = mSysTaskMapper.selectDialogSysTask(pageDto);
		}
		return new PageInfo(listSysTask.size(), listSysTask);
	}
	
	
	
	
	






	@Override
	public PageInfo selectUnDisPatch(Long userId, Long taskGroupId, Integer status) {
		List<SysTaskWithBLOBs> listUnPatch = mSysTaskMapper.selectUnDisPatch(userId, taskGroupId, status);
		return new PageInfo(listUnPatch.size(), listUnPatch);
	}

	
	
	










	@Override
	public Result oneKeyDisPatch(Long taskGroupId,Long userId, List<String> listTaskId, List<SysUser> listUser) {
		
		List<List<String>> splitlist = ListUtil.getSplitlist(listTaskId, listUser); 
		
		
				for(int j=0;j<splitlist.size();j++) {
					List<String> listGrop = splitlist.get(j); 
					SysUser user=listUser.get(j);
					updateTask(taskGroupId,user,listGrop,new ArrayList<>());
				}
		return Result.success();
	}

	
	
	
	



	@Override
	public Result updateTask(Long taskGroupId,SysUser sysUser, List<String> listTaskSelectId,List<String> listTaskAllId) {
		// update SysTask
		try {
			Long userId = sysUser.getId();
			if(listTaskAllId.size()>0) {
				List<String> listUnDispatch = ListUtil.getInsertOrUpDate(listTaskSelectId,listTaskAllId);  //  需要解除授权的 status=-1
				sysUser.setStatus(-1);
				log.info("updateTask-->"+listUnDispatch.toString());
				//  解除授权
				if(listUnDispatch.size()>0) {
					sysUser.setId(null);
					mSysTaskMapper.updateSysTaskListTaskId(sysUser,listUnDispatch);
				}
				
			}
			
			List<String> listDispatch = ListUtil.getInsertOrUpDate(listTaskAllId,listTaskSelectId);  //  需要授权的 status=1
			//	新增授权
			sysUser.setStatus(0);
			log.info("updateTask-->"+listDispatch.toString());
			if(listDispatch.size()>0) {
				sysUser.setId(userId);
				mSysTaskMapper.updateSysTaskListTaskId(sysUser, listDispatch);
			}
			sysUser.setId(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Result.error("请重新选择");
		}
		
		SysTaskUser dbTaskUser = mSysTaskUserMapper.selectTaskUser(taskGroupId, sysUser.getId());
		log.info("listTaskSelectId--->"+listTaskSelectId.toString()+"--->"+listTaskSelectId.size());
			if(!listTaskSelectId.isEmpty()) {		//  有分派的任务
				SysTaskUser sysTaskUser = new SysTaskUser();
				sysTaskUser.setStatus((byte)1); 
				sysTaskUser.setSysUserName(sysUser.getZhName());
				if(dbTaskUser==null) {
					sysTaskUser.setSysTaskGroup(taskGroupId);
					sysTaskUser.setSysUserId(sysUser.getId());
					mSysTaskUserMapper.insert(sysTaskUser);
				}else {
					if(dbTaskUser.getStatus()!=1) {
						sysTaskUser.setId(dbTaskUser.getId());
						mSysTaskUserMapper.update(sysTaskUser);
					}
				}
				
			}else {								// 没分派的任务
				if(dbTaskUser!=null) {					// update SysTaskUser  解除授权
					Long sysUserId = dbTaskUser.getSysUserId();
					Long sysTaskGroupId = dbTaskUser.getSysTaskGroup();
					SysTaskGroup sysTaskGroup = mSysTaskGroupMapper.selectByIdUserId(sysUserId, sysTaskGroupId);
					if(sysTaskGroup!=null) {
						Long taskPubUserId = sysTaskGroup.getTaskPubUserId();
						if(sysUserId==taskPubUserId) {			//发布者不能对自己解除授权
							return Result.success();
						}
					}
					SysTaskUser sysTaskUser = new SysTaskUser();
					sysTaskUser.setStatus((byte)0); 
					sysTaskUser.setId(dbTaskUser.getId());
					mSysTaskUserMapper.update(sysTaskUser);
					log.info("listTaskSelectId--->"+listTaskSelectId.toString()+"--->"+sysTaskUser.toEntityString());
				}
			}
		return Result.success();
	}


	




	@Override
	public Result updateTaskMsg(SysTaskWithBLOBs sysTaskWithBLOBs,Long taskGroupId) {
		
		int updateResult = mSysTaskMapper.updateByPrimaryKeySelective(sysTaskWithBLOBs);
		if(updateResult>0) {
			Byte status = sysTaskWithBLOBs.getStatus();
			SysTaskGroup sysTaskGroup = new SysTaskGroup();
			if(status==2) {
				sysTaskGroup.setTaskFinish(1);
			}else {    
				sysTaskGroup.setTaskFinish(-1);
			}
			mSysTaskGroupMapper.updateById(sysTaskGroup, taskGroupId);
			return Result.success();
		}
		return Result.instance(ResponseCode.missing_parameter.getCode(), ResponseCode.missing_parameter.getMsg());
	}

	




	@Override
	public Result deleteTaskGroup(Long taskGroupId, Long userId) {
		//  删除任务组
		SysTaskGroup sysTaskGroup = new SysTaskGroup();
		sysTaskGroup.setId(taskGroupId);
		sysTaskGroup.setStatus((byte)2);
		sysTaskGroup.setTaskPubUserId(userId);  
		Long deleteBy = mSysTaskGroupMapper.deleteBy(sysTaskGroup);
		if(deleteBy>0) {		//  任务组删除成功
			SysTaskWithBLOBs sysTask = new SysTaskWithBLOBs();
			sysTask.setTaskGroupId(taskGroupId);
			sysTask.setStatus((byte)4);
			int deleteResult = mSysTaskMapper.deleteByTaskGroupId(sysTask);
			log.info("deleteResult-->"+deleteResult);
			if(deleteResult>0) {   
				SysTaskUser sysTaskUser = new SysTaskUser();
				sysTaskUser.setSysTaskGroup(taskGroupId);
				sysTaskUser.setStatus((byte)2); 
				mSysTaskUserMapper.deleteTaskUser(sysTaskUser);
			}else {
				throw new UnauthorizedException("没有权限");
			}
			return deleteResult>0?Result.success():Result.error(ResponseCode.no_permission.getMsg());
		}
		//  删除任务
		return Result.error(ResponseCode.no_permission.getMsg());
	}






	@Override
	public Result insertTask(File fileTask, SysUser sysUser) {
	// 判断文件和文件版本
		

//		File file=new File("C:\\Users\\williambai\\Desktop\\candelete\\qytxlmbtest.csv");
		String[] strArr=new String[2];
		if(!fileTask.exists()) {
			return null;
		}
		
		BufferedReader reader=null;
		boolean fileOk=false;					//  文件是否合法，版本正确，有操作更新权限，否则文件将删除
		String taskCode=null;
		String taskGroupName=null;
		try {
			reader=new BufferedReader(new FileReader(fileTask));
		
			String str="";
			int i=0;	//总行数
			int k=0;
			boolean isPublish=false;
			List<String> columnStr=null;		// csv列字段
			SysTaskGroup dbSysTaskGroup=null;
			SysTaskGroup sysTaskGroup = new SysTaskGroup();
//			SysTaskAndTaskGroupDto sysTaskAndTaskGroupDto = new SysTaskAndTaskGroupDto();
			List<SysTaskWithBLOBs> listSysTask= new ArrayList<>();
			while((str=reader.readLine())!=null) {	//  读到第三行 判断是否为 YYYYMMDD类型
				i++;
				if(i==2) {							// 机构名称
					String[] split = str.split(",");
					if(split.length>=1) {
						strArr[0]=split[0];
						dbSysTaskGroup= mSysTaskGroupMapper.selectTaskGroupByGroupName(split[0]);
						taskGroupName=split[0];
					}
				}  
				if(i==3) {							// 文件版本
					 String[] split = str.split(",");
					 if(split.length>=1) {
						 if(UtReadCsv.isCorrect(split[0])) {	// task_code格式
							 strArr[1]=split[0];
							 if(dbSysTaskGroup!=null) {
//								 fileOk=true;				//任务已存在或没有更新权限
//									return Result.instance(ResponseCode.task_exist.getCode(), ResponseCode.task_exist.getMsg());
								 Long dbTaskCode =Long.valueOf(dbSysTaskGroup.getTaskCode());
								 Long nTaskCode =Long.valueOf(split[0]);
								 taskCode=nTaskCode+"";
								
								if(dbTaskCode<nTaskCode) {		// 数据库任务码小于新任务码更新
									//  判断该用户是否有该任务组权限，有权限则能更新,由发布者对该文件有更新权限的进行更新
									List<SysTaskGroup> listTaksGroup = mSysTaskGroupMapper.selectTaskGroupByUserId(sysUser.getId());
									
									for(SysTaskGroup taskGroup:listTaksGroup) {
										String alltaskGroupName = taskGroup.getTaskGroupName();
										if(alltaskGroupName.equals(dbSysTaskGroup.getTaskGroupName())) {
											isPublish=true;
										}
									}
									if(!isPublish) {		// 是作者，只有作者才有更新文件权限
										//  任务重新发布之后
										fileOk=true;				//任务已存在或没有更新权限
										return Result.instance(ResponseCode.task_exist.getCode(), ResponseCode.task_exist.getMsg());
									}
								}else {
										fileOk=true;				// 任务码错误
									return Result.instance(ResponseCode.file_exist_code.getCode(), ResponseCode.file_exist_code.getMsg());
								}
							 }else {
								 Long nTaskCode =Long.valueOf(split[0]);
								 taskCode=nTaskCode+"";
							}
						 }else {
							 
							 return Result.instance(ResponseCode.file_exist_code.getCode(), ResponseCode.file_exist_code.getMsg());
						}
						 
					 }
				}
				// 读文件
				// 生成SysTaskAndTaskGroup
			
				if(i==4) {		//  sysTaskGroup  
					String[] split=str.split(",");
					sysTaskGroup.setTaskPubUserId(sysUser.getId());
					sysTaskGroup.setTaskPubUserName(sysUser.getZhName());
					sysTaskGroup.setTaskCode(taskCode);
					sysTaskGroup.setTaskGroupName(taskGroupName);
					String[] columnName=str.split(",");
					 columnStr = ChineseToSpell.getColumnStr(str);
					String jsonStr = ChineseToSpell.getJsonStr(str,columnStr); 
//					sysTaskGroup.setTaskColumn(str);	//  TODO 动态生成列 json  手动拼接
					sysTaskGroup.setTaskColumn(jsonStr);	//  TODO 动态生成列 json  手动拼接
					sysTaskGroup.setStatus((byte)1);
				}
				if(i>4) {
				
					String[] split = str.split(",");
					if(split.length>1) {
							
						String taskName=split[0];
							// 电话号码
						String taskNumber=split[1];
						String[] strMutNum=taskNumber.split(";");	//  TODO 测试多电话号码
						for(String strMut:strMutNum) {
							SysTaskWithBLOBs sysTaskWithBLOBs = new SysTaskWithBLOBs();
							sysTaskWithBLOBs.setTaskName(taskName);
							sysTaskWithBLOBs.setTaskNumber(strMut);
							String taskMsg = ChineseToSpell.getJsonStr(str,columnStr); 
//							sysTaskWithBLOBs.setTaskMsg(str);
							sysTaskWithBLOBs.setTaskMsg(taskMsg);
							listSysTask.add(sysTaskWithBLOBs);
							k++;
						}
					}
					
					
					
				}
			}
			sysTaskGroup.setTaskSize(k);
//			sysTaskAndTaskGroupDto.setmSysTaskGroup(sysTaskGroup);
//			sysTaskAndTaskGroupDto.setmSysTask(listSysTask);
			if(isPublish) {		//  重新发布更新
				Long id = dbSysTaskGroup.getId();
				mSysTaskGroupMapper.updateById(sysTaskGroup, id);  // TODO 考虑旧文件删除问题
				//先查出所有电话号码
				List<String> dblistTaskNum = mSysTaskMapper.selectTaskNumByGroupId(id);
				List<String> newListTaskNum=new ArrayList<>();
				for(SysTaskWithBLOBs sysTask:listSysTask) {
					newListTaskNum.add(sysTask.getTaskNumber());
				}
				//比较数据库中的号码和上传文件中的号码
				//先删掉
				List<String> dbListNumUpdate=null;
				//添加新数据
				List<String> webListNumInsert=null;
				try {
					dbListNumUpdate= ListUtil.getProInsert(newListTaskNum, dblistTaskNum);
					if(dbListNumUpdate.size()>0) {
						mSysTaskMapper.deleteByTaskNum(dbListNumUpdate,id);
					}
				
					webListNumInsert= ListUtil.getProInsert(dblistTaskNum, newListTaskNum);	
				} catch (Exception e) {
					// TODO: handle exception
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return Result.instance(ResponseCode.task_num_exist2.getCode(), ResponseCode.task_num_exist2.getMsg()+e.getMessage());
				}
				
				List<SysTaskWithBLOBs> listInsertTask=new ArrayList<>();
				List<SysTaskWithBLOBs> listUpdateTask=new ArrayList<>();
				for(SysTaskWithBLOBs sysTask:listSysTask) {
					log.info("所有数据"+sysTask.getTaskNumber());
					if(webListNumInsert.contains(sysTask.getTaskNumber())) {	// 要添加的数据
						log.info("要添加的数据"+sysTask.getTaskNumber());
						listInsertTask.add(sysTask);
					}else {														// 要修改的数据
						if(!dbListNumUpdate.contains(sysTask.getTaskNumber())) {	// 不包含已删掉的
							log.info("要修改的数据"+sysTask.getTaskNumber());
							listUpdateTask.add(sysTask);
						}
					}
				}
				if(listInsertTask.size()>0) {
					mSysTaskMapper.insertBatch(listInsertTask,id);// TODO ok 考虑号码相同重复问题,同一任务文件中号码不能重复
				}
				if(listUpdateTask.size()>0) {						//上传的CSV文件中不存在，数据库中存在，删除数据库中的数据
					//修改新数据
					mSysTaskMapper.updateSysTaskByTaskNumber(listUpdateTask);   //  TODO 考虑事务没有正常上传完成即回滚
				}
			}else {				//  发布新任务
				mSysTaskGroupMapper.insert(sysTaskGroup);
				//给发布者授权，发布者默认有该文件权限
//				Boolean existTaskUser= mSysTaskUserMapper.selectExistUserGroup(sysTaskGroup.getId(),sysUser.getId());
//				if(!existTaskUser) {
					SysTaskUser sysTaskUser = new SysTaskUser();
					sysTaskUser.setSysTaskGroup(sysTaskGroup.getId());
					sysTaskUser.setSysUserId(sysUser.getId());
					sysTaskUser.setSysUserName(sysUser.getZhName());
					sysTaskUser.setStatus((byte)1);
					mSysTaskUserMapper.insert(sysTaskUser);
//				}
				mSysTaskMapper.insertBatch(listSysTask,sysTaskGroup.getId());// TODO ok 考虑号码相同重复问题,同一任务文件中号码不能重复
			}
//			sysTask.setTaskTimeChain(taskTimeChain);//  时间链条暂时不考虑
			return Result.success();
			
		} catch (Exception e) {
			e.printStackTrace();
			fileTask.delete();	//  友好提示
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}finally {
			if(reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fileOk) {
				fileTask.delete();				//  TODO 文件对于系统来说没有任何意义
			}
			
		}
		return Result.instance(ResponseCode.task_num_exist.getCode(), ResponseCode.task_num_exist.getMsg());
	
//		return insertSysTaskGroup(newFileTask,sysUser);
	}

	/**
	 * 获得任务和任务组
	 * @param fileTask
	 * @return
	 */
	private Result insertSysTaskGroup(File fileTask,SysUser sysUser) {
		return Result.instance(ResponseCode.task_num_exist.getCode(), ResponseCode.task_num_exist.getMsg());
	}


	@Override
	public File convertUtf8(File fileTask) throws IOException {
			String enCodeType= UtReadCsv.getFileEncode2(fileTask.getAbsolutePath());
			BufferedReader read =null;
			BufferedWriter writer=null;
			FileOutputStream fOut=null;
			FileInputStream fRead=null;
			File newFile=null;
				if(!"utf-8".equals(enCodeType)) {
					try {
						System.out.println("编码格式-->"+enCodeType);
						read= new BufferedReader(new InputStreamReader(new FileInputStream(fileTask),enCodeType));
						String str="";
						String parent = fileTask.getParent();
									
						newFile=new File(parent,"TEMP"+System.currentTimeMillis()+".csv");
						writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile),"UTF-8"));
									
						while((str=read.readLine())!=null) { 
							writer.write(str+"\n");
						}
						writer.flush();
						String name = fileTask.getName(); 
						fileTask.delete();
						File fileResult = new File(parent,name);
						fOut= new FileOutputStream(fileResult);
						fRead = new FileInputStream(newFile);
						int len=0;
						byte[] bs=new byte[1024*8];
						while((len=fRead.read(bs))!=-1) {
							fOut.write(bs,0,bs.length);
						}
						fOut.flush();
						fRead.close();
						fileTask=fileResult;
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
						if(read!=null) {
							read.close();
						}
						if(writer!=null) {
						    writer.close();
						}
						if(fOut!=null) {
							fOut.close();
						}
						if(fRead!=null) {
							fRead.close();
						}
						if(newFile!=null) {
							newFile.delete();
							if(newFile.exists()) {
							  System.out.println("文件-->"+newFile.getAbsolutePath());
							}
						}
					}
				}
			
					
		return fileTask;
	}
			
}
