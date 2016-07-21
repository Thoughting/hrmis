package com.eastcom.baseframe.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Entity支持类
 */
@MappedSuperclass
public abstract class BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	@Transient
	public Map<String, Object> toMap() {
		//需要排除的bean属性
		HashSet<String> exclusion = new HashSet<String>();
		exclusion.add("serialVersionUID"); //排除 private static final long serialVersionUID
		Map<String, Object> map = new HashMap<String, Object>();
		Class<?> clazz=getClass();
		for (Field field : clazz.getDeclaredFields()) {
			try {
				String fieldName=field.getName();
				if(!exclusion.contains(fieldName)){
					String firstLetter=fieldName.substring(0, 1);
					map.put(fieldName, 
							clazz.getMethod("get"+fieldName.replaceFirst(firstLetter, firstLetter.toUpperCase())).invoke(this));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

}