package com.eastcom.baseframe.web.modules.sys.web.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.persistence.PageHelper;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.baseframe.web.modules.sys.cache.DictCache;
import com.eastcom.baseframe.web.modules.sys.entity.Dict;
import com.eastcom.baseframe.web.modules.sys.service.DictService;
import com.eastcom.hrmis.modules.emp.EmpConstant;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 字典Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/sys/dict")
public class DictController extends BaseController{

	@Autowired
	private DictService dictService;
	
	@RequiresPermissions("sys:dictmgr:view")
	@ResponseBody
	@RequestMapping(value = "/treeList", method = RequestMethod.POST)
	public List<Dict> treeList(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--获取所有字典列表--");
		List<Dict> result = Lists.newArrayList();
		try {
			List<String> dictIds = Lists.newArrayList();
			dictIds.add(EmpConstant.DICT_CONTRACT_TYPE);
			dictIds.add(EmpConstant.DICT_EMPLOYEE_POST_TYPE);
			dictIds.add(EmpConstant.DICT_EMPLOYEE_MARRY_TYPE);
			dictIds.add(EmpConstant.DICT_EMPLOYEE_POLITY_TYPE);
			dictIds.add(EmpConstant.DICT_EMPLOYEE_EDUCATION_TYPE);
			dictIds.add(EmpConstant.DICT_EMPLOYEE_NATIVE_PLACT_TYPE);
			dictIds.add(EmpConstant.DICT_EMPLOYEE_LABOR_TYPE);
			dictIds.add(EmpConstant.DICT_EMPLOYEE_MEALROOM_TYPE);
			dictIds.add(EmpConstant.DICT_EMPLOYEE_QUITCOMPANY_TYPE);
			dictIds.add(EmpConstant.DICT_EMPLOYEE_CONTRACT_TYPE);
			dictIds.add(EmpConstant.DICT_EMPLOYEE_RELATIONSHIP_TYPE);
			dictIds.add(EmpConstant.DICT_EMPLOYEE_STUDY_TYPE);
			dictIds.add(EmpConstant.DICT_EMPLOYEE_BANK_TYPE);
			dictIds.add(EmpConstant.DICT_EMPLOYEE_DRIVELICENSE_TYPE);
			dictIds.add(EmpConstant.DICT_MANAGE_LEVEL);
			dictIds.add(EmpConstant.DICT_HOUR_LIST_TYPE);
			dictIds.add(EmpConstant.DICT_MINUTE_LIST_TYPE);
			//dictIds.add(EmpConstant.DICT_AT_HOLIDAY_TYPE);

			List<Dict> all = DictCache.getTreeList();
			if (CollectionUtils.isNotEmpty(all)) {
				for (Dict dict : all) {
					for (String dictId : dictIds) {
						if (dictId.equals(dict.getId())) {
							result.add(dict);
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--获取字典类型列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			int pageNo = NumberUtils.toInt((String) params.get("page"), 1);
			int pageSize = NumberUtils.toInt((String) params.get("rows"), 1000);
			String parentId = StringUtils.defaultString((String )params.get("parentId"), "null");
			Map<String, Object> reqParams = new HashMap<String, Object>();
			reqParams.put("parentId", parentId);
			PageHelper<Dict> pageHelper = dictService.find(reqParams, pageNo, pageSize);
			gridJson.setRows(pageHelper.getList());
			gridJson.setTotal(pageHelper.getCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gridJson;
	}
	
	@RequiresPermissions(value = {"sys:dictmgr:add","sys:dictmgr:edit"},logical=Logical.OR)
	@OperationLog(content = "" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改字典项--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			String parentId = StringUtils.defaultIfBlank((String) params.get("parentId"), "null");
			String type = StringUtils.defaultIfBlank((String) params.get("type"), "");
			String code = (String) params.get("code");
			String name = (String) params.get("name");
			String description = (String) params.get("description");
			
			Dict dict = dictService.get(id);
			if (dict == null) {
				//根据parentId与code是否已存在，同一个parentId不能存在相同的code
				Map<String, Object> reqParam = Maps.newHashMap();
				reqParam.put("parentId", parentId);
				reqParam.put("code", code);
				List<Dict> temps = dictService.find(reqParam);
				if (CollectionUtils.isNotEmpty(temps)) {
					json.setSuccess(false);
					json.setMessage("操作失败,编码不唯一");
					return json;
				}
				
				dict = new Dict();
				dict.setCode(code);
				json.setMessage("add");
			} else {
				json.setMessage("update");
			}
			dict.setName(name);
			dict.setType(type);
			dict.setDescription(description);
			
			Dict parentDict = dictService.get(parentId);
			if (parentDict != null) {
				dict.setParent(parentDict);
			}
			
			dictService.saveOrUpdate(dict);
			json.setSuccess(true);
			json.setModel(dict);
			
			DictCache.clearCache();
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	@RequiresPermissions("sys:dictmgr:del")
	@OperationLog(content = "" , type = OperationType.DELETE)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--级联删除字典--");
		AjaxJson json = new AjaxJson();
		try {
			String id = (String) params.get("id");
			if (StringUtils.isNotBlank(id)) {
				dictService.deleteById(id);
				json.setModel(id);
				json.setSuccess(true);
				json.setMessage("操作成功");
				DictCache.clearCache();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("操作失败");
		}
		return json;
	}
	
	@RequiresPermissions("sys:dictmgr:sort")
	@OperationLog(content = "" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/saveSort", method = RequestMethod.POST)
	public AjaxJson saveSort(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--保存排列顺序--");
		AjaxJson json = new AjaxJson();
		try {
			String sortJson = (String) params.get("sortJson");
			JSONParser jsonParser = new JSONParser(sortJson);
			List<Object> sortDatas = jsonParser.parseArray();
			dictService.updateCascadeSort(sortDatas);
			DictCache.clearCache();
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
