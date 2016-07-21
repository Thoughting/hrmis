package com.eastcom.baseframe.web.common.tag;

import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * tag base
 * @author wutingguang <br>
 */
public abstract class BaseTag extends TagSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5211664550372961393L;
	
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
}
