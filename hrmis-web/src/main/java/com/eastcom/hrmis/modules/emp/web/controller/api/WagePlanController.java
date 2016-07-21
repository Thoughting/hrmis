package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.persistence.PageHelper;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.common.utils.excel.ExcelColumn;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.hrmis.modules.emp.entity.CompilationTable;
import com.eastcom.hrmis.modules.emp.entity.EmployeePost;
import com.eastcom.hrmis.modules.emp.entity.WageCountItem;
import com.eastcom.hrmis.modules.emp.entity.WagePlan;
import com.eastcom.hrmis.modules.emp.service.EmployeePostService;
import com.eastcom.hrmis.modules.emp.service.WageCountItemService;
import com.eastcom.hrmis.modules.emp.service.WagePlanService;
import com.eastcom.hrmis.modules.emp.utils.WageCountItemUtil;
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
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工资方案Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/wageplan")
public class WagePlanController extends BaseController {

	@Autowired
	private WagePlanService wagePlanService;
	
	@Autowired
	private WageCountItemService wageCountItemService;
	
	@Autowired
	private EmployeePostService employeePostService;
	
	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:wageplanmgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取工资方案信息--");
		DataGridJson gridJson = new DataGridJson();
		try {
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 9999);
			Map<String, Object> reqParams = Maps.newHashMap(params);
			reqParams.remove("page");
			reqParams.remove("rows");
			PageHelper<CompilationTable> pageHelper = wagePlanService.find(reqParams, pageNo, pageSize);
			gridJson.setRows(pageHelper.getList());
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
	@RequiresPermissions(value = {"emp:wageplanmgr:add","emp:wageplanmgr:edit"},logical=Logical.OR)
	@OperationLog(content = "新增或者修改工资方案信息" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改工资方案信息--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			WagePlan wagePlan = wagePlanService.get(id);
			if (wagePlan == null) {
				wagePlan = new WagePlan();
			}
			wagePlan.setName((String) params.get("name"));
			wagePlan.setCountType(NumberUtils.toInt((String) params.get("countType"), 0));
			wagePlan.setRemark((String) params.get("remark"));
			
			wagePlanService.saveOrUpdate(wagePlan);
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
	 * 删除
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:wageplanmgr:del")
	@OperationLog(content = "删除员工编制表信息" , type = OperationType.DELETE)
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--删除员工编制表信息--");
		AjaxJson json = new AjaxJson();
		try {
			String deleteJson = (String) params.get("deleteJson");
			JSONParser jsonParser = new JSONParser(deleteJson);
			List<Object> deleteDatas = jsonParser.parseArray();
			if (CollectionUtils.isNotEmpty(deleteDatas)) {
				List<String> ids = new ArrayList<String>();
				for (Object item : deleteDatas) {
					Map<String, Object> map = (Map<String, Object>) item;
					String id = StringUtils.defaultString((String) map.get("id"), "0");
					ids.add(id);
				}
				wagePlanService.deleteByIds(ids);
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
	 * 得到工资核算项树图（用于添加工资方案配置）
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:wageplanmgr:auth")
	@ResponseBody
	@RequestMapping(value = "/wageCountItemTreeList", method = RequestMethod.POST)
	public List<WageCountItem> wageCountItemTreeList(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--得到工资核算项树图（用于添加工资方案配置）--");
		List<WageCountItem> result = Lists.newArrayList();
		try {
			String id = StringUtils.defaultString((String )params.get("id"), "null");
			WagePlan wagePlan = wagePlanService.get(id);
			if (wagePlan != null) {
				result = wageCountItemService.findCascadeTree("null");
				if (CollectionUtils.isNotEmpty(result) && CollectionUtils.isNotEmpty(wagePlan.getCountItems())) {
					for (WageCountItem temp : result) {
						for (WageCountItem temp2 : wagePlan.getCountItems()) {
							//一层
							if (temp.getId().equals(temp2.getId())) {
								temp.setChecked(true);
							}
							//二层
							if (CollectionUtils.isNotEmpty(temp.getChildren())) {
								for (WageCountItem temp3 : temp.getChildren()) {
									if (temp3.getId().equals(temp2.getId())) {
										temp3.setChecked(true);
										//二层勾上，一层肯定也得勾
										temp.setChecked(true);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 得到岗位树图（用于添加工资方案配置）
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:wageplanmgr:auth")
	@ResponseBody
	@RequestMapping(value = "/postTreeList", method = RequestMethod.POST)
	public List<EmployeePost> postTreeList(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--得到岗位树图（用于添加工资方案配置）--");
		List<EmployeePost> posts = Lists.newArrayList();
		try {
			String id = StringUtils.defaultString((String )params.get("id"), "null");
			WagePlan wagePlan = wagePlanService.get(id);
			if (wagePlan != null) {
				posts = employeePostService.find(new HashMap<String, Object>());
				if (CollectionUtils.isNotEmpty(posts) && CollectionUtils.isNotEmpty(wagePlan.getPosts())) {
					for (EmployeePost temp : posts) {
						for (EmployeePost temp2 : wagePlan.getPosts()) {
							if (temp.getId().equals(temp2.getId())) {
								temp.setChecked(true);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return posts;
	}
	
	/**
	 * 更新工资计划核算项与岗位配置
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:wageplanmgr:auth")
	@OperationLog(content = "更新工资计划核算项与岗位配置" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/updateSet", method = RequestMethod.POST)
	public AjaxJson updateSet(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--更新工资计划核算项与岗位配置--");
		AjaxJson json = new AjaxJson();
		try {
			//选择的菜单资源列表
			String checkWageCountItemNodesJson = (String) params.get("checkWageCountItemNodes");
			List<Object> checkWageCountItemNodes = new JSONParser(checkWageCountItemNodesJson).parseArray();
			
			//选择的区域资源列表
			String checkPostNodesJson = (String) params.get("checkPostNodes");
			List<Object> checkPostNodes = new JSONParser(checkPostNodesJson).parseArray();
			
			//角色列表
			String selectNodesJson = (String) params.get("selectNode");
			Object selectNode = new JSONParser(selectNodesJson).parse();
			
			wagePlanService.updateSetting(selectNode, checkWageCountItemNodes, checkPostNodes);
			
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
	 * 根据工资方案ID得到表格头
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@OperationLog(content = "" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/column", method = RequestMethod.POST)
	public AjaxJson column(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--根据工资方案ID得到表格头--");
		AjaxJson json = new AjaxJson();
		try {
			String type = StringUtils.defaultString((String )params.get("type"), "配置");
			
			List<List<Map<String, Object>>> columns = Lists.newArrayList();
			List<Map<String, Object>> column_1 = Lists.newArrayList();
			List<Map<String, Object>> column_2 = Lists.newArrayList();
			
			String wagePlanId = StringUtils.defaultString((String )params.get("id"), "null");
			List<WageCountItem> wageCountItems = wageCountItemTreeList(session, request, params);
			if (CollectionUtils.isNotEmpty(wageCountItems) && !"null".equals(wagePlanId)) {
				for (WageCountItem wageCountItem : wageCountItems) {
					if (wageCountItem.isChecked()) {
						Map<String, Object> item_1 = Maps.newHashMap();
						item_1.put("title", wageCountItem.getName());
						item_1.put("align", "center");
						item_1.put("width", wageCountItem.getShowWidth());
						if (CollectionUtils.isNotEmpty(wageCountItem.getChildren())) {
							int childCheckCount = 0;
							for (WageCountItem wageCountItemChild : wageCountItem.getChildren()) {
								if (wageCountItemChild.isChecked()) {
									Map<String, Object> item_2 = Maps.newHashMap();
									item_2.put("field", wageCountItemChild.getId());
									item_2.put("title", wageCountItemChild.getName());
									item_2.put("align", "center");
									item_2.put("width", wageCountItemChild.getShowWidth());
									item_2.put("showFormatter", WageCountItemUtil.getItemFormatter(wageCountItemChild, type));
									column_2.add(item_2);
									childCheckCount++;
								}
							}
							item_1.put("colspan", childCheckCount);
						} else {
							item_1.put("field", wageCountItem.getId());
							item_1.put("showFormatter", WageCountItemUtil.getItemFormatter(wageCountItem, type));
							item_1.put("rowspan", 2);
						}
						column_1.add(item_1);
					}
				}
			}
			columns.add(column_1);
			columns.add(column_2);
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
	 * 自定义工资方案表头
	 * 
	 * @param date
	 * @return
	 */
	public List<ExcelColumn> getExportExcelColumnHeader(HttpSession session, HttpServletRequest request, Map<String, Object> params){
		List<ExcelColumn> excelColumns = Lists.newArrayList();
		
		String wagePlanId = StringUtils.defaultString((String )params.get("wagePlanId"), "null");
		params.put("id", wagePlanId);
		
		List<WageCountItem> wageCountItems = wageCountItemTreeList(session, request, params);
		if (CollectionUtils.isNotEmpty(wageCountItems) && !"null".equals(wagePlanId)) {
			for (WageCountItem wageCountItem : wageCountItems) {
				if (wageCountItem.isChecked()) {
					ExcelColumn column = new ExcelColumn(wageCountItem.getName(), wageCountItem.getId(), wageCountItem.getShowWidth());
					if (CollectionUtils.isNotEmpty(wageCountItem.getChildren())) {
						for (WageCountItem wageCountItemChild : wageCountItem.getChildren()) {
							if (wageCountItemChild.isChecked()) {
								column.getChildren().add(new ExcelColumn(wageCountItemChild.getName(), wageCountItemChild.getId(), wageCountItemChild.getShowWidth()));
							}
						}
					}
					excelColumns.add(column);
				}
			}
		}
		
		return excelColumns;
	}
	
	/**
	 * 
	 * 下拉框
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/combo", method = RequestMethod.POST)
	public AjaxJson combo(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取所有工资方案列表(下拉框)--");
		AjaxJson json = new AjaxJson();
		try {
			List<WagePlan> list = wagePlanService.findAll();
			json.setSuccess(true);
			json.setModel(list);
			json.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
}
