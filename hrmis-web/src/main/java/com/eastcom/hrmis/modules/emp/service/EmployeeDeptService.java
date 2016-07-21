package com.eastcom.hrmis.modules.emp.service;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDept;

import java.util.List;

/**
 * 职务部门Service
 * @author wutingguang <br>
 */
public interface EmployeeDeptService extends CrudService<EmployeeDept> {

	/**
	 * 得到部门员工数
	 * @param id
	 * @return
	 */
	public int getEmployeeCountByDeptId(String id);
	
	/**
	 * 更新部门用户权限(资源)
	 * @param role
	 * @param menus
	 * @param areas
	 * @throws Exception
	 */
	public void updateAuth(String userId, List<Object> resources) throws Exception;
	
	/**
	 * 根据用户id得到部门清单
	 * 
	 * @param id
	 * @return
	 */
	public List<EmployeeDept> getAuthDeptByUserId(String userId);
	
}
