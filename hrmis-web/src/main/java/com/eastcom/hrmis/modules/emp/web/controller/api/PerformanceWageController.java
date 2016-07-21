package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.eastcom.baseframe.common.persistence.PageHelper;
import com.eastcom.baseframe.common.utils.excel.CustomizeToExcel;
import com.eastcom.baseframe.common.utils.excel.ExcelColumn;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.common.utils.DownloadUtil;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.baseframe.web.modules.sys.cache.DynamicgridItemCache;
import com.eastcom.baseframe.web.modules.sys.entity.DynamicgridItem;
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.eastcom.hrmis.modules.emp.entity.EmployeeCheckWorkStatMonth;
import com.eastcom.hrmis.modules.emp.entity.PerformanceWage;
import com.eastcom.hrmis.modules.emp.entity.PerformanceWageItem;
import com.eastcom.hrmis.modules.emp.service.EmployeeCheckWorkStatMonthService;
import com.eastcom.hrmis.modules.emp.service.EmployeeService;
import com.eastcom.hrmis.modules.emp.service.PerformanceWageService;
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
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 绩效工资Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/performancewage")
public class PerformanceWageController extends BaseController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private PerformanceWageService performanceWageService;
	
	@Autowired
	private EmployeeCheckWorkStatMonthService statMonthService;
	
	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:performancewagemgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取绩效工资信息--");
		DataGridJson gridJson = new DataGridJson();
		try {
			List<Map<String, Object>> result = Lists.newArrayList();
			
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 99999);
			int type = NumberUtils.toInt((String) params.get("type"), 0);
			
			//得到员工信息
			Map<String, Object> reqParam = Maps.newHashMap();
			reqParam.put("auditStatus", 2); // 审核通过
			reqParam.put("performanceWageType", type); // 绩效工资类型
			PageHelper<Employee> pageHelper = employeeService.find(reqParam, pageNo, pageSize);
			if (CollectionUtils.isNotEmpty(pageHelper.getList())) {
				DecimalFormat decimalFormat = new DecimalFormat("#.00");
				
				List<DynamicgridItem> dynamicgridItems = DynamicgridItemCache.getDynamicgridLeafItemsByName(type == 1 ? "管理人员遵章绩效奖金表" : "文员绩效考核等级表");;
				for (Employee employee : pageHelper.getList()) {
					Map<String, Object> item = Maps.newHashMap();
					item.put("employeeName", employee.getName());
					item.put("cardId", employee.getCardNo());
					
					//查询绩效工资
					reqParam = Maps.newHashMap();
					reqParam.put("wageDateStr", (String) params.get("wageDateStr"));
					reqParam.put("type", type);
					reqParam.put("employeeId", employee.getId());
					PerformanceWage performanceWage = performanceWageService.getUnique(reqParam);
					if (performanceWage == null) {
						//如果不存在，生成该月份的绩效工资
						performanceWage = new PerformanceWage();
						performanceWage.setEmployee(employee);
						performanceWage.setType(employee.getPerformanceWageType());
						performanceWage.setWageDateStr((String) params.get("wageDateStr"));
						performanceWageService.saveOrUpdate(performanceWage);
					}
					item.put("performanceWageId", performanceWage.getId());
					//填充绩效工资项
					if (CollectionUtils.isNotEmpty(dynamicgridItems)) {
						//管理人员绩效统计项
						//小计
						double xjTotal = 0;
						String xjTotalCountField = "zzjxjze(遵章绩效奖总额),zlgljj(质量管理奖金),qita(其他)";
						//扣款合计
						double koukuan1 = 0;
						String koukuan1Field = "koukuan1(扣款)";
						
						//文员绩效考核等级表
						double sfzzjTotal = 0;
						String sfzzjTotalCountField = "zzjjs(遵章奖基数（元）),zpgl(招聘管理),kqgl(考勤管理),xcgl_rzbsh(薪酬管理（人资部审核）),xcgl_cwbsh(薪酬管理（财务部审核）),rsxbgz(人事协办工作),rzgl(入职管理),ydgl(异动管理),lzgl(离职管理),dahtgl(档案合同管理),ldgx(劳动关系)";
						
						double khbl = 0;
						String khblField = "khbl(考核比例)";
						double khje = 0;
						String khjeField = "khje考核金额（元）";
						
						double qtkk = 0;
						String qtkkField = "qtkk其他扣款";
						
						List<PerformanceWageItem> wageItems = performanceWage.getWageItems();
						for (DynamicgridItem dynamicgridItem : dynamicgridItems) {
							Map<String, Object> temp = Maps.newHashMap();
							temp.put("field", dynamicgridItem.getField());
							if (CollectionUtils.isNotEmpty(wageItems)) {
								for (PerformanceWageItem wageItem : wageItems) {
									if (wageItem.getField().equals(dynamicgridItem.getField())) {
										temp.put("model", wageItem);
										//管理人员绩效统计项
										if (xjTotalCountField.indexOf(wageItem.getField()) != -1) {
											xjTotal += Double.parseDouble(wageItem.getValue());
										}
										if (koukuan1Field.indexOf(wageItem.getField()) != -1) {
											koukuan1 = Double.parseDouble(wageItem.getValue());
										}
										//文员绩效考核等级表统计项
										if (sfzzjTotalCountField.indexOf(wageItem.getField()) != -1) {
											sfzzjTotal += Double.parseDouble(wageItem.getValue());
										}
										if (khblField.indexOf(wageItem.getField()) != -1) {
											khbl = Double.parseDouble(wageItem.getValue());
										}
										if (khjeField.indexOf(wageItem.getField()) != -1) {
											khje = Double.parseDouble(wageItem.getValue());
										}
										if (qtkkField.indexOf(wageItem.getField()) != -1) {
											qtkk = Double.parseDouble(wageItem.getValue());
										}
									}
								}
							}
							//身份证号，姓名
							if (!("cardId".equals(dynamicgridItem.getField()) || "employeeName".equals(dynamicgridItem.getField()))) {
								item.put(dynamicgridItem.getField(), temp);
							}
						}
						
						if (type == 1) {
							//管理人员遵章绩效奖金表
							item.put("xiaoji", xjTotal); // 小计
							//得到员工考勤信息
							EmployeeCheckWorkStatMonth statMonth = statMonthService.findByEmployeeIdAndStatMonth(employee.getId(), (String) params.get("wageDateStr"));
							if (statMonth == null) {
								statMonth = statMonthService.refreshCheckWorkStatMonth(employee, (String) params.get("wageDateStr"));
							}
							double tianshu = statMonth.getShjScDay() + statMonth.getKgScDay() + statMonth.getBjScDay();
							item.put("tianshu", tianshu); // 缺勤天数;
							double gongzhi = Double.parseDouble(decimalFormat.format(xjTotal / 22 * tianshu));
							item.put("gongzhi", gongzhi); // 缺勤工资
							double koukuanheji = gongzhi + koukuan1;
							item.put("koukuanheji", koukuanheji); // 扣款合计
							item.put("heji", xjTotal - koukuanheji); // 扣款合计
						} else if (type == 2) {
							//文员绩效考核等级表
							item.put("sfzzj", sfzzjTotal + khbl * khje - qtkk); // 扣款合计
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
	 * 导出绩效工资Excel
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("emp:performancewagemgr:export")
	@OperationLog(content = "导出绩效工资Excel" , type = OperationType.VIEW)
	@ResponseBody
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(HttpSession session,HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> params) {
		logger.info("--导出绩效工资Excel--");
		try {
			int type = NumberUtils.toInt((String) params.get("type"), 0);
			DataGridJson dataGridJson = list(session, request, params);
			String fileName = type == 1 ? "管理人员遵章绩效奖金表.xlsx" : "文员绩效考核等级表.xlsx";
			String filePath = getExportPath(request) + fileName;
			List<ExcelColumn> excelColumns = DynamicgridItem.toExcelColumns(DynamicgridItemCache.getDynamicgridItemsByName(type == 1 ? "管理人员遵章绩效奖金表" : "文员绩效考核等级表"));
			if (CollectionUtils.isNotEmpty(excelColumns)) {
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
											map.put(entry.getKey(), temp2.get("value"));
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
				CustomizeToExcel.toFile(excelColumns, data ,filePath);
			}
			DownloadUtil.downloadFile(filePath, fileName, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
