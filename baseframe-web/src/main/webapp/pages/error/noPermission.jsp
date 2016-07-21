<%@ page contentType="text/html; charset=utf-8" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>禁止访问</title>
<style type="text/css">
body,p,span { padding:0; margin:0;}
.no_quanxian_repeat {  background:url(<%=path%>/images/noPermission/bg_re.jpg) center repeat; width:100%; height:100%; min-height:600px;}
.no_quanxian_bg { background:url(<%=path%>/images/noPermission/bg.jpg) center no-repeat; width:967px; min-height:600px; margin:0 auto;}
.button_position { position:relative; left:673px; top:230px; width:100px;}
.no_quanxian_bg p { font-size:14px; text-align:center; position:relative; top:342px; color:#6f6f6f;}
.no_quanxian_bg p span { color:#13649b;}
</style>
</head>

<body>
<div class="no_quanxian_repeat">
	<div class="no_quanxian_bg">
    	<div class="button_position">
        	<!--input type="button" value="返回上一页" /-->
        </div>
        <p>如需要相关的访问权限，请联系<span>系统维护人员</span>申请</p>
    </div>
</div>
</body>
</html>
