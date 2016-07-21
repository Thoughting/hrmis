package com.eastcom.baseframe.web.modules.sys.web.controller.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.persistence.PageHelper;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.modules.sys.SysConstant;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.baseframe.web.modules.sys.cache.RoleCache;
import com.eastcom.baseframe.web.modules.sys.entity.Department;
import com.eastcom.baseframe.web.modules.sys.entity.Role;
import com.eastcom.baseframe.web.modules.sys.entity.User;
import com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache;
import com.eastcom.baseframe.web.modules.sys.service.DepartmentService;
import com.eastcom.baseframe.web.modules.sys.service.RoleService;
import com.eastcom.baseframe.web.modules.sys.service.UserService;
import com.eastcom.baseframe.web.modules.sys.utils.PasswordUtil;
import com.eastcom.hrmis.modules.emp.cache.EmployeeDeptCache;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDept;
import com.eastcom.hrmis.modules.emp.service.EmployeeDeptService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 用户Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/sys/user")
public class UserController extends BaseController{

	@Autowired
	private UserService userService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private EmployeeDeptService employeeDeptService;
	
	@RequiresPermissions("sys:usermgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--获取用户列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 1);
			Map<String, Object> reqParams = new HashMap<String, Object>();
			reqParams.put("departmentId", (String) params.get("departmentId"));
			reqParams.put("loginName",(String) params.get("loginName"));
			PageHelper<User> users = userService.find(reqParams, pageNo, pageSize);
			
			List<Map<String, Object>> result = Lists.newArrayList();
			if (CollectionUtils.isNotEmpty(users.getList())) {
				for (User user : users.getList()) {
					Map<String, Object> item = Maps.newHashMap();
					item.put("id", user.getId());
					item.put("loginName", user.getLoginName());
					item.put("password", user.getPassword());
					item.put("code", user.getCode());
					item.put("name", user.getName());
					item.put("email", user.getEmail());
					item.put("mobile", user.getMobile());
					item.put("userType", user.getUserType());
					item.put("userTypeDict", user.getUserTypeDict());
					item.put("loginIp", user.getLoginIp());
					item.put("loginDate", DateUtils.formatDate(user.getLoginDate()));
					item.put("createDate", DateUtils.formatDate(user.getCreateDate()));
					item.put("updateDate", DateUtils.formatDate(user.getUpdateDate()));
					item.put("remarks", user.getRemarks());
					
					List<EmployeeDept> authDepts = employeeDeptService.getAuthDeptByUserId(user.getId());
					if (CollectionUtils.isNotEmpty(authDepts)) {
						String deptNames = "";
						for (EmployeeDept employeeDept : authDepts) {
							deptNames += employeeDept.getName() + "<br />";
						}
						item.put("deptNames", deptNames);
					}
					
					result.add(item);
				}
			}
			gridJson.setRows(result);
			gridJson.setTotal((long )users.getCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gridJson;
	}
	
	@RequiresPermissions(value = {"sys:usermgr:add","sys:usermgr:edit"},logical=Logical.OR)
	@OperationLog(content = "" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改用户--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			String loginName = (String) params.get("loginName");
			String password = (String) params.get("password");
			String name = (String) params.get("name");
			String code = (String) params.get("code");
			String email = (String) params.get("email");
			String mobile = (String) params.get("mobile");
			String userType = (String) params.get("userType");
			
			User user = userService.get(id);
			if (user == null) {
				user = new User();
				user.setLoginName(loginName);
				user.setCreateDate(new Date());
				user.setUpdateDate(new Date());
				user.setPassword(PasswordUtil.entryptPassword(password));
			}
			user.setName(name);
			user.setCode(code);
			user.setEmail(email);
			user.setMobile(mobile);
			user.setUserType(NumberUtils.toInt(userType));
			user.setUpdateDate(new Date());
			
			userService.saveOrUpdate(user);
			SecurityCache.clearCache(user);
			
			json.setSuccess(true);
			json.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	@RequiresPermissions("sys:usermgr:del")
	@OperationLog(content = "" , type = OperationType.DELETE)
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--级联删除角色--");
		AjaxJson json = new AjaxJson();
		try {
			String deleteJson = (String) params.get("deleteJson");
			JSONParser jsonParser = new JSONParser(deleteJson);
			List<Object> deleteDatas = jsonParser.parseArray();
			if (CollectionUtils.isNotEmpty(deleteDatas)) {
				List<String> ids = new ArrayList<String>();
				for (Object item : deleteDatas) {
					Map<String, Object> map = (Map<String, Object>) item;
					String id = StringUtils.defaultString((String) map.get("id"), "0");
					if (SysConstant.USER_SPSADMIN_ID.equals(id)) {
						json.setSuccess(false);
						json.setMessage("系统管理员用户不能删除");
						return json;
					}
					ids.add(id);
					SecurityCache.clearCache(id);
				}
				userService.deleteByIds(ids);
			}
			json.setSuccess(true);
			json.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	@RequiresPermissions("sys:usermgr:auth")
	@ResponseBody
	@RequestMapping(value = "/authOfficeTreeList", method = RequestMethod.POST)
	public List<Department> authOfficeTreeList(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--得到用户部门权限列表树图--");
		List<Department> departments = new ArrayList<Department>();
		try {
			String id = StringUtils.defaultString((String )params.get("id"), "null");
			User user = SecurityCache.getUserById(id);
			Map<String, Object> reqParams = new HashMap<String, Object>();
			reqParams.put("parentId", "null");
			departments =  departmentService.find(reqParams);
			transformAuthOfficeCheckList(departments,user.getDepartmentList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return departments;
	}
	
	@RequiresPermissions("sys:usermgr:auth")
	@ResponseBody
	@RequestMapping(value = "/authRoleGridList", method = RequestMethod.POST)
	public List<Role> authRoleGridList(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--得到用户角色权限列表--");
		List<Role> roles = new ArrayList<Role>();
		try {
			String id = StringUtils.defaultString((String )params.get("id"), "null");
			User user = SecurityCache.getUserById(id);
			roles =  RoleCache.getList();
			transformAuthRoleCheckList(roles,user.getRoleList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roles;
	}
	
	@RequiresPermissions("sys:usermgr:auth")
	@OperationLog(content = "" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/updateAuth", method = RequestMethod.POST)
	public AjaxJson updateAuth(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--更新用户权限(角色,部门)--");
		AjaxJson json = new AjaxJson();
		try {
			String userId = StringUtils.defaultString((String) params.get("userId"), "0");
			//角色
			String roleNodesJson = (String) params.get("roleNodes");
			JSONParser jsonParser = new JSONParser(roleNodesJson);
			List<Object> roleNodes = jsonParser.parseArray();
			
			userService.updateAuth(userId, null, roleNodes);
			SecurityCache.clearCache();
			SecurityCache.clearCache(userId);
			RoleCache.clearCache();
			
			//部门
			String officeNodesJson = (String) params.get("officeNodes");
			jsonParser = new JSONParser(officeNodesJson);
			List<Object> officeNodes = jsonParser.parseArray();
			employeeDeptService.updateAuth(userId, officeNodes);
			EmployeeDeptCache.clearAuthDept();
			
			json.setSuccess(true);
			json.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	/**
	 * 修改密码
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@OperationLog(content = "" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public AjaxJson updatePassword(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--修改帐号密码--");
		AjaxJson json = new AjaxJson();
		try {
			String oldPassword = (String) params.get("oldPassword");
			String newPassword = (String) params.get("newPassword");
			User user = SecurityCache.getLoginUser();
			if (user != null && PasswordUtil.validatePassword(oldPassword, user.getPassword())) {
				user.setPassword(PasswordUtil.entryptPassword(newPassword));
				userService.update(user);
				SecurityCache.clearCache(user);
				json.setSuccess(true);
				json.setMessage("操作成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 勾选中用户机构菜单
	 * @param offices
	 * @param role
	 */
	private void transformAuthOfficeCheckList(List<Department> allOffices,List<Department> userOffices){
		if (CollectionUtils.isNotEmpty(allOffices) && CollectionUtils.isNotEmpty(userOffices)) {
			for (Department office : allOffices) {
				office.setChecked(false);
				for (Department userOffice : userOffices) {
					if (office.getId().equals(userOffice.getId())) {
						office.setChecked(true);
					}
				}
				transformAuthOfficeCheckList(office.getChildren(),userOffices);
			}
		}
	}
	
	/**
	 * 勾选用户角色
	 * @param offices
	 * @param role
	 */
	private void transformAuthRoleCheckList(List<Role> roles,List<Role> userRoles){
		if (CollectionUtils.isNotEmpty(roles) && CollectionUtils.isNotEmpty(userRoles)) {
			for (Role role : roles) {
				role.setChecked(false);
				for (Role userRole : userRoles) {
					if (role.getId().equals(userRole.getId())) {
						role.setChecked(true);
					}
				}
			}
		}
	}
}
