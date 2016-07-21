package com.eastcom.hrmis.modules.emp.service;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.hrmis.modules.emp.entity.CompilationTable;

import java.util.List;

/**
 * 员工编制表Service
 * @author wutingguang <br>
 */
public interface CompilationTableService extends CrudService<CompilationTable> {

	/**
	 * 更新员工编制(部门项目,岗位)
	 * @param compilationTable
	 * @param depts
	 * @param posts
	 * @throws Exception
	 */
	public void updateSetting(Object compilationTable, List<Object> depts, List<Object> posts);
	
}
