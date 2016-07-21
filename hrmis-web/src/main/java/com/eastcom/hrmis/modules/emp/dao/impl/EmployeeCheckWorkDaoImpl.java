package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeCheckWorkDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeCheckWork;
import com.google.common.collect.Maps;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeCheckWorkDaoImpl extends DaoSupport<EmployeeCheckWork> implements EmployeeCheckWorkDao{

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<EmployeeCheckWork> getCheckWorkMonthDate(String employeeId,
			Date date) {
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
	
	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		if (checkParamsKey(params, "employeeId")) {
			hql.append(" and employee.id = :employeeId ");
		}
		if (checkParamsKey(params, "startWorkDate")) {
			params.put("startWorkDate", params.get("startWorkDate"));
			hql.append(" and workDate >= :startWorkDate ");
		}
		if (checkParamsKey(params, "endWorkDate")) {
			params.put("endWorkDate", params.get("endWorkDate"));
			hql.append(" and workDate < :endWorkDate ");
		}
		if (checkParamsKey(params, "workDate")) {
			params.put("workDate", params.get("workDate"));
			hql.append(" and workDate = :workDate ");
		}
		hql.append(" order by workDate desc");
	}

}
