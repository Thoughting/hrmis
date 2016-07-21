package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.hrmis.modules.emp.entity.PerformanceWageItem;
import com.eastcom.hrmis.modules.emp.service.PerformanceWageItemService;
import com.eastcom.hrmis.modules.emp.service.PerformanceWageService;
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
 * 绩效工资项Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/performancewageitem")
public class PerformanceWageItemController extends BaseController {

	@Autowired
	private PerformanceWageItemService performanceWageItemService;
	
	@Autowired
	private PerformanceWageService performanceWageService;
	
	/**
	 * 新增修改
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:performancewagemgr:edit")
	@OperationLog(content = "新增或者修改编制数信息" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改编制数信息--");
		AjaxJson json = new AjaxJson();
		try {
			String strField = "koukuanshuoming扣款说明，beizhu备注，shuoming说明";
			
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			PerformanceWageItem performanceWageItem = performanceWageItemService.get(id);
			if (performanceWageItem == null) {
				performanceWageItem = new PerformanceWageItem();
			}
			performanceWageItem.setField((String) params.get("field"));
			
			String value = (String) params.get("value");
			if (strField.indexOf(performanceWageItem.getField()) != -1 && !"kk".equals(performanceWageItem.getField())) {
				performanceWageItem.setValue(value);
			} else {
				performanceWageItem.setValue(NumberUtils.toDouble(value,0) + "");
			}
			performanceWageItem.setRemark((String) params.get("remark"));
			performanceWageItem.setPerformanceWage(performanceWageService.get(StringUtils.defaultIfBlank((String) params.get("performanceWageId"), "0")));
			
			performanceWageItemService.saveOrUpdate(performanceWageItem);
			
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
