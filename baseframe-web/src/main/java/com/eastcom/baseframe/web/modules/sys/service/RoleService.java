package com.eastcom.baseframe.web.modules.sys.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.baseframe.web.modules.sys.entity.Role;

public interface RoleService extends CrudService<Role>{

	public Role getWithCascadeById(Serializable id);
	
	public List<Role> findWithCascade(Map<String, Object> params);
	
	/**
	 * 更新角色权限(菜单,区域)
	 * @param role
	 * @param menus
	 * @param areas
	 * @throws Exception
	 */
	public void updateAuth(Object role, List<Object> menus, List<Object> areas) throws Exception;
	
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
	
}
