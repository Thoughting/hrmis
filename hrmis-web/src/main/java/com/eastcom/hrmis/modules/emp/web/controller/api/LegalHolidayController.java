package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.hrmis.modules.emp.cache.LegalHolidayCache;
import com.eastcom.hrmis.modules.emp.entity.LegalHoliday;
import com.eastcom.hrmis.modules.emp.service.LegalHolidayService;
import com.google.common.collect.Maps;
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
 * 法定节假日Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/legalholiday")
public class LegalHolidayController extends BaseController {

	@Autowired
	private LegalHolidayService legalHolidayService;
	
	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:legalholidaymgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取法定节假日列表--");
		DataGridJson gridJson = new DataGridJson();
		try {
			Map<String, Object> reqParams = new HashMap<String, Object>();
			reqParams.put("year", (String) params.get("year"));
			List<LegalHoliday> legalHolidays = legalHolidayService.find(reqParams);
			if (CollectionUtils.isNotEmpty(legalHolidays)) {
				gridJson.setRows(legalHolidays);
				gridJson.setTotal((long)legalHolidays.size());
			}
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
	@RequiresPermissions(value = {"emp:legalholidaymgr:add","emp:legalholidaymgr:edit"},logical=Logical.OR)
	@OperationLog(content = "新增或者修改法定节假日" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改法定节假日--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			String holiday = (String) params.get("holiday");
			String remark = (String) params.get("remark");
			
			//判断holiday是否唯一
			Map<String, Object> reqParam = Maps.newHashMap();
			reqParam.put("holiday", holiday);
			if (CollectionUtils.isNotEmpty(legalHolidayService.find(reqParam))) {
				json.setSuccess(false);
				json.setMessage("操作失败,该法定节假日已存在!");
				return json;
			}
			
			LegalHoliday legalHoliday = legalHolidayService.get(id);
			if (legalHoliday == null) {
				legalHoliday = new LegalHoliday();
			}
			legalHoliday.setHoliday(DateUtils.parseDate(holiday));
			legalHoliday.setYear(holiday.split("-")[0] + "年");
			legalHoliday.setRemark(remark);
			
			legalHolidayService.saveOrUpdate(legalHoliday);
			LegalHolidayCache.clearCache();
			
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
	@RequiresPermissions("emp:legalholidaymgr:del")
	@OperationLog(content = "删除法定节假日信息" , type = OperationType.DELETE)
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--删除法定节假日信息--");
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
				legalHolidayService.deleteByIds(ids);
				LegalHolidayCache.clearCache();
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
