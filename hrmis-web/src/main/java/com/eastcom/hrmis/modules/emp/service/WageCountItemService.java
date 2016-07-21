package com.eastcom.hrmis.modules.emp.service;

import com.eastcom.baseframe.common.service.CrudTreeService;
import com.eastcom.hrmis.modules.emp.entity.WageCountItem;

import java.util.List;

/**
 * 工资核算Service
 * @author wutingguang <br>
 */
public interface WageCountItemService extends CrudTreeService<WageCountItem> {

	public void updateCascadeSort(List<Object> sortDatas) throws Exception;

	/**
	 * 得到字典树形图(ALL)
	 * 
	 * @return
	 */
	public List<WageCountItem> findCascadeTree(String parentId);
}
