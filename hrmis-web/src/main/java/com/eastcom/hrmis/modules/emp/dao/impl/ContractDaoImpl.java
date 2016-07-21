package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.hrmis.modules.emp.dao.ContractDao;
import com.eastcom.hrmis.modules.emp.entity.Contract;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class ContractDaoImpl extends DaoSupport<Contract> implements ContractDao{

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		if (checkParamsKey(params, "code")) {
			params.put("code", "%" + params.get("code") + "%");
			hql.append(" and code like :code ");
		}
		if (checkParamsKey(params, "name")) {
			params.put("name", "%" + params.get("name") + "%");
			hql.append(" and name like :name ");
		}
		if (checkParamsKey(params, "keyDesr")) {
			params.put("keyDesr", "%" + params.get("keyDesr") + "%");
			hql.append(" and keyDesr like :keyDesr ");
		}
		if (checkParamsKey(params, "type")) {
			hql.append(" and type = :type ");
		}
		hql.append(" order by createDate desc");
	}
}
