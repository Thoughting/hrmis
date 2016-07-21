package com.eastcom.hrmis.modules.emp.service;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.hrmis.modules.emp.entity.WagePlan;

import java.util.List;

/**
 * 工资方案Service
 * @author wutingguang <br>
 */
public interface WagePlanService extends CrudService<WagePlan> {

	/**
	 * 更新方案关联设置(工资核算项,岗位)
	 * @param compilationTable
	 * @param depts
	 * @param posts
	 * @throws Exception
	 */
	public void updateSetting(Object wagePlan, List<Object> wageCounts, List<Object> posts);
	
}
