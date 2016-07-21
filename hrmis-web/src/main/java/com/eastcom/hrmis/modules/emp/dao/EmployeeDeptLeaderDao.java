package com.eastcom.hrmis.modules.emp.dao;

import com.eastcom.baseframe.common.dao.Dao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDeptLeader;

/**
 * 分管部门领导DAO
 * @author wutingguang <br>
 */
public interface EmployeeDeptLeaderDao extends Dao<EmployeeDeptLeader> {

	/**
	 * 删除分管领导下的部门关系
	 * @param roleId
	 * @throws Exception
	 */
	public void deleteAllDepts(String leaderId);
	
	/**
	 * 插入分管领导下的部门关系
	 * @param roleId
	 * @param menuId
	 * @throws Exception
	 */
	public void insertDeptRelation(String leaderId, String deptId);
	
}
