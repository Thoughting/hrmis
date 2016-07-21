package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.hrmis.modules.emp.dao.PerformanceWageDao;
import com.eastcom.hrmis.modules.emp.entity.PerformanceWage;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class PerformanceWageDaoImpl extends DaoSupport<PerformanceWage> implements PerformanceWageDao{

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		if (checkParamsKey(params, "employeeId")) {
			hql.append(" and employee.id = :employeeId ");
		}
		if (checkParamsKey(params, "type")) {
			hql.append(" and type = :type ");
		}
		if (checkParamsKey(params, "wageDateStr")) {
			hql.append(" and wageDateStr = :wageDateStr ");
		}
	}
	
}
