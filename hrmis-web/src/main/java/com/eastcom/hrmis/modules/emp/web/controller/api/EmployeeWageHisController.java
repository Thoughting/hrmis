package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.eastcom.baseframe.common.persistence.PageHelper;
import com.eastcom.baseframe.common.utils.excel.CustomizeToExcel;
import com.eastcom.baseframe.common.utils.excel.ExcelColumn;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.common.utils.DownloadUtil;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWageHis;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWageHisItem;
import com.eastcom.hrmis.modules.emp.entity.WageCountItem;
import com.eastcom.hrmis.modules.emp.service.EmployeeWageHisService;
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
 * 员工统计工资历史
 * @author wtg
 *
 */
@Controller
@RequestMapping(value="/api/emp/wagehis")
public class EmployeeWageHisController extends BaseController {

	@Autowired
	private EmployeeWageHisService employeeWageHisService;
	
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
	@RequiresPermissions("emp:wagehismgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取员工历史工资信息--");
		DataGridJson gridJson = new DataGridJson();
		try {
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 99999);
			params.remove("page");
			params.remove("rows");
			
			List<Map<String, Object>> result = Lists.newArrayList();
			
			PageHelper<EmployeeWageHis> pageHelper = employeeWageHisService.find(params, pageNo, pageSize);
			if (CollectionUtils.isNotEmpty(pageHelper.getList())) {
				for (EmployeeWageHis wageHis : pageHelper.getList()) {
					Map<String, Object> item = Maps.newHashMap();
					item.put("id", wageHis.getId());
					item.put("wageDate", wageHis.getWageDateStr());
					item.put("employeeName", wageHis.getEmployee().getName());
					item.put("deptName", wageHis.getDeptName());
					item.put("postName", wageHis.getPostName());
					item.put("bankCard", wageHis.getBankCard());
					//工资核算项
					if (wageHis.getWagePlan() != null && CollectionUtils.isNotEmpty(wageHis.getWagePlan().getCountItems())) {
						List<WageCountItem> countItems = wageHis.getWagePlan().getCountItems();
						for (WageCountItem wageCountItem : countItems) {
							Map<String, Object> wageCountMap = Maps.newHashMap();
							wageCountMap.put("wageCountItemId", wageCountItem.getId());
							//员工历史工资项填充
							if (CollectionUtils.isNotEmpty(wageHis.getWageItems())) {
								List<EmployeeWageHisItem> wageItems = wageHis.getWageItems();
								for (EmployeeWageHisItem wageItem : wageItems) {
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
			
			gridJson.setRows(result);
			gridJson.setTotal(pageHelper.getCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gridJson;
	}
	
	/**
	 * 导出员工工资统计Excel
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("emp:wagehismgr:export")
	@OperationLog(content = "导出员工工资历史Excel" , type = OperationType.VIEW)
	@ResponseBody
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(HttpSession session,HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> params) {
		logger.info("--导出员工工资历史Excel--");
		try {
			DataGridJson dataGridJson = list(session, request, params);
			String fileName = "员工工资历史.xlsx";
			String filePath = getExportPath(request) + fileName;
			List<ExcelColumn> excelColumns = wagePlanController.getExportExcelColumnHeader(session, request, params);
			if (CollectionUtils.isNotEmpty(excelColumns)) {
				excelColumns.add(0, new ExcelColumn("银行卡号", "bankCard", 100));
				excelColumns.add(0, new ExcelColumn("姓名", "employeeName", 100));
				excelColumns.add(0, new ExcelColumn("岗位", "postName", 120));
				excelColumns.add(0, new ExcelColumn("部门", "deptName", 120));
				excelColumns.add(0, new ExcelColumn("统计月份", "wageDate", 120));
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
