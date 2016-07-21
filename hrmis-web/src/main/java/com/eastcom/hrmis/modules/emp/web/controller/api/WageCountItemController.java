package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.hrmis.modules.emp.cache.WageCountItemCache;
import com.eastcom.hrmis.modules.emp.entity.WageCountItem;
import com.eastcom.hrmis.modules.emp.service.WageCountItemService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 工资核算项Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/wagecountitem")
public class WageCountItemController extends BaseController {

	@Autowired
	private WageCountItemService wageCountItemService;
	
	//@RequiresPermissions("emp:wagecountitemmgr:view")
	@ResponseBody
	@RequestMapping(value = "/treeList", method = RequestMethod.POST)
	public List<WageCountItem> treeList(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--获取所有工资核算项列表--");
		List<WageCountItem> result = Lists.newArrayList();
		try {
			result =  WageCountItemCache.getTreeList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//@RequiresPermissions("emp:wagecountitemmgr:add")
	@OperationLog(content = "新增或者修改工资核算项" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改工资核算项--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			String parentId = StringUtils.defaultIfBlank((String) params.get("parentId"), "0");
			String code = (String) params.get("code");
			String name = (String) params.get("name");
			String remarks = (String) params.get("remarks");
			int showWidth = NumberUtils.toInt((String) params.get("showWidth"), 100);
			String showFormatter = (String) params.get("showFormatter");
			
			WageCountItem wageCountItem = wageCountItemService.get(id);
			if (wageCountItem == null) {
				//判断code是否唯一
				Map<String, Object> reqParam = Maps.newHashMap();
				reqParam.put("code", code);
				if (CollectionUtils.isNotEmpty(wageCountItemService.find(reqParam))) {
					json.setSuccess(false);
					json.setMessage("操作失败,该编码已存在!");
					return json;
				}
				
				wageCountItem = new WageCountItem();
				wageCountItem.setCode(code);
				json.setMessage("add");
			} else {
				json.setMessage("update");
			}
			wageCountItem.setName(name);
			wageCountItem.setRemarks(remarks);
			wageCountItem.setShowWidth(showWidth);
			wageCountItem.setShowFormatter(showFormatter);
			
			WageCountItem parent = wageCountItemService.get(parentId);
			if (parent != null) {
				wageCountItem.setParent(parent);
			}
			
			wageCountItemService.saveOrUpdate(wageCountItem);
			WageCountItemCache.clearCache();
			json.setSuccess(true);
			json.setModel(wageCountItem);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	//@RequiresPermissions("emp:wagecountitemmgr:del")
	@OperationLog(content = "级联删除工资核算项" , type = OperationType.DELETE)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--级联删除工资核算项--");
		AjaxJson json = new AjaxJson();
		try {
			String id = (String) params.get("id");
			if (StringUtils.isNotBlank(id)) {
				wageCountItemService.deleteById(id);
				WageCountItemCache.clearCache();
				json.setModel(id);
				json.setSuccess(true);
				json.setMessage("操作成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	//@RequiresPermissions("emp:wagecountitemmgr:sort")
	@OperationLog(content = "保存排列顺序" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/saveSort", method = RequestMethod.POST)
	public AjaxJson saveSort(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--保存排列顺序--");
		AjaxJson json = new AjaxJson();
		try {
			String sortJson = (String) params.get("sortJson");
			JSONParser jsonParser = new JSONParser(sortJson);
			List<Object> sortDatas = jsonParser.parseArray();
			wageCountItemService.updateCascadeSort(sortDatas);
			WageCountItemCache.clearCache();
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
