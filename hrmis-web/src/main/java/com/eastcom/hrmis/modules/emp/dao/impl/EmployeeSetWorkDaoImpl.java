package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeSetWorkDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeSetWork;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class EmployeeSetWorkDaoImpl extends DaoSupport<EmployeeSetWork> implements EmployeeSetWorkDao{

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
		if (checkParamsKey(params, "startWorkDate")) {
			params.put("startWorkDate", params.get("startWorkDate"));
			hql.append(" and workDate >= :startWorkDate ");
		}
		if (checkParamsKey(params, "endWorkDate")) {
			params.put("endWorkDate", params.get("endWorkDate"));
			hql.append(" and workDate <= :endWorkDate ");
		}
		if (checkParamsKey(params, "workDate")) {
			params.put("workDate", params.get("workDate"));
			hql.append(" and workDate = :workDate ");
		}
		hql.append(" order by workDate desc");
	}
	
}
