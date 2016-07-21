package com.eastcom.baseframe.web.modules.sys.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.baseframe.web.modules.sys.dao.DynamicgridDao;
import com.eastcom.baseframe.web.modules.sys.entity.Dynamicgrid;

@Repository
public class DynamicgridDaoImpl extends DaoSupport<Dynamicgrid> implements DynamicgridDao{

	@Override
	@Resource(name="defaultSessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}


	
}
