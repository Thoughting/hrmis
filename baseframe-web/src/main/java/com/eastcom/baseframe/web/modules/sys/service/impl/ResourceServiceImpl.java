package com.eastcom.baseframe.web.modules.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.baseframe.web.modules.sys.dao.ResourceDao;
import com.eastcom.baseframe.web.modules.sys.entity.Resource;
import com.eastcom.baseframe.web.modules.sys.service.ResourceService;

@Service
@Transactional(value = "defaultTransactionManager")
public class ResourceServiceImpl extends CrudServiceSupport<ResourceDao, Resource> implements ResourceService{

	@SuppressWarnings("unchecked")
	@Override
	public void updateCascadeSort(List<Object> sortDatas) throws Exception {
		for (Object item : sortDatas) {
			Map<String, Object> map = (Map<String, Object>) item;
			String id = (String) map.get("id");
			String parentId = (String) map.get("parentId");
			int sort = (Integer ) map.get("sort");
			dao.update(id, parentId, sort);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateCascadeSort2(List<Object> sortDatas) throws Exception {
		for (Object item : sortDatas) {
			Map<String, Object> map = (Map<String, Object>) item;
			String id = (String) map.get("id");
			String parentId = (String) map.get("parentId");
			int sort = (Integer ) map.get("sort");
			int level = 1;
			if(parentId !=null && parentId.length()>0){
				Resource parentResource = this.get(parentId);
				if(parentResource != null){
					level = parentResource.getLevel()+1;
				}
			}
			dao.update2(id, parentId, sort, level);
		}
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Resource> findCascadeResourceTree(String parentId) {
		Map<String, Object> reqParams = new HashMap<String, Object>();
		reqParams.put("parentId", parentId);
		List<Resource> resources = dao.find(reqParams);
		if (CollectionUtils.isNotEmpty(resources)) {
			initializeCascadeChildren(resources);
		}
		return resources;
	}

	/**
	 * 递归查询子节点
	 * @param children
	 */
	private void initializeCascadeChildren(List<Resource> resources){
		for (Resource resource : resources) {
			Hibernate.initialize(resource.getChildList());
			if (CollectionUtils.isNotEmpty(resource.getChildList())) {
				initializeCascadeChildren(resource.getChildList());
			}
		}
	}

	
}
