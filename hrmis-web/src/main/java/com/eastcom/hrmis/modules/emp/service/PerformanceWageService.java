package com.eastcom.hrmis.modules.emp.service;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.eastcom.hrmis.modules.emp.entity.PerformanceWage;

/**
 * 员工绩效工资Service
 * @author wutingguang <br>
 */
public interface PerformanceWageService extends CrudService<PerformanceWage> {

	/**
	 * 刷新当前员工绩效工资
	 * 
	 * @param employeeId
	 */
	public void refreshPerformanceWage(Employee employee);
	
	/**
	 * 刷新当前员工绩效工资--绩效工资日期
	 * 
	 * @param employeeId
	 * @param wageDate
	 */
	public void refreshPersormanceWage(Employee employee, String wageDateStr);
	
}
