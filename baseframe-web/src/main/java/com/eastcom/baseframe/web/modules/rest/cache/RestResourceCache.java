package com.eastcom.baseframe.web.modules.rest.cache;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.eastcom.baseframe.common.context.SpringContextHolder;
import com.eastcom.baseframe.common.utils.CacheUtils;
import com.eastcom.baseframe.web.modules.rest.entity.RestResource;
import com.eastcom.baseframe.web.modules.rest.service.RestResourceService;

/**
 * rest 密钥资源 缓存
 * 
 * @author wutingguang <br>
 */
public class RestResourceCache {

	public static final String CACHE_NAME = "REST_RESOURCE_CACHE";
	public static final String CACHE_DICT_LIST = "restResourceList";
	
	private static final RestResourceService restResourceService = SpringContextHolder.getBean(RestResourceService.class);

	/**
	 * List(ALL Tree)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<RestResource> getList(){
		List<RestResource> result = (List<RestResource>) CacheUtils.get(CACHE_NAME,CACHE_DICT_LIST);
		if (result == null){
			result = restResourceService.findAll();
			if (CollectionUtils.isNotEmpty(result)) {
				CacheUtils.put(CACHE_NAME,CACHE_DICT_LIST,result);
			}
		}
		return result;
	}
	
	/**
	 * 清除当前权限缓存
	 */
	public static void clearCache(){
		CacheUtils.remove(CACHE_NAME, CACHE_DICT_LIST);
	}
}
