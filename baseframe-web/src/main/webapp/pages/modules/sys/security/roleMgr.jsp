<%@page import="com.eastcom.baseframe.web.modules.sys.SysConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>角色管理</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/sys/security/roleMgr.js"></script>
</head>
<body>
	<div class="easyui-layout" style="width:100%;height:500px;">
		<div data-options="region:'center'">
		    <table id="datagrid" class="easyui-datagrid" fit="true" singleSelect="true"
		            toolbar="#toolbar" border="false" fitColumns="true">
		        <thead>
		            <tr>
		            	<th field="name" width="50">角色名称</th>
		                <th field="nameCn" width="50">角色中文名称</th>
		                <th field="levelDict" width="50">角色级别</th>
		                <th field="userCount" width="50">用户数</th>
		                <th field="resourceCount" width="50">资源数</th>
		                <th field="areaCount" width="50">区域数</th>
		                <th field="createDate" width="50">创建时间</th>
		                <th field="remarks" width="50">角色描述</th>
		            </tr>
		        </thead>
		    </table>
		    <div id="toolbar">
		    	<shiro:hasPermission name="sys:rolemgr:add">
		    		<a id="addBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
		    	</shiro:hasPermission>
		    	<shiro:hasPermission name="sys:rolemgr:edit">
		    		<a id="editBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true">编辑</a>
		    	</shiro:hasPermission>
		    	<shiro:hasPermission name="sys:rolemgr:auth">
		        	<a id="addAuthBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">授权</a>
		        </shiro:hasPermission>
		        <shiro:hasPermission name="sys:rolemgr:del">
		        	<a id="delBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
		        </shiro:hasPermission>
		        <div style="float: right;display: none;">
		        	<input id="searchText" class="easyui-textbox" type="text" prompt="请输入..."></input>
	        		<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
		        </div>
		    </div>
		    <!-- 新增与编辑弹出框  -->
		    <div id="addAndEditPopWin" class="easyui-window" modal="true" collapsible="false" 
		    	 minimizable="false" maximizable="false" closed="true" resizable="false"
		    	 style="width:320px;padding:10px;" title="角色管理">
		        <form id="addEditForm" method="post">
		            <table cellpadding="5">
		                <tr>
		                    <td>角色名称<font color="red">*</font>:</td>
		                    <td><input id="ud_name" class="easyui-textbox" type="text" name="name" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>角色中文名<font color="red">*</font>:</td>
		                    <td><input id="ud_nameCn" class="easyui-textbox" type="text" name="nameCn" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>角色级别<font color="red">*</font>:</td>
		                    <td>
		                    	<sys:dictselect id="ud_level" name="level" dictType="<%=SysConstant.DICT_SYS_ROLE_LEVEL %>" needNull="false"/>
							</td>
		                </tr>
		                <tr>
		                    <td>角色描述:</td>
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
		    
		    <!-- 授权树图弹出框  -->
		    <div id="authTreePopWin" class="easyui-window" title="授权" modal="true" collapsible="false" 
		    	 minimizable="false" maximizable="false" closed="true" resizable="false"
		    	 style="width:320px;height:300px;">
		    	 <div class="easyui-tabs" tools="#tabTools" fit="true" border="false">
			        <div title="资源" style="padding:10px">
			        	<ul id="authResourceTree" class="easyui-tree" cascadeCheck="false" animate="true" checkbox="true"></ul>
			        </div>
			        <div title="区域" style="padding:10px">
			        	<ul id="authAreaTree" class="easyui-tree" cascadeCheck="false" animate="true" checkbox="true"></ul>
			        </div>
			    </div>
			    <div id="tabTools" border="false">
			    	<a id="confirmAuthBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true">应用</a>
			    </div>
		    </div>
		    
	    </div>
	</div>   
</body>
</html>