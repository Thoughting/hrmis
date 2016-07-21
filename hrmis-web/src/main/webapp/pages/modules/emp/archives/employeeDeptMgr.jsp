<%@page import="com.eastcom.hrmis.modules.emp.EmpConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>职务部门管理</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/emp/archives/employeeDeptMgr.js"></script>
</head>
<body>
	<div class="easyui-layout" style="width:100%;height:650px;">
		<div data-options="region:'center'">
		    <table id="datagrid" class="easyui-datagrid" fit="true" singleSelect="true" pageSize="20"
		            toolbar="#toolbar" border="false" fitColumns="true" pagination="true">
		        <thead>
		            <tr>
		            	<th field="name" width="50">组织名称</th>
		            	<th field="code" width="50">组织编号</th>
		            	<th field="workTimer" width="50">上班时长(小时)</th>
		                <th field="remark" width="50">备注</th>
		            </tr>
		        </thead>
		    </table>
		    <div id="toolbar">
				<table style="line-height:26px;padding: 10px;">
					<tr>
						<td>组织名称：</td>
						<td><input id="name" name="name" type="text" style="width: 120px;"/></td>
						<td>组织编号：</td>
						<td><input id="code" name="code" type="text" style="width: 120px;"/></td>
						<td>
							<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							<shiro:hasPermission name="emp:deptmgr:add">
								<a id="addBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add">新增</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="emp:deptmgr:edit">
								<a id="editBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">编辑</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="emp:deptmgr:del">
								<a id="delBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove">删除</a>
							</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</div>
		    <!-- 新增与编辑弹出框  -->
		    <div id="addAndEditPopWin" class="easyui-window" modal="true" collapsible="false" 
		    	 minimizable="false" maximizable="false" closed="true" resizable="false"
		    	 style="width:360px;padding:10px;" title="项目部门管理">
		        <form id="addEditForm" method="post">
		            <table cellpadding="5">
		            	<tr>
		                    <td>组织名称<font color="red">*</font>:</td>
		                    <td><input id="ud_name" class="easyui-textbox" type="text" name="name" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>组织编号<font color="red">*</font>:</td>
		                    <td><input id="ud_code" class="easyui-textbox" type="text" name="code" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>工作时长(小时)<font color="red">*</font>:</td>
		                    <td><input id="ud_workTimer" class="easyui-textbox" type="text" name="workTimer" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>备注:</td>
		                    <td><input id="ud_remark" class="easyui-textbox" name="remark" data-options="multiline:true" style="height:60px"></input></td>
		                </tr>
		                <tr>
		                    <td></td>
		                    <td align="right">
		                    	<a id="addEditSubmitBtn" href="javascript:void(0)" class="easyui-linkbutton">确定</a>
		                    	<a id="addEditCancleBtn" href="javascript:void(0)" class="easyui-linkbutton">取消</a>
		                    </td>
		                </tr>
		            </table>
		        </form>
		    </div>
	    </div>
	</div>   
</body>
</html>