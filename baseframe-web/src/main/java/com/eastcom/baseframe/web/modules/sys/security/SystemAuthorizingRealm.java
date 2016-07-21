/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.eastcom.baseframe.web.modules.sys.security;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastcom.baseframe.common.config.Global;
import com.eastcom.baseframe.web.modules.sys.entity.Role;
import com.eastcom.baseframe.web.modules.sys.entity.User;
import com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache;
import com.eastcom.baseframe.web.modules.sys.security.session.SessionDAO;
import com.eastcom.baseframe.web.modules.sys.utils.PasswordUtil;

/**
 * 系统安全认证实现类
 * 
 * @author ThinkGem
 * @version 2014-7-5
 */
@Component
public class SystemAuthorizingRealm extends AuthorizingRealm {

	public static final String REALM_NAME = "SystemAuthorizingRealm";
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SessionDAO sessionDAO;
	
	public SystemAuthorizingRealm() {
		super();
		setName(REALM_NAME);
		setCredentialsMatcher(new AllowAllCredentialsMatcher());
	}

	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		SecurityCache.clearCache();
		
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String password = String.valueOf(token.getPassword());
		String userName = token.getUsername();
		logger.info("正在登录用户:" + userName);
		User user = SecurityCache.getUserByLoginName(userName);
		if (user == null) {
			throw new AuthenticationException("msg:该帐号不存在!.");
		}
		if (!PasswordUtil.validatePassword(password, user.getPassword())) {
			throw new AuthenticationException("msg:密码错误!.");
		}
		return new SimpleAuthenticationInfo(new Principal(user),password,userName);
	}
	
	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		//获取当前已登录的用户
		Principal principal = (Principal) getAvailablePrincipal(principals);
		if (!"true".equals(Global.getConfig("user.multiAccountLogin"))) {
			//不允许一个帐号多次登录
			Collection<Session> sessions = sessionDAO.getActiveSessions(true, principal, SecurityCache.getSession());
			if (sessions.size() > 0){
				// 如果是登录进来的，则踢出已在线用户
				if (SecurityUtils.getSubject().isAuthenticated()){
					for (Session session : sessions){
						sessionDAO.delete(session);
					}
				}
				// 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
				else{
					SecurityUtils.getSubject().logout();
					throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
				}
			}
		}
		User user = SecurityCache.getUserByLoginName(principal.getLoginName());
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			//添加权限字符串标志
			info.addStringPermissions(SecurityCache.getPermissionList());
			// 添加用户角色信息
			for (Role role : user.getRoleList()){
				info.addRole(role.getName());
			}
			// 更新登录IP和时间
			// getSystemService().updateUserLoginInfo(user);
			return info;
		} else {
			return null;
		}
	}
	
	/**
	 * 清空授权信息
	 */
	public void clearCachedAuthorizationInfo(){
		this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
	}
	
	/**
	 * 授权用户信息
	 */
	public static class Principal implements Serializable {
		private static final long serialVersionUID = 1L;
		private String id; // 编号
		private String loginName; // 登录名
		private String name; // 姓名
		public Principal(User user) {
			this.id = user.getId();
			this.loginName = user.getLoginName();
			this.name = user.getName();
		}
		public String getId() {
			return id;
		}
		public String getLoginName() {
			return loginName;
		}
		public String getName() {
			return name;
		}
		/**
		 * 获取SESSIONID
		 */
		public String getSessionid() {
			try{
				return (String) SecurityCache.getSession().getId();
			}catch (Exception e) {
				return "";
			}
		}
	}
}
