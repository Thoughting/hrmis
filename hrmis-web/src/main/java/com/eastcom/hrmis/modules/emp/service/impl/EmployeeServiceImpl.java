package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeDao;
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.eastcom.hrmis.modules.emp.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeServiceImpl extends CrudServiceSupport<EmployeeDao, Employee> implements EmployeeService {

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getEmployeeDeptPostCount() {
		return dao.getEmployeeDeptPostCount();
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getEmployeeStatByDeptIdAndDate(String deptId,
			Date date) {
		return dao.getEmployeeStatByDeptIdAndDate(deptId, date);
	}

	@Override
	public void cleanAbnormalRecord() {
		dao.cleanAbnormalRecord();
	}
}
