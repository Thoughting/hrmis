package com.eastcom.hrmis.modules.emp.dao;

import com.eastcom.baseframe.common.dao.Dao;
import com.eastcom.hrmis.modules.emp.entity.Employee;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 员工信息DAO
 * @author wutingguang <br>
 */
public interface EmployeeDao extends Dao<Employee> {

	/**
	 * 按部门与岗位统计员工数量（员工档案必须已通过审核）
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
