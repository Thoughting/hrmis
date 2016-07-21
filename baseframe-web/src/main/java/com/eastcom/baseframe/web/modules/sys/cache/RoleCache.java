package com.eastcom.baseframe.web.modules.sys.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.eastcom.baseframe.common.context.SpringContextHolder;
import com.eastcom.baseframe.common.utils.CacheUtils;
import com.eastcom.baseframe.web.modules.sys.entity.Role;
import com.eastcom.baseframe.web.modules.sys.service.RoleService;
import com.google.common.collect.Maps;

/**
 * 角色Cache
 * 
 * @author wutingguang <br>
 */
public class RoleCache {

	public static final String CACHE_NAME = "ROLE_CACHE";
	public static final String CACHE_DICT_MAP = "roleMap";
	public static final String CACHE_DICT_LIST = "roleList";
	
	private static final RoleService roleService = SpringContextHolder.getBean(RoleService.class);

	/**
	 * List
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Role> getList(){
		List<Role> result = (List<Role>) CacheUtils.get(CACHE_NAME,CACHE_DICT_LIST);
		if (result == null){
			result = roleService.findWithCascade(new HashMap<String, Object>());
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
	public static Map<String,Role> getMap(){
		Map<String, Role> map = (Map<String, Role>) CacheUtils.get(CACHE_NAME,CACHE_DICT_MAP);
		if (map == null) {
			List<Role> list = getList();
			if (CollectionUtils.isNotEmpty(list)) {
				map = Maps.newHashMap();
				for (Role role : list) {
					map.put(role.getName(), role);
					map.put(role.getId(), role);
				}
			}
		}
		return map;
	}
	
	/**
	 * Role
	 * @param name
	 * @return
	 */
	public static Role getRoleByName(String name){
		Map<String, Role> map = getMap();
		if (map != null) {
			return map.get(name);
		}
		return null;
	}
	
	/**
	 * Role
	 * @param id
	 * @return
	 */
	public static Role getRoleById(String id){
		Map<String, Role> map = getMap();
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
	}
}
