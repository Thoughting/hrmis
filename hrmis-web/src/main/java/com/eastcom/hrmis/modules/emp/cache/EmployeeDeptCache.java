package com.eastcom.hrmis.modules.emp.cache;

import com.eastcom.baseframe.common.context.SpringContextHolder;
import com.eastcom.baseframe.common.utils.CacheUtils;
import com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDept;
import com.eastcom.hrmis.modules.emp.service.EmployeeDeptService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工部门Cache
 * @author wutingguang <br>
 */
public class EmployeeDeptCache {

	public static final String CACHE_NAME = "EMPLOYEE_DEPT_CACHE";
	public static final String CACHE_MAP = "employeeDeptMap";
	public static final String CACHE_LIST = "employeeDeptList";
	
	public static final String USER_CACHE_AUTH_EMPLOYEE_DEPT_LIST = "authEmployeeDeptList";
	
	private static final EmployeeDeptService employeeDeptService = SpringContextHolder.getBean(EmployeeDeptService.class);

	/**
	 * 得到list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<EmployeeDept> getList(){
		List<EmployeeDept> result = (List<EmployeeDept>) CacheUtils.get(CACHE_NAME,CACHE_LIST);
		if (result == null){
			result = employeeDeptService.find(new HashMap<String, Object>());
			if (CollectionUtils.isNotEmpty(result)) {
				CacheUtils.put(CACHE_NAME,CACHE_LIST,result);
			}
		}
		return result;
	}
	
	/**
	 * 得到Map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, EmployeeDept> getMap(){
		Map<String, EmployeeDept> result = (Map<String, EmployeeDept>) CacheUtils.get(CACHE_NAME,CACHE_MAP);
		if (result == null) {
			List<EmployeeDept> list = getList();
			if (CollectionUtils.isNotEmpty(list)) {
				result = Maps.newHashMap();
				for (EmployeeDept temp : list) {
					result.put(temp.getId(), temp);
				}
			}
		}
		return result;
	}
	
	/**
	 * 得到当前用户授权的部门列表信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<EmployeeDept> getAuthDeptByLoginUser(){
		List<EmployeeDept> result = (List<EmployeeDept>) SecurityCache.getCache(USER_CACHE_AUTH_EMPLOYEE_DEPT_LIST);
		if (result == null) {
			result = employeeDeptService.getAuthDeptByUserId(SecurityCache.getLoginUser().getId());
			if (CollectionUtils.isNotEmpty(result)) {
				SecurityCache.putCache(USER_CACHE_AUTH_EMPLOYEE_DEPT_LIST, result);
			}
		}
		return result;
	}
	
	/**
	 * 得到当前用户授权的部门ids
	 * 
	 * @return
	 */
	public static List<String> getAuthDeptIdsByLoginUser(){
		List<String> ids = Lists.newArrayList();
		List<EmployeeDept> result = getAuthDeptByLoginUser();
		if (CollectionUtils.isNotEmpty(result)) {
			for (EmployeeDept employeeDept : result) {
				ids.add(employeeDept.getId());
			}
		}
		return ids;
	}
	
	public static void clearAuthDept(){
		SecurityCache.removeCache(USER_CACHE_AUTH_EMPLOYEE_DEPT_LIST);
	}
	
	/**
	 * 清除当前权限缓存
	 */
	public static void clearCache(){
		CacheUtils.remove(CACHE_NAME, CACHE_MAP);
		CacheUtils.remove(CACHE_NAME, CACHE_LIST);
	}
	
}
