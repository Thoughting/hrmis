package com.eastcom.baseframe.web.modules.rest.web.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;

@Controller
@RequestMapping(value="/sys/rest")
public class RestHrefController {

	/*************************** 密钥管理  ****************************/
	
	/**
	 * 密钥管理
	 * @return
	 */
	@RequiresPermissions("sys:restsecretmgr:view")
	@OperationLog(content = "查询REST密钥数据",type = OperationType.VIEW)
	@RequestMapping(value = "/secret")
	public String role() {
		return "/modules/rest/secretMgr";
	}
	
	/**
	 * 密钥资源管理
	 * @return
	 */
	@OperationLog(content = "查询REST密钥资源数据",type = OperationType.VIEW)
	@RequestMapping(value = "/resource")
	public String user() {
		return "/modules/rest/resourceMgr";
	}
	
}
