package com.eastcom.hrmis.modules.emp.web.controller.api;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.persistence.PageHelper;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.common.model.easyui.DataGridJson;
import com.eastcom.baseframe.web.common.utils.DownloadUtil;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationType;
import com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache;
import com.eastcom.hrmis.modules.emp.entity.Contract;
import com.eastcom.hrmis.modules.emp.entity.ContractAnnex;
import com.eastcom.hrmis.modules.emp.service.ContractAnnexService;
import com.eastcom.hrmis.modules.emp.service.ContractService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * 合同Controller
 * @author wutingguang <br>
 */
@Controller
@RequestMapping(value="/api/emp/contract")
public class ContractController extends BaseController {

	@Autowired
	private ContractService contractService;
	
	@Autowired
	private ContractAnnexService contractAnnexService;
	
	/**
	 * 
	 * 查询
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:contractmgr:view")
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public DataGridJson list(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--获取合同列表--");
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
			reqParams.put("keyDesr", (String) params.get("keyDesr"));
			PageHelper<Contract> pageHelper = contractService.find(reqParams, pageNo, pageSize);
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
	@RequiresPermissions(value = {"emp:contractmgr:add","emp:contractmgr:edit"},logical=Logical.OR)
	@OperationLog(content = "新增修改合同信息" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AjaxJson addOrUpdate(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--新增或者修改合同--");
		AjaxJson json = new AjaxJson();
		try {
			String id = StringUtils.defaultIfBlank((String) params.get("id"), "0");
			int type = Integer.parseInt((String) params.get("contractType"));
			String code = (String) params.get("code");
			String name = (String) params.get("name");
			String keyDesr = (String) params.get("keyDesr");
			
			//判断code是否唯一
			Map<String, Object> reqParam = Maps.newHashMap();
			reqParam.put("code", code);
			if (CollectionUtils.isNotEmpty(contractService.find(reqParam))) {
				json.setSuccess(false);
				json.setMessage("操作失败,该编码已存在!");
				return json;
			}
			
			Contract contract = contractService.get(id);
			if (contract == null) {
				contract = new Contract();
				contract.setCreateDate(new Date());
				contract.setUpdateDate(new Date());
				contract.setModifyUser(SecurityCache.getLoginUser().getName());
			}
			contract.setType(type);
			contract.setCode(code);
			contract.setName(name);
			contract.setKeyDesr(keyDesr);
			contract.setUpdateDate(new Date());
			contract.setModifyUser(SecurityCache.getLoginUser().getName());
			
			contractService.saveOrUpdate(contract);
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
	@RequiresPermissions("emp:contractmgr:del")
	@OperationLog(content = "删除合同信息" , type = OperationType.DELETE)
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public AjaxJson delete(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--级联角色--");
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
				contractService.deleteByIds(ids);
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
	 * 上传合同附件
	 * @param request
	 * @param response
	 * @param plupload
	 * @return
	 */
	@RequiresPermissions("emp:contractmgr:annexUpload")
	@OperationLog(content = "上传合同附件" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/annexUpload")
	public AjaxJson annexUpload(MultipartHttpServletRequest request, HttpServletResponse response) {
		logger.info("---上传合同附件---");
		AjaxJson json = new AjaxJson();
		json.setMessage("上传的文件没有有效数据!");
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multiRequest.getFile("file");
		try {
			String contractId = request.getParameter("contractId");
			Contract contract = contractService.get(contractId);
			if (contract != null) {
				ContractAnnex contractAnnex = new ContractAnnex();
				contractAnnex.setAnnexContent(file.getBytes());
				contractAnnex.setAnnexName(file.getOriginalFilename());
				contractAnnex.setContract(contract);
				contractAnnexService.save(contractAnnex);
			}
			json.setMessage("上传成功");
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setMessage("数据处理失败!");
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * 下载合同附件
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:contractmgr:annexUpload")
	@OperationLog(content = "下载合同附件" , type = OperationType.CREATE)
	@ResponseBody
	@RequestMapping(value = "/annexDownload/{annexId}")
	public void annexDownload(HttpServletRequest request,HttpServletResponse response,@PathVariable String annexId) {
		logger.info("--下载合同附件--");
		try {
			ContractAnnex contractAnnex = contractAnnexService.get(annexId);
			if (contractAnnex != null && contractAnnex.getAnnexContent() != null) {
				InputStream in = new ByteArrayInputStream(contractAnnex.getAnnexContent());
				DownloadUtil.downloadFile(in, contractAnnex.getAnnexName(), response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除合同附件
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequiresPermissions("emp:contractmgr:annexDel")
	@OperationLog(content = "删除合同附件" , type = OperationType.DELETE)
	@ResponseBody
	@RequestMapping(value = "/annexDelete", method = RequestMethod.POST)
	public AjaxJson annexDelete(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("--删除合同附件--");
		AjaxJson json = new AjaxJson();
		try {
			String annexId = (String) params.get("annexId");
			ContractAnnex contractAnnex = contractAnnexService.get(annexId);
			if (contractAnnex != null) {
				contractAnnexService.delete(contractAnnex);
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
}
