package com.eastcom.baseframe.web.modules.sys.dao;

import java.util.List;

import com.eastcom.baseframe.common.dao.Dao;
import com.eastcom.baseframe.web.modules.sys.entity.Resource;

public interface ResourceDao extends Dao<Resource>{

	/**
	 * 根据id,parentId,sort进行更新
	 * @param id
	 * @param parentId
	 * @param sort
	 * @throws Exception
	 */
	public void update(String id, String parentId, int sort) throws Exception;
	
	/*
	 * 根据id,parentId,sort,level进行更新
	 */
	public void update2(String id, String parentId, int sort, int level) throws Exception;
	
	/**
	 * 根据用户Id得到其授权的菜单清单
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Resource> findByUserId(String userId) throws Exception;
	
}
