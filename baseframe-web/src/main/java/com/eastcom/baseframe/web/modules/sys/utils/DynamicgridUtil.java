package com.eastcom.baseframe.web.modules.sys.utils;

import com.eastcom.baseframe.web.modules.sys.entity.Dynamicgrid;

public class DynamicgridUtil {
	public static Dynamicgrid getDynamicgrid(String name){
		StringBuffer hql = new StringBuffer();
		hql.append("from Dynamicgrid where name = ? ");
		
		return null;
	}
}
