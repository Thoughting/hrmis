package com.eastcom.baseframe.web.modules.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom.baseframe.common.service.CrudTreeServiceSupport;
import com.eastcom.baseframe.web.modules.sys.dao.AreaDao;
import com.eastcom.baseframe.web.modules.sys.entity.Area;
import com.eastcom.baseframe.web.modules.sys.service.AreaService;

@Service
@Transactional(value = "defaultTransactionManager")
public class AreaServiceImpl extends CrudTreeServiceSupport<AreaDao, Area> implements AreaService{

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

	@Override
	public List<Area> findCascadeTree(String parentId) {
		Map<String, Object> reqParams = new HashMap<String, Object>();
		reqParams.put("parentId", parentId);
		List<Area> result = dao.find(reqParams);
		if (CollectionUtils.isNotEmpty(result)) {
			initializeCascadeChildren(result);
		}
		return result;
	}
	
}
