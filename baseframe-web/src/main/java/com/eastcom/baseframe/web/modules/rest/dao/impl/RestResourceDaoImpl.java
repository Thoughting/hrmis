package com.eastcom.baseframe.web.modules.rest.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.eastcom.baseframe.web.common.dao.DataDaoSupport;
import com.eastcom.baseframe.web.modules.rest.dao.RestResourceDao;
import com.eastcom.baseframe.web.modules.rest.entity.RestResource;

@Repository
public class RestResourceDaoImpl extends DataDaoSupport<RestResource> implements RestResourceDao{

	@Override
	@Resource(name="defaultSessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
}
