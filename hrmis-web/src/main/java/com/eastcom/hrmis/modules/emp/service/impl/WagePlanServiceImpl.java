package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.WagePlanDao;
import com.eastcom.hrmis.modules.emp.entity.WagePlan;
import com.eastcom.hrmis.modules.emp.service.WagePlanService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "hrmisTransactionManager")
public class WagePlanServiceImpl extends CrudServiceSupport<WagePlanDao, WagePlan> implements WagePlanService {

	@SuppressWarnings("unchecked")
	@Override
	public void updateSetting(Object wagePlan, List<Object> wageCounts,
			List<Object> posts) {
		String id = (String) ((Map<String, Object>) wagePlan).get("id");
		dao.deleteAllWageCountSets(id);
		dao.deleteAllPostSets(id);
		//更新工资核算项关联
		if (CollectionUtils.isNotEmpty(wageCounts)) {
			for (Object temp : wageCounts) {
				String wageCountId = (String) ((Map<String, Object>) temp).get("id");
				dao.insertWageCountSet(id, wageCountId);
			}
		}
		//更新岗位关联
		if (CollectionUtils.isNotEmpty(posts)) {
			for (Object temp : posts) {
				String postId = (String) ((Map<String, Object>) temp).get("id");
				dao.insertPostSet(id, postId);
			}
		}
	}

}
