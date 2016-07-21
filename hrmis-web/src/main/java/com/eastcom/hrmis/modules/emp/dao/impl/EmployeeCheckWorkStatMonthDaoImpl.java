package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.hrmis.modules.emp.dao.EmployeeCheckWorkStatMonthDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeCheckWorkStatMonth;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeCheckWorkStatMonthDaoImpl extends DaoSupport<EmployeeCheckWorkStatMonth> implements EmployeeCheckWorkStatMonthDao {

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public EmployeeCheckWorkStatMonth findByEmployeeIdAndStatMonth(
			String employeeId, Date statDate) {
		String statMonth = DateUtils.formatDate(statDate, "yyyy年-MM月");
		return findByEmployeeIdAndStatMonth(employeeId, statMonth);
	}
	
	
	@Override
	public EmployeeCheckWorkStatMonth findByEmployeeIdAndStatMonth(
			String employeeId, String statMonth) {
		EmployeeCheckWorkStatMonth checkWorkStatMonth = null;
		Map<String, Object> reqParam = Maps.newHashMap();
		reqParam.put("employeeId", employeeId);
		reqParam.put("statMonth", statMonth);
		List<EmployeeCheckWorkStatMonth> checkWorkStatMonths = find(reqParam);
		if (CollectionUtils.isNotEmpty(checkWorkStatMonths)) {
			checkWorkStatMonth = checkWorkStatMonths.get(0);
		}
		return checkWorkStatMonth;
	}
	
	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		if (checkParamsKey(params, "employeeId")) {
			hql.append(" and employee.id = :employeeId ");
		}
		if (checkParamsKey(params, "statMonth")) {
			hql.append(" and statMonth = :statMonth ");
		}
	}

}
