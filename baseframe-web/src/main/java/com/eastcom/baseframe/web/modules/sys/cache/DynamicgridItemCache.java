package com.eastcom.baseframe.web.modules.sys.cache;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.eastcom.baseframe.common.context.SpringContextHolder;
import com.eastcom.baseframe.common.utils.CacheUtils;
import com.eastcom.baseframe.web.modules.sys.entity.Dynamicgrid;
import com.eastcom.baseframe.web.modules.sys.entity.DynamicgridItem;
import com.eastcom.baseframe.web.modules.sys.service.DynamicgridItemService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 表格项Cache
 * @author wutingguang <br>
 */
public class DynamicgridItemCache {

	public static final String CACHE_NAME = "DYNAMICGRIDITEM_CACHE";
	public static final String CACHE_DICT_MAP = "dynamicgridItemMap";
	
	private static final DynamicgridItemService dynamicgridItemService = SpringContextHolder.getBean(DynamicgridItemService.class);
	
	/**
	 * 得到表格Map
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, List<DynamicgridItem>> getMap(){
		Map<String, List<DynamicgridItem>> result = (Map<String, List<DynamicgridItem>>) CacheUtils.get(CACHE_NAME,CACHE_DICT_MAP);
		if (result == null){
			List<Dynamicgrid> dynamicgrids = DynamicGridCache.getList();
			if (CollectionUtils.isNotEmpty(dynamicgrids)) {
				result = Maps.newHashMap();
				for (Dynamicgrid dynamicgrid : dynamicgrids) {
					List<DynamicgridItem> dynamicgridItems = dynamicgridItemService.findCascadeTree(dynamicgrid.getId(), "null");
					result.put(dynamicgrid.getId(), dynamicgridItems);
					result.put(dynamicgrid.getName(), dynamicgridItems);
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
	public static List<DynamicgridItem> getDynamicgridItemsByName(String name){
		Map<String, List<DynamicgridItem>> result = getMap();
		if (result != null) {
			return result.get(name);
		}
		return null;
	}
	
	/**
	 * 根据表格id得到表格
	 * @param name
	 * @return
	 */
	public static List<DynamicgridItem> getDynamicgridItemsById(String id){
		Map<String, List<DynamicgridItem>> result = getMap();
		if (result != null) {
			return result.get(id);
		}
		return null;
	}
	
	/**
	 * 根据表格name得到表格 leaf
	 * @param name
	 * @return
	 */
	public static List<DynamicgridItem> getDynamicgridLeafItemsByName(String name){
		List<DynamicgridItem> allItems = Lists.newArrayList();
		List<DynamicgridItem> dynamicgridItems = getDynamicgridItemsByName(name);
		transformDynamicgridLeafItem(allItems, dynamicgridItems);
		return allItems;
	}
	
	/**
	 * 转换所有叶子节点
	 * @param allItems
	 * @param dynamicgridItems
	 */
	private static void transformDynamicgridLeafItem(List<DynamicgridItem> allItems,List<DynamicgridItem> dynamicgridItems){
		if (CollectionUtils.isNotEmpty(dynamicgridItems)) {
			allItems.addAll(dynamicgridItems);
			for (DynamicgridItem dynamicgridItem : dynamicgridItems) {
				if (CollectionUtils.isNotEmpty(dynamicgridItem.getChildren())) {
					transformDynamicgridLeafItem(allItems,dynamicgridItem.getChildren());
				}
			}
		}
	}
	
	/**
	 * 清除当前权限缓存
	 */
	public static void clearCache(){
		CacheUtils.remove(CACHE_NAME, CACHE_DICT_MAP);
	}
	
}
