package com.eastcom.baseframe.web.modules.rest.dao;

import com.eastcom.baseframe.common.dao.Dao;
import com.eastcom.baseframe.web.modules.rest.entity.RestSecret;

/**
 * rest secret dao
 * 
 * @author wutingguang <br>
 */
public interface RestSecretDao extends Dao<RestSecret>{

	/**
	 * 删除密钥下所有的资源权限
	 * @param roleId
	 * @throws Exception
	 */
	public void deleteAllResourceAuth(String secretId) throws Exception;
	
	/**
	 * 插入资源权限
	 * @param roleId
	 * @param menuId
	 * @throws Exception
	 */
	public void insertResourceAuth(String secretId, String resourceId) throws Exception;
	
}
