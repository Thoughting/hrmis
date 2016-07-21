package com.eastcom.hrmis.modules.emp.cache;

import com.eastcom.baseframe.common.context.SpringContextHolder;
import com.eastcom.baseframe.common.utils.CacheUtils;
import com.eastcom.hrmis.modules.emp.entity.WageCountItem;
import com.eastcom.hrmis.modules.emp.service.WageCountItemService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 工资核算项Cache
 * @author wutingguang <br>
 */
public class WageCountItemCache {

	public static final String CACHE_NAME = "WAGE_COUNT_ITEM_CACHE";
	public static final String CACHE_DICT_MAP = "wageCountItemMap";
	public static final String CACHE_DICT_LIST = "wageCountItemList";
	public static final String CACHE_DICT_TREE_LIST = "wageCountItemTreeList";
	
	private static final WageCountItemService wageCountItemService = SpringContextHolder.getBean(WageCountItemService.class);

	/**
	 * list
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static List<WageCountItem> getList(){
		List<WageCountItem> result = (List<WageCountItem>) CacheUtils.get(CACHE_NAME,CACHE_DICT_LIST);
		if (result == null){
			result = wageCountItemService.findAll();
			if (CollectionUtils.isNotEmpty(result)) {
				CacheUtils.put(CACHE_NAME,CACHE_DICT_LIST,result);
			}
		}
		return result;
	}
	
	/**
	 * tree list
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static List<WageCountItem> getTreeList(){
		List<WageCountItem> result = (List<WageCountItem>) CacheUtils.get(CACHE_NAME,CACHE_DICT_TREE_LIST);
		if (result == null){
			result = wageCountItemService.findCascadeTree("null");
			if (CollectionUtils.isNotEmpty(result)) {
				CacheUtils.put(CACHE_NAME,CACHE_DICT_TREE_LIST,result);
			}
		}
		if (result != null) {
			for (WageCountItem wageCountItem : result) {
				wageCountItem.setChecked(false);
				if (CollectionUtils.isNotEmpty(wageCountItem.getChildren())) {
					for (WageCountItem wageCountItemChild : wageCountItem.getChildren()) {
						wageCountItemChild.setChecked(false);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * map
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, WageCountItem> getMap(){
		Map<String, WageCountItem> result = (Map<String, WageCountItem>) CacheUtils.get(CACHE_NAME,CACHE_DICT_MAP);
		if (result == null){
			List<WageCountItem> list = getList();
			if (CollectionUtils.isNotEmpty(list)) {
				result = Maps.newHashMap();
				for (WageCountItem wageCountItem : list) {
					result.put(wageCountItem.getCode(), wageCountItem);
					result.put(wageCountItem.getId(), wageCountItem);
				}
				CacheUtils.put(CACHE_NAME,CACHE_DICT_MAP,result);
			}
		}
		return result;
	}
	
	/**
	 * 根据核算项编码得到核算项
	 * @param code
	 * @return
	 */
	public static WageCountItem getByCode(String code){
		Map<String, WageCountItem> map = getMap();
		if (map != null) {
			return map.get(code);
		}
		return null;
	}
	
	/**
	 * 根据核算项ID得到核算项
	 * @param code
	 * @return
	 */
	public static WageCountItem getById(String id){
		Map<String, WageCountItem> map = getMap();
		if (map != null) {
			return map.get(id);
		}
		return null;
	}
	
	/**
	 * 清除当前权限缓存
	 */
	public static void clearCache(){
		CacheUtils.remove(CACHE_NAME, CACHE_DICT_MAP);
		CacheUtils.remove(CACHE_NAME, CACHE_DICT_LIST);
		CacheUtils.remove(CACHE_NAME, CACHE_DICT_TREE_LIST);
	}
	
}
