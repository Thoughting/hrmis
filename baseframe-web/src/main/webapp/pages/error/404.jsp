<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%response.setStatus(200);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>404 - 页面不存在</title>
	<%@include file="/pages/include/head.jsp" %>
</head>
<body class="error_bodybg">
	<div class="error_msg_bg">
	    <div class="error_msg">
	        <div class="tips_tit_big">404</div>
	        <div class="msg_tips"><span class="im">ERROR！</span>很抱歉您访问的页面不存在或已被删除！</div>
	        <a href="javascript:window.open('${ctx }','_self');" class="back_index">返回首页</a>
	    </div>
	</div>
</body>
</html>