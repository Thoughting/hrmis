package com.eastcom.baseframe.web.modules.sys.web.controller.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.modules.sys.SysConstant;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.baseframe.web.modules.sys.cache.AreaCache;
import com.eastcom.baseframe.web.modules.sys.cache.RoleCache;
import com.eastcom.baseframe.web.modules.sys.entity.Area;
import com.eastcom.baseframe.web.modules.sys.entity.Resource;
import com.eastcom.baseframe.web.modules.sys.entity.Role;
import com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache;
import com.eastcom.baseframe.web.modules.sys.service.ResourceService;
import com.eastcom.baseframe.web.modules.sys.service.RoleService;
import com.google.common.collect.Lists;

/**
 * 角色Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/sys/role")
public class RoleController extends BaseController{

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ResourceService resourceService;
	
	@RequiresPermissions("sys:rolemgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--获取角色列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			List<Role> result = RoleCache.getList();
			if (CollectionUtils.isNotEmpty(result)) {
				gridJson.setRows(result);
				gridJson.setTotal((long) result.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gridJson;
	}
	
	@RequiresPermissions(value = {"sys:rolemgr:add","sys:rolemgr:edit"},logical=Logical.OR)
	@OperationLog(content = "" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改角色--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			String name = (String) params.get("name");
			String nameCn = (String) params.get("nameCn");
			Integer level = NumberUtils.toInt((String) params.get("level"), 1);
			String remarks = (String) params.get("remarks");
			Role role = roleService.get(id);
			if (role == null) {
				role = new Role();
				role.setName(name);
				role.setCreateDate(new Date());
			}
			role.setNameCn(nameCn);
			role.setLevel(level);
			role.setRemarks(remarks);
			roleService.saveOrUpdate(role);
			RoleCache.clearCache();
			
			json.setSuccess(true);
			json.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	@RequiresPermissions("sys:rolemgr:del")
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
					if (SysConstant.ROLE_SPSADMIN_ID.equals(id)) {
						json.setSuccess(false);
						json.setMessage("超级管理员角色不能删除");
						return json;
					}
					ids.add(id);
				}
				roleService.deleteByIds(ids);
				RoleCache.clearCache();
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
	
	@RequiresPermissions("sys:rolemgr:auth")
	@ResponseBody
	@RequestMapping(value = "/authResourceTreeList", method = RequestMethod.POST)
	public List<Resource> authResourceTreeList(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--得到角色资源列表树图--");
		List<Resource> resources = Lists.newArrayList();
		try {
			String id = StringUtils.defaultString((String )params.get("id"), "null");
			Role role = RoleCache.getRoleById(id);
			resources = Lists.newArrayList(SecurityCache.getAllResourceList());
			transformAuthMenuCheckList(resources,role.getResourceList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resources;
	}
	
	@RequiresPermissions("sys:rolemgr:auth")
	@ResponseBody
	@RequestMapping(value = "/authAreaTreeList", method = RequestMethod.POST)
	public List<Area> authAreaTreeList(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--得到角色区域列表树图--");
		List<Area> areas = Lists.newArrayList();
		try {
			String id = StringUtils.defaultString((String )params.get("id"), "null");
			Role role = RoleCache.getRoleById(id);
			areas = Lists.newArrayList(AreaCache.getTreeList());
			transformAuthAreaCheckList(areas,role.getAreaList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return areas;
	}
	
	@RequiresPermissions("sys:rolemgr:auth")
	@OperationLog(content = "" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/updateAuth", method = RequestMethod.POST)
	public AjaxJson updateAuth(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--更新角色资源权限--");
		AjaxJson json = new AjaxJson();
		try {
			//选择的菜单资源列表
			String checkResourceNodesJson = (String) params.get("checkResourceNodes");
			List<Object> checkResourceNodes = new JSONParser(checkResourceNodesJson).parseArray();
			
			//选择的区域资源列表
			List<Object> checkAreaNodes = Lists.newArrayList();
			String checkAreaNodesJson = (String) params.get("checkAreaNodes");
			if (StringUtils.isNotBlank(checkAreaNodesJson)) {
				checkAreaNodes = new JSONParser(checkAreaNodesJson).parseArray();
			}
			
			//角色列表
			String selectNodesJson = (String) params.get("selectNode");
			Object selectNode = new JSONParser(selectNodesJson).parse();
			
			roleService.updateAuth(selectNode,checkResourceNodes,checkAreaNodes);
			RoleCache.clearCache();
			
			SecurityCache.clearCache();
			
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
	 * 标志勾选角色资源
	 * @param allMenus
	 * @param roleMenus
	 */
	public static void transformAuthMenuCheckList(List<Resource> allMenus,List<Resource> roleMenus){
		if (CollectionUtils.isNotEmpty(allMenus) && CollectionUtils.isNotEmpty(roleMenus)) {
			for (Resource menu : allMenus) {
				menu.setChecked(false);
				for (Resource roleMenu : roleMenus) {
					if (menu.getId().equals(roleMenu.getId())) {
						menu.setChecked(true);
					}
				}
				transformAuthMenuCheckList(menu.getChildList(),roleMenus);
			}
		}
	}
	
	/**
	 * 标志勾选角色区域
	 * @param allAreas
	 * @param roleAreas
	 */
	public static void transformAuthAreaCheckList(List<Area> allAreas,List<Area> roleAreas){
		if (CollectionUtils.isNotEmpty(allAreas) && CollectionUtils.isNotEmpty(roleAreas)) {
			for (Area area : allAreas) {
				area.setChecked(false);
				for (Area roleArea : roleAreas) {
					if (area.getId().equals(roleArea.getId())) {
						area.setChecked(true);
					}
				}
				transformAuthAreaCheckList(area.getChildren(),roleAreas);
			}
		}
	}
}
