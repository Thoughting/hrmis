package com.eastcom.baseframe.web.modules.sys.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.eastcom.baseframe.web.modules.sys.dao.RoleDao;
import com.eastcom.baseframe.web.modules.sys.entity.Role;
import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.baseframe.common.utils.StringUtils;

@Repository
public class RoleDaoImpl extends DaoSupport<Role> implements RoleDao{

	@Override
	@Resource(name="defaultSessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPermissionListByRoleId(String roleId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select * from ( ");
		sb.append(" select t2.PERMISSION from ");
		sb.append(" ( select FK_RESOURCE_ID from t_sys_role_resource where FK_ROLE_ID = ? ) t1 ");
		sb.append(" left join t_sys_resource t2 on t1.FK_RESOURCE_ID = t2.ID ");
		sb.append(" ) t where t.PERMISSION is not null ");
		SQLQuery query = createSqlQuery(sb.toString(), new Object[]{roleId});
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPermissionListByRoleId(List<Role> roleList) {
		String[] roleArr = new String[roleList.size()];
		for (int i = 0; i < roleList.size(); i++) {
			roleArr[i] = "'" + roleList.get(i).getId() + "'";
		}
		String roleIds = StringUtils.join(roleArr, ",");
		StringBuilder sb = new StringBuilder();
		sb.append(" select t2.PERMISSION from ( ");
		sb.append(" select FK_RESOURCE_ID from t_sys_role_resource where FK_ROLE_ID in ( " + roleIds + " ) ) t1 ");
		sb.append(" left join t_sys_resource t2 on t1.FK_RESOURCE_ID = t2.ID where t2.PERMISSION is not null ");
		SQLQuery query = createSqlQuery(sb.toString());
		return query.list();
	}
	
	@Override
	public void deleteAllMenuAuth(String roleId) throws Exception {
		executeBySql(" delete from t_sys_role_resource where FK_ROLE_ID = ?",new Object[]{roleId});
	}
	
	@Override
	public void insertMenuAuth(String roleId, String menuId) throws Exception {
		executeBySql(" INSERT INTO t_sys_role_resource(FK_ROLE_ID, FK_RESOURCE_ID) VALUES(?,?)",new Object[]{roleId, menuId});
	}

	@Override
	public void deleteAllAreaAuth(String roleId) throws Exception {
		executeBySql(" delete from t_sys_role_area where FK_ROLE_ID = ?",new Object[]{roleId});
	}

	@Override
	public void insertAreaAuth(String roleId, String areaId) throws Exception {
		executeBySql(" INSERT INTO t_sys_role_area(FK_ROLE_ID, FK_AREA_ID) VALUES(?,?)",new Object[]{roleId, areaId});
	}

}
