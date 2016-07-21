<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>登录日志查询</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/sys/log/loginLogMgr.js"></script>
</head>

<body>
	<div class="easyui-layout" style="width: 100%; height: 650px;">
		<div data-options="region:'center'">
			<table id="datagrid" class="easyui-datagrid" fit="true"
				singleSelect="true" toolbar="#toolbar" border="false"
				fitColumns="true" pagination="true" pageSize="20">
				<thead>
					<tr>
						<th field="loginName" width="50">登录帐号</th>
						<th field="remoteAddr" width="50">登录IP</th>
						<th field="userAgent" width="50">客户端信息</th>
						<th field="loginType" width="50">登录类型</th>
						<th field="createDate" width="50">登录时间</th>
					</tr>
				</thead>
			</table>
			<div id="toolbar">
				<table style="line-height:26px;padding: 10px;">
					<tr>
						<td>登录帐号：</td>
						<td><input id="loginName" type="text"/></td>
						<td>登录时间段：</td>
						<td>
							<input id="startTime" class="Wdate" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
							至
							<input id="endTime" class="Wdate" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
						</td>
						<td>
							<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>

</body>
</html>