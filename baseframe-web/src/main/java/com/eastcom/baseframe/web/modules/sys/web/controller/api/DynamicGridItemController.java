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
import com.eastcom.baseframe.web.modules.sys.cache.DynamicgridItemCache;
import com.eastcom.baseframe.web.modules.sys.entity.DynamicgridItem;
import com.eastcom.baseframe.web.modules.sys.service.DynamicgridItemService;
import com.eastcom.baseframe.web.modules.sys.service.DynamicgridService;
import com.google.common.collect.Lists;

/**
 * 表格项Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/sys/dynamicgriditem")
public class DynamicGridItemController extends BaseController{

	@Autowired
	private DynamicgridItemService dynamicgridItemService;
	
	@Autowired
	private DynamicgridService dynamicgridService;
	
	//@RequiresPermissions("sys:dynamicgriditemmgr:view")
	@ResponseBody
	@RequestMapping(value = "/treeList", method = RequestMethod.POST)
	public List<DynamicgridItem> treeList(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--获取所有表格项列表--");
		List<DynamicgridItem> result = Lists.newArrayList();
		try {
			String dynamicgridId = StringUtils.defaultIfBlank((String) params.get("dynamicgridId"),"0");
			result = DynamicgridItemCache.getDynamicgridItemsById(dynamicgridId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//@RequiresPermissions(value = {"sys:dynamicgriditemmgr:add","sys:dynamicgriditemmgr:edit"},logical=Logical.OR)
	@OperationLog(content = "" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改表格项--");
		AjaxJson json = new AjaxJson();
		try {
			DynamicgridItem dynamicgridItem = dynamicgridItemService.get(StringUtils.defaultIfBlank((String) params.get("id"), "0"));
			if (dynamicgridItem == null) {
				dynamicgridItem = new DynamicgridItem();
				dynamicgridItem.setField((String) params.get("field"));
				dynamicgridItem.setDynamicgrid(dynamicgridService.get(StringUtils.defaultIfBlank((String) params.get("dynamicgridId"), "0")));
				json.setMessage("add");
			} else {
				json.setMessage("update");
			}
			dynamicgridItem.setTitle((String) params.get("title"));
			dynamicgridItem.setWidth(NumberUtils.toInt((String) params.get("width"), 50));
			dynamicgridItem.setAlign((String) params.get("align"));
			dynamicgridItem.setFormatter((String) params.get("formatter"));
			dynamicgridItem.setRemarks((String) params.get("remarks"));
			
			dynamicgridItem.setParent(dynamicgridItemService.get(StringUtils.defaultIfBlank((String) params.get("parentId"), "0")));
			
			dynamicgridItemService.saveOrUpdate(dynamicgridItem);
			DynamicgridItemCache.clearCache();
			
			json.setSuccess(true);
			json.setModel(dynamicgridItem);
			
			AreaCache.clearCache();
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	//@RequiresPermissions("sys:dynamicgriditemmgr:del")
	@OperationLog(content = "" , type = OperationType.DELETE)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--级联删除表格项--");
		AjaxJson json = new AjaxJson();
		try {
			String id = (String) params.get("id");
			if (StringUtils.isNotBlank(id)) {
				dynamicgridItemService.deleteById(id);
				DynamicgridItemCache.clearCache();
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
	
	//@RequiresPermissions("sys:dynamicgriditemmgr:sort")
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
			
			dynamicgridItemService.updateCascadeSort(sortDatas);
			DynamicgridItemCache.clearCache();
			
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
