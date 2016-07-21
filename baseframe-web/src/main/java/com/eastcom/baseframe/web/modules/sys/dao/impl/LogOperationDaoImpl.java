package com.eastcom.baseframe.web.modules.sys.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.modules.sys.dao.LogOperationDao;
import com.eastcom.baseframe.web.modules.sys.entity.LogOperation;

@Repository
public class LogOperationDaoImpl extends DaoSupport<LogOperation> implements
		LogOperationDao {
	
	@Override
	@Resource(name = "defaultSessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		String loginName = (String) params.get("loginName");
		if (StringUtils.isNotBlank(loginName)) {
			hql.append(" and loginName like :loginName ");
		} else {
			params.remove("loginName");
		}
		String startTime = (String) params.get("startTime");
		if (StringUtils.isNotBlank(startTime)) {
			hql.append(" and createDate >= '" + startTime + "' ");
		}
		params.remove("startTime");
		String endTime = (String) params.get("endTime");
		if (StringUtils.isNotBlank(endTime)) {
			hql.append(" and createDate <= '" + endTime + "' ");
		}
		params.remove("endTime");

		hql.append(" and menuItem is not null and menuItem <> '' order by createDate desc ");
	}
	
}
