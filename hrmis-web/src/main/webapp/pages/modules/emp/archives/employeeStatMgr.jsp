<%@page import="com.eastcom.baseframe.common.utils.DateUtils"%>
<%@page import="com.eastcom.hrmis.modules.emp.EmpConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<html>
<head>
<title>员工统计分析</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/emp/archives/employeeStatMgr.js"></script>
</head>

<body>
	<div class="easyui-layout" style="width: 100%; height: 650px;">
		<div data-options="region:'center'">
			<sys:easyuigrid id="datagrid" name="人员统计分析表格" attrHtml="fit='true' rownumbers='true' enable='false'
				singleSelect='true' toolbar='#toolbar' border='false' fitColumns='true'"/>
				
			<div id="toolbar">
				<table style="line-height:26px;padding: 10px;padding-bottom: 0px;">
					<tr>
						<td>部门：</td>
						<td>
							<select id="employeeDept" name="employeeDept" style='height:22px;'>
								<option value=''>--请选择--</option>
							</select>
						</td>
						<td>选择时间：</td>
						<td>
							<input id="compareDate1" name="compareDate1" value="<%=DateUtils.formatDate(new Date(), "yyyy年-MM月") %>" class="Wdate" style="width: 100px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy年-MM月',maxDate:'<%=DateUtils.formatDate(new Date(), "yyyy年-MM月") %>',onpicked:Interactive.refreshGridColumn})"/>
						</td>
						<td>----对比----</td>
						<td>选择时间：</td>
						<td>
							<input id="compareDate2" name="compareDate2" value="<%=DateUtils.formatDate(DateUtils.addDays(new Date(), -365), "yyyy年-MM月") %>" class="Wdate" style="width: 100px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy年-MM月',maxDate:'<%=DateUtils.formatDate(new Date(), "yyyy年-MM月") %>',onpicked:Interactive.refreshGridColumn})"/>
						</td>
						<td>
	        				<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							<shiro:hasPermission name="emp:employeestat:export">
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