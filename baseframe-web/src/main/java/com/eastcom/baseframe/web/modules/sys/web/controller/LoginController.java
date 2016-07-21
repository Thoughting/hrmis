package com.eastcom.baseframe.web.modules.sys.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.BaseController;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.LoginLog;
import com.eastcom.baseframe.web.modules.sys.security.UsernamePasswordToken;

/**
 * 登录Controller
 * 
 * @author wutingguang <br>
 */
@Controller
public class LoginController extends BaseController {

	@RequestMapping(value = "login", method = { RequestMethod.GET })
	public String login(HttpServletRequest req, HttpServletResponse resp) {
		if (SecurityUtils.getSubject().getSession() != null) {
			SecurityUtils.getSubject().logout();
		}
		return "/modules/sys/login";
	}

	@LoginLog
	@RequestMapping(value = "login", method = { RequestMethod.POST })
	public String login(Model model,HttpServletRequest req, HttpServletResponse resp,
			@RequestParam String userName, @RequestParam String password,
			@RequestParam(required=false) String code) {
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password, code);
		try {
			SecurityUtils.getSubject().login(token);
		} catch (Exception e) {
			model.addAttribute("msg", e.getMessage().replace("msg:", ""));
			return "/modules/sys/login";
		}
		return "redirect:index.jsp";
	}

	@RequestMapping(value = "logout", method = { RequestMethod.GET })
	public String logout(HttpServletRequest req, HttpServletResponse resp) {
		if (SecurityUtils.getSubject().getSession() != null) {
			SecurityUtils.getSubject().logout();
		}
		String returnUrl = req.getParameter("returnUrl");
		String view = "redirect:/login";
		if (StringUtils.isNotBlank(returnUrl)) {
			view = view + "?returnUrl=" + returnUrl;
		}
		return view;
	}
	
}
