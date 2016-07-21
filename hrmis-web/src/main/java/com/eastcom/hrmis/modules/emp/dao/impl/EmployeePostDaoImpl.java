package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeePostDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeePost;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class EmployeePostDaoImpl extends DaoSupport<EmployeePost> implements EmployeePostDao {

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		if (checkParamsKey(params, "code_exact")) {
			hql.append(" and code = :code_exact ");
		}
		
		if (checkParamsKey(params, "code")) {
			params.put("code", "%" + params.get("code") + "%");
			hql.append(" and code like :code ");
		}
		if (checkParamsKey(params, "name")) {
			params.put("name", "%" + params.get("name") + "%");
			hql.append(" and name like :name ");
		}
		if (checkParamsKey(params, "remark")) {
			params.put("remark", "%" + params.get("remark") + "%");
			hql.append(" and remark like :remark ");
		}
		if (checkParamsKey(params, "type")) {
			hql.append(" and type = :type ");
		}
		
		hql.append(" order by code ");
	}
	
}
