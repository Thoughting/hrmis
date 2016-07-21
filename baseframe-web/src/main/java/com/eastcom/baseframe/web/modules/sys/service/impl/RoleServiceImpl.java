package com.eastcom.baseframe.web.modules.sys.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.baseframe.web.modules.sys.dao.RoleDao;
import com.eastcom.baseframe.web.modules.sys.entity.Role;
import com.eastcom.baseframe.web.modules.sys.service.RoleService;

@Service
@Transactional(value = "defaultTransactionManager")
public class RoleServiceImpl extends CrudServiceSupport<RoleDao, Role>
		implements RoleService {

	@Override
	@Transactional(readOnly = true)
	public Role getWithCascadeById(Serializable id) {
		Role role = dao.getById(id);
		if (role != null) {
			Hibernate.initialize(role.getResourceList());
			Hibernate.initialize(role.getAreaList());
			Hibernate.initialize(role.getUserList());
		}
		return role;
	}
	
	@OperationLog(content = "新增角色信息数据", type = OperationType.CREATE)
	@Override
	public Serializable save(Role t) {
		return dao.save(t);
	}


	@OperationLog(content = "修改角色信息数据", type = OperationType.MODIFY)
	@Override
	public void saveOrUpdate(Role t) {
		dao.saveOrUpdate(t);
	}

	@OperationLog(content = "查询角色信息数据")
	@Override
	@Transactional(readOnly = true)
	public List<Role> findWithCascade(Map<String, Object> params){
		List<Role> result = dao.find(params);
		if (CollectionUtils.isNotEmpty(result)) {
			for (Role role : result) {
				Hibernate.initialize(role.getAreaList());
				Hibernate.initialize(role.getResourceList());
				Hibernate.initialize(role.getUserList());
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateAuth(Object role, List<Object> menus, List<Object> areas)
			throws Exception {
		String roleId = (String) ((Map<String, Object>) role).get("id");
		dao.deleteAllMenuAuth(roleId);
		dao.deleteAllAreaAuth(roleId);
		//更新资源权限
		if (CollectionUtils.isNotEmpty(menus)) {
			for (Object menuTemp : menus) {
				String menuId = (String) ((Map<String, Object>) menuTemp).get("id");
				dao.insertMenuAuth(roleId, menuId);
			}
		}
		//更新区域权限
		if (CollectionUtils.isNotEmpty(areas)) {
			for (Object areaTemp : areas) {
				String areaId = (String) ((Map<String, Object>) areaTemp).get("id");
				dao.insertAreaAuth(roleId, areaId);
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getPermissionListByRoleId(String roleId) {
		return dao.getPermissionListByRoleId(roleId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getPermissionListByRoleId(List<Role> roleList) {
		return dao.getPermissionListByRoleId(roleList);
	}

}
