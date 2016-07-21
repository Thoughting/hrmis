package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWageActualItem;
import com.eastcom.hrmis.modules.emp.service.EmployeeWageActualItemService;
import com.eastcom.hrmis.modules.emp.service.EmployeeWageService;
import com.eastcom.hrmis.modules.emp.service.WageCountItemService;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 员工实发工资项Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/actualwageitem")
public class EmployeeWageActualItemController extends BaseController {

	@Autowired
	private EmployeeWageActualItemService employeeWageActualItemService;
	
	@Autowired
	private WageCountItemService wageCountItemService;
	
	@Autowired
	private EmployeeWageService employeeWageService;
	
	/**
	 * 新增修改
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:wagecheckmgr:edit")
	@OperationLog(content = "新增或者修改员工实发工资项信息" , type = OperationType.VIEW)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改员工实发工资项信息--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			EmployeeWageActualItem item = employeeWageActualItemService.get(id);
			if (item == null) {
				item = new EmployeeWageActualItem();
			}
			item.setCount(NumberUtils.toDouble((String) params.get("count"), 0));
			item.setRemark((String) params.get("remark"));
			item.setWageCountItem(wageCountItemService.get(StringUtils.defaultIfBlank((String) params.get("wageCountItemId"), "0")));
			item.setEmployeeWage(employeeWageService.get(StringUtils.defaultIfBlank((String) params.get("employeeWageId"), "0")));
			
			employeeWageActualItemService.saveOrUpdate(item);
			
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
