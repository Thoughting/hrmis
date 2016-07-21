package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.persistence.PageHelper;
import com.eastcom.baseframe.common.utils.DateUtils;
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
import org.apache.shiro.authz.annotation.Logical;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 员工实发工资Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/wage")
public class EmployeeWageController extends BaseController {

	@Autowired
	private EmployeeWageService employeeWageService;
	
	@Autowired
	private EmployeeWageItemService employeeWageItemService;
	
	@Autowired
	private WagePlanService wagePlanService;
	
	@Autowired
	private WageTemplateService wageTemplateService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private WagePlanController wagePlanController;
	
	@Autowired
	private EmployeeWageHisService wageHisService;
	
	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequiresPermissions("emp:wagecheckmgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取员工实发工资信息--");
		DataGridJson gridJson = new DataGridJson();
		try {
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 99999);
			String wageDateStr = (String) params.get("wageDateStr");
			String type = (String) params.get("type");
			
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
			reqParam.put("name", (String) params.get("employeeName"));
			reqParam.put("auditStatus", 2); // 审核通过
			Date d = DateUtils.parseDate(wageDateStr, "yyyy年-MM月");
			d.setDate(1);
			reqParam.put("start_quitCompanyDate", DateUtils.formatDate(d)); // 如果已离职，离职时间需要大于当前月份
			d.setMonth(d.getMonth() + 1);
			d.setDate(d.getDate() - 1);
			reqParam.put("end_enrtyDate", DateUtils.formatDate(d)); // 入职时间需要小于当前月份+1
			PageHelper<Employee> pageHelper = employeeService.find(reqParam, pageNo, pageSize);
			
			//得到员工实发工资列表（时间）
			reqParam = Maps.newHashMap();
			reqParam.put("wageDateStr", wageDateStr);
			List<EmployeeWage> employeeWages = employeeWageService.find(reqParam);
			
