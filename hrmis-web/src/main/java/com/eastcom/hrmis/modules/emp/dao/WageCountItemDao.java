package com.eastcom.hrmis.modules.emp.dao;

import com.eastcom.baseframe.common.dao.TreeDao;
import com.eastcom.hrmis.modules.emp.entity.WageCountItem;

/**
 * 工资核算项DAO
 * @author wutingguang <br>
 */
public interface WageCountItemDao extends TreeDao<WageCountItem> {

	/**
	 * 根据id,parentId,sort进行更新
	 * 
	 * @param id
	 * @param parentId
	 * @param sort
	 * @throws Exception
	 */
	public void update(String id, String parentId, int sort) throws Exception;

}
