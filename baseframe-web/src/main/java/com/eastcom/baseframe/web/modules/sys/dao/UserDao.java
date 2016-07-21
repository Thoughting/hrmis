package com.eastcom.baseframe.web.modules.sys.dao;

import com.eastcom.baseframe.web.modules.sys.entity.User;
import com.eastcom.baseframe.common.dao.Dao;

public interface UserDao extends Dao<User>{

	/**
	 * 删除用户下所有的部门
	 * @param userId
	 * @throws Exception
	 */
	public void deleteAllOfficeAuth(String userId) throws Exception;
	
	/**
	 * 插入关联部门
	 * @param userId
	 * @param officeId
	 * @throws Exception
	 */
	public void insertOfficeAuth(String userId, String officeId) throws Exception;
	
	/**
	 * 删除用户下所属角色
	 * @param userId
	 * @throws Exception
	 */
	public void deleteAllRoleAuth(String userId) throws Exception;
	
	/**
	 * 插入关联角色
	 * @param userId
	 * @param roleId
	 * @throws Exception
	 */
	public void insertRoleAuth(String userId, String roleId) throws Exception;
	
}
