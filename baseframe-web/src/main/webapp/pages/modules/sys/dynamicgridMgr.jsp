<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>动态表格管理</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/sys/dynamicgridMgr.js"></script>
</head>
<body>
	<div class="easyui-layout" style="width:100%;height:500px;">
		<div data-options="region:'center'">
		    <table id="datagrid" class="easyui-datagrid" fit="true" singleSelect="true"
		            toolbar="#toolbar" border="false" fitColumns="true">
		        <thead>
		            <tr>
		            	<th field="name" width="50">表格名称</th>
		                <th field="code" width="50">表格编号</th>
		                <th field="headDepth" width="50">标题头深度</th>
		                <th field="remarks" width="50">备注</th>
		            </tr>
		        </thead>
		    </table>
		    <div id="toolbar">
	    	<div id="toolbar">
		    	<shiro:hasPermission name="sys:dynamicgridmgr:add">
		    		<a id="addBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
		    	</shiro:hasPermission>
		    	<shiro:hasPermission name="sys:dynamicgridmgr:edit">
		        	<a id="editBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">编辑</a>
		        </shiro:hasPermission>
		        <shiro:hasPermission name="sys:dynamicgridmgr:del">
		        	<a id="delBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
		        </shiro:hasPermission>
		        <div style="float: right;">
		        	<input id="searchText" class="easyui-textbox" type="text" prompt="请输入..."></input>
	        		<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
		        </div>
		    </div>
		    <!-- 新增与编辑弹出框  -->
		    <div id="addAndEditPopWin" class="easyui-window" modal="true" collapsible="false" 
		    	 minimizable="false" maximizable="false" closed="true" resizable="false"
		    	 style="width:320px;padding:10px;" title="表格管理">
		        <form id="addEditForm" method="post">
		            <table cellpadding="5">
		            	<tr>
		                    <td>表格编号<font color="red">*</font>:</td>
		                    <td><input id="ud_code" class="easyui-textbox" type="text" name="code" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>表格名称<font color="red">*</font>:</td>
		                    <td><input id="ud_name" class="easyui-textbox" type="text" name="name" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>标题头深度<font color="red">*</font>:</td>
		                    <td><input id="ud_headDepth" class="easyui-textbox" type="text" name="headDepth" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>备注:</td>
		                    <td><input id="ud_remarks" class="easyui-textbox" name="remarks" data-options="multiline:true" style="height:60px"></input></td>
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