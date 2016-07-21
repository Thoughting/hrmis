package com.eastcom.baseframe.web.modules.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "T_SYS_LOG_OPERATION")
@DynamicInsert @DynamicUpdate
public class LogOperation extends IdEntity<LogOperation>{
	private static final long serialVersionUID = 1L;
	
	public LogOperation() {
		super();
	}
	
	public LogOperation(String id) {
		this();
		this.id = id;
	}
	
	private String remoteAddr; 	// 请求用户的IP地址
	private String userAgent;	// 请求用户代理信息
	private String loginName;	// 登录帐号
	private String requestUri; 	// 请求的URI
	private int dicMethodType; 	// 请求的方式
	private int isSuccess;		// 操作是否有成功
	private String content;		// 操作内容
	private int isHasRights;	// 是否有操作权限
	private String menuItem;	// 资源菜单
	private Date createDate;	// 日志创建时间
	
	@Column(name="REMOTE_ADDR")
	public String getRemoteAddr() {
		return remoteAddr;
	}
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	
	@Column(name="USER_AGENT")
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	@Column(name="LOGIN_NAME")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Column(name="REQUEST_URI")
	public String getRequestUri() {
		return requestUri;
	}
	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}
	
	@Column(name="DIC_METHOD_TYPE")
	public int getDicMethodType() {
		return dicMethodType;
	}
	public void setDicMethodType(int dicMethodType) {
		this.dicMethodType = dicMethodType;
	}
	
	@Column(name="CONTENT")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name="IS_SUCCESS")
	public int getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(int isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	@Column(name="IS_HAS_RIGHTS")
	public int getIsHasRights() {
		return isHasRights;
	}
	public void setIsHasRights(int isHasRights) {
		this.isHasRights = isHasRights;
	}
	
	@Column(name="CREATE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name="MENU_ITEM")
	public String getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(String menuItem) {
		this.menuItem = menuItem;
	}

}
