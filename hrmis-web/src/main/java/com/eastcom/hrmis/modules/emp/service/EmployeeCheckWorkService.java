package com.eastcom.hrmis.modules.emp.service;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.hrmis.modules.emp.entity.EmployeeCheckWork;

import java.util.Date;
import java.util.List;

/**
 * 员工考勤Service
 * @author wutingguang <br>
 */
public interface EmployeeCheckWorkService extends CrudService<EmployeeCheckWork> {

	/**
	 * 根据日期得到其一个月的考勤记录
	 * @param data
	 * @return
	 */
	public List<EmployeeCheckWork> getCheckWorkMonthDate(String employeeId, Date date);

}
