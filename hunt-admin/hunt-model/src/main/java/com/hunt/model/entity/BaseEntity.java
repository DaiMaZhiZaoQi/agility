package com.hunt.model.entity;

import java.io.Serializable;
import java.lang.reflect.Field;



/**
 * 实体基类
 * @author williambai
 *
 */
public class BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String toEntityString() {
		Class<? extends BaseEntity> clazz = this.getClass();
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder sbu = new StringBuilder();
		sbu.append(clazz.getName()+":"); 
		for(int i=0;i<fields.length;i++) {
			Field field=fields[i];
			field.setAccessible(true);
			String name = field.getName();	//  属性名称
			sbu.append(name+"=");
			try {
				Object value =field.get(this);	//  属性值
				sbu.append(value);
				if(i!=fields.length-1) {
					sbu.append(",\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return sbu.toString();
		
	}
}
