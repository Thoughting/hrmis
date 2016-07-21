package com.eastcom.hrmis.modules.emp.web.controller.api;

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
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.eastcom.hrmis.modules.emp.entity.EmployeeCheckWork;
import com.eastcom.hrmis.modules.emp.entity.EmployeeSetWork;
import com.eastcom.hrmis.modules.emp.service.EmployeeCheckWorkService;
import com.eastcom.hrmis.modules.emp.service.EmployeeService;
import com.eastcom.hrmis.modules.emp.service.EmployeeSetWorkService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 排班与考勤差异统计
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/differencework")
public class DifferenceWorkController extends BaseController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeSetWorkService employeeSetWorkService;
	
	@Autowired
	private EmployeeCheckWorkService employeeCheckWorkService;
	
	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:setandcheckworkdiff:view")
	@SuppressWarnings("unchecked")
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
			PageHelper<Employee> empPageHelper = employeeService.find(reqParams, pageNo, pageSize);
			
			if (CollectionUtils.isNotEmpty(empPageHelper.getList())) {
				for (Employee employee : empPageHelper.getList()) {
					Map<String, Object> item = Maps.newHashMap();
					item.put("employeeId", employee.getId());
					item.put("name", employee.getName());
					item.put("code", employee.getCode());
					
					//查询员工排班记录
					List<EmployeeSetWork> setWorks = employeeSetWorkService.getSetWorkMonthDate(employee.getId(), DateUtils.parseDate((String) params.get("setWorkDate"), "yyyy年-MM月"));
					if (CollectionUtils.isNotEmpty(setWorks)) {
						for (EmployeeSetWork employeeSetWork : setWorks) {
							Map<String, Object> temp = (Map<String, Object>) item.get(DateUtils.formatDate(employeeSetWork.getWorkDate(), "yyyy-MM-dd"));
							if (temp == null) {
								temp = Maps.newHashMap();
							}
							temp.put("setwork", employeeSetWork);
							item.put(DateUtils.formatDate(employeeSetWork.getWorkDate(), "yyyy-MM-dd"), temp);
						}
					}
					
					//查询员工考勤记录
					List<EmployeeCheckWork> checkWorks = employeeCheckWorkService.getCheckWorkMonthDate(employee.getId(), DateUtils.parseDate((String) params.get("setWorkDate"), "yyyy年-MM月"));
					if (CollectionUtils.isNotEmpty(checkWorks)) {
						for (EmployeeCheckWork employeeCheckWork : checkWorks) {
							Map<String, Object> temp = (Map<String, Object>) item.get(DateUtils.formatDate(employeeCheckWork.getWorkDate(), "yyyy-MM-dd"));
							if (temp == null) {
								temp = Maps.newHashMap();
							}
							temp.put("checkwork", employeeCheckWork);
							item.put(DateUtils.formatDate(employeeCheckWork.getWorkDate(), "yyyy-MM-dd"), temp);
						}
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
	 * 排班考勤差异统计Excel
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("emp:setandcheckworkdiff:export")
	@OperationLog(content = "排班考勤差异统计Excel" , type = OperationType.VIEW)
	@ResponseBody
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(HttpSession session,HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> params) {
		logger.info("--排班考勤差异统计Excel--");
		try {
			Date date = DateUtils.parseDate((String) params.get("setWorkDate"), "yyyy年-MM月");
			
			DataGridJson dataGridJson = list(session, request, params);
			String fileName = "排班考勤差异统计.xlsx";
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
							if (key.indexOf("-") != -1) {
								Map<String,Object> temp = (Map<String, Object>) map.get(key);
								if (temp != null) {
									String describe = "";
									String setworkDescribe = "";
									String checkworkDescribe = "";
									Map<String, Object> setwork = (Map<String, Object>) temp.get("setwork");
									if (setwork != null) {
										List<String> gridCellDescribe = (List<String>) setwork.get("gridCellDescribe");
										setworkDescribe = gridCellDescribe.get(1);
										setworkDescribe = setworkDescribe.replace("<font size=\"4\">", "");
										setworkDescribe = setworkDescribe.replace("</font>", "");
										setworkDescribe = setworkDescribe.replace("&nbsp;", "");
										setworkDescribe = setworkDescribe.replace("<br/>", "\n");
									}
									Map<String, Object> checkwork = (Map<String, Object>) temp.get("checkwork");
									if (checkwork != null) {
										List<String> gridCellDescribe = (List<String>) checkwork.get("gridCellDescribe");
										checkworkDescribe = gridCellDescribe.get(1);
										checkworkDescribe = checkworkDescribe.replace("<font size=\"4\">", "");
										checkworkDescribe = checkworkDescribe.replace("</font>", "");
										checkworkDescribe = checkworkDescribe.replace("&nbsp;", "");
										checkworkDescribe = checkworkDescribe.replace("<br/>", "\n");
									}
									
									if (StringUtils.isNotBlank(setworkDescribe) || StringUtils.isNotBlank(checkworkDescribe)) {
										describe += StringUtils.isNotBlank(setworkDescribe) ? setworkDescribe : "全天";
										describe += "\n----\n";
										describe += StringUtils.isNotBlank(checkworkDescribe) ? checkworkDescribe : "全天";
									}
									map.put(key, describe);
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
