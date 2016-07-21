package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.hrmis.modules.emp.dao.PerformanceWageItemDao;
import com.eastcom.hrmis.modules.emp.entity.PerformanceWageItem;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class PerformanceWageItemDaoImpl extends DaoSupport<PerformanceWageItem> implements PerformanceWageItemDao {

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		if (checkParamsKey(params, "performanceWageId")) {
			hql.append(" and performanceWage.id = :performanceWageId ");
		}
	}
}
