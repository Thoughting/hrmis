package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.eastcom.baseframe.common.persistence.PageHelper;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache;
import com.eastcom.hrmis.modules.emp.entity.EmployeeOrder;
import com.eastcom.hrmis.modules.emp.service.EmployeeOrderService;
import com.google.common.collect.Lists;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代办工单Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/agency/order")
public class EmployeeOrderController extends BaseController {

	@Autowired
	private EmployeeOrderService employeeOrderService;
	
	/**
	 * 
	 * 查询代办工单
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("index")
	@ResponseBody
	@RequestMapping(value = "/db_list", method = RequestMethod.POST)
	public DataGridJson dbList(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取代办工单列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 999);
			
			Map<String, Object> reqParams = new HashMap<String, Object>();
			reqParams.put("status", 0);
			
			List<Integer> orderTypes = Lists.newArrayList();
			orderTypes.add(0);
			orderTypes.add(1);
			orderTypes.add(2);
			orderTypes.add(3);
			orderTypes.add(4);
			if (SecurityCache.hasPermission("emp:baseinfomgr:audit")) {
				//审核档案权限
				orderTypes.add(5);
			}
			reqParams.put("orderTypes", orderTypes);
			reqParams.put("createBy", (String) params.get("createBy"));
			PageHelper<EmployeeOrder> pageHelper = employeeOrderService.find(reqParams, pageNo, pageSize);
			gridJson.setRows(pageHelper.getList());
			gridJson.setTotal(pageHelper.getCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gridJson;
	}
	
	/**
	 * 
	 * 查询已办工单
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("index")
	@ResponseBody
	@RequestMapping(value = "/yb_list", method = RequestMethod.POST)
	public DataGridJson ybList(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取已办工单列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 999);
			Map<String, Object> reqParams = new HashMap<String, Object>();
			reqParams.put("status", 1);
			
			List<Integer> orderTypes = Lists.newArrayList();
			orderTypes.add(0);
			orderTypes.add(1);
			orderTypes.add(2);
			orderTypes.add(3);
			orderTypes.add(4);
			if (SecurityCache.hasPermission("emp:baseinfomgr:audit")) {
				//审核档案权限
				orderTypes.add(5);
			}
			reqParams.put("orderTypes", orderTypes);
			reqParams.put("createBy", (String) params.get("createBy"));
			PageHelper<EmployeeOrder> pageHelper = employeeOrderService.find(reqParams, pageNo, pageSize);
			gridJson.setRows(pageHelper.getList());
			gridJson.setTotal(pageHelper.getCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gridJson;
	}
	
	/**
	 * 代办工单确认
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("index:employeeorder:confirm")
	@OperationLog(content = "员工代办工单确认" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/db_confirm", method = RequestMethod.POST)
	public AjaxJson dbConfirm(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--员工代办工单确认--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			EmployeeOrder agencyOrder = employeeOrderService.get(id);
			if (agencyOrder != null) {
				agencyOrder.setStatus(1);
				employeeOrderService.saveOrUpdate(agencyOrder);
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
	
}
