package com.eastcom.hrmis.modules.emp.dao;

import com.eastcom.baseframe.common.dao.Dao;
import com.eastcom.hrmis.modules.emp.entity.CompilationTable;

/**
 * 员工编制表
 * @author wutingguang <br>
 */
public interface CompilationTableDao extends Dao<CompilationTable> {

	/**
	 * 清空部门项目关联
	 * @param id
	 * @throws Exception
	 */
	public void deleteAllDeptSets(String id);
	
	/**
	 * 插入部门项目关联
	 * @param id
	 * @param deptId
	 * @throws Exception
	 */
	public void insertDeptSet(String id, String deptId);
	
	/**
	 * 清空岗位关联
	 * @param id
	 * @throws Exception
	 */
	public void deleteAllPostSets(String id);
	
	/**
	 * 插入岗位关联
	 * @param roleId
	 * @param areaId
	 * @throws Exception
	 */
	public void insertPostSet(String id, String postId);
	
}
