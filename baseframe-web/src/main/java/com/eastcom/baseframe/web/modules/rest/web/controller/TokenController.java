package com.eastcom.baseframe.web.modules.rest.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eastcom.baseframe.common.utils.Cryptos;
import com.eastcom.baseframe.common.utils.IdGen;
import com.eastcom.baseframe.web.common.model.AjaxJson;
import com.eastcom.baseframe.web.modules.rest.cache.RestSecretCache;
import com.eastcom.baseframe.web.modules.rest.entity.AccessToken;
import com.eastcom.baseframe.web.modules.rest.entity.RestSecret;

/**
 * restful token controller
 * 
 * @author wutingguang <br>
 */
@RestController
@RequestMapping(value = "/rest")
public class TokenController extends RESTBaseController {

	/**
	 * 得到access_token信息
	 * 
	 * http://localhost:8080/cartutor-web/rest/token?appId=spsadmin&secret=c9f3e4a5dfa0503567dba4e4bb9daa78
	 * 
	 * @param appId 第三方用户唯一凭证，访问rest
	 * @param secret
	 * @return
	 */
	@RequestMapping(value = "/token", method = RequestMethod.GET)
	public Object token(@RequestParam String appId,@RequestParam String secret) {
		logger.info("------得到Token------");
		AjaxJson json = new AjaxJson(false,"认证失败！",null);
		RestSecret restSecret = RestSecretCache.getRestSecretByCode(appId);
		if (restSecret != null) {
			try {
				String decrypt = Cryptos.aesDecrypt(secret, restSecret.getDecryptKey());
				if (decrypt.equals(appId)) {
					String accessToken = IdGen.randomBase62(16);
					AccessToken.put(appId, accessToken);
					return "{\"accessToken\":\"" + accessToken + "\",\"expiresIn\":" + AccessToken.EXPIRES_IN + "}";
				}
			} catch (Exception e) {
				e.printStackTrace();
				json.setSuccess(false);
				json.setMessage(e.getMessage());
			}
		}
		return new AjaxJson();
	}
	
}
