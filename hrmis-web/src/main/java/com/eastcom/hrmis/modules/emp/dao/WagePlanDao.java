package com.eastcom.hrmis.modules.emp.dao;

import com.eastcom.baseframe.common.dao.Dao;
import com.eastcom.hrmis.modules.emp.entity.WagePlan;

/**
 * 工资方案DAO
 * @author wutingguang <br>
 */
public interface WagePlanDao extends Dao<WagePlan> {

	/**
	 * 清空工资核算项关联
	 * @param id
	 * @throws Exception
	 */
	public void deleteAllWageCountSets(String id);
	
	/**
	 * 插入工资核算项关联
	 * @param id
	 * @param deptId
	 * @throws Exception
	 */
	public void insertWageCountSet(String id, String wageCountId);
	
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