			if (wagePlan != null && CollectionUtils.isNotEmpty(wagePlan.getPosts())) {
				List<Employee> employees = pageHelper.getList();
				if (CollectionUtils.isNotEmpty(employees)) {
					for (Employee employee : employees) {
						Map<String, Object> item = Maps.newHashMap();
						item.put("employeeId", employee.getId());
						item.put("employeeName", employee.getName());
						item.put("deptName", employee.getEmployeeDeptName());
						item.put("postName", employee.getEmployeePostName());
						item.put("bankCard", employee.getBankCard());
						//当前月份是否有实发工资，没有则新生成记录
						EmployeeWage employeeWage = null;
						if (CollectionUtils.isNotEmpty(employeeWages)) {
							for (EmployeeWage t : employeeWages) {
								if (t.getEmployee() != null && t.getEmployee().getId().equals(employee.getId())) {
									employeeWage = t;
									break;
								}
							}
						}
						if (employeeWage == null) {
							employeeWage = new EmployeeWage();
							employeeWage.setAuditStatus(0);
							employeeWage.setWageDateStr(wageDateStr);
							employeeWage.setEmployee(employee);
							employeeWageService.saveOrUpdate(employeeWage);
						}
						
						item.put("employeeWageId", employeeWage.getId());
						item.put("auditStatusDict", employeeWage.getAuditStatusDict());
						item.put("auditStatus", employeeWage.getAuditStatus());
						item.put("auditContent", employeeWage.getAuditContent());
						
						//员工实发工资条如在未审核状态，则须实时计算其当前工资信息
						if (employeeWage.getAuditStatus() != 2) {
							Map<String, Object> itemMap = employeeWageItemService.getEmployeeWageTemplate(employee);
							employeeWage = employeeWageService.realStatEmployeeWageInfo(employeeWage,itemMap);
						}
						
						//统计界面，审核未通过不须返回
						if ("统计".equals(type) && employeeWage.getAuditStatus() != 2) {
							continue;
						}
						
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
								//员工实际工资项填充
								if (CollectionUtils.isNotEmpty(employeeWage.getWageItems())) {
									List<EmployeeWageActualItem> wageActualItems = employeeWage.getWageItems();
									for (EmployeeWageActualItem wageItem : wageActualItems) {
										if (wageCountItem.getId().equals(wageItem.getWageCountItem().getId())) {
											wageCountMap.put("model", wageItem);
											wageCountMap.put("backgroundColor", "#FFCC33");
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
	 * 审核档案,更新档案状态
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions(value = {"emp:wagecheckmgr:audit","emp:wagecheckmgr:sendaudit","emp:wagecheckmgr:resetaudit"},logical=Logical.OR)
	@OperationLog(content = "审核员工实发工资状态" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/audit", method = RequestMethod.POST)
	public AjaxJson audit(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--审核员工实发工资状态--");
		AjaxJson json = new AjaxJson();
		try {
			String wagesJson = (String) params.get("wages");
			JSONParser jsonParser = new JSONParser(wagesJson);
			List<Object> wageDatas = jsonParser.parseArray();
			if (CollectionUtils.isNotEmpty(wageDatas)) {
				for (Object item : wageDatas) {
					Map<String, Object> map = (Map<String, Object>) item;
					String wageId = StringUtils.defaultString((String) map.get("employeeWageId"), "0");
					EmployeeWage employeeWage = employeeWageService.get(wageId);
					if (employeeWage != null) {
						String auditStatus = (String) params.get("auditStatus");
						if (StringUtils.isBlank(auditStatus)) {
							auditStatus = (String) params.get("ud_auditStatus");
						}
						employeeWage.setAuditStatus(NumberUtils.toInt(auditStatus, 0));
						employeeWage.setAuditContent((String) params.get("ud_auditContent"));
						employeeWageService.saveOrUpdate(employeeWage);
						
						//当审核通过时，需要往工资历史表中填充历史记录
						if ("2".equals(auditStatus)) {
							wageHisService.cloneByWage(employeeWage);
						}
						
					}
				}
				json.setSuccess(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	/**
	 * 导出员工工资核实Excel
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("emp:wagecheckmgr:export")
	@OperationLog(content = "导出员工工资核实Excel" , type = OperationType.VIEW)
	@ResponseBody
	@RequestMapping(value = "/check_export", method = RequestMethod.POST)
	public void check_export(HttpSession session,HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> params) {
		logger.info("--导出员工工资核实Excel--");
		try {
			DataGridJson dataGridJson = list(session, request, params);
			String fileName = "员工工资配置.xlsx";
			String filePath = getExportPath(request) + fileName;
			List<ExcelColumn> excelColumns = wagePlanController.getExportExcelColumnHeader(session, request, params);
			if (CollectionUtils.isNotEmpty(excelColumns)) {
				excelColumns.add(0, new ExcelColumn("姓名", "employeeName", 100));
				excelColumns.add(0, new ExcelColumn("岗位", "postName", 120));
				excelColumns.add(0, new ExcelColumn("部门", "deptName", 120));
				excelColumns.add(0, new ExcelColumn("审核状态", "auditStatusDict", 100));
				removeUnit(excelColumns);
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
	
	/**
	 * 导出员工工资统计Excel
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("emp:wagestatmgr:export")
	@OperationLog(content = "导出员工工资统计Excel" , type = OperationType.VIEW)
	@ResponseBody
	@RequestMapping(value = "/stat_export", method = RequestMethod.POST)
	public void stat_export(HttpSession session,HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> params) {
		logger.info("--导出员工工资统计Excel--");
		try {
			DataGridJson dataGridJson = list(session, request, params);
			String fileName = "员工工资统计.xlsx";
			String filePath = getExportPath(request) + fileName;
			List<ExcelColumn> excelColumns = wagePlanController.getExportExcelColumnHeader(session, request, params);
			if (CollectionUtils.isNotEmpty(excelColumns)) {
				excelColumns.add(0, new ExcelColumn("银行卡号", "bankCard", 100));
				excelColumns.add(0, new ExcelColumn("姓名", "employeeName", 100));
				excelColumns.add(0, new ExcelColumn("岗位", "postName", 120));
				excelColumns.add(0, new ExcelColumn("部门", "deptName", 120));
				removeUnit(excelColumns);
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
	
	/**
	 * 核实与统计去除单位
	 * 
	 * @param excelColumns
	 */
	private void removeUnit(List<ExcelColumn> excelColumns){
		if (CollectionUtils.isNotEmpty(excelColumns)) {
			for (ExcelColumn excelColumn : excelColumns) {
				if (excelColumn.getTitle().indexOf("（") != -1) {
					excelColumn.setTitle(excelColumn.getTitle().substring(0, excelColumn.getTitle().indexOf("（")));
				}
				removeUnit(excelColumn.getChildren());
			}
		}
	}
	
}
