package com.eastcom.baseframe.web.modules.sys.security.cache;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.eastcom.baseframe.common.context.SpringContextHolder;
import com.eastcom.baseframe.common.utils.CacheUtils;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.modules.sys.entity.Resource;
import com.eastcom.baseframe.web.modules.sys.entity.Role;
import com.eastcom.baseframe.web.modules.sys.entity.User;
import com.eastcom.baseframe.web.modules.sys.security.SystemAuthorizingRealm;
import com.eastcom.baseframe.web.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.eastcom.baseframe.web.modules.sys.service.ResourceService;
import com.eastcom.baseframe.web.modules.sys.service.RoleService;
import com.eastcom.baseframe.web.modules.sys.service.UserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 权限Cache
 * @author wutingguang <br>
 */
public class SecurityCache {
	
	public static final String CACHE_RESOURCE_ALL_LIST = "resourceAllList";
	public static final String CACHE_RESOURCE_NAME_PATH_MAP = "menuNamePathMap";
	
	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "userCacheId_";
	public static final String USER_CACHE_LOGIN_NAME_ = "userCacheLoginName_";
	public static final String USER_CACHE_ROLE_LIST = "roleList";
	public static final String USER_CACHE_CURRENT_ROLE = "currentRole";
	public static final String USER_CACHE_MENU_LIST = "menuList";
	public static final String USER_CACHE_MENU_TREEJSON_MAP = "menuTreeJsonMap";
	public static final String USER_CACHE_PERMISSION_LIST = "permissionList";
	public static final String USER_CACHE_AUTH_RESOURCE_ALL_LIST = "authResourceAllList";
	
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";
	
	private static final UserService userService = SpringContextHolder.getBean(UserService.class);
	private static final ResourceService resourceService = SpringContextHolder.getBean(ResourceService.class);
	private static final RoleService roleService = SpringContextHolder.getBean(RoleService.class);
	
	/**
	 * 得到当前登录用户
	 * @return
	 */
	public static User getLoginUser(){
		Principal principal = getPrincipal();
		if (principal != null) {
			User user = getUserById(principal.getId());
			if (user != null) {
				return user;
			}
		}
		return new User();
	}
	
	/**
	 * 根据id得到User信息
	 * @param id
	 * @return
	 */
	public static User getUserById(String id) {
		User user = (User) CacheUtils.get(USER_CACHE,USER_CACHE_ID_ + id);
		if (user == null) {
			user = userService.getWithCascadeById(id);
			if (user != null) {
				CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
				CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
			}
		}
		return user;
	}
	
	/**
	 * 根据登录名得到User信息
	 * @param loginName
	 * @return
	 */
	public static User getUserByLoginName(String loginName) {
		User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
		if (user == null) {
			user = userService.getUnique("loginName", loginName);
			if (user != null) {
				user = userService.getWithCascadeById(user.getId());
				CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
				CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
			}
		}
		return user;
	}
	
	/**
	 * 得到所有的菜单
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static List<Resource> getAllResourceList(){
		List<Resource> resourceList = (List<Resource>) CacheUtils.get(CACHE_RESOURCE_ALL_LIST);
		if (resourceList == null){
			resourceList = resourceService.findCascadeResourceTree("null");
			if (CollectionUtils.isNotEmpty(resourceList)) {
				CacheUtils.put(CACHE_RESOURCE_ALL_LIST,resourceList);
			}
		}
		return resourceList;
	}
	
	/**
	 * 得到所有的菜单
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static List<Resource> getAllAuthResourceList(){
		List<Resource> authResourceList = (List<Resource>) getCache(USER_CACHE_AUTH_RESOURCE_ALL_LIST);
		if (authResourceList == null){
			authResourceList = Lists.newArrayList();
			List<Resource> allResource = getAllResourceList();
			if (CollectionUtils.isNotEmpty(allResource)) {
				for (Resource resource : allResource) {
					if (hasPermission(resource.getPermission())) {
						authResourceList.add(resource);
					}
				}
			}
			if (CollectionUtils.isNotEmpty(authResourceList)) {
				putCache(USER_CACHE_AUTH_RESOURCE_ALL_LIST, authResourceList);
			}
		}
		return authResourceList;
	}
	
	/**
	 * 通过url查找对应的菜单id
	 * 匹配优先原则：1.完全相同 2.匹配前缀的长度最长
	 * @param href
	 * @return
	 */
	public static String getMenuId(String href){
		String menuId = "";
		
		if(StringUtils.isNotBlank(href)){
			List<Resource> menuList = getAllResourceList();
			Resource match = new Resource();
			if (menuList != null && menuList.size() > 0) {
				getMenuId(menuList, href, match);
			}
			
			if (match != null && StringUtils.isNotBlank(match.getId())) {
				menuId = match.getId();
			}
		}

		return menuId;
	}
	
