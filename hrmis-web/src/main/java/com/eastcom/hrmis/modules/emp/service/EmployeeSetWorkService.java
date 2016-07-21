package com.eastcom.hrmis.modules.emp.service;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.hrmis.modules.emp.entity.EmployeeSetWork;

import java.util.Date;
import java.util.List;

/**
 * 员工排班Service
 * @author wutingguang <br>
 */
public interface EmployeeSetWorkService extends CrudService<EmployeeSetWork> {

	/**
	 * 根据日期得到其一个月的排班记录
	 * @param data
	 * @return
	 */
	public List<EmployeeSetWork> getSetWorkMonthDate(String employeeId, Date date);
	
}
