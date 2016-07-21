package com.eastcom.baseframe.web.modules.sys.web.controller.api;

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
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.baseframe.web.modules.sys.cache.DepartmentCache;
import com.eastcom.baseframe.web.modules.sys.entity.Department;
import com.eastcom.baseframe.web.modules.sys.service.DepartmentService;
import com.google.common.collect.Lists;

/**
 * 机构Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/sys/department")
public class DepartmentController extends BaseController{

	@Autowired
	private DepartmentService departmentService;
	
	@RequiresPermissions("sys:departmentmgr:view")
	@ResponseBody
	@RequestMapping(value = "/treeList", method = RequestMethod.POST)
	public List<Department> treeList(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--获取所有部门列表--");
		List<Department> result = Lists.newArrayList();
		try {
			List<Department> departments =  DepartmentCache.getTreeList();
			String needRoot = StringUtils.defaultString((String) params.get("needRoot"),"0");
			if ("1".equals(needRoot)) {
				Department root = new Department();
				root.setId("root");
				root.setName("所有部门");
				root.setChildren(departments);
				result.add(root);
			} else {
				result = departments;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequiresPermissions(value = {"sys:departmentmgr:add","sys:departmentmgr:edit"},logical=Logical.OR)
	@OperationLog(content = "" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改机构--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			String parentId = StringUtils.defaultIfBlank((String) params.get("parentId"), "0");
			String code = (String) params.get("code");
			String name = (String) params.get("name");
			String type = (String) params.get("type");
			String remarks = (String) params.get("remarks");
			Department department = departmentService.get(id);
			if (department == null) {
				department = new Department();
				department.setCode(code);
				json.setMessage("add");
			} else {
				json.setMessage("update");
			}
			department.setName(name);
			department.setType(NumberUtils.toInt(type,0));
			department.setRemarks(remarks);
			
			Department parentDepartment = departmentService.get(parentId);
			if (parentDepartment != null) {
				department.setParent(parentDepartment);
			}
			
			departmentService.saveOrUpdate(department);
			json.setSuccess(true);
			json.setModel(department);
			
			DepartmentCache.clearCache();
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	@RequiresPermissions("sys:departmentmgr:del")
	@OperationLog(content = "" , type = OperationType.DELETE)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--级联删除机构--");
		AjaxJson json = new AjaxJson();
		try {
			String id = (String) params.get("id");
			if (StringUtils.isNotBlank(id)) {
				departmentService.deleteById(id);
				json.setModel(id);
				json.setSuccess(true);
				json.setMessage("操作成功");
				DepartmentCache.clearCache();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	@RequiresPermissions("sys:departmentmgr:sort")
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
			departmentService.updateCascadeSort(sortDatas);
			DepartmentCache.clearCache();
			json.setSuccess(true);
			json.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
}
