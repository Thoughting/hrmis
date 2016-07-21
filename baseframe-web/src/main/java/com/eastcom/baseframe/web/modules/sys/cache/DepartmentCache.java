package com.eastcom.baseframe.web.modules.sys.cache;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.eastcom.baseframe.common.context.SpringContextHolder;
import com.eastcom.baseframe.common.utils.CacheUtils;
import com.eastcom.baseframe.web.modules.sys.entity.Department;
import com.eastcom.baseframe.web.modules.sys.service.DepartmentService;

/**
 * 部门Cache
 * @author wutingguang <br>
 */
public class DepartmentCache {

	public static final String CACHE_NAME = "DEPARTMENT_CACHE";
	public static final String CACHE_DICT_MAP = "departmentMap";
	public static final String CACHE_DICT_TREE_LIST = "departmentTreeList";
	
	private static final DepartmentService departmentService = SpringContextHolder.getBean(DepartmentService.class);

	/**
	 * 得到部门List(ALL Tree)
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static List<Department> getTreeList(){
		List<Department> result = (List<Department>) CacheUtils.get(CACHE_NAME,CACHE_DICT_TREE_LIST);
		if (result == null){
			result = departmentService.findCascadeTree("null");
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
