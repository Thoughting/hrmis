package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.baseframe.common.utils.excel.CustomizeToExcel;
import com.eastcom.baseframe.common.utils.excel.ExcelColumn;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.common.utils.DownloadUtil;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.baseframe.web.modules.sys.cache.DynamicgridItemCache;
import com.eastcom.baseframe.web.modules.sys.entity.DynamicgridItem;
import com.eastcom.hrmis.modules.emp.service.EmployeeService;
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
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 人员统计Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/stat")
public class EmployeeStatController extends BaseController {

	@Autowired
	private EmployeeService employeeService;
	
	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:employeestat:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取员工对比统计信息--");
		DataGridJson gridJson = new DataGridJson();
		try {
			String deptId = (String) params.get("deptId");
			Date compareDate1 = DateUtils.parseDate((String) params.get("compareDate1"),"yyyy年-MM月");
			Date compareDate2 = DateUtils.parseDate((String) params.get("compareDate2"),"yyyy年-MM月");
			
			List<Map<String, Object>> list = Lists.newArrayList();
			
			Map<String, Object> compareMap1 = employeeService.getEmployeeStatByDeptIdAndDate(deptId, compareDate1);
			Map<String, Object> compareMap2 = employeeService.getEmployeeStatByDeptIdAndDate(deptId, compareDate2);
			
			for (Map.Entry<String, Object> entry : compareMap1.entrySet()) {  
				if (!"statDate".equals(entry.getKey())) {
					DecimalFormat decimalFormat = new DecimalFormat("#.00");
					double v1 = (Integer)entry.getValue();
					double v2 = (Integer)compareMap2.get(entry.getKey());
					if (v2 == 0) {
						compareMap1.put(entry.getKey(), v1 + " (" + v1 * 100 + "%)");
					} else if(v1 == v2){
						compareMap1.put(entry.getKey(), v1 + " (" + 0.0 + "%)");
					} else {
						compareMap1.put(entry.getKey(), (int) v1 + " (" + decimalFormat.format((v1 - v2) / v2 * 100) + "%)");
					}
				}
			} 
			
			list.add(compareMap1);
			list.add(compareMap2);
			gridJson.setRows(list);
			gridJson.setTotal((long) list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gridJson;
	}
	
	/**
	 * 导出员工统计Excel
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:employeestat:export")
	@OperationLog(content = "导出员工统计Excel" , type = OperationType.VIEW)
	@ResponseBody
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(HttpSession session,HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> params) {
		logger.info("--导出员工统计Excel--");
		try {
			DataGridJson dataGridJson = list(session, request, params);
			
			String fileName = "员工统计对比.xlsx";
			String filePath = getExportPath(request) + fileName;
			List<ExcelColumn> excelColumns = DynamicgridItem.toExcelColumns(DynamicgridItemCache.getDynamicgridItemsByName("人员统计分析表格"));
			if (CollectionUtils.isNotEmpty(excelColumns)) {
				ObjectMapper m = new ObjectMapper(); 
				String json = m.writeValueAsString(dataGridJson.getRows());
				List<Map<String,Object>> data = convertJsonArrToMap(json);
				CustomizeToExcel.toFile(excelColumns, data ,filePath);
			}
			DownloadUtil.downloadFile(filePath, fileName, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
