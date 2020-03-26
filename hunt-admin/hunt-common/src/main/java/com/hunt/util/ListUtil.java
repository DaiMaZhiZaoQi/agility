package com.hunt.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.sun.javafx.collections.MappingChange.Map;

/**
 * 集合格式化
 * @author williambai
 *
 */
public class ListUtil {

	 /**
     * 比较两个集合
     * @param dbList 数据库存在的数据
     * @param list	 客户端产生数据
     * @return list中不同的数据
     */
    public static List<Long>  getInsert(List<Long> dbList,List<?> list){
    	List<Long> listUpdateId = new ArrayList<>();
    	
    	for(Object o:list) {
    		Long id=Long.valueOf(o+"");
    		if(!dbList.contains(id)) {
    			listUpdateId.add(id);
    		}
    	}
    	return listUpdateId;
    }
    
    public static void main(String[] args) {
    	String string="";
    	String[] split2 = string.split(",");
    	if(split2.length>0) {
    		List<String> asList4 = Arrays.asList(split2); 
    		List<Object> arrayList = new ArrayList<>();
    		System.out.println("arrayList--->"+asList4.toString()+"--->"+asList4.size()+"--->"+asList4.get(0));
    	}
		String[] str1= {"1","2","3","3","4","5","5","6","9","10"};
		String[] str2={"10","12","3","41","5","5","16","9","10"}; 
		String str3="";
		String[] split = str3.split(",");
		List<String> asList3 = Arrays.asList(split); 
		System.out.println("split-->"+asList3.toString());
		List<String> asList = Arrays.asList(str1);
		List<String> asList2 = Arrays.asList(str2);
		List<String> proInsert;
		try {
//			proInsert = getProInsert(asList2,asList);
			List<String> proInsert2 = getInsertOrUpDate(asList, asList2);
			System.out.println("需要授权的--->"+proInsert2.toString());
			proInsert = getInsertOrUpDate(asList2,asList);
			System.out.println("需要解除授权的--->"+proInsert.toString()); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("exception-->"+e.getMessage());
			e.printStackTrace();
		}
	}
    
    /**
     * 获得两个集合中不同的数据   listWebIn中有重复的会抛异常，这个是在插入
     * @param listDb	
     * @param listWebIn  需要更新的集合,
     * @return
     * @throws Exception 
     */
    public static List<String> getProInsert(List<String> listDb,List<String> listWebIn) throws Exception{
    	// 哪个集合要添加就循环哪个
    	
    	HashMap<String, Integer> map=new HashMap<String,Integer>(listDb.size());
    	for(String str:listDb) {
    		map.put(str, 1);
    	}
    	HashMap<String, Integer> mapDiff=new HashMap<String,Integer>();	// 不同的数据
    	for(String str:listWebIn) {
    		Integer integer = map.get(str);
    		if(integer!=null) {  //  相同
    			Integer integer2 = mapDiff.get(str);
    			if(integer2!=null) {
    				integer2++;
    				mapDiff.replace(str, integer2);
    			}else {
    				mapDiff.put(str, 2);
				}
//    			continue;
    		}else {
    			mapDiff.put(str, 1);
    		}
    	}
    	List<String> listDiff=new ArrayList<>();
    	Set<Entry<String,Integer>> entrySet = mapDiff.entrySet();
    	for(Entry<String,Integer> entry:entrySet) {
    		if(entry.getValue()==1) {
    			listDiff.add(entry.getKey());
    		}else if(entry.getValue()>=3) {		//  提供的数据源存在重复
    			throw new Exception("->"+entry.getKey());
			}
//    		System.out.println(entry.getKey()+"出现次数-->"+entry.getValue());
    	}
//    	Set<Entry<String,Integer>> entrySet2 = mapWebIn.entrySet();
//    	for(Entry<String,Integer> entry:entrySet2) {
//    		
//    		System.out.println(entry.getKey()+"出现次数-->"+entry.getValue());
//    	}
    	return listDiff;
    }
    
    
    /**
     * 获得两个集合中不同的数据 
     * @param listDb	
     * @param listWebIn  需要更新的集合,
     * @return
     * @throws Exception 
     */
    public static List<String> getInsertOrUpDate(List<String> listDb,List<String> listWebIn) throws Exception{
    	// 哪个集合要添加就循环哪个
    	
    	HashMap<String, Integer> map=new HashMap<String,Integer>(listDb.size());
    	for(String str:listDb) {
    		map.put(str, 1); 
    	}
    	HashMap<String, Integer> mapDiff=new HashMap<String,Integer>();	// 不同的数据
    	for(String str:listWebIn) {
    		Integer integer = map.get(str);
    		if(integer!=null) {  //  相同
    			Integer integer2 = mapDiff.get(str);
    			if(integer2!=null) {
    				integer2++;
    				mapDiff.replace(str, integer2);
    			}else {
    				mapDiff.put(str, 2);
    			}
    		}else {
    			mapDiff.put(str, 1);
    		}
    	}
    	List<String> listDiff=new ArrayList<>();
    	Set<Entry<String,Integer>> entrySet = mapDiff.entrySet();
    	for(Entry<String,Integer> entry:entrySet) {
    		if(entry.getValue()==1) {
    			listDiff.add(entry.getKey());
    		}
    	}
    	return listDiff;
    }
    
    
    
    
    
    
    /**
     * 比较两个集合
     * @param dbList 数据库存在的数据
     * @param list	 客户端产生数据
     * @return dbList不同的数据
     */
    public static  List<Long> getUpdate(List<Long> dbList,List<? extends Object> list){
    	List<Long> listWeb=new ArrayList<>();
    	for(Object obj:list) {
    		listWeb.add(Long.parseLong(obj+""));
    	}
    	List<Long> listInsert=new ArrayList<>();
    	for(Long l:dbList) {
    		if(!listWeb.contains(l)){
    			listInsert.add(l);
    		}
    	} 
    	System.out.println("getUpdate-->"+listInsert.toString());
    	return listInsert;
    }
    
    
    public static <T>List<List<String>>  getSplitlist(List<String> listTarget,List<T> listUser){
		int taskSize = listTarget.size();		//  任务数	10个任务
		int userSize = listUser.size();		//	人	10个人
		List<List<String>> listGroup=new ArrayList<>();
		
		int n = (int)Math.round((double)taskSize/(double)userSize);			//  拆分数
		if(n==0)n=1;
		System.out.println(n);
		int sCount=0;
		if(taskSize>=userSize) {
			sCount=userSize;
		}else if(taskSize<userSize){
			sCount=taskSize;
		}
		
		for(int i=0;i<sCount;i++) {
			if(i==sCount-1) {
				List<String> subList = listTarget.subList(i*n, taskSize);
				if(!subList.isEmpty()) {
					listGroup.add(subList);
				}
			}else {
				listGroup.add(listTarget.subList(i*n, (i+1)*n>taskSize?taskSize:(i+1)*n));
			}
		}
		
		return listGroup;
	}
  
    
    @Test
    public void testSplit() {
    	String[] aarTarget= {"1","2","3","4","5","6","7","8","9","10"};
    	String[] aarUser= {"1","2","3"};
    	List<String> listTarget=Arrays.asList(aarTarget);
    	List<String> listUser = Arrays.asList(aarUser);
    	List<List<String>> splitlist = getSplitlist(listTarget, listUser);
    	System.out.println("splitList--->"+splitlist); 
    } 
}
