package com.eastcom.hrmis.modules.emp.service;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWage;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWageHis;
import com.eastcom.hrmis.modules.emp.entity.WagePlan;

import java.util.List;
import java.util.Map;

/**
 * 员工工资统计历史Service
 * 
 * @author wtg
 *
 */
public interface EmployeeWageHisService extends CrudService<EmployeeWageHis> {

	/**
	 * 得到工资计划（用于构建tab）
	 * 
	 * @param params
	 * @return
	 */
	public List<WagePlan> getWagePlanInfos(Map<String, Object> params);
	
	/**
	 * 将员工工资统计放到历史表
	 * 
	 * @param wage
	 */
	public void cloneByWage(EmployeeWage wage) throws Exception;
	
}
