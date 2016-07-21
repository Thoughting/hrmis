package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeWageHisDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWageHis;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class EmployeeWageHisDaoImpl extends DaoSupport<EmployeeWageHis> implements EmployeeWageHisDao {

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		if (checkParamsKey(params, "employeeName")) {
			params.put("employeeName", "%" + params.get("employeeName") + "%");
			hql.append(" and employee.name like :employeeName ");
		}
		if (checkParamsKey(params, "deptName")) {
			if ("--请选择--".equals("" + params.get("deptName"))) {
				params.remove("deptName");
			} else {
				hql.append(" and deptName = :deptName ");
			}
		}
		if (checkParamsKey(params, "postName")) {
			hql.append(" and postName = :postName ");
		}
		
		if (checkParamsKey(params, "start_wageDate")) {
			hql.append(" and wageDateStr >= :start_wageDate ");
		}
		if (checkParamsKey(params, "end_wageDate")) {
			hql.append(" and wageDateStr <= :end_wageDate ");
		}
		
		if (checkParamsKey(params, "wageDate")) {
			hql.append(" and wageDateStr = :wageDate ");
		}
		
		if (checkParamsKey(params, "wagePlanId")) {
			hql.append(" and wagePlan.id = :wagePlanId ");
		}
		
	}
	
}
