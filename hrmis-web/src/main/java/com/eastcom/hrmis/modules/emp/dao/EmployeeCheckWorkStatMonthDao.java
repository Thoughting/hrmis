package com.eastcom.hrmis.modules.emp.dao;

import com.eastcom.baseframe.common.dao.Dao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeCheckWorkStatMonth;

import java.util.Date;

/**
 * 
 * 员工考勤统计DAO
 * @author wutingguang <br>
 */
public interface EmployeeCheckWorkStatMonthDao extends Dao<EmployeeCheckWorkStatMonth> {

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
	
}
