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
import com.eastcom.baseframe.web.modules.sys.cache.AreaCache;
import com.eastcom.baseframe.web.modules.sys.entity.Area;
import com.eastcom.baseframe.web.modules.sys.service.AreaService;
import com.google.common.collect.Lists;

/**
 * 区域Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/sys/area")
public class AreaController extends BaseController{

	@Autowired
	private AreaService areaService;
	
	@RequiresPermissions("sys:areamgr:view")
	@ResponseBody
	@RequestMapping(value = "/treeList", method = RequestMethod.POST)
	public List<Area> treeList(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--获取所有区域列表--");
		List<Area> result = Lists.newArrayList();
		try {
			List<Area> areas =  AreaCache.getTreeList();
			String needRoot = StringUtils.defaultString((String) params.get("needRoot"),"0");
			if ("1".equals(needRoot)) {
				Area root = new Area();
				root.setId("root");
				root.setName("所有区域");
				root.setChildren(areas);
				result.add(root);
			} else {
				result = areas;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequiresPermissions(value = {"sys:areamgr:add","sys:areamgr:edit"},logical=Logical.OR)
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
			Area area = areaService.get(id);
			if (area == null) {
				area = new Area();
				area.setCode(code);
				json.setMessage("add");
			} else {
				json.setMessage("update");
			}
			area.setName(name);
			area.setType(NumberUtils.toInt(type,0));
			area.setRemarks(remarks);
			
			Area parent = areaService.get(parentId);
			if (parent != null) {
				area.setParent(parent);
			}
			
			areaService.saveOrUpdate(area);
			json.setSuccess(true);
			json.setModel(area);
			
			AreaCache.clearCache();
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	@RequiresPermissions("sys:areamgr:del")
	@OperationLog(content = "" , type = OperationType.DELETE)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--级联删除机构--");
		AjaxJson json = new AjaxJson();
		try {
			String id = (String) params.get("id");
			if (StringUtils.isNotBlank(id)) {
				areaService.deleteById(id);
				json.setModel(id);
				json.setSuccess(true);
				json.setMessage("操作成功");
				AreaCache.clearCache();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	@RequiresPermissions("sys:areamgr:sort")
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
			areaService.updateCascadeSort(sortDatas);
			AreaCache.clearCache();
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
