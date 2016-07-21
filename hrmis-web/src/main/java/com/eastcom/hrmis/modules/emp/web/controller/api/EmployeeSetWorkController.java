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
import com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache;
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.eastcom.hrmis.modules.emp.entity.EmployeeSetWork;
import com.eastcom.hrmis.modules.emp.entity.EmployeeSetWorkPlan;
import com.eastcom.hrmis.modules.emp.service.EmployeeService;
import com.eastcom.hrmis.modules.emp.service.EmployeeSetWorkPlanService;
import com.eastcom.hrmis.modules.emp.service.EmployeeSetWorkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.DateUtil;
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
 * 员工排班Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/setwork")
public class EmployeeSetWorkController extends BaseController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeSetWorkService employeeSetWorkService;
	
	@Autowired
	private EmployeeSetWorkPlanService employeeSetWorkPlanService;
	
	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:setworkmgr:view")
	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取员工排班情况列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			List<Map<String, Object>> result = Lists.newArrayList();
			//查询员工档案
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 99999);
			
			String employeeDept = (String) params.get("employeeDept");
			Map<String, Object> reqParams = Maps.newHashMap();
			reqParams.put("employeeDept", employeeDept);
			reqParams.put("auditStatus", 2);
			Date d = DateUtils.parseDate((String) params.get("setWorkDate"), "yyyy年-MM月");
			d.setDate(1);
			reqParams.put("start_quitCompanyDate", DateUtils.formatDate(d)); // 如果已离职，离职时间需要大于当前月份
			d.setMonth(d.getMonth() + 1);
			d.setDate(d.getDate() - 1);
			reqParams.put("end_enrtyDate", DateUtils.formatDate(d)); // 入职时间需要小于当前月份+1
			PageHelper<Employee> empPageHelper = employeeService.find(reqParams, pageNo, pageSize);
			
			if (CollectionUtils.isNotEmpty(empPageHelper.getList())) {
				for (Employee employee : empPageHelper.getList()) {
					Map<String, Object> item = Maps.newHashMap();
					item.put("employeeId", employee.getId());
					item.put("name", employee.getName());
					item.put("code", employee.getCode());
					item.put("quitCompanyDate", employee.getQuitCompanyDate() != null ? DateUtils.formatDate(employee.getQuitCompanyDate()) : null); // 离职日期
					item.put("entryDate", employee.getEnrtyDate() != null ? DateUtils.formatDate(employee.getEnrtyDate()) : null);// 入职时间
					item.put("workMonth", (String) params.get("setWorkDate"));
					
					//查询员工排班记录
					Date setWorkDate = DateUtils.parseDate((String) params.get("setWorkDate"), "yyyy年-MM月");
					List<EmployeeSetWork> setWorks = employeeSetWorkService.getSetWorkMonthDate(employee.getId(), setWorkDate);
					if (CollectionUtils.isNotEmpty(setWorks)) {
						for (EmployeeSetWork employeeSetWork : setWorks) {
							item.put(DateUtils.formatDate(employeeSetWork.getWorkDate(), "yyyy-MM-dd"), employeeSetWork);
						}
					}

					//入职框
					if (employee.getEnrtyDate() != null){
						item.put(DateUtils.formatDate(DateUtils.addDays(employee.getEnrtyDate(),-1)),"入职");
					}

					//离职框
					if (employee.getQuitCompanyDate() != null){
						item.put(DateUtils.formatDate(DateUtils.addDays(employee.getQuitCompanyDate(),1)),"离职");
					}

					//查询是否有员工排班计划，并判断是否已提交，已提交的不能编辑
					reqParams = Maps.newHashMap();
					reqParams.put("employeeId", employee.getId());
					reqParams.put("workMonth", (String) params.get("setWorkDate"));
					EmployeeSetWorkPlan setWorkPlan = employeeSetWorkPlanService.getUnique(reqParams);
					if (setWorkPlan != null) {
						item.put("commitStatus", setWorkPlan.getCommitStatus());
						item.put("commitStatusDict", setWorkPlan.getCommitStatusDict());
					} else {
						item.put("commitStatus", 0);
						item.put("commitStatusDict", "未提交");
					}
					result.add(item);
				}
			}
			gridJson.setRows(result);
			gridJson.setTotal(empPageHelper.getCount());
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
	@RequiresPermissions("emp:setworkmgr:edit")
	@OperationLog(content = "新增或者修改排班" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改排班--");
		AjaxJson json = new AjaxJson();
		try {
			String employeeId = StringUtils.defaultIfBlank((String) params.get("employeeId"), "0");
			Employee employee = employeeService.get(employeeId);
			if (employee != null) {
				String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
				EmployeeSetWork employeeSetWork = employeeSetWorkService.get(id);
				if (employeeSetWork == null) {
					employeeSetWork = new EmployeeSetWork();
					employeeSetWork.setEmployee(employee);
				}
				employeeSetWork.setWorkDate(DateUtils.parseDate((String) params.get("setWorkDate")));
				employeeSetWork.setIsAtWork(NumberUtils.toInt((String) params.get("isAtWork"), 1));
				employeeSetWork.setAtWorkTimer(NumberUtils.toInt((String) params.get("atWorkTimer"), 0));
				employeeSetWork.setAtWorkSpecialStatus(NumberUtils.toInt((String) params.get("atWorkSpecialStatus"), 0));
				employeeSetWork.setAtHolidayType(NumberUtils.toInt((String) params.get("atHolidayType"), 0));
				employeeSetWork.setAtHolidayHour(NumberUtils.toInt((String) params.get("atHolidayHour"), 0));
				employeeSetWork.setAtHolidayMinute(NumberUtils.toInt((String) params.get("atHolidayMinute"), 0));
				employeeSetWork.setRemark((String) params.get("remark"));
				employeeSetWork.setAtHolidayTimer(NumberUtils.toInt((String) params.get("atHolidayTimer"), 0));

				employeeSetWorkService.saveOrUpdate(employeeSetWork);
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
	 * 提交排班
	 * 
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("emp:setworkmgr:commit")
	@OperationLog(content = "提交排班" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/commit", method = RequestMethod.POST)
	public AjaxJson commit(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--提交排班--");
		AjaxJson json = new AjaxJson();
		try {
			String employeesJson = (String) params.get("employees");
			JSONParser jsonParser = new JSONParser(employeesJson);
			List<Object> employeeDatas = jsonParser.parseArray();
			if (CollectionUtils.isNotEmpty(employeeDatas)) {
				for (Object item : employeeDatas) {
					Map<String, Object> map = (Map<String, Object>) item;
					String employeeId = StringUtils.defaultString((String) map.get("employeeId"), "0");
					Employee employee = employeeService.get(employeeId);
					if (employee != null) {
						String workDate = (String) params.get("setWorkDate");
						Map<String, Object> reqParam = Maps.newHashMap();
						reqParam.put("employeeId", employee.getId());
						reqParam.put("workMonth", workDate);
						EmployeeSetWorkPlan setWorkPlan = employeeSetWorkPlanService.getUnique(reqParam);
						if (setWorkPlan == null) {
							setWorkPlan = new EmployeeSetWorkPlan();
							setWorkPlan.setEmployee(employee);
							setWorkPlan.setWorkMonth(workDate);
						}
						setWorkPlan.setCommitStatus(1);
						setWorkPlan.setCommiter(SecurityCache.getLoginUser().getLoginName());
						setWorkPlan.setCommitDate(new Date());
						employeeSetWorkPlanService.saveOrUpdate(setWorkPlan);
					}
				}
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
	
	/**
	 * 回退提交排班
	 * 
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("emp:setworkmgr:rollback")
	@OperationLog(content = "回滚提交排班" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/rollback", method = RequestMethod.POST)
	public AjaxJson rollback(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--回滚提交排班--");
		AjaxJson json = new AjaxJson();
		try {
			String employeesJson = (String) params.get("employees");
			JSONParser jsonParser = new JSONParser(employeesJson);
			List<Object> employeeDatas = jsonParser.parseArray();
			if (CollectionUtils.isNotEmpty(employeeDatas)) {
				String workDate = (String) params.get("setWorkDate");
				for (Object item : employeeDatas) {
					Map<String, Object> map = (Map<String, Object>) item;
					String employeeId = StringUtils.defaultString((String) map.get("employeeId"), "0");
					Map<String, Object> reqParam = Maps.newHashMap();
					reqParam.put("employeeId", employeeId);
					reqParam.put("workMonth", workDate);
					EmployeeSetWorkPlan setWorkPlan = employeeSetWorkPlanService.getUnique(reqParam);
					if (setWorkPlan != null) {
						setWorkPlan.setCommitStatus(0);
						setWorkPlan.setCommiter(SecurityCache.getLoginUser().getLoginName());
						setWorkPlan.setCommitDate(new Date());
						employeeSetWorkPlanService.saveOrUpdate(setWorkPlan);
					}
				}
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
	
	/**
	 * 导出排班Excel
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("emp:setworkmgr:export")
	@OperationLog(content = "导出排班Excel" , type = OperationType.VIEW)
	@ResponseBody
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(HttpSession session,HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> params) {
		logger.info("--导出排班Excel--");
		try {
			Date date = DateUtils.parseDate((String) params.get("setWorkDate"), "yyyy年-MM月");
			
			DataGridJson dataGridJson = list(session, request, params);
			String fileName = "员工排班.xlsx";
			String filePath = getExportPath(request) + fileName;
			List<ExcelColumn> excelColumns = getExportExcelColumnHeader(date);
			if (CollectionUtils.isNotEmpty(excelColumns)) {
				ObjectMapper m = new ObjectMapper(); 
				String json = m.writeValueAsString(dataGridJson.getRows());
				List<Map<String,Object>> data = convertJsonArrToMap(json);
				if (CollectionUtils.isNotEmpty(data)) {
					for (Map<String, Object> map : data) {
						System.out.println("------------");
						for (String key : map.keySet()) {
							if (key.indexOf("-") != -1 && !(map.get(key) instanceof String)) {
								Map<String,Object> temp = (Map<String, Object>) map.get(key);
								if (temp != null) {
									List<String> gridCellDescribe = (List<String>) temp.get("gridCellDescribe");
									String describe = gridCellDescribe.get(1);
									describe = describe.replace("<font size=\"4\">", "");
									describe = describe.replace("</font>", "");
									describe = describe.replace("&nbsp;", "");
									describe = describe.replace("<br/>", "\n");
									map.put(key, describe);
								} else {
									map.put(key, 0);
								}

							}
							if ("kgTj".equals(key)) {
								map.put(key, ((String )map.get(key)).replace("<br/>", "\n"));
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
	 * 根据月份得到表格头
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@OperationLog(content = "" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/column", method = RequestMethod.POST)
	public AjaxJson column(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--根据月份得到表格头--");
		AjaxJson json = new AjaxJson();
		try {
			Date date = DateUtils.parseDate((String) params.get("date"), "yyyy年-MM月");
			List<List<Map<String, Object>>> columns = getMonthColumnHeader(date);
			json.setModel(columns);
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
	 * 月份表头
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private List<List<Map<String, Object>>> getMonthColumnHeader(Date date){
		List<List<Map<String, Object>>> columns = Lists.newArrayList();
		List<Map<String, Object>> column_1 = Lists.newArrayList();
		List<Map<String, Object>> column_2 = Lists.newArrayList();
		int maxDay = DateUtils.getMonthMaxDay(date);
		for (int i = 1; i <= maxDay; i++) {
			date.setDate(i);
			Map<String, Object> item_1 = Maps.newHashMap();
			item_1.put("title", DateUtils.formatDate(date, "dd"));
			item_1.put("align", "center");
			item_1.put("width", 50);
			item_1.put("colspan", 1);
			column_1.add(item_1);
			
			Map<String, Object> item_2 = Maps.newHashMap();
			item_2.put("field", DateUtils.formatDate(date, "yyyy-MM-dd"));
			item_2.put("title", DateUtils.getWeekOfDate(date).replaceAll("星期", ""));
			item_2.put("align", "center");
			item_2.put("width", 50);
			column_2.add(item_2);
		}
		columns.add(column_1);
		columns.add(column_2);
		return columns;
	}
	
	/**
	 * excel导出月份表头
	 * @param date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private List<ExcelColumn> getExportExcelColumnHeader(Date date){
		List<ExcelColumn> excelColumns = Lists.newArrayList();
		
		excelColumns.add(new ExcelColumn("编号", "code", 80));
		excelColumns.add(new ExcelColumn("姓名", "name", 80));
		
		//日期头
		int maxDay = DateUtils.getMonthMaxDay(date);
		for (int i = 1; i <= maxDay; i++) {
			date.setDate(i);
			ExcelColumn dateColumn = new ExcelColumn(DateUtils.formatDate(date, "dd"), "", 50);
			dateColumn.getChildren().add(new ExcelColumn(DateUtils.getWeekOfDate(date).replaceAll("星期", ""), DateUtils.formatDate(date, "yyyy-MM-dd"), 50));
			excelColumns.add(dateColumn);
		}
		
		return excelColumns;
	}
	
}
