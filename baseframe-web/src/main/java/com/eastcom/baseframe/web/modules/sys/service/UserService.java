package com.eastcom.baseframe.web.modules.sys.service;

import java.io.Serializable;
import java.util.List;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.baseframe.web.modules.sys.entity.User;

public interface UserService extends CrudService<User>{

	public User getWithCascadeById(Serializable id);
	
	public User getWithCascadeByLoginName(String loginName);
	
	/**
	 * 更新权限
	 * @param menus
	 * @param roles
	 * @throws Exception
	 */
	public void updateAuth(String userId, List<Object> offices, List<Object> roles) throws Exception;
	
}
