package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudTreeServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.WageCountItemDao;
import com.eastcom.hrmis.modules.emp.entity.WageCountItem;
import com.eastcom.hrmis.modules.emp.service.WageCountItemService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "defaultTransactionManager")
public class WageCountItemServiceImpl extends CrudTreeServiceSupport<WageCountItemDao, WageCountItem> implements WageCountItemService{

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
	public List<WageCountItem> findCascadeTree(String parentId) {
		Map<String, Object> reqParams = new HashMap<String, Object>();
		reqParams.put("parentId", parentId);
		List<WageCountItem> result = dao.find(reqParams);
		if (CollectionUtils.isNotEmpty(result)) {
			initializeCascadeChildren(result);
		}
		return result;
	}

}
