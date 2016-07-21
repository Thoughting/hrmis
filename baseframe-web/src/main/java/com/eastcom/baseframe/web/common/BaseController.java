/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.eastcom.baseframe.web.common;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.druid.support.json.JSONParser;
import com.eastcom.baseframe.common.beanvalidator.BeanValidators;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.baseframe.common.utils.NetWorkUtils;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 控制器支持类
 * @author ThinkGem
 * @version 2013-3-23
 */
public abstract class BaseController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(model, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(redirectAttributes, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组，不传入此参数时，同@Valid注解验证
	 * @return 验证成功：继续执行；验证失败：抛出异常跳转400页面。
	 */
	protected void beanValidator(Object object, Class<?>... groups) {
		BeanValidators.validateWithException(validator, object, groups);
	}
	
	/**
	 * 添加Model消息
	 * @param message
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		model.addAttribute("message", sb.toString());
	}
	
	/**
	 * 添加Flash消息
	 * @param message
	 */
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}
	
	/**
	 * 客户端返回JSON字符串
	 * @param response
	 * @param object
	 * @return
	 */
	protected String renderString(HttpServletResponse response, Object object) {
		return null;
//		return renderString(response, JsonMapper.toJsonString(object), "application/json");
	}
	
	/**
	 * 客户端返回字符串
	 * @param response
	 * @param string
	 * @return
	 */
	protected String renderString(HttpServletResponse response, String string, String type) {
		try {
			response.reset();
	        response.setContentType(type);
	        response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 参数绑定异常
	 */
	@ExceptionHandler({BindException.class, ConstraintViolationException.class, ValidationException.class})
    public String bindException() {  
        return "error/400";
    }
	
	/**
	 * 授权登录异常
	 */
	@ExceptionHandler({AuthenticationException.class})
    public String authenticationException() {  
        return "error/403";
    }
	
	/**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}
	
	/**
	 * 把xml字符转换为xml对象传给页面
	 * 
	 * @param response
	 * @param xml字符串
	 * @return void
	 */
	protected void sendXMLMsgToView(HttpServletResponse response, String xml) {
		Document document = null;
		try {
			document = DocumentHelper.parseText(xml.toString());

		} catch (DocumentException e) {
			logger.error("XML转Document对象出错：" + e.getMessage());
			e.printStackTrace();
		}
		sendMsgToView(response, document.asXML());
	}

	/**
	 * 返回信息到前端
	 * 
	 * @param response
	 * @param msg
	 */
	protected void sendMsgToView(HttpServletResponse response, String msg) {
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(msg);
		out.flush();
		out.close();
	}
	
	/**
	 * 获取web服务的basePath，即如"http://xx.xx:xx/xxx
	 * 
	 * @param request
	 * @return
	 */
	protected String getBasePath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getContextPath();
	}

	/**
	 * 获取会话路径
	 * 
	 * @return 会话路径
	 */
	protected String getContextPath(HttpServletRequest request) {
		try {
			return request.getContextPath();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return null;
	}

	/**
	 * 获取应用物理路径
	 * 
	 * @return 应用物理路径
	 */
	protected String getRealPath(HttpSession session) {
		try {
			return session.getServletContext().getRealPath("/");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return null;
	}
	
	/**
	 * 得到excel导出路径
	 * 
	 * @param request
	 * @return
	 */
	protected String getExportPath(HttpServletRequest request){
		String path = request.getSession().getServletContext().getRealPath("/templates/export") + File.separator
				+ DateUtils.formatDate(new Date(), "yyyyMMddHH") + File.separator;
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		return path;
	}
	
	/**
	 * 将传入的JSON转换成Map
	 * 
	 * @param request
	 * @return
	 */
	protected Map<String,Object> convertJsonToMap(HttpServletRequest request){
		String json = NetWorkUtils.getPostData(request);
		if (StringUtils.isNotBlank(json)) {
			JSONParser jsonParser = new JSONParser(json);
			return jsonParser.parseMap();
		}
		return Maps.newHashMap();
	}
	
	/**
	 * 将传入的 JSON ARR 转换成 List Map
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String,Object>> convertJsonArrToMap(HttpServletRequest request){
		List<Map<String,Object>> list = Lists.newArrayList();
		String json = NetWorkUtils.getPostData(request);
		if (StringUtils.isNotBlank(json)) {
			JSONParser jsonParser = new JSONParser(json);
			List<Object> jsonMaps = jsonParser.parseArray();
			if (CollectionUtils.isNotEmpty(jsonMaps)) {
				for (Object data : jsonMaps) {
					Map<String, Object> jsonMap = (Map<String, Object>) data;
					list.add(jsonMap);
				}
			}
		}
		return list;
	}
	
	/**
	 * 将传入的JSON转换成Map
	 * 
	 * @param request
	 * @return
	 */
	protected Map<String,Object> convertJsonToMap(String json){
		if (StringUtils.isNotBlank(json)) {
			JSONParser jsonParser = new JSONParser(json);
			return jsonParser.parseMap();
		}
		return Maps.newHashMap();
	}
	
	/**
	 * 将传入的 JSON ARR 转换成 List Map
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String,Object>> convertJsonArrToMap(String json){
		List<Map<String,Object>> list = Lists.newArrayList();
		if (StringUtils.isNotBlank(json)) {
			JSONParser jsonParser = new JSONParser(json);
			List<Object> jsonMaps = jsonParser.parseArray();
			if (CollectionUtils.isNotEmpty(jsonMaps)) {
				for (Object data : jsonMaps) {
					Map<String, Object> jsonMap = (Map<String, Object>) data;
					list.add(jsonMap);
				}
			}
		}
		return list;
	}
}
