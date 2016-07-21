package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.web.common.dao.DataDaoSupport;
import com.eastcom.hrmis.modules.emp.cache.EmployeeDeptCache;
import com.eastcom.hrmis.modules.emp.dao.EmployeeOrderDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeOrder;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeOrderDaoImpl extends DataDaoSupport<EmployeeOrder> implements EmployeeOrderDao {

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
			params.put("type", Integer.parseInt("" + params.get("type")));
			hql.append(" and type = :type ");
		}
		if (checkParamsKey(params, "status")) {
			params.put("status", Integer.parseInt("" + params.get("status")));
			hql.append(" and status = :status ");
		}
		if (checkParamsKey(params, "createBy")) {
			params.put("createBy", "%" + params.get("createBy") + "%");
			hql.append(" and createBy like :createBy ");
		}
		
		//根据授权部门进行过滤
		if ("all".equals((String) params.get("employeeDept"))) {
			params.remove("employeeDept");
		} else {
			List<String> authDeptIds = EmployeeDeptCache.getAuthDeptIdsByLoginUser();
			if (CollectionUtils.isNotEmpty(authDeptIds)) {
				params.put("authDeptIds", authDeptIds);
				hql.append(" and employee.employeeDept.id in ( :authDeptIds ) ");
			}
		}
		
		//根据工单类型进行过滤
		if (checkParamsKey(params, "orderTypes")) {
			hql.append(" and type in ( :orderTypes ) ");
		}
		
		hql.append(" order by updateDate desc");
	}
	
}
