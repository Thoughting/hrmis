package com.eastcom.baseframe.web.modules.sys.web.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;

/**
 * sys href Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/sys")
public class SysHrefController {
	
	/*************************** 安全管理  ****************************/
	
	/**
	 * 角色管理
	 * @return
	 */
	@RequiresPermissions("sys:rolemgr:view")
	@OperationLog(content = "查询角色信息数据",type = OperationType.VIEW)
	@RequestMapping(value = "/role")
	public String role() {
		return "/modules/sys/security/roleMgr";
	}
	
	/**
	 * 用户管理
	 * @return
	 */
	@RequiresPermissions("sys:usermgr:view")
	@OperationLog(content = "查询用户信息数据",type = OperationType.VIEW)
	@RequestMapping(value = "/user")
	public String user() {
		return "/modules/sys/security/userMgr";
	}
	
	/**
	 * 菜单资源管理
	 * @return
	 */
	@RequiresPermissions("sys:resourcemgr:view")
	@OperationLog(content = "查询资源信息数据",type = OperationType.VIEW)
	@RequestMapping(value = "/resource")
	public String resource() {
		return "/modules/sys/security/resourceMgr";
	}
	
	/**
	 * 机构管理
	 * @return
	 */
	@RequiresPermissions("sys:departmentmgr:view")
	@OperationLog(content = "查询机构信息数据",type = OperationType.VIEW)
	@RequestMapping(value = "/department")
	public String department() {
		return "/modules/sys/security/departmentMgr";
	}
	
	/**
	 * 区域管理
	 * @return
	 */
	@RequiresPermissions("sys:areamgr:view")
	@OperationLog(content = "查询区域信息数据",type = OperationType.VIEW)
	@RequestMapping(value = "/area")
	public String area() {
		return "/modules/sys/security/areaMgr";
	}
	
	/**
	 * 字典管理
	 * @return
	 */
	@RequiresPermissions("sys:dictmgr:view")
	@OperationLog(content = "查询字典类型数据",type = OperationType.VIEW)
	@RequestMapping(value = "/dictMgr")
	public String dictMgr() {
		return "/modules/sys/dictMgr";
	}
	
	/*************************** 日志管理  ****************************/
	
	/**
	 * 登录日志查询
	 * @return
	 */
	@RequiresPermissions("sys:loginlogmgr:view")
	@RequestMapping(value = "/loginLogMgr")
	public String loginLogMgr() {
		return "/modules/sys/log/loginLogMgr";
	}
	
	/**
	 * 操作日志查询
	 * @return
	 */
	@RequiresPermissions("sys:operationlogmgr:view")
	@RequestMapping(value = "/operationLogMgr")
	public String operationLogMgr() {
		return "/modules/sys/log/operationLogMgr";
	}
	
	/*************************** 动态表格  ****************************/
	
	/**
	 * 表格管理
	 * @return
	 */
	//@RequiresPermissions("sys:dynamicgridmgr:view")
	@OperationLog(content = "查询动态表格信息数据",type = OperationType.VIEW)
	@RequestMapping(value = "/dynamicgridMgr")
	public String dynamicgridMgr() {
		return "/modules/sys/dynamicgridMgr";
	}
	
	/**
	 * 表格项管理
	 * @return
	 */
	//@RequiresPermissions("sys:dynamicgriditemmgr:view")
	@OperationLog(content = "查询动态表格项信息数据",type = OperationType.VIEW)
	@RequestMapping(value = "/dynamicgriditemMgr")
	public String dynamicgriditemMgr() {
		return "/modules/sys/dynamicgriditemMgr";
	}
	
}
