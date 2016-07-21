package com.eastcom.baseframe.web.modules.sys.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.eastcom.baseframe.web.modules.sys.dao.UserDao;
import com.eastcom.baseframe.web.modules.sys.entity.User;
import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.baseframe.common.utils.StringUtils;

@Repository
public class UserDaoImpl extends DaoSupport<User> implements UserDao{

	@Override
	@Resource(name="defaultSessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public void deleteAllOfficeAuth(String userId) throws Exception {
		executeBySql(" delete from t_sys_user_department where FK_USER_ID = ?",new Object[]{userId});
	}

	@Override
	public void insertOfficeAuth(String userId, String officeId)
			throws Exception {
		executeBySql(" INSERT INTO t_sys_user_department(FK_USER_ID, FK_DEPARTMENT_ID) VALUES(?,?)",new Object[]{userId, officeId});
	}

	@Override
	public void deleteAllRoleAuth(String userId) throws Exception {
		executeBySql(" delete from t_sys_user_role where FK_USER_ID = ?",new Object[]{userId});
	}

	@Override
	public void insertRoleAuth(String userId, String roleId) throws Exception {
		executeBySql(" INSERT INTO t_sys_user_role(FK_USER_ID, FK_ROLE_ID) VALUES(?,?)",new Object[]{userId, roleId});
	}
	
	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		String departmentId = (String) params.get("departmentId");
		params.remove("departmentId");
		if (StringUtils.isNotBlank(departmentId) && !"root".equals(departmentId)) {
			hql.append(" and id in( select u.id from User u join u.departmentList o where o.id = '" + departmentId + "' ) ");
		}
		if (checkParamsKey(params, "loginName")) {
			params.put("loginName", "%" + params.get("loginName") + "%");
			params.put("name", "%" + params.get("loginName") + "%");
			hql.append(" and ( loginName like :loginName or name like :name ) ");
		}
		hql.append(" order by createDate ");
	}
	
}
