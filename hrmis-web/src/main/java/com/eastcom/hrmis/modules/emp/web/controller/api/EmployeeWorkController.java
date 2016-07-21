package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.persistence.PageHelper;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.eastcom.hrmis.modules.emp.entity.EmployeeStudy;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWork;
import com.eastcom.hrmis.modules.emp.service.EmployeeService;
import com.eastcom.hrmis.modules.emp.service.EmployeeWorkService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
 * 员工工作经历Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/work")
public class EmployeeWorkController extends BaseController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeWorkService employeeWorkService;
	
	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取员工学习经历列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 999);
			Map<String, Object> reqParams = new HashMap<String, Object>();
			reqParams.put("employeeId", (String) params.get("employeeId"));
			PageHelper<EmployeeStudy> pageHelper = employeeWorkService.find(reqParams, pageNo, pageSize);
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
	@OperationLog(content = "" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改员工学习经历--");
		AjaxJson json = new AjaxJson();
		try {
			String employeeId = StringUtils.defaultIfBlank((String) params.get("employeeId"), "0");
			Employee employee = employeeService.get(employeeId);
			if (employee != null) {
				String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
				String startDate = (String) params.get("startDate");
				String endDate = (String) params.get("endDate");
				String company = (String) params.get("company");
				String jobName = (String) params.get("jobName");
				String witness = (String) params.get("witness");
				String telephone = (String) params.get("telephone");
				
				EmployeeWork employeeWork = employeeWorkService.get(id);
				if (employeeWork == null) {
					employeeWork = new EmployeeWork();
					employeeWork.setEmployee(employee);
				}
				
				employeeWork.setStartDate(DateUtils.parseDate(startDate));
				employeeWork.setEndDate(DateUtils.parseDate(endDate));
				employeeWork.setCompany(company);
				employeeWork.setJobName(jobName);
				employeeWork.setWitness(witness);
				employeeWork.setTelephone(telephone);
				employeeWorkService.saveOrUpdate(employeeWork);
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
	 * 删除
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@OperationLog(content = "" , type = OperationType.DELETE)
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--删除员工学习经历--");
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
				employeeWorkService.deleteByIds(ids);
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
	
}
