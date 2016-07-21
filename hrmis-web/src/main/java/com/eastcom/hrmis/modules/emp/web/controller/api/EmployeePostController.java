package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.persistence.PageHelper;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.hrmis.modules.emp.entity.EmployeePost;
import com.eastcom.hrmis.modules.emp.entity.WagePlan;
import com.eastcom.hrmis.modules.emp.service.EmployeePostService;
import com.eastcom.hrmis.modules.emp.service.WagePlanService;
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
 * 职务岗位Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/post")
public class EmployeePostController extends BaseController {

	@Autowired
	private EmployeePostService employeePostService;
	
	@Autowired
	private WagePlanService wagePlanService;
	
	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:postmgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取职务岗位列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 1);
			Map<String, Object> reqParams = new HashMap<String, Object>();
			if (StringUtils.isNotEmpty((String) params.get("type"))) {
				reqParams.put("type", Integer.parseInt((String) params.get("type")));
			}
			reqParams.put("code", (String) params.get("code"));
			reqParams.put("name", (String) params.get("name"));
			reqParams.put("remark", (String) params.get("remark"));
			
			PageHelper<EmployeePost> pageHelper = employeePostService.find(reqParams, pageNo, pageSize);
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
	@RequiresPermissions(value = {"emp:postmgr:add","emp:postmgr:edit"},logical=Logical.OR)
	@OperationLog(content = "新增或者修改职务岗位" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改职务岗位--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			int type = Integer.parseInt((String) params.get("postType"));
			String code = (String) params.get("code");
			String name = (String) params.get("name");
			String remark = (String) params.get("remark");
			
			EmployeePost employeePost = employeePostService.get(id);
			if (employeePost == null) {
				//判断code是否唯一
				Map<String, Object> reqParam = Maps.newHashMap();
				reqParam.put("code_exact", code);
				if (CollectionUtils.isNotEmpty(employeePostService.find(reqParam))) {
					json.setSuccess(false);
					json.setMessage("操作失败,该编码已存在!");
					return json;
				}
				employeePost = new EmployeePost();
			}
			employeePost.setCode(code);
			employeePost.setType(type);
			employeePost.setName(name);
			employeePost.setRemark(remark);
			
			employeePostService.saveOrUpdate(employeePost);
			
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
	@RequiresPermissions("emp:postmgr:del")
	@OperationLog(content = "删除职务岗位" , type = OperationType.DELETE)
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--删除职务岗位--");
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
				employeePostService.deleteByIds(ids);
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
			List<EmployeePost> result = Lists.newArrayList();
			String wagePlanId = StringUtils.defaultIfBlank((String) params.get("wagePlanId"), "0");
			if ("0".equals(wagePlanId)) {
				Map<String, Object> reqParams = new HashMap<String, Object>();
				if (StringUtils.isNotEmpty((String) params.get("type"))) {
					reqParams.put("type", Integer.parseInt((String) params.get("type")));
				}
				result = employeePostService.find(reqParams);
			} else {
				WagePlan wagePlan = wagePlanService.get(wagePlanId);
				if (wagePlan != null && CollectionUtils.isNotEmpty(wagePlan.getPosts())) {
					result = wagePlan.getPosts();
				}
			}
			json.setSuccess(true);
			json.setModel(result);
			json.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
}
