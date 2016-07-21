<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ taglib prefix="sitemesh"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>人力资源管理系统-<sitemesh:title /></title>
<%@include file="/pages/include/head.jsp"%>

<sitemesh:head />
</head>

<body>

	<!-- navigation   -->
	<c:import url="/pages/layouts/default/navigation.jsp"></c:import>

	<!-- content -->
	<div id="main" style="background: #fff">
		<sitemesh:body></sitemesh:body>
	</div>
	
	<!-- copyright   -->
	<c:import url="/pages/layouts/default/copyright.jsp"></c:import>
	
</body>
</html>