	/**
	 * 递归获取最匹配url的菜单id
	 * @param menuList
	 * @param href
	 * @param match
	 * @return 是否需要递归继续
	 */
	private static boolean getMenuId(List<Resource> menuList, String href, Resource match){
		if (menuList != null && menuList.size() > 0) {
			for (Resource menu : menuList) {
				
				boolean isContinue = true;
				List<Resource> list = menu.getChildList();
				if(list!= null && list.size() > 0){
					isContinue = getMenuId(list, href, match);
					
					if (!isContinue) {
						return false;
					}
				}
				
				if (StringUtils.isBlank(menu.getHref())) {
					continue;
				}
				
				if(StringUtils.equalsIgnoreCase(href, menu.getHref())){
					match.setId(menu.getId());
					match.setHref(menu.getHref());
					return false;
				}
				
				if (StringUtils.startsWithIgnoreCase(href, menu.getHref()) 
						&& StringUtils.isNotBlank(match.getHref()) && StringUtils.isNotBlank(menu.getHref()) 
						&& match.getHref().length() < menu.getHref().length()) {
					match.setId(menu.getId());
					match.setHref(menu.getHref());
				}
			}
		}
		
		return true;
	}

	
	/**
	 * 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static String getMenuNamePath(String href) throws Exception{
		Map<String, String> menuMap = (Map<String, String>)CacheUtils.get(CACHE_RESOURCE_NAME_PATH_MAP);
		if (menuMap == null){
			List<Resource> menuList = getAllResourceList();
			if (CollectionUtils.isNotEmpty(menuList)) {
				menuMap = Maps.newHashMap();
				transformMenuNamePath(menuMap,menuList,"");
				CacheUtils.put(CACHE_RESOURCE_NAME_PATH_MAP, menuMap);
			}
		}
		return menuMap == null ? "" : menuMap.get(href);
	}
	
	/**
	 * 递归取得当前位置信息
	 * @param allMenus
	 * @param menu
	 * @param namePath
	 */
	private static void transformMenuNamePath(Map<String, String> menuMap,List<Resource> allMenus,String parentNamePath){
		for (Resource menu : allMenus) {
			if (StringUtils.isNotBlank(parentNamePath)) {
				menu.setNamePath(parentNamePath + " > " + menu.getName());
			} else {
				menu.setNamePath(menu.getName());
			}
			if (CollectionUtils.isNotEmpty(menu.getChildList())) {
				transformMenuNamePath(menuMap,menu.getChildList(),menu.getNamePath());
			}
			if (StringUtils.isNotBlank(menu.getHref())) {
				menuMap.put(menu.getHref(), menu.getNamePath());
			}
		}
	}
	
	/**
	 * 获取当前用户授权角色列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Role> getRoleList(){
		List<Role> roleList = (List<Role>) getCache(USER_CACHE_ROLE_LIST);
		if (roleList == null) {
			User user = getLoginUser();
			roleList = user.getRoleList();
			if (CollectionUtils.isNotEmpty(roleList)) {
				putCache(USER_CACHE_ROLE_LIST, roleList);
			}
		}
		return roleList;
	}
	
	/**
	 * 得到当前用户的授权列表
	 * @return
	 */
	public static Role getCurrentRole(){
		Role currentRole = (Role) getCache(USER_CACHE_CURRENT_ROLE);
		if (currentRole == null) {
			List<Role> roleList = getRoleList();
			int maxLevel = Integer.MAX_VALUE;
			for (Role role : roleList) {
				if (role.getLevel() < maxLevel) {
					//默认取级别最小，权限最大的角色
					maxLevel = role.getLevel();
					currentRole = role;
				}
			}
			putCache(USER_CACHE_CURRENT_ROLE, currentRole);
		}
		return currentRole;
	}
	
