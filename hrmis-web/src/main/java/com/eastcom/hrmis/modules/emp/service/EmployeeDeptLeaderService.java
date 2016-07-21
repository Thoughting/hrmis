package com.eastcom.hrmis.modules.emp.service;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDeptLeader;

import java.util.List;

/**
 * 分管部门领导Service
 * @author wutingguang <br>
 */
public interface EmployeeDeptLeaderService extends CrudService<EmployeeDeptLeader> {

	public void saveOrUpdate(EmployeeDeptLeader deptLeader, List<Object> checkDeptNodes);
	
}
