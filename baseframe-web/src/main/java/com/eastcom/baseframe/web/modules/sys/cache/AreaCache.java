package com.eastcom.baseframe.web.modules.sys.cache;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.eastcom.baseframe.common.context.SpringContextHolder;
import com.eastcom.baseframe.common.utils.CacheUtils;
import com.eastcom.baseframe.web.modules.sys.entity.Area;
import com.eastcom.baseframe.web.modules.sys.service.AreaService;

/**
 * 区域Cache
 * @author wutingguang <br>
 */
public class AreaCache {

	public static final String CACHE_NAME = "AREA_CACHE";
	public static final String CACHE_DICT_MAP = "areaMap";
	public static final String CACHE_DICT_TREE_LIST = "areaTreeList";
	
	private static final AreaService areaService = SpringContextHolder.getBean(AreaService.class);

	/**
	 * 得到部门List(ALL Tree)
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static List<Area> getTreeList(){
		List<Area> result = (List<Area>) CacheUtils.get(CACHE_NAME,CACHE_DICT_TREE_LIST);
		if (result == null){
			result = areaService.findCascadeTree("null");
			if (CollectionUtils.isNotEmpty(result)) {
				CacheUtils.put(CACHE_NAME,CACHE_DICT_TREE_LIST,result);
			}
		}
		return result;
	}
	
	/**
	 * 清除当前权限缓存
	 */
	public static void clearCache(){
		CacheUtils.remove(CACHE_NAME, CACHE_DICT_MAP);
		CacheUtils.remove(CACHE_NAME, CACHE_DICT_TREE_LIST);
	}
	
}
