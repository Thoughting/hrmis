package com.eastcom.baseframe.web.modules.sys.cache;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.eastcom.baseframe.common.context.SpringContextHolder;
import com.eastcom.baseframe.common.utils.CacheUtils;
import com.eastcom.baseframe.web.modules.sys.entity.Dynamicgrid;
import com.eastcom.baseframe.web.modules.sys.service.DynamicgridService;
import com.google.common.collect.Maps;

/**
 * 表格Cache
 * @author wutingguang <br>
 */
public class DynamicGridCache {
	
	public static final String CACHE_NAME = "DYNAMICGRID_CACHE";
	public static final String CACHE_DICT_MAP = "dynamicgridMap";
	public static final String CACHE_DICT_LIST = "dynamicgridTreeList";
	
	private static final DynamicgridService dynamicGridService = SpringContextHolder.getBean(DynamicgridService.class);
	
	/**
	 * 得到表格List
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static List<Dynamicgrid> getList(){
		List<Dynamicgrid> result = (List<Dynamicgrid>) CacheUtils.get(CACHE_NAME,CACHE_DICT_LIST);
		if (result == null){
			result = dynamicGridService.findAll();
			if (CollectionUtils.isNotEmpty(result)) {
				CacheUtils.put(CACHE_NAME,CACHE_DICT_LIST,result);
			}
		}
		return result;
	}
	
	/**
	 * 得到表格Map
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Dynamicgrid> getMap(){
		Map<String, Dynamicgrid> result = (Map<String, Dynamicgrid>) CacheUtils.get(CACHE_NAME,CACHE_DICT_MAP);
		if (result == null){
			List<Dynamicgrid> dynamicgrids = getList();
			if (CollectionUtils.isNotEmpty(dynamicgrids)) {
				result = Maps.newHashMap();
				for (Dynamicgrid dynamicgrid : dynamicgrids) {
					result.put(dynamicgrid.getName(), dynamicgrid);
				}
				CacheUtils.put(CACHE_NAME,CACHE_DICT_MAP,result);
			}
		}
		return result;
	}
	
	/**
	 * 根据表格name得到表格
	 * @param name
	 * @return
	 */
	public static Dynamicgrid getDynamicgridByName(String name){
		Map<String, Dynamicgrid> result = getMap();
		if (result != null) {
			return result.get(name);
		}
		return null;
	}
	
	/**
	 * 清除当前权限缓存
	 */
	public static void clearCache(){
		CacheUtils.remove(CACHE_NAME, CACHE_DICT_MAP);
		CacheUtils.remove(CACHE_NAME, CACHE_DICT_LIST);
	}
	
}
