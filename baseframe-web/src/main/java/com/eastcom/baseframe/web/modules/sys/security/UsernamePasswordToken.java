package com.eastcom.baseframe.web.modules.sys.security;

/**
 * 用户和密码（包含验证码）令牌类
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {

	private static final long serialVersionUID = 1L;
	/*
	 * 验证码
	 */
	private String code;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public UsernamePasswordToken() {
		super();
	}

	public UsernamePasswordToken(String username, String password, String code) {
		super(username, password);
		this.code = code;
	}
}