package com.eastcom.baseframe.web.modules.sys.cache;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.eastcom.baseframe.common.context.SpringContextHolder;
import com.eastcom.baseframe.common.utils.CacheUtils;
import com.eastcom.baseframe.web.modules.sys.entity.Dict;
import com.eastcom.baseframe.web.modules.sys.service.DictService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 字典Cache
 * @author wutingguang <br>
 */
public class DictCache {

	public static final String CACHE_NAME = "SYS_DICT_CACHE";
	public static final String CACHE_DICT_MAP = "sysDictMap";
	public static final String CACHE_DICT_TREE_LIST = "sysDictTreeList";
	
	private static final DictService dictService = SpringContextHolder.getBean(DictService.class);
	
	/**
	 * 得到字典List(ALL Tree)
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static List<Dict> getTreeList(){
		List<Dict> dicts = (List<Dict>) CacheUtils.get(CACHE_NAME,CACHE_DICT_TREE_LIST);
		if (dicts == null){
			dicts = dictService.findCascadeTree("null");
			if (CollectionUtils.isNotEmpty(dicts)) {
				CacheUtils.put(CACHE_NAME,CACHE_DICT_TREE_LIST,dicts);
			}
		}
		return dicts;
	}
	
	/**
	 * 得到字典Map(ALL)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized static Map<String, List<Dict>> getDictMap(){
		Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>)CacheUtils.get(CACHE_NAME,CACHE_DICT_MAP);
		if (dictMap == null) {
			dictMap = Maps.newHashMap();
			Map<String, Object> reqParam = Maps.newHashMap();
			reqParam.put("parentId", "not_null");
			List<Dict> dicts = dictService.find(reqParam);
			if (CollectionUtils.isNotEmpty(dicts)) {
				for (Dict dict : dicts){
					List<Dict> dictList = dictMap.get(dict.getParent().getId());
					if (dictList != null){
						dictList.add(dict);
					}else{
						dictMap.put(dict.getParent().getId(), Lists.newArrayList(dict));
					}
				}
				CacheUtils.put(CACHE_NAME,CACHE_DICT_MAP, dictMap);
			}
		}
		return dictMap;
	}
	
	/**
	 * 根据字典类型返回字典清单
	 * @param type
	 * @return
	 */
	public static List<Dict> getDictList(String type){
		List<Dict> dictList = getDictMap().get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}
	
	/**
	 * 根据类型与编码得到字典值
	 * @param type
	 * @param code
	 * @return
	 */
	public static String getDictValue(String type,Object code){
		List<Dict> dictList = getDictList(type);
		if (code != null && CollectionUtils.isNotEmpty(dictList)) {
			for (Dict dict : dictList) {
				if (dict.getCode().equals(code.toString())) {
					return dict.getName();
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据类型与编码得到字典值
	 * @param type
	 * @param code
	 * @return
	 */
	public static String getDictValue(String type,int code){
		List<Dict> dictList = getDictList(type + "");
		if (CollectionUtils.isNotEmpty(dictList)) {
			for (Dict dict : dictList) {
				if ((code + "").toString().equals(dict.getCode())) {
					return dict.getName();
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据0，1返回是或否
	 * @param code
	 * @return
	 */
	public static String getYesOrNo(int code){
		switch (code) {
			case 0:
				return "否";
			case 1:
				return "是";
		}
		return "";
	}
	
	/**
	 * 根据0，1返回有或无
	 * @param code
	 * @return
	 */
	public static String getHaveOrNotHave(int code){
		switch (code) {
			case 0:
				return "无";
			case 1:
				return "有";
		}
		return "";
	}
	
	/**
	 * 根据0，1返回女或男
	 * @param code
	 * @return
	 */
	public static String getBoyOrGrid(int code){
		switch (code) {
			case 0:
				return "女";
			case 1:
				return "男";
		}
		return "";
	}
	
	
	/**
	 * 清除当前权限缓存
	 */
	public static void clearCache(){
		CacheUtils.remove(CACHE_NAME, CACHE_DICT_MAP);
		CacheUtils.remove(CACHE_NAME, CACHE_DICT_TREE_LIST);
	}
	
}
