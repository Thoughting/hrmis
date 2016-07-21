package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.eastcom.baseframe.common.utils.excel.CustomizeToExcel;
import com.eastcom.baseframe.common.utils.excel.ExcelColumn;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.common.utils.DownloadUtil;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.hrmis.modules.emp.service.EmployeeCheckWorkStatMonthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
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
 * 员工考勤统计Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/checkworkstat")
public class EmployeeCheckWorkStatMonthController extends BaseController {

	@Autowired
	private EmployeeCheckWorkStatMonthService employeeCheckWorkStatMonthService;
	
	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:checkworkstat:view")
	@ResponseBody
	@RequestMapping(value = "/dept_list", method = RequestMethod.POST)
	public DataGridJson dept_list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取部门考勤统计情况列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			String statMonth = (String) params.get("statMonth");
			
			//实时统计汇聚
			//employeeCheckWorkStatMonthService.refreshCheckWorkStatMonth(statMonth);
			
			List<Map<String, Object>> result = employeeCheckWorkStatMonthService.getDeptCheckWorkStatByStatMonth(statMonth);
			if (CollectionUtils.isNotEmpty(result)) {
				gridJson.setRows(result);
				gridJson.setTotal((long)result.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gridJson;
	}
	
	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:checkworkstat:view")
	@ResponseBody
	@RequestMapping(value = "/emp_list", method = RequestMethod.POST)
	public DataGridJson emp_list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取员工考勤统计情况列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			String statMonth = (String) params.get("statMonth");
			String deptId = (String) params.get("deptId");
			List<Map<String, Object>> result = employeeCheckWorkStatMonthService.getEmployeeCheckWorkStatByDeptIdAndStatMonth(deptId, statMonth);
			if (CollectionUtils.isNotEmpty(result)) {
				gridJson.setRows(result);
				gridJson.setTotal((long)result.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gridJson;
	}
	
	/**
	 * 导出部门考勤统计Excel
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:checkworkstat:export")
	@OperationLog(content = "" , type = OperationType.VIEW)
	@ResponseBody
	@RequestMapping(value = "/dept_export", method = RequestMethod.POST)
	public void dept_export(HttpSession session,HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> params) {
		logger.info("--导出部门考勤统计Excel--");
		try {
			DataGridJson dataGridJson = dept_list(session, request, params);
			String fileName = "部门考勤统计.xlsx";
			String filePath = getExportPath(request) + fileName;
			List<ExcelColumn> excelColumns = getDeptExportExcelColumnHeader();
			if (CollectionUtils.isNotEmpty(excelColumns)) {
				ObjectMapper m = new ObjectMapper(); 
				String json = m.writeValueAsString(dataGridJson.getRows());
				List<Map<String,Object>> data = convertJsonArrToMap(json);
				if (CollectionUtils.isNotEmpty(data)) {
					for (Map<String, Object> map : data) {
						for (Map.Entry<String, Object> entry : map.entrySet()) {
							if (entry.getValue() == null || (entry.getValue() instanceof Double && (Double) entry.getValue() == 0)) {
								entry.setValue(0);
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
	 * excel导出月份表头
	 * @param date
	 * @return
	 */
	private List<ExcelColumn> getDeptExportExcelColumnHeader(){
		List<ExcelColumn> excelColumns = Lists.newArrayList();
		
		excelColumns.add(new ExcelColumn("项目", "NAME", 120));
		excelColumns.add(new ExcelColumn("人数", "EMP_COUNT", 80));
		excelColumns.add(new ExcelColumn("迟到（人次）", "CD_CS_COUNT", 100));
		excelColumns.add(new ExcelColumn("早退（人次）", "ZT_CS_COUNT", 100));
		excelColumns.add(new ExcelColumn("旷工（天）", "KG_SC_DAY", 100));
		excelColumns.add(new ExcelColumn("加班（天）", "JBL_XSC_DAY", 100));
		excelColumns.add(new ExcelColumn("休假（天）", "ZCXJ_SC_DAY", 100));
		excelColumns.add(new ExcelColumn("补休（天）", "BXJ_SC_DAY", 100));
		excelColumns.add(new ExcelColumn("事假（天）", "SHJ_SC_DAY", 100));
		excelColumns.add(new ExcelColumn("病假（天）", "BJ_SC_DAY", 100));
		excelColumns.add(new ExcelColumn("婚假（天）", "HJ_SC_DAY", 100));
		excelColumns.add(new ExcelColumn("产假（天）", "CJ_SC_DAY", 100));
		excelColumns.add(new ExcelColumn("丧假（天）", "SJ_SC_DAY", 100));
		excelColumns.add(new ExcelColumn("年休假（天）", "NXJ_SC_DAY", 100));
		
		return excelColumns;
	}
}
