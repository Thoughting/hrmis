package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.hrmis.modules.emp.dao.LegalHolidayDao;
import com.eastcom.hrmis.modules.emp.entity.LegalHoliday;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class LegalHolidayDaoImpl extends DaoSupport<LegalHoliday> implements LegalHolidayDao{

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		if (checkParamsKey(params, "year")) {
			hql.append(" and year = :year ");
		}
		if (checkParamsKey(params, "holiday")) {
			params.put("holiday", DateUtils.parseDate(params.get("holiday")));
			hql.append(" and holiday = :holiday ");
		}
		hql.append(" order by holiday ");
	}
	
}
