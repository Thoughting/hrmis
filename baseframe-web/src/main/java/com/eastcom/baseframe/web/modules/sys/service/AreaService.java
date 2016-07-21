package com.eastcom.baseframe.web.modules.sys.service;

import java.util.List;

import com.eastcom.baseframe.common.service.CrudTreeService;
import com.eastcom.baseframe.web.modules.sys.entity.Area;

public interface AreaService extends CrudTreeService<Area>{

	public void updateCascadeSort(List<Object> sortDatas) throws Exception;

	/**
	 * 得到字典树形图(ALL)
	 * 
	 * @return
	 */
	public List<Area> findCascadeTree(String parentId);
	
}
