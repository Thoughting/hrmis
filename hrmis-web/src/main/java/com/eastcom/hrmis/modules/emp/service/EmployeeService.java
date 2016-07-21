package com.eastcom.hrmis.modules.emp.service;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.hrmis.modules.emp.entity.Employee;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 员工档案信息Service
 * @author wutingguang <br>
 */
public interface EmployeeService extends CrudService<Employee> {

	/**
	 * 按部门与岗位统计员工数量（员工档案必须已通过审核,并且在职）
	 * @return
	 */
	public List<Map<String, Object>> getEmployeeDeptPostCount();
	
	/**
	 * 根据日期以及部门ID得到员工统计信息
	 * @param deptId
	 * @param date
	 * @return
	 */
	public Map<String, Object> getEmployeeStatByDeptIdAndDate(String deptId, Date date);

	/**
	 * 清理异常记录
	 */
	public void cleanAbnormalRecord();

}
