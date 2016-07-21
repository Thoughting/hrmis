package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.hrmis.modules.emp.dao.PerformanceWageDao;
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.eastcom.hrmis.modules.emp.entity.PerformanceWage;
import com.eastcom.hrmis.modules.emp.service.PerformanceWageService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "hrmisTransactionManager")
public class PerformanceWageServiceImpl extends CrudServiceSupport<PerformanceWageDao, PerformanceWage> implements PerformanceWageService {

	@Override
	public void refreshPerformanceWage(Employee employee) {
		refreshPersormanceWage(employee, DateUtils.formatDate(new Date(), "yyyy年-MM月"));
	}

	@Override
	public void refreshPersormanceWage(Employee employee, String wageDateStr) {
		PerformanceWage performanceWage = null;
		
		Map<String, Object> reqParam = Maps.newHashMap();
		reqParam.put("employeeId", employee.getId());
		reqParam.put("wageDateStr", wageDateStr);
		List<PerformanceWage> performanceWages = find(reqParam);
		if (performanceWages != null && performanceWages.size() == 1) {
			performanceWage = performanceWages.get(0);
			if (employee.getPerformanceWageType() != performanceWage.getType()) {
				dao.delete(performanceWages);
				performanceWage = null;
			}
		}
		
		if (employee.getPerformanceWageType() != 0 && performanceWage == null) {
			performanceWage = new PerformanceWage();
			performanceWage.setEmployee(employee);
			performanceWage.setType(employee.getPerformanceWageType());
			performanceWage.setWageDateStr(wageDateStr);
			dao.saveOrUpdate(performanceWage);
		}
		
	}

}
