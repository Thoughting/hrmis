package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.config.Global;
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
import com.eastcom.baseframe.web.modules.sys.cache.DynamicgridItemCache;
import com.eastcom.baseframe.web.modules.sys.entity.DynamicgridItem;
import com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache;
import com.eastcom.hrmis.modules.emp.entity.*;
import com.eastcom.hrmis.modules.emp.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * 
 * 员工基本信息Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/info")
public class EmployeeController extends BaseController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeDeptService employeeDeptService;
	
	@Autowired
	private EmployeePostService employeePostService;
	
	@Autowired
	private EmployeeAnnexService annexService;
	
	@Autowired
	private EmployeeContractHomeAnnexService homeAnnexService;
	
	@Autowired
	private EmployeeContractFinalAnnexService finalAnnexService;
	
	@Autowired
	private WagePlanService wagePlanService;
	
	@Autowired
	private PerformanceWageService performanceWageService;
	
	@Autowired
	private EmployeeCheckWorkStatMonthService employeeCheckWorkStatMonthService;
	
	@Autowired
	private EmployeeOrderService employeeOrderService;
	
	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:baseinfomgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取员工基本信息--");
		DataGridJson gridJson = new DataGridJson();
		try {
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 1);
			Map<String, Object> reqParams = Maps.newHashMap(params);
			reqParams.remove("page");
			reqParams.remove("rows");
			PageHelper<Employee> pageHelper = employeeService.find(reqParams, pageNo, pageSize);
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
	@RequiresPermissions(value = {"emp:baseinfomgr:add","emp:baseinfomgr:edit"},logical=Logical.OR)
	@OperationLog(content = "新增或者修改员工档案信息" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改员工档案信息--");
		AjaxJson json = new AjaxJson();
		try {
			String addOrUpdate = (String ) params.get("addOrUpdate");
			if (StringUtils.isNotEmpty(addOrUpdate) && "add".equals(addOrUpdate)){
				//不能存在相同的code
				Map<String, Object> reqParam = Maps.newHashMap();
				reqParam.put("code", (String) params.get("code"));
				List<Employee> temps = employeeService.find(reqParam);
				if (CollectionUtils.isNotEmpty(temps)) {
					json.setSuccess(false);
					json.setMessage("操作失败,编码不唯一");
					return json;
				}
				//不能存在相同的身份证号码
				reqParam = Maps.newHashMap();
				reqParam.put("cardNo", (String) params.get("cardNo"));
				temps = employeeService.find(reqParam);
				if (CollectionUtils.isNotEmpty(temps)) {
					json.setSuccess(false);
					json.setMessage("操作失败,身份证号码不唯一");
					return json;
				}
			}

			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			Employee employee = employeeService.get(id);
			if (employee == null) {
				employee = new Employee();
				employee.setCreateDate(new Date());
			}
			employee.setCode((String) params.get("code"));
			employee.setName((String) params.get("name"));
			employee.setSex(NumberUtils.toInt((String) params.get("sex"), 1));
			employee.setAge(NumberUtils.toInt((String) params.get("age"), 0));
			employee.setEnrtyDate(DateUtils.parseDate((String) params.get("enrtyDate")));
			employee.setEnrtyDateType(NumberUtils.toInt((String) params.get("enrtyDateType"), 0));
			employee.setRegularDate(DateUtils.parseDate((String) params.get("regularDate")));
			employee.setRegularDateTwo(DateUtils.parseDate((String) params.get("regularDateTwo")));
			employee.setRetireDate(DateUtils.parseDate((String) params.get("retireDate")));
			employee.setNation((String) params.get("nation"));
			employee.setMarryType(NumberUtils.toInt((String) params.get("marryType"), 0));
			employee.setHeight((String) params.get("height"));
			employee.setCardNo((String) params.get("cardNo"));
			employee.setCardNoValidDate(DateUtils.parseDate((String) params.get("cardNoValidDate")));
			employee.setIsCardNoLongTerm(NumberUtils.toInt((String) params.get("isCardNoLongTerm"), 0));
			employee.setBirthDate(DateUtils.parseDate((String) params.get("birthDate")));
			employee.setEducation(NumberUtils.toInt((String) params.get("education"), 1));
			employee.setManageLevel(NumberUtils.toInt((String) params.get("manageLevel"), 1));
			employee.setJobTitle((String) params.get("jobTitle"));
			employee.setJobCapacity((String) params.get("jobCapacity"));
			employee.setPolity(NumberUtils.toInt((String) params.get("polity"), 1));
			employee.setDriveLicenseType(NumberUtils.toInt((String) params.get("driveLicenseType"), 0));
			employee.setDriveLicenseGetDate(DateUtils.parseDate((String) params.get("driveLicenseGetDate")));
			employee.setDriveLicenseValidDate(DateUtils.parseDate((String) params.get("driveLicenseValidDate")));
			employee.setMajor((String) params.get("major"));
			employee.setNativePlaceType(NumberUtils.toInt((String) params.get("nativePlaceType"), 1));
			employee.setNativePlaceAddr((String) params.get("nativePlaceAddr"));
			employee.setNativePlace((String) params.get("nativePlace"));
			employee.setContactAddr((String) params.get("contactAddr"));
			employee.setTelephone((String) params.get("telephone"));
			employee.setEmergentName((String) params.get("emergentName"));
			employee.setEmergentTelephone((String) params.get("emergentTelephone"));
			employee.setMealRoomType(NumberUtils.toInt((String) params.get("mealRoomType"), 1));
			employee.setPerformanceWageType(NumberUtils.toInt((String) params.get("performanceWageType"), 0));
			employee.setLaborType(NumberUtils.toInt((String) params.get("laborType"), 1));
			employee.setContractStartDate(DateUtils.parseDate((String) params.get("contractStartDate")));
			employee.setContractEndDate(DateUtils.parseDate((String) params.get("contractEndDate")));
			employee.setContractSignDateType(NumberUtils.toInt((String) params.get("contractSignDateType"), 1));
			employee.setContractSignDate(DateUtils.parseDate((String) params.get("contractSignDate")));
			employee.setContractTermCond(NumberUtils.toInt((String) params.get("contractTermCond"), 0));
			employee.setHasRiskAgreement(NumberUtils.toInt((String) params.get("hasRiskAgreement"), 0));
			employee.setHasPercentAgreement(NumberUtils.toInt((String) params.get("hasPercentAgreement"), 0));
			employee.setBankType(NumberUtils.toInt((String) params.get("bankType"), 0));
			employee.setBankCard((String) params.get("bankCard"));
			employee.setCharacterRemark((String) params.get("characterRemark"));
			employee.setHasLaborDispute(NumberUtils.toInt((String) params.get("hasLaborDispute"), 0));
			employee.setLaborDisputeResult((String) params.get("laborDisputeResult"));
			employee.setEnrtyIntorducerCompany((String) params.get("enrtyIntorducerCompany"));
			employee.setEnrtyIntorducer((String) params.get("enrtyIntorducer"));
			employee.setHasDiseaseHistory(NumberUtils.toInt((String) params.get("hasDiseaseHistory"), 0));
			employee.setDiseaseHistory((String) params.get("diseaseHistory"));
			employee.setHasFriendInCompany(NumberUtils.toInt((String) params.get("hasFriendInCompany"), 0));
			employee.setFriendDept((String) params.get("friendDept"));
			employee.setFriendName((String) params.get("friendName"));
			employee.setFriendJobTitle((String) params.get("friendJobTitle"));
			employee.setContractType(NumberUtils.toInt((String) params.get("contractType"), 0));
			employee.setHasSignForm(NumberUtils.toInt((String) params.get("hasSignForm"), 0));
			employee.setHasInsure(NumberUtils.toInt((String) params.get("hasInsure"), 0));
			employee.setInsureNo((String) params.get("insureNo"));
			employee.setInsureDate(DateUtils.parseDate((String) params.get("insureDate")));
			employee.setInsurePayBase(NumberUtils.toDouble((String) params.get("insurePayBase"), 0));
			employee.setHasPersionInsure(NumberUtils.toInt((String) params.get("hasPersionInsure"), 0));
			employee.setHasInjuryInsure(NumberUtils.toInt((String) params.get("hasInjuryInsure"), 0));
			employee.setHasBirthInsure(NumberUtils.toInt((String) params.get("hasBirthInsure"), 0));
			employee.setHasMedicalInsure(NumberUtils.toInt((String) params.get("hasMedicalInsure"), 0));
			employee.setHasSeriousInsure(NumberUtils.toInt((String) params.get("hasSeriousInsure"), 0));
			employee.setHasGsbInsure(NumberUtils.toInt((String) params.get("hasGsbInsure"), 0));
			employee.setHasNonPurchaseCommit(NumberUtils.toInt((String) params.get("hasNonPurchaseCommit"), 0));
			employee.setHasPublicFund(NumberUtils.toInt((String) params.get("hasPublicFund"), 0));
			employee.setPublicFundPayBase(NumberUtils.toDouble((String) params.get("publicFundPayBase"), 0));
			employee.setPublicFundDate(DateUtils.parseDate((String) params.get("publicFundDate")));
			employee.setHasQuitCompany(NumberUtils.toInt((String) params.get("hasQuitCompany"), 0));
			employee.setQuitCompanyType(NumberUtils.toInt((String) params.get("quitCompanyType"), 0));
			employee.setQuitCompanyDate(DateUtils.parseDate((String) params.get("quitCompanyDate")));
			employee.setQuitCompanyResult((String) params.get("quitCompanyResult"));
			
			employee.setPostChangeRemark((String) params.get("postChangeRemark"));
			employee.setDeptChangeRemark((String) params.get("deptChangeRemark"));
			employee.setOperaChangeRemark((String) params.get("operaChangeRemark"));
			employee.setWageChangeRemark((String) params.get("wageChangeRemark"));
			
			employee.setEmployeeDept(employeeDeptService.get(StringUtils.defaultIfBlank((String) params.get("employeeDept"), "0")));
			employee.setEmployeePost(employeePostService.get(StringUtils.defaultIfBlank((String) params.get("employeePost"), "0")));
			employee.setWagePlan(wagePlanService.get(StringUtils.defaultIfBlank((String) params.get("wagePlan"), "0")));
			
			employee.setOverTimeRate(NumberUtils.toInt((String) params.get("overTimeRate"), 1));
			
			employee.setModifyer(SecurityCache.getLoginUser().getName());
			employee.setModifyDate(new Date());
			employee.setRecordStatus(1);
			
			//修改过的档案,需要重新审核,审核状态更改为未提交审核
			employee.setAuditStatus(NumberUtils.toInt((String) params.get("auditStatus"), 0));
			
			//档案更新，重新统计其考勤记录--主要是所在部门更新，部门上班时长会有所变化
			employeeCheckWorkStatMonthService.deleteByEmployeeId(employee.getId());
			
			//档案更新 如果存在合同期到期提醒代办工单，将其设置为已办
			Map<String, Object> orderParam = Maps.newHashMap();
			orderParam.put("employeeId", employee.getId());
			orderParam.put("type", 4);
			orderParam.put("status", 0);
			List<EmployeeOrder> orders = employeeOrderService.find(orderParam);
			if (CollectionUtils.isNotEmpty(orders)) {
				for (EmployeeOrder employeeOrder : orders) {
					employeeOrder.setStatus(1);
					employeeOrderService.saveOrUpdate(employeeOrder);
				}
			}
			
			//员工代办工单  5：档案配置审核提醒
			orderParam = Maps.newHashMap();
			orderParam.put("employeeId", employee.getId());
			orderParam.put("type", 5);
			orderParam.put("status", 0);
			orders = employeeOrderService.find(orderParam);
			//如果员工档案为审核中状态，生成审核提醒代办工单
			if (CollectionUtils.isEmpty(orders) && employee.getAuditStatus() == 1) {
				EmployeeOrder order = new EmployeeOrder();
				order.setEmployee(employee);
				order.setContent(employee.getName() + "档案数据更新配置，请审核！");
				order.setStatus(0);
				order.setType(5);
				employeeOrderService.save(order);
				logger.info(employee.getName() + " 创建工单:" + order.getTypeDict() + "成功！");
			}
			//如果员工档案为审核成功或者失败状态，将审核提醒代办工单设置为已办
			if (CollectionUtils.isNotEmpty(orders) && ( employee.getAuditStatus() == 2 || employee.getAuditStatus() == 3 )) {
				for (EmployeeOrder employeeOrder : orders) {
					employeeOrder.setStatus(1);
					employeeOrderService.saveOrUpdate(employeeOrder);
				}
			}
			
			employeeService.saveOrUpdate(employee);
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
	@RequiresPermissions("emp:baseinfomgr:del")
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--删除员工档案信息--");
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
				employeeService.deleteByIds(ids);
				logger.info("--删除成功--");
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
	 * 审核档案,更新档案状态
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("emp:baseinfomgr:audit")
	@OperationLog(content = "审核档案,更新档案状态" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/audit", method = RequestMethod.POST)
	public AjaxJson audit(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--审核档案,更新档案状态--");
		AjaxJson json = new AjaxJson();
		try {
			String employeesJson = (String) params.get("employees");
			JSONParser jsonParser = new JSONParser(employeesJson);
			List<Object> employeeDatas = jsonParser.parseArray();
			if (CollectionUtils.isNotEmpty(employeeDatas)) {
				for (Object item : employeeDatas) {
					Map<String, Object> map = (Map<String, Object>) item;
					String id = StringUtils.defaultString((String) map.get("id"), "0");
					Employee employee = employeeService.get(id);
					if (employee != null) {
						String auditStatus = (String) params.get("auditStatus");
						if (StringUtils.isBlank(auditStatus)) {
							auditStatus = (String) params.get("ud_auditStatus");
						}
						employee.setAuditStatus(NumberUtils.toInt(auditStatus, 0));
						employee.setAuditContent((String) params.get("ud_auditContent"));
						employeeService.saveOrUpdate(employee);
						
						//员工代办工单  5：档案配置审核提醒
						Map<String, Object> orderParam = Maps.newHashMap();
						orderParam.put("employeeId", employee.getId());
						orderParam.put("type", 5);
						orderParam.put("status", 0);
						List<EmployeeOrder> orders = employeeOrderService.find(orderParam);
						
						//如果员工档案为审核中状态，生成审核提醒代办工单
						if (CollectionUtils.isEmpty(orders) && employee.getAuditStatus() == 1) {
							EmployeeOrder order = new EmployeeOrder();
							order.setEmployee(employee);
							order.setContent(employee.getName() + "档案数据更新配置，请审核！");
							order.setStatus(0);
							order.setType(5);
							employeeOrderService.save(order);
							logger.info(employee.getName() + " 创建工单:" + order.getTypeDict() + "成功！");
						}
						//如果员工档案为审核成功或者失败状态，将审核提醒代办工单设置为已办
						if (CollectionUtils.isNotEmpty(orders) && ( employee.getAuditStatus() == 2 || employee.getAuditStatus() == 3 )) {
							for (EmployeeOrder employeeOrder : orders) {
								employeeOrder.setStatus(1);
								employeeOrderService.saveOrUpdate(employeeOrder);
							}
						}
					}
				}
				json.setSuccess(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	/**
	 * 设置员工转正
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:baseinfomgr:regular")
	@OperationLog(content = "设置员工转正" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/regular", method = RequestMethod.POST)
	public AjaxJson regular(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--设置员工转正--");
		AjaxJson json = new AjaxJson();
		try {
			String employeesJson = (String) params.get("employees");
			JSONParser jsonParser = new JSONParser(employeesJson);
			List<Object> employeeDatas = jsonParser.parseArray();
			if (CollectionUtils.isNotEmpty(employeeDatas)) {
				for (Object item : employeeDatas) {
					Map<String, Object> map = (Map<String, Object>) item;
					String id = StringUtils.defaultString((String) map.get("id"), "0");
					Employee employee = employeeService.get(id);
					if (employee != null) {
						employee.setIsRegular(NumberUtils.toInt((String) params.get("ud_isRegular"), 1));
						employee.setRegularDate(DateUtils.parseDate((String) params.get("ud_regularDate")));
						employee.setRegularDateTwo(DateUtils.parseDate((String) params.get("ud_regularDateTwo")));

						employee.setRegularHandleDate(new Date());
						//转正则设置离职状态为0
						employee.setHasQuitCompany(0);
						employeeService.saveOrUpdate(employee);

						//如果存在转正提醒工单，将其工单改成已办
						Map<String, Object> orderParam = Maps.newHashMap();
						orderParam.put("employeeId", employee.getId());
						orderParam.put("type", 3);
						orderParam.put("status", 0);
						List<EmployeeOrder> orders = employeeOrderService.find(orderParam);
						if (CollectionUtils.isNotEmpty(orders)) {
							for (EmployeeOrder employeeOrder : orders) {
								employeeOrder.setStatus(1);
								employeeOrderService.saveOrUpdate(employeeOrder);
							}
						}
					}
				}
				json.setSuccess(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}

	/**
	 * 设置员工离职
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:baseinfomgr:quitcompany")
	@OperationLog(content = "设置员工离职" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/quitCompany", method = RequestMethod.POST)
	public AjaxJson quitCompany(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--设置员工离职--");
		AjaxJson json = new AjaxJson();
		try {
			String employeesJson = (String) params.get("employees");
			JSONParser jsonParser = new JSONParser(employeesJson);
			List<Object> employeeDatas = jsonParser.parseArray();
			if (CollectionUtils.isNotEmpty(employeeDatas)) {
				for (Object item : employeeDatas) {
					Map<String, Object> map = (Map<String, Object>) item;
					String id = StringUtils.defaultString((String) map.get("id"), "0");
					Employee employee = employeeService.get(id);
					if (employee != null) {
						employee.setHasQuitCompany(NumberUtils.toInt((String) params.get("ud_hasQuitCompany"), 0));
						employee.setQuitCompanyType(NumberUtils.toInt((String) params.get("ud_quitCompanyType"), 0));
						employee.setQuitCompanyDate(DateUtils.parseDate((String) params.get("ud_quitCompanyDate")));
						employee.setQuitCompanyResult((String) params.get("ud_quitCompanyResult"));
						//离职则设转正状态为0
						employee.setIsRegular(0);
						employeeService.saveOrUpdate(employee);
					}
				}
				json.setSuccess(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	/**
	 * 根据ID得到员工档案详情
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions(value = {"emp:baseinfomgr:add","emp:baseinfomgr:edit"},logical=Logical.OR)
	@OperationLog(content = "根据ID得到员工档案详情" , type = OperationType.VIEW)
	@ResponseBody
	@RequestMapping(value = "/detail", method = RequestMethod.POST)
	public AjaxJson detail(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--根据ID得到员工档案详情--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			Employee employee = employeeService.get(id);
			json.setModel(employee);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	/**
	 * 头像上传
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@OperationLog(content = "修改员工头像" , type = OperationType.VIEW)
	@ResponseBody
	@RequestMapping(value = "/headerUpload")
	public AjaxJson headerUpload(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--修改员工头像--");
		AjaxJson json = new AjaxJson();
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multiRequest.getFile("file");
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			Employee employee = employeeService.get(id);
			if (employee != null) {
				employee.setHeaderImg(file.getBytes());
				employee.setHeaderImgName(employee.getId() + "-" + Math.random() + "." + StringUtils.lowerCase(file.getOriginalFilename().substring(file.getOriginalFilename()
						.lastIndexOf('.') + 1)));
				employeeService.saveOrUpdate(employee);
				
				//如果存在旧文件,则将旧文件删除
				String directoryUrl = Global.getConfig("tmp.user_header.url") + employee.getId();
				File oldDirectory = new File(getRealPath(session) + directoryUrl);
				if (oldDirectory.exists()) {
					FileUtils.cleanDirectory(oldDirectory);
				}
				
				json.setMessage("上传成功");
				json.setSuccess(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setMessage("数据处理失败!");
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * 得到头像URL
	 * @param request
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/headerImg", method = RequestMethod.POST)
	public AjaxJson headerImg(HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params) {
		logger.info("--获得员工头像--");
		AjaxJson json = new AjaxJson();
		json.setModel(Global.getConfig("default.header.url"));
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			Employee employee = employeeService.get(id);
			if (employee != null && employee.getHeaderImg() != null) {
				//创建目录
				String directoryUrl = Global.getConfig("tmp.user_header.url") + employee.getId();
				File directory = new File(getRealPath(session) + directoryUrl);
				if (!directory.exists()) {
					directory.mkdir();
				}
				//创建文件
				String url = directoryUrl + "/" + employee.getHeaderImgName();
				File file = new File(getRealPath(session) + url);
				if (!file.exists()) {
					file.createNewFile();
					OutputStream output = new FileOutputStream(file);
					BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
					bufferedOutput.write(employee.getHeaderImg());
					bufferedOutput.close();
					output.close();
				}
				json.setModel(url);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 根据类型得到员工附件
	 *
	 * @param session
	 * @param request
	 * @param params
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/employeeAnnexList", method = RequestMethod.POST)
	public AjaxJson employeeAnnexList(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("---根据类型得到员工附件---");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			String annexType = (String) params.get("annexType");
			Employee employee = employeeService.get(id);
			if (employee != null && StringUtils.isNotBlank(annexType)) {
				if ("signForm".equals(annexType) && employee.getSignForm() != null) {
					//签收表
					json.setModel(employee.getSignFormName());
				} else if ("nonPurchaseCommit".equals(annexType) && employee.getNonPurchaseCommit() != null) {
					//不购承诺
					json.setModel(employee.getNonPurchaseCommitName());
				} else if ("annex".equals(annexType)){
					//员工附件
					Map<String, Object> reqParams = new HashMap<String, Object>();
					reqParams.put("employeeId", (String) params.get("employeeId"));
					List<EmployeeAnnex> annexs = annexService.find(reqParams);
					json.setModel(annexs);
				} else if ("contractHome".equals(annexType)){
					//合同首页
					Map<String, Object> reqParams = new HashMap<String, Object>();
					reqParams.put("employeeId", (String) params.get("employeeId"));
					List<EmployeeContractHomeAnnex> homeAnnexs = homeAnnexService.find(reqParams);
					json.setModel(homeAnnexs);
				} else if ("contractFinal".equals(annexType)){
					//合同尾页
					Map<String, Object> reqParams = new HashMap<String, Object>();
					reqParams.put("employeeId", id);
					List<EmployeeContractFinalAnnex> finalAnnexs = finalAnnexService.find(reqParams);
					json.setModel(finalAnnexs);
				} else if ("postChangeAnnex".equals(annexType) && employee.getPostChangeAnnex() != null) {
					//职位变更
					json.setModel(employee.getPostChangeAnnexName());
				} else if ("deptChangeAnnex".equals(annexType) && employee.getDeptChangeAnnex() != null) {
					//部门变更
					json.setModel(employee.getDeptChangeAnnexName());
				} else if ("operaChangeAnnex".equals(annexType) && employee.getOperaChangeAnnex() != null) {
					//操作变更
					json.setModel(employee.getOperaChangeAnnexName());
				} else if ("wageChangeAnnex".equals(annexType) && employee.getWageChangeAnnex() != null) {
					//工资变更
					json.setModel(employee.getWageChangeAnnexName());
				}
			}
			json.setMessage(annexType);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * 根据类型上传员工附件
	 * @param request
	 * @param response
	 * @param plupload
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/employeeAnnexUpload")
	public AjaxJson employeeAnnexUpload(MultipartHttpServletRequest request, HttpServletResponse response) {
		logger.info("---根据类型上传员工附件---");
		AjaxJson json = new AjaxJson();
		json.setMessage("上传的文件没有有效数据!");
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multiRequest.getFile("file");
		try {
			String content = request.getParameter("content");
			if (content.split("_").length == 2) {
				String id = StringUtils.defaultIfBlank(content.split("_")[0], "0");
				String annexType = content.split("_")[1];
				Employee employee = employeeService.get(id);
				if (employee != null && StringUtils.isNotBlank(annexType)) {
					if ("signForm".equals(annexType)) {
						//签收表
						employee.setSignFormName(file.getOriginalFilename());
						employee.setSignForm(file.getBytes());
						employee.setHasSignForm(1);
						employeeService.saveOrUpdate(employee);
					} else if ("nonPurchaseCommit".equals(annexType)) {
						//不购承诺
						employee.setNonPurchaseCommitName(file.getOriginalFilename());
						employee.setNonPurchaseCommit(file.getBytes());
						employee.setHasNonPurchaseCommit(1);
						employeeService.saveOrUpdate(employee);
					} else if ("annex".equals(annexType)){
						//员工附件
						EmployeeAnnex annex = new EmployeeAnnex();
						annex.setAnnexContent(file.getBytes());
						annex.setAnnexName(file.getOriginalFilename());
						annex.setEmployee(employee);
						annexService.save(annex);
					} else if ("contractHome".equals(annexType)){
						//合同首页
						EmployeeContractHomeAnnex homeAnnex = new EmployeeContractHomeAnnex();
						homeAnnex.setAnnexContent(file.getBytes());
						homeAnnex.setAnnexName(file.getOriginalFilename());
						homeAnnex.setEmployee(employee);
						homeAnnexService.save(homeAnnex);
					} else if ("contractFinal".equals(annexType)){
						//合同尾页
						EmployeeContractFinalAnnex finalAnnex = new EmployeeContractFinalAnnex();
						finalAnnex.setAnnexContent(file.getBytes());
						finalAnnex.setAnnexName(file.getOriginalFilename());
						finalAnnex.setEmployee(employee);
						finalAnnexService.save(finalAnnex);
					} else if ("postChangeAnnex".equals(annexType)) {
						//职位变更
						employee.setPostChangeAnnexName(file.getOriginalFilename());
						employee.setPostChangeAnnex(file.getBytes());
						employeeService.saveOrUpdate(employee);
					} else if ("deptChangeAnnex".equals(annexType)) {
						//部门变更
						employee.setDeptChangeAnnexName(file.getOriginalFilename());
						employee.setDeptChangeAnnex(file.getBytes());
						employeeService.saveOrUpdate(employee);
					} else if ("operaChangeAnnex".equals(annexType)) {
						//操作变更
						employee.setOperaChangeAnnexName(file.getOriginalFilename());
						employee.setOperaChangeAnnex(file.getBytes());
						employeeService.saveOrUpdate(employee);
					} else if ("wageChangeAnnex".equals(annexType)) {
						//工资变更
						employee.setWageChangeAnnexName(file.getOriginalFilename());
						employee.setWageChangeAnnex(file.getBytes());
						employeeService.saveOrUpdate(employee);
					}
				}
				json.setMessage("上传成功");
				json.setSuccess(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setMessage("数据处理失败!");
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * 根据类型下载员工附件
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("restriction")
	@ResponseBody
	@RequestMapping(value = "/employeeAnnexDownload")
	public void employeeAnnexDownload(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> params) {
		logger.info("--根据类型下载员工附件--");
		try {
			InputStream in = null; // 文件输入
			String fileName = null; // 文件名
			
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			String annexId = StringUtils.defaultIfBlank((String) params.get("annexId"), "0");
			String annexType = request.getParameter("annexType");
			Employee employee = employeeService.get(id);
			if (employee != null && StringUtils.isNotBlank(annexType)) {
				if ("signForm".equals(annexType) && employee.getSignForm() != null) {
					//签收表
					in = new ByteArrayInputStream(employee.getSignForm());
					fileName = employee.getSignFormName();
				} else if ("nonPurchaseCommit".equals(annexType) && employee.getNonPurchaseCommit() != null) {
					//不购承诺
					in = new ByteArrayInputStream(employee.getNonPurchaseCommit());
					fileName = employee.getNonPurchaseCommitName();
				} else if ("annex".equals(annexType)){
					//员工附件
					EmployeeAnnex annex = annexService.get(annexId);
					if (annex != null && annex.getAnnexContent() != null) {
						in = new ByteArrayInputStream(annex.getAnnexContent());
						fileName = annex.getAnnexName();
					}
				} else if ("contractHome".equals(annexType)){
					//合同首页
					EmployeeContractHomeAnnex homeAnnex = homeAnnexService.get(annexId);
					if (homeAnnex != null && homeAnnex.getAnnexContent() != null) {
						in = new ByteArrayInputStream(homeAnnex.getAnnexContent());
						fileName = homeAnnex.getAnnexName();
					}
				} else if ("contractFinal".equals(annexType)){
					//合同尾页
					EmployeeContractFinalAnnex finalAnnex = finalAnnexService.get(annexId);
					if (finalAnnex != null && finalAnnex.getAnnexContent() != null) {
						in = new ByteArrayInputStream(finalAnnex.getAnnexContent());
						fileName = finalAnnex.getAnnexName();
					}
				} else if ("postChangeAnnex".equals(annexType)) {
					//职位变更
					in = new ByteArrayInputStream(employee.getPostChangeAnnex());
					fileName = employee.getPostChangeAnnexName();
				} else if ("deptChangeAnnex".equals(annexType)) {
					//部门变更
					in = new ByteArrayInputStream(employee.getDeptChangeAnnex());
					fileName = employee.getDeptChangeAnnexName();
				} else if ("operaChangeAnnex".equals(annexType)) {
					//操作变更
					in = new ByteArrayInputStream(employee.getOperaChangeAnnex());
					fileName = employee.getOperaChangeAnnexName();
				} else if ("wageChangeAnnex".equals(annexType)) {
					//工资变更
					in = new ByteArrayInputStream(employee.getWageChangeAnnex());
					fileName = employee.getWageChangeAnnexName();
				}
				
				if (in != null && StringUtils.isNotBlank(fileName)) {
					DownloadUtil.downloadFile(in, fileName , response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据类型删除员工附件
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@OperationLog(content = "" , type = OperationType.DELETE)
	@ResponseBody
	@RequestMapping(value = "/employeeAnnexDelete", method = RequestMethod.POST)
	public AjaxJson employeeAnnexDelete(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--根据类型删除员工附件--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			String annexId = StringUtils.defaultIfBlank((String) params.get("annexId"), "0");
			String annexType = request.getParameter("annexType");
			Employee employee = employeeService.get(id);
			if (employee != null && StringUtils.isNotBlank(annexType)) {
				if ("signForm".equals(annexType)) {
					//签收表
					employee.setSignForm(null);
					employee.setSignFormName(null);
					employee.setHasSignForm(0);
					employeeService.saveOrUpdate(employee);
				} else if ("nonPurchaseCommit".equals(annexType)) {
					//不购承诺
					employee.setNonPurchaseCommit(null);
					employee.setNonPurchaseCommitName(null);
					employee.setHasPublicFund(0);
					employeeService.saveOrUpdate(employee);
				} else if ("annex".equals(annexType)){
					//员工附件
					EmployeeAnnex annex = annexService.get(annexId);
					if (annex != null) {
						annexService.delete(annex);
					}
				} else if ("contractHome".equals(annexType)){
					//合同首页
					EmployeeContractHomeAnnex homeAnnex = homeAnnexService.get(annexId);
					if (homeAnnex != null) {
						homeAnnexService.delete(homeAnnex);
					}
				} else if ("contractFinal".equals(annexType)){
					//合同尾页
					EmployeeContractFinalAnnex finalAnnex = finalAnnexService.get(annexId);
					if (finalAnnex != null) {
						finalAnnexService.delete(finalAnnex);
					}
				} else if ("postChangeAnnex".equals(annexType)) {
					//职位变更
					employee.setPostChangeAnnex(null);
					employee.setPostChangeAnnexName(null);
					employeeService.saveOrUpdate(employee);
				} else if ("deptChangeAnnex".equals(annexType)) {
					//部门变更
					employee.setDeptChangeAnnex(null);
					employee.setDeptChangeAnnexName(null);
					employeeService.saveOrUpdate(employee);
				} else if ("operaChangeAnnex".equals(annexType)) {
					//操作变更
					employee.setOperaChangeAnnex(null);
					employee.setOperaChangeAnnexName(null);
					employeeService.saveOrUpdate(employee);
				} else if ("wageChangeAnnex".equals(annexType)) {
					//工资变更
					employee.setWageChangeAnnex(null);
					employee.setWageChangeAnnexName(null);
					employeeService.saveOrUpdate(employee);
				}
			}
			json.setModel(annexType);
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
	 * 得到合同首页附件清单
	 * @param request
	 * @param response
	 * @param plupload
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/contractHomeAnnexList", method = RequestMethod.POST)
	public AjaxJson contractHomeAnnexList(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("---得到合同首页附件清单---");
		AjaxJson json = new AjaxJson();
		try {
			Map<String, Object> reqParams = new HashMap<String, Object>();
			reqParams.put("employeeId", (String) params.get("employeeId"));
			List<EmployeeContractHomeAnnex> homeAnnexs = homeAnnexService.find(reqParams);
			json.setModel(homeAnnexs);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * 上传合同首页附件
	 * @param request
	 * @param response
	 * @param plupload
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/contractHomeAnnexUpload")
	public AjaxJson contractHomeAnnexUpload(MultipartHttpServletRequest request, HttpServletResponse response) {
		logger.info("---上传合同首页附件---");
		AjaxJson json = new AjaxJson();
		json.setMessage("上传的文件没有有效数据!");
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multiRequest.getFile("file");
		try {
			String employeeId = request.getParameter("employeeId");
			Employee employee = employeeService.get(employeeId);
			if (employee != null) {
				EmployeeContractHomeAnnex homeAnnex = new EmployeeContractHomeAnnex();
				homeAnnex.setAnnexContent(file.getBytes());
				homeAnnex.setAnnexName(file.getOriginalFilename());
				homeAnnex.setEmployee(employee);
				homeAnnexService.save(homeAnnex);
			}
			json.setMessage("上传成功");
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setMessage("数据处理失败!");
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * 下载合同首页附件
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@OperationLog(content = "" , type = OperationType.DELETE)
	@ResponseBody
	@RequestMapping(value = "/contractHomeAnnexDownload/{annexId}")
	public void contractHomeAnnexDownload(HttpServletRequest request,HttpServletResponse response,@PathVariable String annexId) {
		logger.info("--下载合同首页附件--");
		try {
			EmployeeContractHomeAnnex homeAnnex = homeAnnexService.get(annexId);
			if (homeAnnex != null && homeAnnex.getAnnexContent() != null) {
				InputStream in = new ByteArrayInputStream(homeAnnex.getAnnexContent());
				DownloadUtil.downloadFile(in, homeAnnex.getAnnexName(), response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除合同首页附件
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@OperationLog(content = "" , type = OperationType.DELETE)
	@ResponseBody
	@RequestMapping(value = "/contractHomeAnnexDelete", method = RequestMethod.POST)
	public AjaxJson contractHomeAnnexDelete(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--删除合同首页附件--");
		AjaxJson json = new AjaxJson();
		try {
			String annexId = (String) params.get("annexId");
			EmployeeContractHomeAnnex homeAnnex = homeAnnexService.get(annexId);
			if (homeAnnex != null) {
				homeAnnexService.delete(homeAnnex);
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
	 * 得到合同尾页附件清单
	 * @param request
	 * @param response
	 * @param plupload
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/contractFinalAnnexList", method = RequestMethod.POST)
	public AjaxJson contractFinalAnnexList(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("---得到合同尾页附件清单---");
		AjaxJson json = new AjaxJson();
		try {
			Map<String, Object> reqParams = new HashMap<String, Object>();
			reqParams.put("employeeId", (String) params.get("employeeId"));
			List<EmployeeContractFinalAnnex> finalAnnexs = finalAnnexService.find(reqParams);
			json.setModel(finalAnnexs);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * 上传合同尾页附件
	 * @param request
	 * @param response
	 * @param plupload
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/contractFinalAnnexUpload")
	public AjaxJson contractFinalAnnexUpload(MultipartHttpServletRequest request, HttpServletResponse response) {
		logger.info("---上传合同尾页附件---");
		AjaxJson json = new AjaxJson();
		json.setMessage("上传的文件没有有效数据!");
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multiRequest.getFile("file");
		try {
			String employeeId = request.getParameter("employeeId");
			Employee employee = employeeService.get(employeeId);
			if (employee != null) {
				EmployeeContractFinalAnnex finalAnnex = new EmployeeContractFinalAnnex();
				finalAnnex.setAnnexContent(file.getBytes());
				finalAnnex.setAnnexName(file.getOriginalFilename());
				finalAnnex.setEmployee(employee);
				finalAnnexService.save(finalAnnex);
			}
			json.setMessage("上传成功");
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setMessage("数据处理失败!");
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * 下载合同尾页附件
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@OperationLog(content = "" , type = OperationType.DELETE)
	@ResponseBody
	@RequestMapping(value = "/contractFinalAnnexDownload/{annexId}")
	public void contractFinalAnnexDownload(HttpServletRequest request,HttpServletResponse response,@PathVariable String annexId) {
		logger.info("--下载合同尾页附件--");
		try {
			EmployeeContractFinalAnnex finalAnnex = finalAnnexService.get(annexId);
			if (finalAnnex != null && finalAnnex.getAnnexContent() != null) {
				InputStream in = new ByteArrayInputStream(finalAnnex.getAnnexContent());
				DownloadUtil.downloadFile(in, finalAnnex.getAnnexName(), response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除合同尾页附件
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@OperationLog(content = "" , type = OperationType.DELETE)
	@ResponseBody
	@RequestMapping(value = "/contractFinalAnnexDelete", method = RequestMethod.POST)
	public AjaxJson contractFinalAnnexDelete(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--删除合同尾页附件--");
		AjaxJson json = new AjaxJson();
		try {
			String annexId = (String) params.get("annexId");
			EmployeeContractFinalAnnex finalAnnex = finalAnnexService.get(annexId);
			if (finalAnnex != null) {
				finalAnnexService.delete(finalAnnex);
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
	 * 导出员工Excel
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:baseinfomgr:export")
	@OperationLog(content = "导出员工Excel" , type = OperationType.VIEW)
	@ResponseBody
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> params) {
		logger.info("--导出员工Excel--");
		try {
			String fileName = "员工档案.xlsx";
			String filePath = getExportPath(request) + fileName;
			
			Map<String, Object> reqParams = Maps.newHashMap(params);
			List<Employee> employees = employeeService.find(reqParams);
			List<ExcelColumn> excelColumns = DynamicgridItem.toExcelColumns(DynamicgridItemCache.getDynamicgridItemsByName("员工档案表格"));
			if (CollectionUtils.isNotEmpty(excelColumns)) {
				for (ExcelColumn excelColumn : excelColumns) {
					if ("浏览".equals(excelColumn.getTitle())) {
						excelColumns.remove(excelColumn);
						break;
					}
				}
				for (ExcelColumn excelColumn : excelColumns) {
					if ("附件清单".equals(excelColumn.getTitle())) {
						excelColumns.remove(excelColumn);
						break;
					}
				}
				ObjectMapper m = new ObjectMapper(); 
				String json = m.writeValueAsString(employees);  
				List<Map<String,Object>> data = convertJsonArrToMap(json);
				
				CustomizeToExcel.toFile(excelColumns, data ,filePath);
			}
			DownloadUtil.downloadFile(filePath, fileName, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
