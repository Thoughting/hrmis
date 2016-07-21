package com.eastcom.baseframe.common.utils;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * MySqlUtils
 * 
 * @author wutingguang <br>
 */
public class MySqlUtils extends com.alibaba.druid.util.MySqlUtils{

	/**
	 * 创建保存更新的sql语句
	 * 
	 * @param tableName
	 * @param map
	 * @param columnMapper
	 * @return
	 */
	public static Object[] makeSaveUpdateSqlAndParams(String tableName, Map<String, String> columnMapper,Map<String, Object> propertyMap){
		StringBuffer sbsql = new StringBuffer();
		List<Object> params = Lists.newArrayList();
		sbsql.append(" insert into " + tableName);
		
		//构建insert块
		String tmp_1 = "";
		String tmp_2 = "";
		for (String key : propertyMap.keySet()){
			if (columnMapper.get(key) != null) {
				tmp_1 += " " + columnMapper.get(key) + ",";
				tmp_2 += " ?,";
				params.add(propertyMap.get(key));
			}
		}
		tmp_1 = tmp_1.substring(0, tmp_1.length() - 1);
		tmp_2 = tmp_2.substring(0, tmp_2.length() - 1);
		
		sbsql.append(" ( " + tmp_1 + " ) values ( " + tmp_2 + " ) ON DUPLICATE KEY UPDATE ");
		
		//构建update块
		String tmp_3 = "";
		for (String key : propertyMap.keySet()){
			if (columnMapper.get(key) != null) {
				tmp_3 += " " + columnMapper.get(key) + " = ?,";
				params.add(propertyMap.get(key));
			}
		}
		tmp_3 = tmp_3.substring(0, tmp_3.length() - 1);
		
		sbsql.append(tmp_3);
		return new Object[]{sbsql.toString(),params.toArray()};
	}
	
}
