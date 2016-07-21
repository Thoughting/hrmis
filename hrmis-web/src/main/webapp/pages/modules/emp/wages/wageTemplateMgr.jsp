<%@page import="com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache"%>
<%@page import="com.eastcom.baseframe.common.utils.DateUtils"%>
<%@page import="com.eastcom.hrmis.modules.emp.EmpConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<html>
<head>
<title>工资方案数据配置</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/emp/wages/wageTemplateMgr.js"></script>
<script type="text/javascript">
var hasGridEdit = <%=SecurityCache.hasPermission("emp:wagetemplatemgr:edit")%>;

</script>
</head>

<body>
	<div class="easyui-layout" style="width: 100%; height: 650px;">
		<div data-options="region:'center'">
			<table id="datagrid" class="easyui-datagrid" fit="true" rownumbers="true" enable="false"
				singleSelect="true" toolbar="#toolbar" border="false" pagination="true" pageSize="20">
			</table>
			<div id="toolbar">
				<table style="line-height:26px;padding: 10px;padding-bottom: 0px;">
					<tr>
						<td>所属工资方案：</td>
						<td>
							<select id="wagePlanCombo" name="wagePlanCombo" style='height:22px;'>
								<option value=''>--请选择--</option>
							</select>
						</td>
						<td>
							<shiro:hasPermission name="emp:wagetemplatemgr:export">
								<a id="exportBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">导出</a>
							</shiro:hasPermission>
	        			</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
</html>