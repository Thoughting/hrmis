package com.eastcom.hrmis.modules.emp.web.controller;

import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.hrmis.modules.emp.entity.CompilationTable;
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.eastcom.hrmis.modules.emp.entity.WagePlan;
import com.eastcom.hrmis.modules.emp.service.CompilationTableService;
import com.eastcom.hrmis.modules.emp.service.EmployeeService;
import com.eastcom.hrmis.modules.emp.service.EmployeeWageHisService;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/emp")
public class EmpHrefController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private CompilationTableService compilationTableService;
	
	@Autowired
	private EmployeeWageHisService employeeWageHisService;
	
	/*************************** 考勤管理  ****************************/
	
	/**
	 * 考勤管理
	 * @return
	 */
	@RequiresPermissions("emp:checkworkmgr:view")
	@OperationLog(content = "查询考勤数据",type = OperationType.VIEW)
	@RequestMapping(value = "/workcheck/checkWorkMgr")
	public String checkWorkMgr() {
		return "/modules/emp/workcheck/checkWorkMgr";
	}
	
	/**
	 * 排班管理
	 * @return
	 */
	@RequiresPermissions("emp:setworkmgr:view")
	@OperationLog(content = "查询排班数据",type = OperationType.VIEW)
	@RequestMapping(value = "/workcheck/setWorkMgr")
	public String setWorkMgr() {
		return "/modules/emp/workcheck/setWorkMgr";
	}
	
	/**
	 * 法定节假日配置
	 * @return
	 */
	@RequiresPermissions("emp:legalholidaymgr:view")
	@OperationLog(content = "查询法定节假日配置数据",type = OperationType.VIEW)
	@RequestMapping(value = "/workcheck/legalHolidayMgr")
	public String legalHolidayMgr() {
		return "/modules/emp/workcheck/legalHolidayMgr";
	}
	
	
	/**
	 * 排班与考勤差异统计
	 * @return
	 */
	@RequiresPermissions("emp:setandcheckworkdiff:view")
	@OperationLog(content = "查询排班与考勤差异统计数据",type = OperationType.VIEW)
	@RequestMapping(value = "/workcheck/differenceWorkMgr")
	public String differenceWorkMgr() {
		return "/modules/emp/workcheck/differenceWorkMgr";
	}
	
	/**
	 * 考勤统计分析
	 * @return
	 */
	@RequiresPermissions("emp:checkworkstat:view")
	@OperationLog(content = "查询考勤统计分析数据",type = OperationType.VIEW)
	@RequestMapping(value = "/workcheck/checkWorkStatMgr")
	public String checkWorkStatMgr() {
		return "/modules/emp/workcheck/checkWorkStatMgr";
	}
	
	/*************************** 工资核算  ****************************/
	
	/**
	 * 工资方案管理
	 * @return
	 */
	@RequiresPermissions("emp:wageplanmgr:view")
	@OperationLog(content = "查询工资方案数据",type = OperationType.VIEW)
	@RequestMapping(value = "/wages/wagePlanMgr")
	public String wagePlanMgr() {
		return "/modules/emp/wages/wagePlanMgr";
	}
	
	/**
	 * 工资核算项管理
	 * @return
	 */
	//@RequiresPermissions("emp:wagecountitemmgr:view")
	@OperationLog(content = "查询工资核算项数据",type = OperationType.VIEW)
	@RequestMapping(value = "/wages/wageCountItemMgr")
	public String wageCountItemMgr() {
		return "/modules/emp/wages/wageCountItemMgr";
	}
	
	/**
	 * 工资方案数据配置
	 * @return
	 */
	@RequiresPermissions("emp:wagetemplatemgr:view")
	@OperationLog(content = "查询工资方案数据配置",type = OperationType.VIEW)
	@RequestMapping(value = "/wages/wageTemplateMgr")
	public String wageTemplateMgr() {
		return "/modules/emp/wages/wageTemplateMgr";
	}
	
	/**
	 * 员工工资配置
	 * @return
	 */
	@RequiresPermissions("emp:employeewageitemmgr:view")
	@OperationLog(content = "查询员工工资配置",type = OperationType.VIEW)
	@RequestMapping(value = "/wages/employeeWageItemMgr")
	public String employeeWageItemMgr() {
		return "/modules/emp/wages/employeeWageItemMgr";
	}
	
	/**
	 * 员工工资核实
	 * @return
	 */
	@RequiresPermissions("emp:wagecheckmgr:view")
	@OperationLog(content = "查询员工工资核实数据",type = OperationType.VIEW)
	@RequestMapping(value = "/wages/employeeWageCheckMgr")
	public String employeeWageCheckMgr() {
		return "/modules/emp/wages/employeeWageCheckMgr";
	}
	
	/**
	 * 员工工资统计
	 * @return
	 */
	@RequiresPermissions("emp:wagestatmgr:view")
	@OperationLog(content = "查询员工工资统计数据",type = OperationType.VIEW)
	@RequestMapping(value = "/wages/employeeWageStatMgr")
	public String employeeWageStatMgr() {
		return "/modules/emp/wages/employeeWageStatMgr";
	}
	
	/**
	 * 查询员工工资历史
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequiresPermissions("emp:wagehismgr:view")
	@OperationLog(content = "查询员工工资统计数据",type = OperationType.VIEW)
	@RequestMapping(value = "/wages/employeeWageHisMgr")
	public String employeeWageHisMgr(Model model,@RequestParam Map<String, Object> params) {
		Map<String, Object> reqParam = Maps.newHashMap();
		Date day = new Date();
		day.setMonth(day.getMonth() - 1);
		reqParam.put("start_wageDate", params.get("start_wageDate") == null ? DateUtils.formatDate(day, "yyyy年-MM月") : params.get("start_wageDate"));
		day.setMonth(day.getMonth() + 1);
		reqParam.put("end_wageDate", params.get("end_wageDate") == null ? DateUtils.formatDate(day, "yyyy年-MM月") : params.get("end_wageDate"));
		
		reqParam.put("deptName",params.get("deptName"));
		reqParam.put("employeeName",params.get("employeeName"));
		
		List<WagePlan> tabs = employeeWageHisService.getWagePlanInfos(reqParam);
		model.addAttribute("tabs", tabs);
		
		model.addAttribute("start_wageDate", reqParam.get("start_wageDate"));
		model.addAttribute("end_wageDate", reqParam.get("end_wageDate"));
		model.addAttribute("deptName", reqParam.get("deptName"));
		
		if (reqParam.get("employeeName") != null && StringUtils.isNotEmpty((String)reqParam.get("employeeName"))) {
			model.addAttribute("employeeName", StringUtils.replace((String)reqParam.get("employeeName"), "%", ""));
		}
		
		return "/modules/emp/wages/employeeWageHisMgr";
	}
	
	/**
	 * 绩效工资管理
	 * @return
	 */
	@RequiresPermissions("emp:performancewagemgr:view")
	@OperationLog(content = "查询绩效工资数据",type = OperationType.VIEW)
	@RequestMapping(value = "/wages/performanceWageMgr")
	public String performancePayMgr() {
		return "/modules/emp/wages/performanceWageMgr";
	}
	
	/*************************** 档案管理  ****************************/
	
	/**
	 * 职务岗位管理
	 * @return
	 */
	@RequiresPermissions("emp:postmgr:view")
	@OperationLog(content = "查询职务岗位数据",type = OperationType.VIEW)
	@RequestMapping(value = "/archives/employeePostMgr")
	public String employeePostMgr() {
		return "/modules/emp/archives/employeePostMgr";
	}
	
	/**
	 * 职务部门管理
	 * @return
	 */
	@RequiresPermissions("emp:deptmgr:view")
	@OperationLog(content = "查询职务部门数据",type = OperationType.VIEW)
	@RequestMapping(value = "/archives/employeeDeptMgr")
	public String employeeDeptMgr() {
		return "/modules/emp/archives/employeeDeptMgr";
	}
	
	/**
	 * 分管部门领导管理
	 * @return
	 */
	@RequiresPermissions("emp:deptleader:view")
	@OperationLog(content = "查询分管部门领导数据",type = OperationType.VIEW)
	@RequestMapping(value = "/archives/employeeDeptLeaderMgr")
	public String employeeDeptLeaderMgr() {
		return "/modules/emp/archives/employeeDeptLeaderMgr";
	}
	
	/**
	 * 员工档案管理
	 * @return
	 */
	@RequiresPermissions("emp:baseinfomgr:view")
	@OperationLog(content = "查询员工档案数据",type = OperationType.VIEW)
	@RequestMapping(value = "/archives/employeeMgr")
	public String employeeMgr() {
		return "/modules/emp/archives/employeeMgr";
	}
	
	/**
	 * 员工档案新增
	 * @return
	 */
	@RequiresPermissions("emp:baseinfomgr:add")
	@OperationLog(content = "员工档案新增",type = OperationType.VIEW)
	@RequestMapping(value = "/archives/employeeAdd")
	public String employeeAdd(Model model) {
		//员工新增时候,创建一个新员工,没有点击保存,关闭界面的时候则将该新增员工删除
		Employee employee = new Employee();
		employee.setDriveLicenseType(6);
		employee.setBankType(4);
		employee.setPolity(2);
		employee.setMarryType(0);
		employee.setManageLevel(10);
		employee.setHasRiskAgreement(0);
		employeeService.save(employee);
		
		model.addAttribute("employee", employee);
		model.addAttribute("saveFlag", 0);
		model.addAttribute("addOrUpdate", "add");
		return "/modules/emp/archives/employeeMgr_add_edit";
	}
	
	/**
	 * 员工档案编辑
	 * @return
	 */
	@RequiresPermissions("emp:baseinfomgr:edit")
	@OperationLog(content = "员工档案编辑",type = OperationType.VIEW)
	@RequestMapping(value = "/archives/employeeEdit")
	public String employeeEdit(Model model,@RequestParam Map<String, Object> params) {
		String id = StringUtils.defaultIfBlank((String) params.get("content"), "0");
		Employee employee = employeeService.get(id);
		model.addAttribute("employee", employee);
		model.addAttribute("addOrUpdate", "update");
		return "/modules/emp/archives/employeeMgr_add_edit";
	}

	/**
	 * 员工档案详情
	 * @return
	 */
	@RequiresPermissions("emp:baseinfomgr:edit")
	@OperationLog(content = "员工档案详情",type = OperationType.VIEW)
	@RequestMapping(value = "/archives/employeeDetail")
	public String employeeDetail(Model model,@RequestParam Map<String, Object> params) {
		String id = StringUtils.defaultIfBlank((String) params.get("content"), "0");
		Employee employee = employeeService.get(id);
		if (employee != null){
			employee.setPostChangeRemark(StringUtils.replace(employee.getPostChangeRemark(),"\n","<br />"));
			employee.setDeptChangeRemark(StringUtils.replace(employee.getDeptChangeRemark(),"\n","<br />"));
			employee.setOperaChangeRemark(StringUtils.replace(employee.getOperaChangeRemark(),"\n","<br />"));
			employee.setWageChangeRemark(StringUtils.replace(employee.getWageChangeRemark(),"\n","<br />"));

		}
		model.addAttribute("employee", employee);
		return "/modules/emp/archives/employeeMgr_detail";
	}

	/*@OperationLog(content = "员工档案编辑",type = OperationType.VIEW)
	@RequestMapping(value = "/archives/employeeEdit/{id}")
	public String employeeEdit(@PathVariable String id) {
		return "/emp/archives/employeeMgr_add_edit";
	}*/
	
	/**
	 * 编制表分类管理
	 * @return
	 */
	@RequiresPermissions("emp:compilationtablemgr:view")
	@OperationLog(content = "查询编制表分类数据",type = OperationType.VIEW)
	@RequestMapping(value = "/archives/compilationTableMgr")
	public String compilationTableMgr() {
		return "/modules/emp/archives/compilationTableMgr";
	}
	
	/**
	 * 人员编制动态表
	 * @return
	 */
	@RequiresPermissions("emp:compilationtableshow:view")
	@OperationLog(content = "查询人员编制动态表",type = OperationType.VIEW)
	@RequestMapping(value = "/archives/compilationTableShow")
	public String compilationTableShow(Model model) {
		List<CompilationTable> compilationTables = compilationTableService.findAll();
		model.addAttribute("compilationTables", compilationTables);
		model.addAttribute("gridCount", compilationTables.size());
		return "/modules/emp/archives/compilationTableShow";
	}
	
	/**
	 * 员工统计分析对比
	 * @return
	 */
	@RequiresPermissions("emp:employeestat:view")
	@OperationLog(content = "查询员工统计分析对比信息",type = OperationType.VIEW)
	@RequestMapping(value = "/archives/employeeStatMgr")
	public String employeeStatMgr(Model model) {
		return "/modules/emp/archives/employeeStatMgr";
	}
	
	/*************************** 合同管理  ****************************/
	
	/**
	 * 合同类型管理
	 * @return
	 */
	@RequiresPermissions("emp:contracttypemgr:view")
	@OperationLog(content = "查询合同类型数据",type = OperationType.VIEW)
	@RequestMapping(value = "/contract/contractType")
	public String contractType() {
		return "/modules/emp/contract/contractTypeMgr";
	}
	
	/**
	 * 合同管理
	 * @return
	 */
	@RequiresPermissions("emp:contractmgr:view")
	@OperationLog(content = "查询合同数据",type = OperationType.VIEW)
	@RequestMapping(value = "/contract/contractMgr")
	public String contractMgr() {
		return "/modules/emp/contract/contractMgr";
	}
	
}
