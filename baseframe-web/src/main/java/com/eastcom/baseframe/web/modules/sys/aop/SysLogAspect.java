package com.eastcom.baseframe.web.modules.sys.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.baseframe.common.utils.IdGen;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.utils.Servlets;
import com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog;
import com.eastcom.baseframe.web.modules.sys.entity.LogLogin;
import com.eastcom.baseframe.web.modules.sys.entity.LogOperation;
import com.eastcom.baseframe.web.modules.sys.entity.User;
import com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache;
import com.eastcom.baseframe.web.modules.sys.service.LogLoginService;
import com.eastcom.baseframe.web.modules.sys.service.LogOperationService;
import com.eastcom.baseframe.web.modules.sys.service.UserService;

@Aspect
@Component
public class SysLogAspect {

	@Autowired
	private LogLoginService logLoginService;

	@Autowired
	private LogOperationService logOperationService;

	@Autowired
	private UserService userService;
	
	@Pointcut("@annotation(com.eastcom.baseframe.web.modules.sys.aop.annotation.LoginLog)")
	public void loginLogAspect() {
	}

	@Pointcut("@annotation(com.eastcom.baseframe.web.modules.sys.aop.annotation.OperationLog)")
	public void operationLogAspect() {
	}

	/**
	 * 记录登录日志
	 * 
	 * @param joinPoint
	 */
	@After("loginLogAspect()")
	public void afterLoginLog(JoinPoint joinPoint) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();

		LogLogin log = new LogLogin();
		log.setRemoteAddr(Servlets.getRemoteAddr(request));
		log.setUserAgent(Servlets.getUserAgent(request));
		log.setLoginType(0);
		log.setIsSuccess(0);
		log.setContent("登录失败");
		log.setLoginName("");
		log.setCreateDate(DateUtils.getNow());

		// 异步保存日志信息
		new SaveLoginLogThread(log).start();
	}

	/**
	 * 记录controller的操作日志
	 * 
	 * @param joinPoint
	 */
	@AfterReturning("operationLogAspect()")
	public void afterOperationLog(JoinPoint joinPoint) {
		doAfterOperationLog(1, joinPoint, null);
	}

	@AfterThrowing(pointcut = "operationLogAspect()", throwing = "e")
	public void afterThrowingOperationLog(JoinPoint joinPoint, Throwable e) {
		doAfterOperationLog(0, joinPoint, e);
	}
	
	private void doAfterOperationLog(int isSuccess ,JoinPoint joinPoint, Throwable e){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();

		OperationLog annotation = getMethodAnnotation(joinPoint,
				OperationLog.class);

		LogOperation log = new LogOperation(IdGen.uuid());
		log.setRemoteAddr(Servlets.getRemoteAddr(request));
		log.setUserAgent(Servlets.getUserAgent(request));
		log.setRequestUri(Servlets.getHref(request));
		log.setDicMethodType(0);
		log.setMenuItem("");
		log.setIsHasRights(1);
		log.setIsSuccess(isSuccess);
		log.setContent("");
		log.setLoginName(SecurityCache.getLoginUser().getLoginName());
		log.setCreateDate(DateUtils.getNow());

		// 异步保存日志信息
		new SaveOperationLogThread(log, annotation, joinPoint, e).start();
	}

	static <T extends Annotation> T getMethodAnnotation(JoinPoint joinPoint,
			Class<T> annotationClass) {
		try {
			Class<?> targetClass = joinPoint.getTarget().getClass();
			String methodName = joinPoint.getSignature().getName();
			Method[] methods = targetClass.getMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)
						&& method.getParameterTypes().length == joinPoint
								.getArgs().length) {
					return method.getAnnotation(annotationClass);
				}
			}
		} catch (Exception e) {
			System.out.print(e.getStackTrace());
		}

		return null;
	}

	/**
	 * 异步保存登录日志
	 * 
	 * @author XZK
	 *
	 */
	public class SaveLoginLogThread extends Thread {

		private LogLogin log;

		public SaveLoginLogThread(LogLogin log) {
			this.log = log;
		}

		@Override
		public void run() {
			try {
				User user = SecurityCache.getLoginUser();
				if (user != null && StringUtils.isNotBlank(user.getId())) {
					log.setIsSuccess(1);
					log.setContent("登录成功");
					log.setLoginName(user.getLoginName());
					
					//保存用户最近一次登录信息
					user.setLoginIp(log.getRemoteAddr());
					user.setLoginDate(new Date());
					userService.update(user);
					
					logLoginService.save(log);
				}
			} catch (Exception e) {
				System.out.print(e.getStackTrace());
			}
		}
	}

	/**
	 * 异步保存操作日志
	 * 
	 * @author XZK
	 *
	 */
	public class SaveOperationLogThread extends Thread {
		
		private LogOperation log;
		private OperationLog annotation;
		private JoinPoint joinPoint;
		private Throwable e;
		
		public SaveOperationLogThread(LogOperation log,
				OperationLog annotation, JoinPoint joinPoint, Throwable e) {
			this.log = log;
			this.annotation = annotation;
			this.joinPoint = joinPoint;
			this.e = e;
		}

		@Override
		public void run() {
			try {
				// 保存日志信息
				String menuId = SecurityCache.getMenuId(log.getRequestUri());
				if (StringUtils.isNotBlank(menuId)) {
					log.setMenuItem(SecurityCache.getMenuNamePath(log.getRequestUri()));
				}

				if (log.getIsSuccess() == 1) {
					log.setContent(annotation.content() + " 操作成功");
				} else {
					log.setContent(annotation.content() + " 操作失败,异常信息:" + e.getStackTrace().toString());
				}
				logOperationService.save(log);
			} catch (Exception e) {
				System.out.print(e.getStackTrace());
			}
		}
	}

}
