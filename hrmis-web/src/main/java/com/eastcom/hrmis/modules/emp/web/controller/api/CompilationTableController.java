package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.persistence.PageHelper;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.hrmis.modules.emp.entity.CompilationTable;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDept;
import com.eastcom.hrmis.modules.emp.entity.EmployeePost;
import com.eastcom.hrmis.modules.emp.service.CompilationTableService;
import com.eastcom.hrmis.modules.emp.service.EmployeeDeptService;
import com.eastcom.hrmis.modules.emp.service.EmployeePostService;
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
 * 员工编制表Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/compilationtable")
public class CompilationTableController extends BaseController {

	@Autowired
	private CompilationTableService compilationTableService;
	
	@Autowired
	private EmployeeDeptService employeeDeptService;
	
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
	@RequiresPermissions("emp:compilationtablemgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取员工编制表信息--");
		DataGridJson gridJson = new DataGridJson();
		try {
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 9999);
			Map<String, Object> reqParams = Maps.newHashMap(params);
			reqParams.remove("page");
			reqParams.remove("rows");
			PageHelper<CompilationTable> pageHelper = compilationTableService.find(reqParams, pageNo, pageSize);
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
	@RequiresPermissions(value = {"emp:compilationtablemgr:add","emp:compilationtablemgr:edit"},logical=Logical.OR)
	@OperationLog(content = "新增或者修改员工编制表信息" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改员工编制表信息--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			CompilationTable compilationTable = compilationTableService.get(id);
			if (compilationTable == null) {
				compilationTable = new CompilationTable();
			}
			compilationTable.setName((String) params.get("name"));
			compilationTable.setRemark((String) params.get("remark"));
			
			compilationTableService.saveOrUpdate(compilationTable);
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
	@RequiresPermissions("emp:compilationtablemgr:del")
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
				compilationTableService.deleteByIds(ids);
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
	 * 得到部门树图（用于添加员工编制）
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:compilationtablemgr:auth")
	@OperationLog(content = "得到部门树图（用于添加员工编制）" , type = OperationType.VIEW)
	@ResponseBody
	@RequestMapping(value = "/deptTreeList", method = RequestMethod.POST)
	public List<EmployeeDept> deptTreeList(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--得到部门树图（用于添加员工编制）--");
		List<EmployeeDept> depts = Lists.newArrayList();
		try {
			String id = StringUtils.defaultString((String )params.get("id"), "null");
			CompilationTable compilationTable = compilationTableService.get(id);
			if (compilationTable != null) {
				depts = employeeDeptService.find(new HashMap<String, Object>());
				if (CollectionUtils.isNotEmpty(depts) && CollectionUtils.isNotEmpty(compilationTable.getDepts())) {
					for (EmployeeDept temp : depts) {
						for (EmployeeDept temp2 : compilationTable.getDepts()) {
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
	 * 得到岗位树图（用于添加员工编制）
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:compilationtablemgr:auth")
	@OperationLog(content = "得到岗位树图（用于添加员工编制）" , type = OperationType.VIEW)
	@ResponseBody
	@RequestMapping(value = "/postTreeList", method = RequestMethod.POST)
	public List<EmployeePost> postTreeList(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--得到岗位树图（用于添加员工编制）--");
		List<EmployeePost> posts = Lists.newArrayList();
		try {
			String id = StringUtils.defaultString((String )params.get("id"), "null");
			CompilationTable compilationTable = compilationTableService.get(id);
			if (compilationTable != null) {
				posts = employeePostService.find(new HashMap<String, Object>());
				if (CollectionUtils.isNotEmpty(posts) && CollectionUtils.isNotEmpty(compilationTable.getPosts())) {
					for (EmployeePost temp : posts) {
						for (EmployeePost temp2 : compilationTable.getPosts()) {
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
	 * 更新员工编制部门项目与岗位配置
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:compilationtablemgr:auth")
	@OperationLog(content = "更新员工编制部门项目与岗位配置" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/updateDeptPostSet", method = RequestMethod.POST)
	public AjaxJson updateDeptPostSet(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--更新员工编制部门项目与岗位配置--");
		AjaxJson json = new AjaxJson();
		try {
			//部门
			String checkDeptNodesJson = (String) params.get("checkDeptNodes");
			List<Object> checkDeptNodes = new JSONParser(checkDeptNodesJson).parseArray();
			
			//岗位
			String checkPostNodesJson = (String) params.get("checkPostNodes");
			List<Object> checkPostNodes = new JSONParser(checkPostNodesJson).parseArray();
			
			//编制表
			String selectNodesJson = (String) params.get("selectNode");
			Object selectNode = new JSONParser(selectNodesJson).parse();
			
			compilationTableService.updateSetting(selectNode, checkDeptNodes, checkPostNodes);
			
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
