package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeCheckWorkDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeCheckWork;
import com.eastcom.hrmis.modules.emp.service.EmployeeCheckWorkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeCheckWorkServiceImpl extends CrudServiceSupport<EmployeeCheckWorkDao, EmployeeCheckWork> implements EmployeeCheckWorkService{

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeCheckWork> getCheckWorkMonthDate(String employeeId,Date date) {
		return dao.getCheckWorkMonthDate(employeeId, date);
	}

}
