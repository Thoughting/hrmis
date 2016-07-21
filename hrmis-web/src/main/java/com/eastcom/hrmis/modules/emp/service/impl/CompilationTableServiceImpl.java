package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.CompilationTableDao;
import com.eastcom.hrmis.modules.emp.entity.CompilationTable;
import com.eastcom.hrmis.modules.emp.service.CompilationTableService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "hrmisTransactionManager")
public class CompilationTableServiceImpl extends CrudServiceSupport<CompilationTableDao, CompilationTable> implements CompilationTableService{

	@SuppressWarnings("unchecked")
	@Override
	public void updateSetting(Object compilationTable, List<Object> depts,
			List<Object> posts) {
		String id = (String) ((Map<String, Object>) compilationTable).get("id");
		dao.deleteAllDeptSets(id);
		dao.deleteAllPostSets(id);
		//更新部门项目关联
		if (CollectionUtils.isNotEmpty(depts)) {
			for (Object temp : depts) {
				String deptId = (String) ((Map<String, Object>) temp).get("id");
				dao.insertDeptSet(id, deptId);
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
