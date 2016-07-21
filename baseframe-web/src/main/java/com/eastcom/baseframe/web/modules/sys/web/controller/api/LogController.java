package com.eastcom.baseframe.web.modules.sys.web.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.persistence.PageHelper;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.baseframe.web.modules.sys.cache.DictCache;
import com.eastcom.baseframe.web.modules.sys.entity.Dict;
import com.eastcom.baseframe.web.modules.sys.entity.LogLogin;
import com.eastcom.baseframe.web.modules.sys.service.DictService;
import com.eastcom.baseframe.web.modules.sys.service.LogLoginService;
import com.eastcom.baseframe.web.modules.sys.service.LogOperationService;

/**
 * 日志Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/sys/log")
public class LogController extends BaseController{

	@Autowired
	private LogLoginService logLoginService;
	
	@Autowired
	private LogOperationService logOperationService;
	
	@RequiresPermissions("sys:loginlogmgr:view")
	@ResponseBody
	@RequestMapping(value = "/list_login", method = RequestMethod.POST)
	public DataGridJson listLogin(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--获取登录日志列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 10);
			
			String loginName = (String )params.get("loginName");
			String startTime = (String )params.get("startTime");
			String endTime = (String )params.get("endTime");
			
			Map<String, Object> reqParams = new HashMap<String, Object>();
			reqParams.put("loginName", loginName);
			reqParams.put("startTime", startTime);
			reqParams.put("endTime", endTime);
			
			PageHelper<LogLogin> pageHelper = logLoginService.find(reqParams, pageNo, pageSize);
			gridJson.setRows(pageHelper.getList());
			gridJson.setTotal(pageHelper.getCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gridJson;
	}
	
	@RequiresPermissions("sys:operationlogmgr:view")
	@ResponseBody
	@RequestMapping(value = "/list_operation", method = RequestMethod.POST)
	public DataGridJson listOperation(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--获取操作日志列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 10);
			
			String loginName = (String )params.get("loginName");
			String startTime = (String )params.get("startTime");
			String endTime = (String )params.get("endTime");
			
			Map<String, Object> reqParams = new HashMap<String, Object>();
			reqParams.put("loginName", loginName);
			reqParams.put("startTime", startTime);
			reqParams.put("endTime", endTime);
			
			PageHelper<LogLogin> pageHelper = logOperationService.find(reqParams, pageNo, pageSize);
			gridJson.setRows(pageHelper.getList());
			gridJson.setTotal(pageHelper.getCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gridJson;
	}
}
