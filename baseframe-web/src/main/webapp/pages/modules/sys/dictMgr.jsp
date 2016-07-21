<%@page import="com.eastcom.baseframe.web.modules.sys.SysConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>字典管理</title>
<meta name="decorator" content="default" />

<!-- 加入easyui tree grid 拖拽扩展 -->
<script src="${ctxPlugins}/jquery-easyui/extended/treegrid-dnd.js"></script>
<script src="${ctxScripts}/modules/sys/dictMgr.js"></script>
</head>
<body>   
	<div class="easyui-layout" style="width:100%;height:650px;">
		<div data-options="region:'center'">
		    <table id="datagrid" class="easyui-treegrid" fit="true" idField="id" treeField="name"
		    	   toolbar="#toolbar" border="false" fitColumns="true" pageSize="20">
		        <thead>
		            <tr>
		            	<th field="name" width="50">字典名称</th>
		            	<th field="code" width="50">字典编码</th>
		                <th field="type" width="50">字典类型</th>
		                <th field="description" width="50">字典描述</th>
		            </tr>
		        </thead>
		    </table>
		    <div id="toolbar">
		    	<shiro:hasPermission name="sys:dictmgr:add">
		        	<a id="addChildrenBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增子节点</a>
		        	<a id="addBrotherBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true">新增兄弟节点</a>
		        	<a id="editBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">编辑</a>
	        	</shiro:hasPermission>
	        	<shiro:hasPermission name="sys:dictmgr:del">
	        		<a id="delBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
	        	</shiro:hasPermission>
	        	<shiro:hasPermission name="sys:dictmgr:sort">
	        		<a id="saveSort" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" disabled="true">保存排列顺序</a>
	        		<a id="cancelSort" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" disabled="true">取消更改</a>
		        </shiro:hasPermission>
		        <div style="float: right;display: none;">
		        	<input id="searchText" class="easyui-textbox" type="text" prompt="请输入..."></input>
	        		<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
		        </div>
		    </div>
		    
		    <!-- 新增与编辑弹出框  -->
		    <div id="addAndEditPopWin" class="easyui-window" modal="true" collapsible="false" 
		    	 minimizable="false" maximizable="false" closed="true" resizable="false"
		    	 style="width:300px;padding:10px;" title="字典管理">
		        <form id="addEditForm" method="post">
		            <table cellpadding="5">
		                <tr>
		                    <td>字典编码<font color="red">*</font>:</td>
		                    <td><input id="ud_code" class="easyui-textbox" type="text" name="code" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>字典名称<font color="red">*</font>:</td>
		                    <td><input id="ud_name" class="easyui-textbox" type="text" name="name" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>字典类型:</td>
		                    <td><input id="ud_type" class="easyui-textbox" type="text" name="type"></input></td>
		                </tr>
		                <tr>
		                    <td>字典描述:</td>
		                    <td><input id="ud_description" class="easyui-textbox" name="description" data-options="multiline:true" style="height:60px"></input></td>
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