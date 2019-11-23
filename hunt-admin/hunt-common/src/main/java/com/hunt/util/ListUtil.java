package com.hunt.util;

import java.util.ArrayList;
import java.util.List;

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
    
    /**
     * 比较两个集合
     * @param dbList 数据库存在的数据
     * @param list	 客户端产生数据
     * @return dbList不同的数据
     */
    public static List<Long> getUpdate(List<Long> dbList,List<?> list){
    	List<Long> listInsert=new ArrayList<>();
    	for(Long l:dbList) {
    		if(!list.contains(l+"")){
    			listInsert.add(l);
    		}
    	}
    	return listInsert;
    }
}
