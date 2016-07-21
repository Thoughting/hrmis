package com.eastcom.baseframe.web.modules.rest.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.modules.rest.cache.RestSecretCache;
import com.eastcom.baseframe.web.modules.rest.entity.AccessToken;

/**
 * REST API 控制器基类
 * 
 * @author wutingguang <br>
 */
public abstract class RESTBaseController extends BaseController {

	/**
	 * 鉴权
	 * @param accessToken
	 * @return
	 */
	protected AjaxJson checkAccessToken(HttpServletRequest request,String accessToken){
		AjaxJson json = new AjaxJson();
		try {
			//首先判断token是否过期
			if (AccessToken.isAlive(accessToken)) {
				//其次判断appid是否有接口权限
				String appId = AccessToken.getAppId(accessToken);
				String url = request.getRequestURI().replace(request.getContextPath(), "") + "?";
				if(RestSecretCache.hasPermission(appId, url)){
					json.setSuccess(true);
				} else {
					json.setSuccess(false);
					json.setMessage("提示：没有API访问权限，请联系管理员配置");
				}
			} else {
				json.setSuccess(false);
				json.setMessage("提示：AccessToken未认证或者已过期，请重新获取");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage(e.getMessage());
		}
		return json;
	}
	
}
