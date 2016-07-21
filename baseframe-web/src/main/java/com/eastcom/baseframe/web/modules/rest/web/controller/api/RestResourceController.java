package com.eastcom.baseframe.web.modules.rest.web.controller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.modules.rest.cache.RestResourceCache;
import com.eastcom.baseframe.web.modules.rest.cache.RestSecretCache;
import com.eastcom.baseframe.web.modules.rest.entity.RestResource;
import com.eastcom.baseframe.web.modules.rest.service.RestResourceService;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;

/**
 * 密钥资源
 * rest resource controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/sys/rest/resource")
public class RestResourceController extends BaseController {

	@Autowired
	private RestResourceService restResourceService;
	
	@RequiresPermissions("sys:restresourcemgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--获取密钥资源列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			List<RestResource> result = restResourceService.findAll();
			if (CollectionUtils.isNotEmpty(result)) {
				gridJson.setRows(result);
				gridJson.setTotal((long) result.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gridJson;
	}
	
	@RequiresPermissions(value = {"sys:restresourcemgr:add","sys:restresourcemgr:edit"},logical=Logical.OR)
	@OperationLog(content = "" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改密钥资源--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			String name = (String) params.get("name");
			String url = (String) params.get("url");
			String remark = (String) params.get("remark");
			RestResource restResource = restResourceService.get(id);
			if (restResource == null) {
				restResource = new RestResource();
			}
			restResource.setName(name);
			restResource.setUrl(url);
			restResource.setRemark(remark);

			restResourceService.saveOrUpdate(restResource);
			RestResourceCache.clearCache();
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
	
	@RequiresPermissions("sys:restresourcemgr:del")
	@OperationLog(content = "" , type = OperationType.DELETE)
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		logger.info("--级联删除密钥资源--");
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
				restResourceService.deleteByIds(ids);
				RestResourceCache.clearCache();
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
