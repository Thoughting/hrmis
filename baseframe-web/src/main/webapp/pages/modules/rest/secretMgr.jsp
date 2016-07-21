<%@page import="com.eastcom.baseframe.web.modules.rest.RestConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>密钥管理</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/rest/secretMgr.js"></script>
</head>
<body>
	<div class="easyui-layout" style="width:100%;height:500px;">
		<div data-options="region:'center'">
		    <table id="datagrid" class="easyui-datagrid" fit="true" singleSelect="true"
		            toolbar="#toolbar" border="false" fitColumns="true">
		        <thead>
		            <tr>
		            	<th field="code" width="50">APP_ID(ID)</th>
		                <th field="encryp" width="100">APP_SECRET(加密串)</th>
		                <th field="typeDict" width="50">密钥类型</th>
		                <th field="enableDict" width="30">启用状态</th>
		                <th field="resourceCount" width="50">接口资源数</th>
		                <th field="updateBy" width="50">最后修改人</th>
		                <th field="remark" width="50">描述</th>
		            </tr>
		        </thead>
		    </table>
		    <div id="toolbar">
		    	<shiro:hasPermission name="sys:restsecretmgr:add">
		    		<a id="addBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
		    	</shiro:hasPermission>
		    	<shiro:hasPermission name="sys:restsecretmgr:edit">
		    		<a id="editBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true">编辑</a>
		    	</shiro:hasPermission>
		    	<shiro:hasPermission name="sys:restsecretmgr:reset">
		    	 	<a id="resetBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">重置</a>
		    	</shiro:hasPermission>
		    	<shiro:hasPermission name="sys:restsecretmgr:auth">
		    	 	<a id="addAuthBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">授权</a>
		    	</shiro:hasPermission>
		    	<shiro:hasPermission name="sys:restsecretmgr:del">
	    	 		<a id="delBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
		    	</shiro:hasPermission>
		    </div>
		    <!-- 新增与编辑弹出框  -->
		    <div id="addAndEditPopWin" class="easyui-window" modal="true" collapsible="false" 
		    	 minimizable="false" maximizable="false" closed="true" resizable="false"
		    	 style="width:320px;padding:10px;" title="密钥">
		        <form id="addEditForm" method="post">
		            <table cellpadding="5">
		                <tr>
		                    <td>APP_ID<font color="red">*</font>:</td>
		                    <td><input id="ud_code" class="easyui-textbox" type="text" name="code" data-options="required:true"></input></td>
		                </tr>
		                 <tr>
		                    <td>密钥类型<font color="red">*</font>:</td>
		                    <td>
		                    	<sys:dictselect id="ud_type" name="type" dictType="<%=RestConstant.DICT_REST_SECRET_TYPE %>" needNull="false"/>
		                    </td>
		                </tr>
		                <tr>
		                    <td>启用状态<font color="red">*</font>:</td>
		                    <td>
			                    <select id="ud_enable" name="enable" style='height:22px;'>
									<option value='1'>启用</option>
									<option value='0'>禁用</option>
								</select>
							</td>
		                </tr>
		                <tr>
		                    <td>描述:</td>
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
		    
		    <!-- 授权树图弹出框  -->
		    <div id="authTreePopWin" class="easyui-window" title="授权" modal="true" collapsible="false" 
		    	 minimizable="false" maximizable="false" closed="true" resizable="false"
		    	 style="width:320px;height:300px;">
		    	 <div class="easyui-tabs" tools="#tabTools" fit="true" border="false">
			        <div title="资源" style="padding:10px">
			        	<ul id="authResourceTree" class="easyui-tree" cascadeCheck="false" animate="true" checkbox="true"></ul>
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