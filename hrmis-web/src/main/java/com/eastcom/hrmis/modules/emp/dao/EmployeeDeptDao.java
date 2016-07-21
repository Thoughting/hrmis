package com.eastcom.hrmis.modules.emp.dao;

import com.eastcom.baseframe.common.dao.Dao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDept;

import java.util.List;

/**
 * 职务部门Dao
 * @author wutingguang <br>
 */
public interface EmployeeDeptDao extends Dao<EmployeeDept> {

	/**
	 * 得到部门员工数
	 * @param id
	 * @return
	 */
	public int getEmployeeCountByDeptId(String id);
	
	/**
	 * 根据用户id得到部门清单
	 * 
	 * @param id
	 * @return
	 */
	public List<EmployeeDept> getAuthDeptByUserId(String id);
	
	/**
	 * 删除该停车场下所有的系统用户权限
	 * @param roleId
	 * @throws Exception
	 */
	public void deleteAllDeptsAuth(String id);
	
	/**
	 * 插入资源权限
	 * @param roleId
	 * @param menuId
	 * @throws Exception
	 */
	public void insertDeptAuth(String id, String userId);
	
}
