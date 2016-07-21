package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeSetWorkDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeSetWork;
import com.eastcom.hrmis.modules.emp.service.EmployeeSetWorkService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeSetWorkServiceImpl extends CrudServiceSupport<EmployeeSetWorkDao, EmployeeSetWork> implements EmployeeSetWorkService {

	@SuppressWarnings("deprecation")
	@Override
	public List<EmployeeSetWork> getSetWorkMonthDate(String employeeId,Date date) {
		date.setDate(1);
		Date startWorkDate = new Date(date.getTime());
		date.setMonth(date.getMonth() + 1);
		Date endWorkDate = new Date(date.getTime());
		
		Map<String, Object> reqParam = Maps.newHashMap();
		reqParam.put("startWorkDate", startWorkDate);
		reqParam.put("endWorkDate", endWorkDate);
		reqParam.put("employeeId", employeeId);
		return find(reqParam);
	}

}
