package com.eastcom.baseframe.web.modules.sys.service;

import java.util.List;

import com.eastcom.baseframe.common.service.CrudTreeService;
import com.eastcom.baseframe.web.modules.sys.entity.DynamicgridItem;

/**
 * 表格项Service
 * @author wutingguang <br>
 */
public interface DynamicgridItemService extends CrudTreeService<DynamicgridItem>{
	
	public void updateCascadeSort(List<Object> sortDatas) throws Exception;

	/**
	 * 得到字典树形图(ALL)
	 * 
	 * @return
	 */
	public List<DynamicgridItem> findCascadeTree(String dynamicgridId, String parentId);
	
}
