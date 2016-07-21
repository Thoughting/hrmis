package com.eastcom.baseframe.web.modules.rest.web.controller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.utils.Cryptos;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.modules.rest.cache.RestSecretCache;
import com.eastcom.baseframe.web.modules.rest.entity.RestResource;
import com.eastcom.baseframe.web.modules.rest.entity.RestSecret;
import com.eastcom.baseframe.web.modules.rest.service.RestResourceService;
import com.eastcom.baseframe.web.modules.rest.service.RestSecretService;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.google.common.collect.Lists;

/**
 * rest secret controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/sys/rest/secret")
public class RestSecretController extends BaseController {

	@Autowired
	private RestSecretService restSecretService;
	
	@Autowired
	private RestResourceService restResourceService;
	
	@RequiresPermissions("sys:restsecretmgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--获取密钥列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			List<RestSecret> result = restSecretService.findAll();
			if (CollectionUtils.isNotEmpty(result)) {
				gridJson.setRows(result);
				gridJson.setTotal((long) result.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gridJson;
	}
	
	@RequiresPermissions(value = {"sys:restsecretmgr:add","sys:restsecretmgr:edit"},logical=Logical.OR)
	@OperationLog(content = "" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改密钥--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			String code = (String) params.get("code");
			Integer enable = NumberUtils.toInt((String) params.get("enable"), 0);
			String remark = (String) params.get("remark");
			RestSecret restSecret = restSecretService.get(id);
			if (restSecret == null) {
				restSecret = new RestSecret();
				restSecret.setCode(code);
				restSecret.setDecryptKey(Cryptos.generateAesKeyString());
			}
			restSecret.setEnable(enable);
			restSecret.setRemark(remark);
			restSecret.setType(NumberUtils.toInt((String) params.get("type"), 0));
			
			restSecretService.saveOrUpdate(restSecret);
			RestSecretCache.clearCache();
			
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
	 * 重置密钥
	 * 
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("sys:restsecretmgr:reset")
	@OperationLog(content = "" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public AjaxJson reset(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--重置密钥--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			RestSecret restSecret = restSecretService.get(id);
			if (restSecret != null) {
				restSecret.setDecryptKey(Cryptos.generateAesKeyString());
				restSecretService.saveOrUpdate(restSecret);
				RestSecretCache.clearCache();
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
	
	@RequiresPermissions("sys:restsecretmgr:del")
	@OperationLog(content = "" , type = OperationType.DELETE)
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--级联删除密钥--");
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
				restSecretService.deleteByIds(ids);
				RestSecretCache.clearCache();
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
	
	@RequiresPermissions("sys:restsecretmgr:auth")
	@OperationLog(content = "" , type = OperationType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "/updateAuth", method = RequestMethod.POST)
	public AjaxJson updateAuth(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--更新角色资源权限--");
		AjaxJson json = new AjaxJson();
		try {
			//选择的密钥资源列表
			String checkResourceNodesJson = (String) params.get("checkResourceNodes");
			List<Object> checkResourceNodes = new JSONParser(checkResourceNodesJson).parseArray();
			//密钥列表
			String selectNodesJson = (String) params.get("selectNode");
			Object selectNode = new JSONParser(selectNodesJson).parse();
			restSecretService.updateAuth(selectNode,checkResourceNodes);
			RestSecretCache.clearCache();
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
	 * 得到REST密钥资源列表树图
	 * 
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("sys:restsecretmgr:auth")
	@ResponseBody
	@RequestMapping(value = "/authRestResourceTreeList", method = RequestMethod.POST)
	public List<RestResource> authRestResourceTreeList(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--得到REST密钥资源列表树图--");
		List<RestResource> allResources = Lists.newArrayList();
		try {
			String id = StringUtils.defaultString((String )params.get("id"), "null");
			allResources = restResourceService.findAll();
			RestSecret restSecret = restSecretService.get(id);
			if (restSecret != null) {
				transformAuthResourceCheckList(allResources, restSecret.getResources());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allResources;
	}
	
	/**
	 * 标志勾选REST密钥资源
	 * @param allMenus
	 * @param roleMenus
	 */
	private static void transformAuthResourceCheckList(List<RestResource> allResources,List<RestResource> secretResources){
		if (CollectionUtils.isNotEmpty(allResources) && CollectionUtils.isNotEmpty(secretResources)) {
			for (RestResource restResource : allResources) {
				restResource.setChecked(false);
				for (RestResource secretResource : secretResources) {
					if (restResource.getId().equals(secretResource.getId())) {
						restResource.setChecked(true);
					}
				}
			}
		}
	}
}