	/**
	 * 得到当前用户权限清单
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getPermissionList(){
		List<String> permissionList = (List<String>) getCache(USER_CACHE_PERMISSION_LIST);
		if (permissionList == null) {
			Role role = getCurrentRole();
			if (role != null) {
				permissionList = roleService.getPermissionListByRoleId(role.getId());
			}
			putCache(USER_CACHE_PERMISSION_LIST, permissionList);
		}
		return permissionList;
	}
	
	/**
	 * 根据角色名称进行角色切换
	 * @param roleName
	 */
	public static boolean changeRoleByName(String roleName){
		List<Role> list = getRoleList();
		if (CollectionUtils.isNotEmpty(list)) {
			for (Role role : list) {
				if (role.getName().equals(roleName)) {
					putCache(USER_CACHE_CURRENT_ROLE, role);
					
					removeCache(USER_CACHE_MENU_LIST);
					removeCache(USER_CACHE_MENU_TREEJSON_MAP);
					removeCache(USER_CACHE_PERMISSION_LIST);
					
					CacheUtils.remove(CACHE_RESOURCE_ALL_LIST);
					CacheUtils.remove(CACHE_RESOURCE_NAME_PATH_MAP);
					//清空shiro授权信息
					RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
					SystemAuthorizingRealm realm = (SystemAuthorizingRealm) securityManager.getRealms().iterator().next();
					realm.clearCachedAuthorizationInfo();
					
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 是否拥有该权限
	 * @param permission
	 * @return
	 */
	public static boolean hasPermission(String permission){
		List<String> permissionList = getPermissionList();
		if (CollectionUtils.isNotEmpty(permissionList)) {
			return permissionList.contains(permission);
		}
		return false;
	}
	
	
	
	/**
	 * 清除当前权限缓存
	 */
	public static void clearCache(){
		removeCache(USER_CACHE_ROLE_LIST);
		removeCache(USER_CACHE_CURRENT_ROLE);
		removeCache(USER_CACHE_MENU_LIST);
		removeCache(USER_CACHE_MENU_TREEJSON_MAP);
		removeCache(USER_CACHE_PERMISSION_LIST);
		removeCache(USER_CACHE_AUTH_RESOURCE_ALL_LIST);
		
		CacheUtils.remove(CACHE_RESOURCE_ALL_LIST);
		CacheUtils.remove(CACHE_RESOURCE_NAME_PATH_MAP);
		
		//清空shiro授权信息
		RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
		SystemAuthorizingRealm realm = (SystemAuthorizingRealm) securityManager.getRealms().iterator().next();
		realm.clearCachedAuthorizationInfo();
		
		clearCache(getLoginUser());
	}
	
	/**
	 * 清除当前指定用户缓存
	 * @param user
	 */
	public static void clearCache(User user){
		CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
	}
	
	/**
	 * 根据ID清除当前指定用户缓存
	 * @param user
	 */
	public static void clearCache(String userId){
		User user = getUserById(userId);
		if (user != null) {
			clearCache(user);
		}
	}
	
	
	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			if (principal != null){
				return principal;
			}
			subject.logout();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 得到当前session
	 * @return
	 */
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
			subject.logout();
		}catch (InvalidSessionException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/****************** session cache method ******************/
	public static Object getCache(String key) {
		return getSession().getAttribute(key);
	}
	public static void putCache(String key, Object value) {
		getSession().setAttribute(key, value);
	}
	public static void removeCache(String key) {
		getSession().removeAttribute(key);
	}

}
