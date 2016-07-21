package com.eastcom.hrmis.modules.emp.dao;

import com.eastcom.baseframe.common.dao.Dao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeCheckWork;

import java.util.Date;
import java.util.List;

/**
 * 员工考勤DAO
 * @author wutingguang <br>
 */
public interface EmployeeCheckWorkDao extends Dao<EmployeeCheckWork> {

	/**
	 * 得到该员工当月考勤记录
	 * @param employeeId
	 * @param date
	 * @return
	 */
	public List<EmployeeCheckWork> getCheckWorkMonthDate(String employeeId, Date date);
	
}
