package com.eastcom.hrmis.modules.emp.service;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.hrmis.modules.emp.entity.CompilationCount;

import java.util.List;
import java.util.Map;

/**
 * 编制数Service
 * @author wutingguang <br>
 */
public interface CompilationCountService extends CrudService<CompilationCount> {

	/**
	 * 根据编制表ID得到起配置的部门以及分管领导名称
	 * @param compilationId
	 * @return
	 */
	public List<Map<String, Object>> getLeaderAndDeptName(String compilationId);
	
}
