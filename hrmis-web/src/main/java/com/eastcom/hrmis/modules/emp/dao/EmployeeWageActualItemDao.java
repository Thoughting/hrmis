package com.eastcom.hrmis.modules.emp.dao;

import com.eastcom.baseframe.common.dao.Dao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWage;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWageActualItem;
import com.eastcom.hrmis.modules.emp.entity.WageCountItem;

/**
 * 员工实发工资项DAO
 * @author wutingguang <br>
 */
public interface EmployeeWageActualItemDao extends Dao<EmployeeWageActualItem> {

	/**
	 * 根据员工工资条ID以及工资核算项编码得到员工实发工资项
	 * @param employeeWageId
	 * @param wageCountItemCode
	 * @return
	 */
	public EmployeeWageActualItem getByWageIdAndCountItemCode(String employeeWageId, String wageCountItemCode);
	
	/**
	 * 得到员工实发工资项
	 * @param employeeWage
	 * @param wageCountItem
	 * @return
	 */
	public EmployeeWageActualItem getWageActualItem(EmployeeWage employeeWage, WageCountItem wageCountItem, String wageCountItemCode);
	
}
