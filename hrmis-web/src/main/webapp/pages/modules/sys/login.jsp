<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>系统登录</title>

<!-- jquery -->
<script src="${ctxPlugins}/jquery-1.11.3.min.js"></script>
<script src="${ctxPlugins}/json2.js"></script>


<link rel="stylesheet" href="${ ctxStatic}/logins/default/css/css.css" >
<link rel="stylesheet" href="${ ctxStatic}/logins/default/css/login.css" >

<script type="text/javascript">
	$(function(){
		$("#submitBtn").click(function(){
			$("#loginForm").submit();
		})
		
		$("#resetBtn").click(function(){
			$("#userName").val("");
			$("#password").val("");
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
						<td height="62" colspan="2"><input id="userName" name="userName" type="text"
							class="name" /></td>
					</tr>
					<tr>
						<td height="62" colspan="2"><input id="password" name="password" type="password"
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
						<td width="89" height="24"><!-- <input type="checkbox"
							name="checkbox" id="checkbox" /> <label for="checkbox">记住密码</label> --></td>
					</tr>
					<tr>
						<td width="89" height="50">
							<a id="submitBtn" href="javascript:void(0)" class="login" >登录</a>
						</td>
						<td align="left">
							<a id="resetBtn" href="javascript:void(0)" class="reset">重置</a>
						</td>
					</tr>
				</table>
			</form>
			<div class="login_newBox">
				<div class="listBox">
					<font color="red">${msg }</font>
					<marquee direction="up" height="60" onmouseout="this.start()" onmouseover="this.stop()" scrollAmount="1" scrollDelay="0.5">
						<ul>
						</ul>
					</marquee>
				</div>
			</div>
		</div>
		<div class="logFoot">
			<br /> 广东路通交通服务有限公司版权所有
		</div>
	</div>
</body>
</html>
