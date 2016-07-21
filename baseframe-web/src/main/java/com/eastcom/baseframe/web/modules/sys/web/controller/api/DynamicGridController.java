package com.eastcom.baseframe.web.modules.sys.web.controller.api;

import java.util.ArrayList;
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
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.baseframe.web.modules.sys.cache.DynamicGridCache;
import com.eastcom.baseframe.web.modules.sys.entity.Dynamicgrid;
import com.eastcom.baseframe.web.modules.sys.service.DynamicgridService;

@Controller
@RequestMapping(value = "/api/sys/dynamicgrid")
public class DynamicGridController extends BaseController{
	
	@Autowired
	private DynamicgridService dynamicgridService;

	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	//@RequiresPermissions("sys:dynamicgridmgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--获取动态表格列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			List<Dynamicgrid> dynamicgrids = DynamicGridCache.getList();
			gridJson.setRows(dynamicgrids);
			gridJson.setTotal((long) dynamicgrids.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gridJson;
	}
	
	/**
	 * 新增修改
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	//@RequiresPermissions(value = {"sys:dynamicgridmgr:add","sys:dynamicgridmgr:edit"},logical=Logical.OR)
	@OperationLog(content = "" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改动态表格--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			Dynamicgrid dynamicgrid = dynamicgridService.get(id);
			if (dynamicgrid == null) {
				dynamicgrid = new Dynamicgrid();
				dynamicgrid.setCode((String) params.get("code"));
			}
			dynamicgrid.setName((String) params.get("name"));
			dynamicgrid.setHeadDepth(NumberUtils.toInt((String) params.get("headDepth"), 1));
			dynamicgrid.setRemarks((String) params.get("remarks"));
			
			dynamicgridService.saveOrUpdate(dynamicgrid);
			DynamicGridCache.clearCache();
			
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
	 * 删除
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	//@RequiresPermissions("sys:dynamicgridmgr:del")
	@OperationLog(content = "" , type = OperationType.DELETE)
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--删除动态表格--");
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
					ids.add(id);
				}
				dynamicgridService.deleteByIds(ids);
				DynamicGridCache.clearCache();
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
	
	/**
	 * 
	 * 下拉框
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/combo", method = RequestMethod.POST)
	public AjaxJson combo(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--获取所有动态表格列表(下拉框)--");
		AjaxJson json = new AjaxJson();
		try {
			List<Dynamicgrid> list = DynamicGridCache.getList();
			json.setSuccess(true);
			json.setModel(list);
			json.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
}
