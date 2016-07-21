<%@page import="com.eastcom.baseframe.common.utils.DateUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="default" />
<title>工作台</title>

<script src="${ctxScripts}/modules/emp/index.js"></script>
<script type="text/javascript">

</script>
</head>

<body>
	<div class="easyui-layout" style="width:100%;height:650px;">
        <div data-options="region:'center',iconCls:'icon-ok'">
            <div id="mainTab" class="easyui-tabs" fit="true" border="false" toolbar="#toolbar">
		      	<div title="待办事项" style="overflow: hidden;">
		      		<table style="line-height:26px;padding: 10px;">
						<tr>
							<td>创建人：</td>
							<td>
								<input id="db_createBy" type="text"/>
							</td>
							<td>
								<a href="javascript:$('#dbGrid').datagrid('reload')" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</td>
						</tr>
					</table>
		      		<table id="dbGrid" class="easyui-datagrid" style="width:100%;height: 567px;" pagination="true"
		      				fitColumns="true" singleSelect="true" rownumbers="true" pageSize="20">
				        <thead>
				            <tr>
								<th field="statusDict" width="50" align="center">处理状态</th>
								<th field="employeeDeptName" width="50" align="center">项目名称</th>
				                <th field="typeDict" width="50" align="center">工单类型</th>
				                <th field="content" width="100" align="center">工单内容</th>
				                <th field="createBy" width="50" align="center">创建人</th>
				                <th field="createDate" width="50" align="center">创建时间</th>
				            </tr>
				        </thead>
				    </table>
		     	</div>
		        <div title="已办事项" style="overflow: hidden;">
		        	<table style="line-height:26px;padding: 10px;">
						<tr>
							<td>创建人：</td>
							<td>
								<input id="yb_createBy" type="text"/>
							</td>
							<td>
								<a href="javascript:$('#ybGrid').datagrid('reload')" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</td>
						</tr>
					</table>
		        	<table id="ybGrid" class="easyui-datagrid" style="width:100%;height: 567px;" pagination="true"
		      				fitColumns="true" singleSelect="true" rownumbers="true" pageSize="20">
				        <thead>
				            <tr>
								<th field="statusDict" width="50" align="center">处理状态</th>
								<th field="employeeDeptName" width="50" align="center">项目名称</th>
				                <th field="typeDict" width="50" align="center">工单类型</th>
				                <th field="content" width="100" align="center">工单内容</th>
				                <th field="createBy" width="50" align="center">创建人</th>
				                <th field="createDate" width="50" align="center">创建时间</th>
				                <th field="updateBy" width="50" align="center">处理人</th>
				                <th field="updateDate" width="50" align="center">处理时间</th>
				            </tr>
				        </thead>
				    </table>
		        </div>
		    </div>
        </div>
    </div>
</body>
</html>