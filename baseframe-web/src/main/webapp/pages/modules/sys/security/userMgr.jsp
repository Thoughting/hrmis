<%@page import="com.eastcom.baseframe.web.modules.sys.SysConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>用户管理</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/sys/security/userMgr.js"></script>
</head>
<body>
	<div class="easyui-layout" style="width:100%;height:500px;">
		<div data-options="region:'west',title:'分组',split:true" style="width:250px;">
	    	<div class="easyui-tabs" fit="true" border="false">
		        <div title="部门">
		        	<ul id="officeTree" class="easyui-tree" animate="true"></ul>
		        </div>
		    </div>
	    </div>   
	    <div data-options="region:'center',title:'全部'">
		    <table id="datagrid" class="easyui-datagrid" fit="true"
		            toolbar="#toolbar" pagination="true" border="false" fitColumns="true" singleSelect="true">
		        <thead>
		            <tr>
		                <th field="loginName" width="50">用户名</th>
		                <th field="code" width="50">工号</th>
		                <th field="name" width="50">中文名</th>
		                <th field="email" width="50">邮箱</th>
		                <th field="mobile" width="50">手机</th>
		                <th field="userTypeDict" width="50">用户类型</th>
		                <th field="loginIp" width="50">最后登录IP</th>
		                <th field="loginDate" width="50">最后登录日期</th>
		            </tr>
		        </thead>
		    </table>
		    <div id="toolbar">
		    	<shiro:hasPermission name="sys:usermgr:add">
		    		<a id="addBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
		    	</shiro:hasPermission>
		    	<shiro:hasPermission name="sys:usermgr:edit">
		        	<a id="editBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">编辑</a>
		        </shiro:hasPermission>
		        <shiro:hasPermission name="sys:usermgr:auth">
		        	<a id="authBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">授权</a>
		        </shiro:hasPermission>
		        <shiro:hasPermission name="sys:usermgr:del">
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
		    	 style="width:300px;padding:10px;" title="用户管理">
		        <form id="addEditForm" method="post">
		            <table cellpadding="5">
		                <tr>
		                    <td>用户名<font color="red">*</font>:</td>
		                    <td><input id="ud_loginName" class="easyui-textbox" type="text" name="loginName" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>密码<font color="red">*</font>:</td>
		                    <td><input id="ud_password" class="easyui-textbox" type="text" name="password" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>中文名:</td>
		                    <td><input id="ud_name" class="easyui-textbox" name="name"></input></td>
		                </tr>
		                <tr>
		                    <td>工号:</td>
		                    <td><input id="ud_code" class="easyui-textbox" name="code"></input></td>
		                </tr>
		                <tr>
		                    <td>邮箱:</td>
		                    <td><input id="ud_email" class="easyui-textbox" name="email"></input></td>
		                </tr>
		                <tr>
		                    <td>手机:</td>
		                    <td><input id="ud_mobile" class="easyui-textbox" name="mobile"></input></td>
		                </tr>
		                <tr>
		                    <td>用户类型:</td>
		                    <td>
		                    	<sys:dictselect id="ud_userType" name="userType" dictType="<%=SysConstant.DICT_SYS_USER_TYPE %>" needNull="false"/>
		                    </td>
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
			        <div title="角色">
			        	<table id="authRoleDataGrid" class="easyui-datagrid" fit="true" singleSelect="false"
			        		   border="false" fitColumns="true" checkOnSelect="true">
					        <thead>
					            <tr>
					            	<th field="ck" checkbox="true"></th>
					                <th field="nameCn" width="50">角色名称</th>
					                <th field="remarks" width="50">角色描述</th>
					            </tr>
					        </thead>
					    </table>
			        </div>
			        <div title="部门">
			        	<ul id="authOfficeTree" class="easyui-tree" cascadeCheck="false" animate="true" checkbox="true"></ul>
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