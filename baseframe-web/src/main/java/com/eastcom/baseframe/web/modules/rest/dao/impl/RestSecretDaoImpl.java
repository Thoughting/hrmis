package com.eastcom.baseframe.web.modules.rest.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.eastcom.baseframe.web.common.dao.DataDaoSupport;
import com.eastcom.baseframe.web.modules.rest.dao.RestSecretDao;
import com.eastcom.baseframe.web.modules.rest.entity.RestSecret;

@Repository
public class RestSecretDaoImpl extends DataDaoSupport<RestSecret> implements RestSecretDao{

	@Override
	@Resource(name="defaultSessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public void deleteAllResourceAuth(String secretId) throws Exception {
		executeBySql(" delete from t_rest_secret_resource where FK_REST_SECRET_ID = ?",new Object[]{secretId});		
	}

	@Override
	public void insertResourceAuth(String secretId, String resourceId)
			throws Exception {
		executeBySql(" INSERT INTO t_rest_secret_resource(FK_REST_SECRET_ID, FK_REST_RESOURCE_ID) VALUES(?,?)",new Object[]{secretId, resourceId});
	}
	
}
