package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.common.utils.excel.CustomizeToExcel;
import com.eastcom.baseframe.common.utils.excel.ExcelColumn;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.common.utils.DownloadUtil;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.hrmis.modules.emp.entity.EmployeePost;
import com.eastcom.hrmis.modules.emp.entity.WageCountItem;
import com.eastcom.hrmis.modules.emp.entity.WagePlan;
import com.eastcom.hrmis.modules.emp.entity.WageTemplate;
import com.eastcom.hrmis.modules.emp.service.EmployeePostService;
import com.eastcom.hrmis.modules.emp.service.WageCountItemService;
import com.eastcom.hrmis.modules.emp.service.WagePlanService;
import com.eastcom.hrmis.modules.emp.service.WageTemplateService;
import com.eastcom.hrmis.modules.emp.utils.WageCountItemUtil;
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
 * 工资数据模板Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/wagetemplate")
public class WageTemplateController extends BaseController {

	@Autowired
	private WageTemplateService wageTemplateService;
	
	@Autowired
	private WagePlanService wagePlanService;
	
	@Autowired
	private WageCountItemService wageCountItemService;
	
	@Autowired
	private EmployeePostService employeePostService;
	
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
	@RequiresPermissions("emp:wagetemplatemgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取工资模板数信息--");
		DataGridJson gridJson = new DataGridJson();
		try {
			List<Map<String, Object>> result = Lists.newArrayList();
			
			String wagePlanId = StringUtils.defaultIfBlank((String) params.get("wagePlanId"), "0");
			WagePlan wagePlan = wagePlanService.get(wagePlanId);
			if (wagePlan != null && CollectionUtils.isNotEmpty(wagePlan.getPosts())) {
				Map<String, Object> reqParam = Maps.newHashMap();
				reqParam.put("wagePlanId", wagePlanId);
				List<WageTemplate> wageTemplates = wageTemplateService.find(reqParam);
				
				//得到岗位
				List<EmployeePost> employeePosts = wagePlan.getPosts();
				for (EmployeePost employeePost : employeePosts) {
					Map<String, Object> item = Maps.newHashMap();
					item.put("wagePlanId", wagePlanId);
					item.put("postName", employeePost.getName());
					item.put("postId", employeePost.getId());
					//工资核算项
					if (CollectionUtils.isNotEmpty(wagePlan.getCountItems())) {
						List<WageCountItem> countItems = wagePlan.getCountItems();
						for (WageCountItem wageCountItem : countItems) {
							//设置formatter
							wageCountItem.setShowFormatter(WageCountItemUtil.getItemFormatter(wageCountItem, "配置"));
							//工资模板数值填充
							Map<String, Object> wageCountMap = Maps.newHashMap();
							wageCountMap.put("wageCountItemId", wageCountItem.getId());
							if (CollectionUtils.isNotEmpty(wageTemplates)) {
								for (WageTemplate wageTemplate : wageTemplates) {
									if (employeePost.getId().equals(wageTemplate.getEmployeePost().getId()) && wageCountItem.getId().equals(wageTemplate.getWageCountItem().getId())) {
										wageCountMap.put("model", wageTemplate);
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
			gridJson.setTotal((long) result.size());
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
	@RequiresPermissions("emp:wagetemplatemgr:edit")
	@OperationLog(content = "新增或者修改工资项配置信息" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改工资项配置信息--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			WageTemplate wageTemplate = wageTemplateService.get(id);
			if (wageTemplate == null) {
				wageTemplate = new WageTemplate();
			}
			wageTemplate.setCount(NumberUtils.toDouble((String) params.get("count"), 0));
			wageTemplate.setWagePlan(wagePlanService.get(StringUtils.defaultIfBlank((String) params.get("wagePlanId"), "0")));
			wageTemplate.setWageCountItem(wageCountItemService.get(StringUtils.defaultIfBlank((String) params.get("wageCountItemId"), "0")));
			wageTemplate.setEmployeePost(employeePostService.get(StringUtils.defaultIfBlank((String) params.get("postId"), "0")));
			
			wageTemplateService.saveOrUpdate(wageTemplate);
			//更新影响
			wageTemplateService.updateEffect(wageTemplate);
			
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
	 * 导出工资方案数据配置Excel
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("emp:wagetemplatemgr:export")
	@OperationLog(content = "工资方案数据配置Excel" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(HttpSession session,HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> params) {
		logger.info("--工资方案数据配置Excel--");
		try {
			DataGridJson dataGridJson = list(session, request, params);
			String fileName = "工资方案数据配置.xlsx";
			String filePath = getExportPath(request) + fileName;
			List<ExcelColumn> excelColumns = wagePlanController.getExportExcelColumnHeader(session, request, params);
			if (CollectionUtils.isNotEmpty(excelColumns)) {
				excelColumns.add(0, new ExcelColumn("岗位", "postName", 120));
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
