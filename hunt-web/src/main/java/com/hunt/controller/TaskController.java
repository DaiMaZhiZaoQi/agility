package com.hunt.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hunt.controller.JsonUtils.TestObj;
import com.hunt.model.dto.PageDto;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysUserDto;
import com.hunt.model.entity.SysTaskGroup;
import com.hunt.model.entity.SysTaskWithBLOBs;
import com.hunt.model.entity.SysUser;
import com.hunt.properties.PropertiesUtil;
import com.hunt.service.SysTaskService;
import com.hunt.service.SysUserService;
import com.hunt.util.ResponseCode;
import com.hunt.util.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.HttpMethod;
import net.sf.jsqlparser.statement.update.Update;

@Api(value="我的任务,发布任务更新任务")
@Controller
@RequestMapping("task")
public class TaskController extends BaseController{
	static Logger log= LoggerFactory.getLogger(TaskController.class);
	
	@Autowired
	private SysTaskService mSysTaskService;
	@Autowired
	PropertiesUtil mPropertiesUtil;
	@Autowired
	SysUserService mSysUserService;
	
	@ApiOperation(value="myTask",httpMethod="GET",produces="text/html")
	@RequestMapping("myTask")
	public String task() {
		return "home/myTask";
	}
	
	/**
	 * 查询任务组考虑分页，排序，权限，用户id,一个人可能有多个任务组
	 * @param 
	 * @return
	 */
	@ApiOperation(value="listTaskGroup",httpMethod="POST",produces="application/json")
	@RequestMapping(value="listTaskGroup",method=RequestMethod.POST)
	@ResponseBody
	public PageInfo listTaskGroup(@RequestBody PageDto pageDto) {  
		return mSysTaskService.selectTaskGroup(pageDto);
	}  
	
	
	@ApiOperation(value="上传文件",produces="application/json",httpMethod="POST")
	@ResponseBody
	@RequestMapping(value="upTF",method=RequestMethod.POST)
	public Result upTaskFile(@RequestParam(value="taskFile",required=true,defaultValue="")MultipartFile[] file,
							@RequestParam(value="userId",required=true,defaultValue="")Long userId,
							HttpServletRequest request
			) {  
		//  TODO 判断有无任务发布权限					
		log.info("文件--->"+Arrays.toString(file));
		if(file.length<=0)return Result.error("请选择文件");
		for(MultipartFile mf:file) {
			try {
				// 定义文件的父级目录
				SysUser sysUser = mSysUserService.selectById(userId);
				String fileTop=sysUser.getLoginName();
				String fileName= mf.getOriginalFilename();
				String fileType=fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase().trim();
				if(!fileType.equals("csv"))return new Result(ResponseCode.missing_parameter.getCode(),"不支持的格式,请上传csv文件");
				File fileTask = createFile(fileTop, request, fileType, mPropertiesUtil.getTaskFilePath());
				if(fileTask==null)return Result.error(ResponseCode.file_config_fail.getMsg());
				mf.transferTo(fileTask);
				//  文件解码
				File newFileTask = mSysTaskService.convertUtf8(fileTask);
				//	读文件，添加到数据库
				return mSysTaskService.insertTask(newFileTask,sysUser);
			} catch (Exception e) {
				e.printStackTrace();  
				return Result.error("文件存储错误");
			}
		}
		return Result.success();
	}
	
	
	
	public static class TestObj{
		private HashMap<String, String> map;

		public HashMap<String, String> getMap() {
			return map;
		}

		public void setMap(HashMap<String, String> map) {
			this.map = map;
		}
	}
	
	@ApiOperation(value="删除任务组",httpMethod="GET",produces="application/json")
	@ResponseBody
	@RequestMapping(value="deleteTaskGroup",method=RequestMethod.GET)
	public Result deleteTaskGroup(@RequestParam("taskGroupId")Long taskGroupId,@RequestParam("userId")Long userId) {
		return mSysTaskService.deleteTaskGroup(taskGroupId,userId);
	}
	
	@ApiOperation(value="修改任务",httpMethod="POST",produces="application/json")
	@RequestMapping(value="updateTaskMsg",method=RequestMethod.POST)
	@ResponseBody
	public Result updateTaskMsg(@RequestParam String taskMsg,@RequestParam("taskGroupId")Long taskGroupId) {
		System.out.println("updateTaskMsg-->"+taskMsg);
		Gson gson = new Gson();
		TestObj object =  gson.fromJson(taskMsg,TestObj.class );
		HashMap<String,String> map = object.getMap();
		
		 Iterator it = map.keySet().iterator();
         while (it.hasNext()) {
             String key = (String)it.next();
             String value = (String)map.get(key);
           System.out.println("key: " + key + ", value: " + value);
         }
         //{map:{"xm":"我的测试","dhhm":"18666163064","zz":"深圳市罗湖区","jg":"广东江门","qt":"后天联系他","id":2,"status":1,"taskCount":9,"record":"更多","editing":true}}
         String taskNum = map.get("dhhm");
         String taskName = map.get("xm");
         Long id=Long.valueOf(map.get("id"));
         Byte status = Byte.valueOf(map.get("status"));
         map.remove("id");
         map.remove("status");
         map.remove("taskCount");
         map.remove("record");
         map.remove("editing");
         map.remove("updateTime");
         String dbTaskMsg=JsonUtils.toJSon(map);
         SysTaskWithBLOBs sysTaskWithBLOBs = new SysTaskWithBLOBs();
         
         sysTaskWithBLOBs.setId(id);
         sysTaskWithBLOBs.setTaskName(taskName);
         sysTaskWithBLOBs.setStatus(status); 
         sysTaskWithBLOBs.setTaskMsg(dbTaskMsg);   
         
         log.info(taskNum+"--"+taskName+"--"+status+"--"+dbTaskMsg);
            
        return mSysTaskService.updateTaskMsg(sysTaskWithBLOBs,taskGroupId);
	}
	
