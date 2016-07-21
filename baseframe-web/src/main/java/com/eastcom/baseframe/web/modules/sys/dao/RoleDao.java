package com.eastcom.baseframe.web.modules.sys.dao;

import java.util.List;

import com.eastcom.baseframe.web.modules.sys.entity.Role;
import com.eastcom.baseframe.common.dao.Dao;

public interface RoleDao extends Dao<Role>{
	
	/**
	 * 根据角色ID得到权限值清单
	 * @param roleId
	 * @return
	 */
	public List<String> getPermissionListByRoleId(String roleId);
	
	/**
	 * 根据角色IDs得到权限值清单
	 * @param roleId
	 * @return
	 */
	public List<String> getPermissionListByRoleId(List<Role> roleList);
	
	/**
	 * 删除角色下所有的资源权限
	 * @param roleId
	 * @throws Exception
	 */
	public void deleteAllMenuAuth(String roleId) throws Exception;
	
	/**
	 * 插入资源权限
	 * @param roleId
	 * @param menuId
	 * @throws Exception
	 */
	public void insertMenuAuth(String roleId, String menuId) throws Exception;
	
	/**
	 * 删除角色下所有的区域权限
	 * @param roleId
	 * @throws Exception
	 */
	public void deleteAllAreaAuth(String roleId) throws Exception;
	
	/**
	 * 插入区域权限
	 * @param roleId
	 * @param menuId
	 * @throws Exception
	 */
	public void insertAreaAuth(String roleId, String areaId) throws Exception;
	
}
