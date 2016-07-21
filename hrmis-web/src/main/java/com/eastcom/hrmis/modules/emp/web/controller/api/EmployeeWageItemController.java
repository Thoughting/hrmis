package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.eastcom.baseframe.common.persistence.PageHelper;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.common.utils.excel.CustomizeToExcel;
import com.eastcom.baseframe.common.utils.excel.ExcelColumn;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.common.utils.DownloadUtil;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.hrmis.modules.emp.entity.*;
import com.eastcom.hrmis.modules.emp.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 员工工资项Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/employeewageitem")
public class EmployeeWageItemController extends BaseController {

	@Autowired
	private EmployeeWageItemService employeeWageItemService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private WageCountItemService wageCountItemService;
	
	@Autowired
	private WagePlanService wagePlanService;
	
	@Autowired
	private WageTemplateService wageTemplateService;
	
	@Autowired
	private WagePlanController wagePlanController;
	
	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:employeewageitemmgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取员工工资项信息--");
		DataGridJson gridJson = new DataGridJson();
		try {
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 99999);
			
			List<Map<String, Object>> result = Lists.newArrayList();
			
			//得到工资方案
			String wagePlanId = StringUtils.defaultIfBlank((String) params.get("wagePlanId"), "0");
			WagePlan wagePlan = wagePlanService.get(wagePlanId);
			
			//得到工资方案工资核算模板值
			Map<String, Object> reqParam = Maps.newHashMap();
			reqParam.put("wagePlanId", wagePlanId);
			List<WageTemplate> wageTemplates = wageTemplateService.find(reqParam);
			
			//得到员工信息
			reqParam = Maps.newHashMap();
			reqParam.put("wagePlan", wagePlanId);
			reqParam.put("employeePost", (String) params.get("employeePost"));
			reqParam.put("employeeDept", (String) params.get("employeeDept"));
			reqParam.put("name", (String) params.get("name"));
			reqParam.put("auditStatus", 2); // 审核通过
			reqParam.put("hasQuitCompany", 0); // 未离职
			
			PageHelper<Employee> pageHelper = employeeService.find(reqParam, pageNo, pageSize);
			
			if (wagePlan != null && CollectionUtils.isNotEmpty(wagePlan.getPosts())) {
				List<Employee> employees = pageHelper.getList();
				if (CollectionUtils.isNotEmpty(employees)) {
					for (Employee employee : employees) {
						Map<String, Object> item = Maps.newHashMap();
						item.put("employeeId", employee.getId());
						item.put("employeeName", employee.getName());
						item.put("deptName", employee.getEmployeeDeptName());
						item.put("postName", employee.getEmployeePostName());
						//工资核算项
						if (CollectionUtils.isNotEmpty(wagePlan.getCountItems())) {
							List<WageCountItem> countItems = wagePlan.getCountItems();
							for (WageCountItem wageCountItem : countItems) {
								Map<String, Object> wageCountMap = Maps.newHashMap();
								wageCountMap.put("wageCountItemId", wageCountItem.getId());
								
								//工资模板数值填充
								if (CollectionUtils.isNotEmpty(wageTemplates)) {
									for (WageTemplate wageTemplate : wageTemplates) {
										if (employee.getEmployeePost() != null && employee.getEmployeePost().getId().equals(wageTemplate.getEmployeePost().getId()) && wageCountItem.getId().equals(wageTemplate.getWageCountItem().getId())) {
											wageCountMap.put("model", wageTemplate);
											wageCountMap.put("backgroundColor", "#FFFFFF");
											break;
										}
									}
								}
								//员工工资项填充
								if (CollectionUtils.isNotEmpty(employee.getWageItems())) {
									List<EmployeeWageItem> wageItems = employee.getWageItems();
									for (EmployeeWageItem wageItem : wageItems) {
										if (wageCountItem.getId().equals(wageItem.getWageCountItem().getId())) {
											wageCountMap.put("model", wageItem);
											wageCountMap.put("backgroundColor", "#CCFFCC");
											break;
										}
									}
								}
								item.put(wageCountItem.getId(), wageCountMap);
							}
						}
						result.add(item);
					}
				}
			}
			gridJson.setRows(result);
			gridJson.setTotal(pageHelper.getCount());
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
	@RequiresPermissions("emp:employeewageitemmgr:edit")
	@OperationLog(content = "新增或者修改员工工资项信息" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改员工工资项信息--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			EmployeeWageItem employeeWageItem = employeeWageItemService.get(id);
			if (employeeWageItem == null) {
				employeeWageItem = new EmployeeWageItem();
			}
			employeeWageItem.setCount(NumberUtils.toDouble((String) params.get("count"), 0));
			employeeWageItem.setEmployee(employeeService.get(StringUtils.defaultIfBlank((String) params.get("employeeId"), "0")));
			employeeWageItem.setWageCountItem(wageCountItemService.get(StringUtils.defaultIfBlank((String) params.get("wageCountItemId"), "0")));
			
			employeeWageItemService.saveOrUpdate(employeeWageItem);
			//更新影响
			employeeWageItemService.updateEffect(employeeWageItem);
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
	 * 导出员工工资配置Excel
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("emp:employeewageitemmgr:export")
	@OperationLog(content = "导出员工工资配置Excel" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(HttpSession session,HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> params) {
		logger.info("--导出员工工资配置Excel--");
		try {
			DataGridJson dataGridJson = list(session, request, params);
			String fileName = "员工工资配置.xlsx";
			String filePath = getExportPath(request) + fileName;
			List<ExcelColumn> excelColumns = wagePlanController.getExportExcelColumnHeader(session, request, params);
			if (CollectionUtils.isNotEmpty(excelColumns)) {
				excelColumns.add(0, new ExcelColumn("姓名", "employeeName", 120));
				excelColumns.add(0, new ExcelColumn("岗位", "postName", 120));
				excelColumns.add(0, new ExcelColumn("部门", "deptName", 120));
				ObjectMapper m = new ObjectMapper(); 
				String json = m.writeValueAsString(dataGridJson.getRows());
				List<Map<String,Object>> data = convertJsonArrToMap(json);
				if (CollectionUtils.isNotEmpty(data)) {
					for (Map<String, Object> map : data) {
						for (Map.Entry<String, Object> entry : map.entrySet()) {
							if (entry.getValue() != null && entry.getValue() instanceof Map) {
								Map<String,Object> map2 = (Map<String, Object>) entry.getValue();
								for (String key : map2.keySet()) {
									if ("model".equals(key)) {
										Map<String,Object> temp2 = (Map<String, Object>) map2.get(key);
										if (temp2 != null) {
											String count = ((Double) temp2.get("count")).toString();
											if (count.lastIndexOf(".0") == count.length() - 2) {
												count = count.replace(".0", "");
											}
											map.put(entry.getKey(), count);
										}
										break;
									} else {
										map.put(entry.getKey(), 0);
									}
								}
							}
						} 
					}
				}
				CustomizeToExcel.toFile(excelColumns ,data ,filePath);
			}
			DownloadUtil.downloadFile(filePath, fileName, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
