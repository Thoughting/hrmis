package com.eastcom.baseframe.web.modules.rest.cache;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.eastcom.baseframe.common.context.SpringContextHolder;
import com.eastcom.baseframe.common.utils.CacheUtils;
import com.eastcom.baseframe.web.modules.rest.entity.RestResource;
import com.eastcom.baseframe.web.modules.rest.entity.RestSecret;
import com.eastcom.baseframe.web.modules.rest.service.RestSecretService;
import com.google.common.collect.Maps;

/**
 * rest secret 密钥 cache
 * @author wutingguang <br>
 */
public class RestSecretCache {

	public static final String CACHE_NAME = "REST_SECRET_CACHE";
	public static final String CACHE_DICT_MAP = "restSecretMap";
	public static final String CACHE_DICT_LIST = "restSecretList";
	
	private static final RestSecretService restSecretService = SpringContextHolder.getBean(RestSecretService.class);

	/**
	 * List(ALL Tree)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<RestSecret> getList(){
		List<RestSecret> result = (List<RestSecret>) CacheUtils.get(CACHE_NAME,CACHE_DICT_LIST);
		if (result == null){
			result = restSecretService.findAll();
			if (CollectionUtils.isNotEmpty(result)) {
				CacheUtils.put(CACHE_NAME,CACHE_DICT_LIST,result);
			}
		}
		return result;
	}
	
	/**
	 * Map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, RestSecret> getMap(){
		Map<String, RestSecret> map = (Map<String, RestSecret>) CacheUtils.get(CACHE_NAME,CACHE_DICT_MAP);
		if (map == null) {
			List<RestSecret> list = getList();
			if (CollectionUtils.isNotEmpty(list)) {
				map = Maps.newHashMap();
				for (RestSecret restSecret : list) {
					map.put(restSecret.getCode(), restSecret);
					map.put(restSecret.getId(), restSecret);
				}
			}
		}
		return map;
	}
	
	/**
	 * 根据code获得 rest secret
	 * @param code
	 * @return
	 */
	public static RestSecret getRestSecretByCode(String code){
		Map<String, RestSecret> map = getMap();
		if (map != null) {
			return map.get(code);
		}
		return null;
	}
	
	/**
	 * 根据id获得 rest secret
	 * @param code
	 * @return
	 */
	public static RestSecret getRestSecretById(String id){
		Map<String, RestSecret> map = getMap();
		if (map != null) {
			return map.get(id);
		}
		return null;
	}
	
	/**
	 * 判断指定的code是否有使用该接口url的权限
	 * 
	 * @param url
	 * @return
	 */
	public static boolean hasPermission(String code,String url){
		RestSecret restSecret = getRestSecretByCode(code);
		if (restSecret != null && CollectionUtils.isNotEmpty(restSecret.getResources())) {
			for (RestResource resource : restSecret.getResources()) {
				if (url.equals(resource.getUrl())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 清除当前权限缓存
	 */
	public static void clearCache(){
		CacheUtils.remove(CACHE_NAME, CACHE_DICT_MAP);
		CacheUtils.remove(CACHE_NAME, CACHE_DICT_LIST);
	}
	
}
