package com.eastcom.baseframe.web.modules.sys.dao;

import com.eastcom.baseframe.common.dao.TreeDao;
import com.eastcom.baseframe.web.modules.sys.entity.Dict;

/**
 * 字典DAO
 * @author wutingguang <br>
 */
public interface DictDao extends TreeDao<Dict>{

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
