package com.eastcom.baseframe.web.modules.sys.service;

import java.util.List;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.baseframe.web.modules.sys.entity.Resource;

public interface ResourceService extends CrudService<Resource> {

	public void updateCascadeSort(List<Object> sortDatas) throws Exception;
	
	public void updateCascadeSort2(List<Object> sortDatas) throws Exception;

	/**
	 * 得到菜单树形图(ALL)
	 * 
	 * @return
	 */
	public List<Resource> findCascadeResourceTree(String parentId);

}
