package com.eastcom.baseframe.web.modules.rest.service;

import java.util.List;

import com.eastcom.baseframe.common.service.CrudService;
import com.eastcom.baseframe.web.modules.rest.entity.RestSecret;

/**
 * rest secret service
 * 
 * @author wutingguang <br>
 */
public interface RestSecretService extends CrudService<RestSecret>{

	/**
	 * 更新密钥权限(资源)
	 * @param role
	 * @param menus
	 * @param areas
	 * @throws Exception
	 */
	public void updateAuth(Object restSecret, List<Object> resources) throws Exception;
	
	/**
	 * 添加全部资源授权
	 * 
	 * @param restSecret
	 */
	public void addAllResourceAuth(RestSecret restSecret) throws Exception;
	
}
