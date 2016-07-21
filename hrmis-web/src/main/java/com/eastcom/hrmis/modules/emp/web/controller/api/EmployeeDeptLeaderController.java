package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDeptLeader;
import com.eastcom.hrmis.modules.emp.service.EmployeeDeptLeaderService;
import com.eastcom.hrmis.modules.emp.service.EmployeeDeptService;
import org.apache.commons.collections.CollectionUtils;
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
 * 分管部门领导Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/deptleader")
public class EmployeeDeptLeaderController extends BaseController {

	@Autowired
	private EmployeeDeptLeaderService deptLeaderService;
	
	@Autowired
	private EmployeeDeptService deptService;
	
	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:deptleader:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取分管部门领导列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			List<EmployeeDeptLeader> deptLeaders = deptLeaderService.find(new HashMap<String, Object>());
			gridJson.setRows(deptLeaders);
			gridJson.setTotal((long) deptLeaders.size());
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
	@RequiresPermissions(value = {"emp:deptleader:add","emp:deptleader:edit"},logical=Logical.OR)
	@OperationLog(content = "新增或者修改分管部门领导" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改分管部门领导--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			String name = (String) params.get("name");
			String remark = (String) params.get("remark");
			
			EmployeeDeptLeader employeeDeptLeader = deptLeaderService.get(id);
			if (employeeDeptLeader == null) {
				employeeDeptLeader = new EmployeeDeptLeader();
			}
			employeeDeptLeader.setName(name);
			employeeDeptLeader.setRemark(remark);
			
			//选择的部门列表
			String checkDeptNodesJson = (String) params.get("checkDeptNodes");
			List<Object> checkDeptNodes = new JSONParser(checkDeptNodesJson).parseArray();
			
			deptLeaderService.saveOrUpdate(employeeDeptLeader,checkDeptNodes);
			
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
	@RequiresPermissions("emp:deptleader:del")
	@OperationLog(content = "删除分管部门领导" , type = OperationType.DELETE)
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--删除分管部门领导--");
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
				deptLeaderService.deleteByIds(ids);
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
