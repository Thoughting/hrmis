<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ page import="com.eastcom.baseframe.common.beanvalidator.BeanValidators"%>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%@ include file="/pages/include/taglib.jsp"%>
<%response.setStatus(200);%>
<%
	Throwable ex = null;
	if (exception != null)
		ex = exception;
	if (request.getAttribute("javax.servlet.error.exception") != null)
		ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
	//记录日志
	if (ex!=null){
		Logger logger = LoggerFactory.getLogger("500.jsp");
		logger.error(ex.getMessage(), ex);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>500 - 系统内部错误</title>
	<%@include file="/pages/include/head.jsp" %>
</head>
<body class="error_bodybg">
	<div class="error_msg_bg">
	    <div class="error_msg">
	        <div class="tips_tit_big">500</div>
	        <div class="msg_tips"><span class="im">ERROR！</span>
	        <%
				if (ex!=null){
					if (ex instanceof javax.validation.ConstraintViolationException){
						for (String s : BeanValidators.extractPropertyAndMessageAsList((javax.validation.ConstraintViolationException)ex, ": ")){
							out.print(s+"<br/>");
						}
					}else{
						out.print(ex+"<br/>");
					}
				}
			%>
	        </div>
	       <a href="javascript:window.open('${ctx }','_self');" class="close_it">返回上一页</a>
	        <a href="javascript:window.open('${ctx }','_self');" class="back_index">返回首页</a>
	    </div>
	</div>
</body>
</html>