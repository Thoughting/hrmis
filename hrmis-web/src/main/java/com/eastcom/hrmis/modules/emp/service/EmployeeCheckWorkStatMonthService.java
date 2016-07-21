package com.eastcom.hrmis.modules.emp.service;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.eastcom.hrmis.modules.emp.entity.EmployeeCheckWorkStatMonth;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 员工考勤统计Service
 * @author wutingguang <br>
 */
public interface EmployeeCheckWorkStatMonthService extends CrudService<EmployeeCheckWorkStatMonth> {

	/**
	 * 根据员工ID与统计月份得到统计信息
	 * @param employeeId
	 * @param statMonth
	 * @return
	 */
	public EmployeeCheckWorkStatMonth findByEmployeeIdAndStatMonth(String employeeId, String statMonth);

	/**
	 * 根据员工ID与统计月份得到统计信息
	 * @param employeeId
	 * @param statMonth
	 * @return
	 */
	public EmployeeCheckWorkStatMonth findByEmployeeIdAndStatMonth(String employeeId, Date statDate);
	
	/**
	 * 根据部门ID与统计月份得到部门下员工考勤统计信息
	 * @param deptId
	 * @param statMonth
	 * @return
	 */
	public List<Map<String, Object>> getEmployeeCheckWorkStatByDeptIdAndStatMonth(String deptId, String statMonth);
	
	/**
	 * 根据统计月份得到考勤统计信息，图表
	 * 
	 * @param statMonth
	 * @return
	 */
	public List<Map<String, Object>> getDeptCheckWorkStatByStatMonth(String statMonth);
	
	/**
	 * 刷新员工考勤统计--做全员考勤统计的时候，需要对所选月份
	 * @param statMonth
	 * @throws Exception
	 */
	public void refreshCheckWorkStatMonth(String statMonth) throws Exception;
	
	/**
	 * 刷新员工考勤统计信息
	 * @param employee
	 * @param statMonth
	 */
	public EmployeeCheckWorkStatMonth refreshCheckWorkStatMonth(Employee employee, String statMonth) throws Exception;
	
	/**
	 * 根据部门ID删除部门下员工统计信息
	 * 一般用于部门变更之后，需要做重新统计（部门工作时长）
	 * 
	 * @param deptId
	 */
	public void deleteByDeptId(String deptId);
	
	/**
	 * 根据员工ID删除该员工下所有统计信息
	 * 一般用于员工信息变更之后，为防止其部门变更导致部门工作时长变更，须重新统计
	 * @param employeeId
	 */
	public void deleteByEmployeeId(String employeeId);
	
}
