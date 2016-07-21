package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.CompilationCountDao;
import com.eastcom.hrmis.modules.emp.entity.CompilationCount;
import com.eastcom.hrmis.modules.emp.service.CompilationCountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "hrmisTransactionManager")
public class CompilationCountServiceImpl extends CrudServiceSupport<CompilationCountDao, CompilationCount> implements CompilationCountService{

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getLeaderAndDeptName(String compilationId) {
		return dao.getLeaderAndDeptName(compilationId);
	}

}
