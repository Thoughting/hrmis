package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.persistence.PageHelper;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.hrmis.modules.emp.cache.EmployeeDeptCache;
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDept;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDeptLeader;
import com.eastcom.hrmis.modules.emp.service.EmployeeCheckWorkStatMonthService;
import com.eastcom.hrmis.modules.emp.service.EmployeeDeptLeaderService;
import com.eastcom.hrmis.modules.emp.service.EmployeeDeptService;
import com.eastcom.hrmis.modules.emp.service.EmployeeService;
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
 * 职务部门Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/dept")
public class EmployeeDeptController extends BaseController {

	@Autowired
	private EmployeeDeptService employeeDeptService;
	
	@Autowired
	private EmployeeDeptLeaderService employeeDeptLeaderService;
	
	@Autowired
	private EmployeeService employeeService;
	
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
	@RequiresPermissions("emp:deptmgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取职务部门列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 1);
			Map<String, Object> reqParams = new HashMap<String, Object>();
			reqParams.put("code", (String) params.get("code"));
			reqParams.put("name", (String) params.get("name"));
			PageHelper<EmployeeDept> pageHelper = employeeDeptService.find(reqParams, pageNo, pageSize);
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
	@RequiresPermissions(value = {"emp:deptmgr:add","emp:deptmgr:edit"},logical=Logical.OR)
	@OperationLog(content = "新增或者修改职务部门" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改职务部门--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			String code = (String) params.get("code");
			String name = (String) params.get("name");
			String remark = (String) params.get("remark");
			String telephone = (String) params.get("telephone");
			double workTimer = Double.parseDouble((String) params.get("workTimer"));
			
			EmployeeDept employeeDept = employeeDeptService.get(id);
			if (employeeDept == null) {
				//判断code是否唯一
				Map<String, Object> reqParam = Maps.newHashMap();
				reqParam.put("code_exact", code);
				if (CollectionUtils.isNotEmpty(employeeDeptService.find(reqParam))) {
					json.setSuccess(false);
					json.setMessage("操作失败,该编码已存在!");
					return json;
				}
				
				employeeDept = new EmployeeDept();
			}
			employeeDept.setCode(code);
			employeeDept.setName(name);
			employeeDept.setRemark(remark);
			employeeDept.setTelephone(telephone);
			employeeDept.setWorkTimer(workTimer);
			
			employeeDeptService.saveOrUpdate(employeeDept);
			
			//刷新考勤统计
			employeeCheckWorkStatMonthService.deleteByDeptId(employeeDept.getId());
			
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
	@RequiresPermissions("emp:deptmgr:del")
	@OperationLog(content = "删除职务部门" , type = OperationType.DELETE)
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--删除职务部门--");
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
				employeeDeptService.deleteByIds(ids);
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
		logger.info("--获取所有职务部门列表(下拉框)--");
		AjaxJson json = new AjaxJson();
		try {
			List<EmployeeDept> list = EmployeeDeptCache.getAuthDeptByLoginUser();
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
	
	/**
	 * 
	 * 自动生成员工编号
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/newEmpCode", method = RequestMethod.POST)
	public AjaxJson newEmpCode(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--自动生成员工编号--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			String employeeId = StringUtils.defaultIfBlank((String) params.get("employeeId"), "0");
			EmployeeDept employeeDept = employeeDeptService.get(id);
			Employee employee = employeeService.get(employeeId);
			if (employeeDept != null && employee != null) {
				String code = "";
				if (employee.getEmployeeDept() != null && employeeDept.getId().equals(employee.getEmployeeDept().getId())) {
					code = employee.getCode();
				} else {
					code = employeeDept.getCode();
					int count = employeeDeptService.getEmployeeCountByDeptId(id) + 1;
					if (count < 10) {
						code += "00" + count;
					} else if (count >= 10 && count < 100) {
						code += "0" + count;
					} else {
						code += "" + count;
					}
				}
				json.setModel(code);
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
	 * 得到部门树图（用于设置分管领导）
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/tree", method = RequestMethod.POST)
	public List<EmployeeDept> tree(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--得到部门树图（用于设置分管领导）--");
		List<EmployeeDept> depts = Lists.newArrayList();
		try {
			//是否有传分管领导ID
			String leaderId = StringUtils.defaultString((String )params.get("leaderId"), "null");
			EmployeeDeptLeader deptLeader = employeeDeptLeaderService.get(leaderId);
			if (deptLeader == null) {
				//取未设置分管领导的部门列表
				Map<String, Object> reqParams = new HashMap<String, Object>();
				reqParams.put("deptLeaderId", "null");
				depts = employeeDeptService.find(reqParams);
			} else {
				//取全部部门列表
				depts = employeeDeptService.findAll();
				if (CollectionUtils.isNotEmpty(depts) && CollectionUtils.isNotEmpty(deptLeader.getDepts())) {
					for (EmployeeDept temp : depts) {
						for (EmployeeDept temp2 : deptLeader.getDepts()) {
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
		return depts;
	}
	
	/**
	 * 得到部门授权树图
	 * 
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/authTreeList", method = RequestMethod.POST)
	public List<EmployeeDept> authTreeList(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("-得到部门授权树图--");
		List<EmployeeDept> all = Lists.newArrayList();
		try {
			String id = StringUtils.defaultString((String )params.get("id"), "null");
			all = employeeDeptService.find(new HashMap<String, Object>());
			List<EmployeeDept> checkDepts = employeeDeptService.getAuthDeptByUserId(id);
			transformAuthCheckList(all, checkDepts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return all;
	}
	
	/**
	 * 标志勾选
	 * @param all
	 * @param check
	 */
	private static void transformAuthCheckList(List<EmployeeDept> all, List<EmployeeDept> check){
		if (CollectionUtils.isNotEmpty(all) && CollectionUtils.isNotEmpty(check)) {
			for (EmployeeDept dept : all) {
				dept.setChecked(false);
				for (EmployeeDept checkDept : check) {
					if (dept.getId().equals(checkDept.getId())) {
						dept.setChecked(true);
					}
				}
			}
		}
	}
}
