<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统登录</title>
<%@include file="/pages/include/head.jsp"%>

<link href="${ ctxStatic}/logins/default/css/css.css" rel="stylesheet"
	type="text/css" />
<link href="${ ctxStatic}/logins/default/css/login.css" rel="stylesheet"
	type="text/css" />
	
<script type="text/javascript">
	$(function(){
		$("#submitBtn").bind("click",function(){
			$("#loginForm").submit();
		})
	})
</script>

</head>

<body class="longinBg">
	<div class="main" style="width: 1000px; margin: 0 auto;">
		<div class="login_logBox">
			<form id="loginForm" class="form-signin" action="${ctx }/login" method="post">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td height="62" colspan="2"><input name="userName" type="text"
							class="name" /></td>
					</tr>
					<tr>
						<td height="62" colspan="2"><input name="password" type="password"
							class="name pass" /></td>
					</tr>
					<tr>
						<td colspan="2">
							<%-- <label for="txtCode">验证码</label>
							<input type="text" style="width: 40px;" />
							<img src="${ctx}/servlet/validateCodeServlet" style="width: 70px;" />
							<a href="javascript:void(0)">看不清</a> --%>
						</td>
					</tr>
					<tr>
						<td width="89" height="24"><input type="checkbox"
							name="checkbox" id="checkbox" /> <label for="checkbox">记住密码</label></td>
						<td align="center"><a href="javascript:void(0)">忘记密码</a></td>
					</tr>
					<tr>
						<td width="89" height="50">
							<a id="submitBtn" href="javascript:void(0)" class="login" >登录</a>
						</td>
						<td align="left">
							<a href="javascript:void(0)" class="reset">重置</a>
						</td>
					</tr>
				</table>
			</form>
			<div class="login_newBox">
				<div class="listBox">
					<marquee direction="up" height="60" onmouseout="this.start()" onmouseover="this.stop()" scrollAmount="1" scrollDelay="0.5">
						<ul>
							<!-- <li><a href="#">江苏移动统一采集系统上线</a></li>
							<li><a href="#">江苏移动统一采集系统上线</a></li>
							<li><a href="#">江苏移动统一采集系统上线</a></li> -->
						</ul>
					</marquee>
				</div>
			</div>
		</div>
		<div class="logFoot">
			<br /> 杭州东方通信软件技术有限公司提供技术支持
		</div>
	</div>
</body>
</html>
