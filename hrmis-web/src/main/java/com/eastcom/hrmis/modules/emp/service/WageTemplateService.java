package com.eastcom.hrmis.modules.emp.service;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.hrmis.modules.emp.entity.WageTemplate;

/**
 * 工资数据模板Service
 * @author wutingguang <br>
 */
public interface WageTemplateService extends CrudService<WageTemplate> {

	/**
	 * 更新模板影响项
	 * @param wageTemplate
	 */
	public void updateEffect(WageTemplate wageTemplate);
	
}