	@ApiOperation(value="查询任务栏",produces="application/json",httpMethod="POST")
	@ResponseBody
	@RequestMapping(value="taskColumn",method=RequestMethod.POST)
	public Result listTaskGroup(@RequestParam Long userId ,@RequestParam Long sysTaskGroupId) {
		 SysTaskGroup selectGroupDetail = mSysTaskService.selectGroupDetail(userId, sysTaskGroupId);
		 String jSon = JsonUtils.toJSon(selectGroupDetail); 
		 return Result.instance(Result.success().getCode(), jSon);
	}   
	
	@ApiOperation(value="查询任务列表",produces="application/json",httpMethod="POST")
	@ResponseBody
	@RequestMapping(value="taskList",method=RequestMethod.POST)
	public PageInfo listTask(@RequestBody PageDto pageDto) {
		return mSysTaskService.selectTaskList(pageDto);
	}
	
	
	
	@ApiOperation(value="查询未分配的任务数",produces="application/json",httpMethod="POST")
	@ResponseBody
	@RequestMapping(value="listUnPatch",method=RequestMethod.POST)  
	public PageInfo listUnDisPatch(@RequestParam(value="userId") Long userId,
									@RequestParam(value="taskGroupId") Long taskGroupId,
									@RequestParam(value="status",defaultValue="-1")Integer status) {
			return mSysTaskService.selectUnDisPatch(userId,taskGroupId,status);
	}
	  
	
	@ApiOperation(value="一键授权",produces="application/json",httpMethod="POST")
	@ResponseBody
	@RequestMapping(value="oneKey",method=RequestMethod.POST)
	public Result oneKeyDisPatch(@RequestParam(value="taskGroupId")Long taskGroupId,
								@RequestParam(value="userId")Long userId,
								@RequestParam(value="listTaskId") String listTaskIdStr,
								@RequestParam(value="listUser")String listUserStr) {
		
		if(StringUtils.isEmpty(listTaskIdStr)) {
			return Result.instance(ResponseCode.task_num_0.getCode(), ResponseCode.task_num_0.getMsg());
		}
		String[] split = listTaskIdStr.split(",");
		
		List<String> listTaskId = Arrays.asList(split);
		List<SysUserDto> listUserDto = Arrays.asList(JsonUtils.readValue(listUserStr, SysUserDto[].class));
		ArrayList<SysUser> listUser = new ArrayList<>();
		for(SysUserDto userDto:listUserDto) {
			SysUser sysUser = new SysUser();
			sysUser.setId(userDto.getId());
			sysUser.setZhName(userDto.getZhName());
			sysUser.setStatus(userDto.getStatus());
			listUser.add(sysUser);
		}
		return mSysTaskService.oneKeyDisPatch(taskGroupId, userId, listTaskId, listUser);
	}
	
	
	@ApiOperation(value="任务派发",produces="application/json",httpMethod="POST")
	@ResponseBody
	@RequestMapping(value="dispatchTask",method=RequestMethod.POST)
	public Result dispatchTask(@RequestParam(value="userId") Long userId,
								@RequestParam(value="taskGroupId")Long taskGroupId,
								@RequestParam(value="taskUserId")Long taskUserId,
								@RequestParam(value="zhName")String zhName,
								@RequestParam(value="strTaskSelectId")String strTaskSelectId,
								@RequestParam(value="strTaskAllId")String strTaskAllId
								) {
		//  判断有无派发任务权限
		
		
		SysUser sysUser = new SysUser(); 
		sysUser.setId(taskUserId);
		sysUser.setZhName(zhName); 
		List<String> listTaskSelectId=null;
		if(!StringUtils.isEmpty(strTaskSelectId)) {
			String[] splitSelect = strTaskSelectId.split(","); 
			List<String> listTaskSelectIdTemp = Arrays.asList(splitSelect);
			listTaskSelectId=new ArrayList<String>(listTaskSelectIdTemp);
		}else {
			listTaskSelectId=new ArrayList<>();
		}
		List<String> listTaskAllId=null;
		if(!StringUtils.isEmpty(strTaskAllId)) {
			String[] splitAllSelect = strTaskAllId.split(",");
			List<String> listTaskAllIdTemp = Arrays.asList(splitAllSelect);
			 listTaskAllId=new ArrayList<String>(listTaskAllIdTemp);
		}else {
			 listTaskAllId=new ArrayList<>();
		}
		return mSysTaskService.updateTask(taskGroupId,sysUser, listTaskSelectId,listTaskAllId);
	}
	
	
	
	
	@RequestMapping(value="TlistTaskGroup",method=RequestMethod.POST)
	@ResponseBody
	public PageInfo tlistTaskGroup(@RequestParam(value="page",defaultValue="1",required=false) Integer page,
			@RequestParam(value="rows",defaultValue="15",required=false) Integer rows,
			@RequestParam(value="userId",defaultValue="1",required=false) Long userId) {  
		PageDto pageDto = new PageDto();
		pageDto.setPage(1);
		pageDto.setRows(rows);
		pageDto.setUserId(userId);
		return mSysTaskService.selectTaskGroup(pageDto);
	}

}
