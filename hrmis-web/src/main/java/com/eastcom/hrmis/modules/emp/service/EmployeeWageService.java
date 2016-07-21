package com.eastcom.hrmis.modules.emp.service;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWage;

import java.util.Map;

/**
 * 员工实发工资Service
 * @author wutingguang <br>
 */
public interface EmployeeWageService extends CrudService<EmployeeWage> {

	/**
	 * 实时计算员工工资信息,员工工资须为未审核状态
	 * 
	 * @param employeeWage
	 * @param items
	 * @return
	 */
	public EmployeeWage realStatEmployeeWageInfo(EmployeeWage employeeWage, Map<String, Object> itemMap) throws Exception;
	
}
