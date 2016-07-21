package com.eastcom.hrmis.modules.emp.service;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWageItem;

import java.util.Map;

/**
 * 员工工资项Service
 * @author wutingguang <br>
 */
public interface EmployeeWageItemService extends CrudService<EmployeeWageItem> {

	/**
	 * 更新影响项
	 * @param wageTemplate
	 */
	public void updateEffect(EmployeeWageItem wageItem);
	
	/**
	 * 得到员工工资模板配置（工资模板与员工数据配置合并）
	 * @param employee
	 * @return
	 */
	public Map<String, Object> getEmployeeWageTemplate(Employee employee);
	
}
