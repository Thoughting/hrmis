package com.eastcom.baseframe.web.modules.sys.web.controller.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.eastcom.baseframe.common.utils.CacheUtils;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.baseframe.web.modules.sys.entity.Resource;
import com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache;
import com.eastcom.baseframe.web.modules.sys.service.ResourceService;

/**
 * 资源Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/sys/resource")
public class ResourceController extends BaseController{

	@Autowired
	private ResourceService resourceService;
	
	@RequiresPermissions("sys:resourcemgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<Resource> list(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--获取资源列表--");
		List<Resource> result = new ArrayList<Resource>();
		try {
			result = SecurityCache.getAllResourceList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequiresPermissions(value = {"sys:resourcemgr:add","sys:resourcemgr:edit"},logical=Logical.OR)
	@OperationLog(content = "" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改资源点--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			String parentId = StringUtils.defaultIfBlank((String) params.get("parentId"), "0");
			String name = (String) params.get("name");
			String permission = (String) params.get("permission");
			String href = (String) params.get("href");
			String type = (String) params.get("type");
			String remarks = (String) params.get("remarks");
			Resource resource = resourceService.get(id);
			if (resource == null) {
				resource = new Resource();
				resource.setCreateDate(new Date());
				json.setMessage("add");
			} else {
				json.setMessage("update");
			}
			resource.setName(name);
			resource.setPermission(permission);
			resource.setHref(href);
			resource.setType(NumberUtils.toInt(type, 0));
			resource.setRemarks(remarks);
			
			Resource parentResource = resourceService.get(parentId);
			if (parentResource != null) {
				resource.setParent(parentResource);
				int level = Resource.levelCount(parentResource, 2);
				resource.setLevel(level);
			}else{
				resource.setLevel(1);
			}
			
			resourceService.saveOrUpdate(resource);
			json.setSuccess(true);
			json.setModel(resource);
			
			SecurityCache.clearCache();
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	@RequiresPermissions("sys:resourcemgr:del")
	@OperationLog(content = "" , type = OperationType.DELETE)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--级联删除资源点--");
		AjaxJson json = new AjaxJson();
		try {
			String id = (String) params.get("id");
			if (StringUtils.isNotBlank(id)) {
				resourceService.deleteById(id);
				json.setModel(id);
				json.setSuccess(true);
				json.setMessage("操作成功");
				
				SecurityCache.clearCache();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	@RequiresPermissions("sys:resourcemgr:sort")
	@OperationLog(content = "" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/saveSort", method = RequestMethod.POST)
	public AjaxJson saveSort(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--保存排列顺序--");
		AjaxJson json = new AjaxJson();
		try {
			String sortJson = (String) params.get("sortJson");
			JSONParser jsonParser = new JSONParser(sortJson);
			List<Object> sortDatas = jsonParser.parseArray();
			resourceService.updateCascadeSort2(sortDatas);
			json.setSuccess(true);
			json.setMessage("操作成功");
			
			SecurityCache.clearCache();
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
}
