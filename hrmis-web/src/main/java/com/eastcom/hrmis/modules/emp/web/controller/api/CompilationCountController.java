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
import com.eastcom.hrmis.modules.emp.entity.CompilationCount;
import com.eastcom.hrmis.modules.emp.entity.CompilationTable;
import com.eastcom.hrmis.modules.emp.entity.EmployeePost;
import com.eastcom.hrmis.modules.emp.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
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
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * 编制数Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/compilationcount")
public class CompilationCountController extends BaseController {

	@Autowired
	private CompilationCountService compilationCountService;
	
	@Autowired
	private CompilationTableService compilationTableService;
	
	@Autowired
	private EmployeeDeptService employeeDeptService;
	
	@Autowired
	private EmployeePostService employeePostService;
	
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
	@RequiresPermissions("emp:compilationtableshow:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取编制数信息--");
		DataGridJson gridJson = new DataGridJson();
		try {
			String compilationId = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			CompilationTable compilationTable = compilationTableService.get(compilationId);
			//分管领导，部门
			List<Map<String, Object>> result = compilationCountService.getLeaderAndDeptName(compilationId);
			if (compilationTable != null && CollectionUtils.isNotEmpty(result)) {
				//小计
				Map<String, Object> total = Maps.newHashMap();
				total.put("leaderName", "小计");
				
				//得到人员编制数
				List<EmployeePost> employeePosts = compilationTable.getPosts();
				if (CollectionUtils.isNotEmpty(employeePosts)) {
					//设定编制数
					Map<String, Object> reqParam = Maps.newHashMap();
					reqParam.put("compilationId", compilationId);
					List<CompilationCount> compilationCounts = compilationCountService.find(reqParam);
					
					//实际编制数
					List<Map<String, Object>> actualCounts = employeeService.getEmployeeDeptPostCount();
					
					for (Map<String, Object> map : result) {
						map.put("compilationId", compilationId);
						String deptId = (String) map.get("deptId");
						for (EmployeePost employeePost : employeePosts) {
							String postId = (String) employeePost.getId();
							
							//根据编制表ID，部门ID，岗位ID，得到编制数
							Map<String, Object> countMap = Maps.newHashMap();
							countMap.put("postId", postId);
							CompilationCount compilationCount = null;
							if (CollectionUtils.isNotEmpty(compilationCounts)) {
								for (CompilationCount tempCompilationCount : compilationCounts) {
									try {
										if (deptId.equals(tempCompilationCount.getEmployeeDept().getId()) && postId.equals(tempCompilationCount.getEmployeePost().getId())) {
											compilationCount = tempCompilationCount;
											break;
										}
									} catch (Exception e) {
									}
								}
							}
							countMap.put("model", compilationCount);
							map.put(postId + "_DING", countMap);
							int currentCount = compilationCount == null ? 0 : compilationCount.getCount();
							
							//得到在职数
							int actualConut = 0;
							if (CollectionUtils.isNotEmpty(actualCounts)) {
								for (Map<String, Object> actualCountMap : actualCounts) {
									if (deptId.equals((String) actualCountMap.get("deptId")) && postId.equals((String) actualCountMap.get("postId"))) {
										actualConut = ((BigInteger) actualCountMap.get("count")).intValue();
										break;
									}
								}
							}
							map.put(postId + "_ZAI", actualConut);
							
							//得到缺人数
							int queCount = currentCount - actualConut;
							map.put(postId + "_QUE", queCount);
							
							//得到统计值
							if (total.get(postId + "_DING") == null) {
								total.put(postId + "_DING",0);
							}
							total.put(postId + "_DING", (int) total.get(postId + "_DING") + currentCount);
							
							if (total.get(postId + "_ZAI") == null) {
								total.put(postId + "_ZAI",0);
							}
							total.put(postId + "_ZAI", (int) total.get(postId + "_ZAI") + actualConut);
							
							if (total.get(postId + "_QUE") == null) {
								total.put(postId + "_QUE",0);
							}
							total.put(postId + "_QUE", (int) total.get(postId + "_QUE") + queCount);
						}
					}
				}
				
				result.add(total);
				
				gridJson.setRows(result);
				gridJson.setTotal((long) result.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gridJson;
	}
	
	/**
	 * 
	 * 查询统计信息（合计）
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:compilationtableshow:view")
	@ResponseBody
	@RequestMapping(value = "/total", method = RequestMethod.POST)
	public AjaxJson total(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--查询统计信息（合计）--");
		AjaxJson json = new AjaxJson();
		try {
			String compilationId = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			CompilationTable compilationTable = compilationTableService.get(compilationId);
			if (compilationTable != null) {
				DataGridJson dataGridJson = list(session, request, params);
				if (CollectionUtils.isNotEmpty(dataGridJson.getRows())) {
					for (Object row : dataGridJson.getRows()) {
						Map<String, Object> map = (Map<String, Object>) row;
						if (map.get("deptName") == null) {
							//统计项
							int ding = 0;
							int zai = 0;
							int que = 0;
							for (Map.Entry<String, Object> entry : map.entrySet()) {
								if (entry.getKey().indexOf("_DING") != -1) {
									ding += (Integer) entry.getValue();
								} else if (entry.getKey().indexOf("_ZAI") != -1) {
									zai += (Integer) entry.getValue();
								} else if (entry.getKey().indexOf("_QUE") != -1) {
									que += (Integer) entry.getValue();
								}
							}
							json.setModel("合计：定编" + ding + "人，在编" + zai + "人，缺编" + que + "人");
						}
					}
				}
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
	 * 新增修改
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:compilationtablemgr:edit")
	@OperationLog(content = "新增或者修改编制数信息" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改编制数信息--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			CompilationCount compilationCount = compilationCountService.get(id);
			if (compilationCount == null) {
				compilationCount = new CompilationCount();
			}
			compilationCount.setCount(NumberUtils.toInt((String) params.get("count"), 1));
			
			compilationCount.setCompilationTable(compilationTableService.get(StringUtils.defaultIfBlank((String) params.get("compilationId"), "0")));
			compilationCount.setEmployeeDept(employeeDeptService.get(StringUtils.defaultIfBlank((String) params.get("deptId"), "0")));
			compilationCount.setEmployeePost(employeePostService.get(StringUtils.defaultIfBlank((String) params.get("postId"), "0")));
			
			compilationCountService.saveOrUpdate(compilationCount);
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
	 * 导出动态编制表Excel
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("emp:compliationtableshow:export")
	@OperationLog(content = "导出动态编制表Excel" , type = OperationType.VIEW)
	@ResponseBody
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(HttpSession session,HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> params) {
		logger.info("--导出动态编制表Excel--");
		try {
			String compilationId = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			CompilationTable compilationTable = compilationTableService.get(compilationId);
			if (compilationTable != null) {
				DataGridJson dataGridJson = list(session, request, params);
				String fileName = compilationTable.getName() + ".xlsx";
				String filePath = getExportPath(request) + fileName;
				List<ExcelColumn> excelColumns = compilationTable.toExcelColumns();
				if (CollectionUtils.isNotEmpty(excelColumns)) {
					ObjectMapper m = new ObjectMapper(); 
					String json = m.writeValueAsString(dataGridJson.getRows());  
					List<Map<String,Object>> data = convertJsonArrToMap(json);
					if (CollectionUtils.isNotEmpty(data)) {
						for (Map<String, Object> map : data) {
							for (String key : map.keySet()) {
								if (key.indexOf("_DING") != -1) {
									if (!(map.get(key) instanceof Integer)) {
										Map<String,Object> temp = (Map<String, Object>) ((Map<String, Object>) map.get(key)).get("model");
										if (temp != null) {
											map.put(key, temp.get("count"));
										} else {
											map.put(key, 0);
										}
									}
								}
							}
						}
					}
					Workbook wb = CustomizeToExcel.getWorkbook(excelColumns, data);
					Sheet sheet = wb.getSheetAt(0);
					if (CollectionUtils.isNotEmpty(data)) {
						//合并领导单元格
						int index = 2;
						int dataRow = 2;
						String leaderName = "";
						for (int i = 0; i < data.size(); i++) {
							Map<String,Object> temp = data.get(i);
							if (i == 0) {
								leaderName = (String) temp.get("leaderName");
								continue;
							}
							if (!leaderName.equals((String) temp.get("leaderName"))) {
								leaderName = (String) temp.get("leaderName");
								sheet.addMergedRegion(new CellRangeAddress(index, dataRow, 0, 0));
								index = dataRow + 1;
							}
							dataRow++;
						}
						//合并合计单元格
						int total = sheet.getPhysicalNumberOfRows();
						sheet.addMergedRegion(new CellRangeAddress(total - 1, total - 1, 0, 1));
					}
					CustomizeToExcel.toFile(wb ,filePath);
				}
				DownloadUtil.downloadFile(filePath, fileName, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
