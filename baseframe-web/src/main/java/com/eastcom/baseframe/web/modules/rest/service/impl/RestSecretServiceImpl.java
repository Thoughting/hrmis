package com.eastcom.baseframe.web.modules.rest.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.baseframe.web.modules.rest.cache.RestResourceCache;
import com.eastcom.baseframe.web.modules.rest.dao.RestSecretDao;
import com.eastcom.baseframe.web.modules.rest.entity.RestResource;
import com.eastcom.baseframe.web.modules.rest.entity.RestSecret;
import com.eastcom.baseframe.web.modules.rest.service.RestSecretService;

@Service
@Transactional(value = "defaultTransactionManager")
public class RestSecretServiceImpl extends CrudServiceSupport<RestSecretDao, RestSecret> implements RestSecretService {

	@SuppressWarnings("unchecked")
	@Override
	public void updateAuth(Object restSecret, List<Object> resources)
			throws Exception {
		String secretId = (String) ((Map<String, Object>) restSecret).get("id");
		dao.deleteAllResourceAuth(secretId);
		//更新资源权限
		if (CollectionUtils.isNotEmpty(resources)) {
			for (Object resourceTemp : resources) {
				String resourceId = (String) ((Map<String, Object>) resourceTemp).get("id");
				dao.insertResourceAuth(secretId, resourceId);
			}
		}
	}

	@Override
	public void addAllResourceAuth(RestSecret restSecret) throws Exception{
		List<RestResource> resources = RestResourceCache.getList();
		if (CollectionUtils.isNotEmpty(resources)) {
			dao.deleteAllResourceAuth(restSecret.getId());
			for (RestResource restResource : resources) {
				dao.insertResourceAuth(restSecret.getId(), restResource.getId());
			}
		}
	}

}
