package com.eastcom.baseframe.web.common.model;

/**
 * 
 * JSON模型<br>
 * Description: 用户后台向前台返回的JSON对象<br>
 * Copyright: eastcom_sw Copyright (C) 2013 <br>
 * 
 * @author <a href="mailto:邮箱">hujian</a><br>
 * @e-mail: hujian@eastcom-sw.com<br>
 * 
 */
public class AjaxJson implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7350201149211534896L;

	private boolean success = false;
	private String message = "";
	private Object model = null;

	public AjaxJson() {
		super();
	}
	
	public AjaxJson(boolean success, String message, Object model) {
		super();
		this.success = success;
		this.message = message;
		this.model = model;
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getModel() {
		return model;
	}
	public void setModel(Object model) {
		this.model = model;
	}


}
