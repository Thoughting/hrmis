package com.eastcom.baseframe.web.modules.sys.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.baseframe.web.modules.sys.dao.UserDao;
import com.eastcom.baseframe.web.modules.sys.entity.User;
import com.eastcom.baseframe.web.modules.sys.service.UserService;

@Service
@Transactional(value = "defaultTransactionManager")
public class UserServiceImpl extends CrudServiceSupport<UserDao, User> implements UserService{

	@Override
	@Transactional(readOnly = true)
	public User getWithCascadeById(Serializable id){
		User user = dao.getById(id);
		if (user != null) {
			Hibernate.initialize(user.getDepartmentList());
			Hibernate.initialize(user.getRoleList());
		}
		return user;
	}
	
	@Override
	@Transactional(readOnly = true)
	public User getWithCascadeByLoginName(String loginName) {
		User user = dao.getUnique("loginName", loginName);
		if (user != null) {
			Hibernate.initialize(user.getDepartmentList());
			Hibernate.initialize(user.getRoleList());
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateAuth(String userId, List<Object> offices,
			List<Object> roles) throws Exception {
		dao.deleteAllOfficeAuth(userId);
		if (CollectionUtils.isNotEmpty(offices)) {
			for (Object item : offices) {
				String officeId = (String) ((Map<String, Object>) item).get("id");
				dao.insertOfficeAuth(userId, officeId);
			}
		}
		
		dao.deleteAllRoleAuth(userId);
		if (CollectionUtils.isNotEmpty(roles)) {
			for (Object item : roles) {
				String roleId = (String) ((Map<String, Object>) item).get("id");
				dao.insertRoleAuth(userId, roleId);
			}
		}
	}

}
