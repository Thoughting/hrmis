<%@page import="com.eastcom.hrmis.modules.emp.EmpConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>职务岗位管理</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/emp/archives/employeePostMgr.js"></script>
</head>
<body>
	<div class="easyui-layout" style="width:100%;height:650px;">
		<div data-options="region:'center'">
		    <table id="datagrid" class="easyui-datagrid" fit="true" singleSelect="true" pageSize="20"
		            toolbar="#toolbar" border="false" fitColumns="true" pagination="true">
		        <thead>
		            <tr>
		            	<th field="typeDict" width="50">职务类型</th>
		            	<th field="name" width="50">岗位名称</th>
		            	<th field="code" width="50">岗位编号</th>
		                <th field="remark" width="50">备注</th>
		            </tr>
		        </thead>
		    </table>
		    <div id="toolbar">
				<table style="line-height:26px;padding: 10px;">
					<tr>
						<td>职务类型：</td>
						<td>
							<sys:dictselect id="postType" name="postType" dictType="<%=EmpConstant.DICT_EMPLOYEE_POST_TYPE %>" needNull="true" attrHtml="style='height:22px;'"/>
						</td>
						<td>岗位编号：</td>
						<td><input id="code" name="code" type="text" style="width: 120px;"/></td>
						<td>岗位名称：</td>
						<td><input id="name" name="name" type="text" style="width: 120px;"/></td>
						<td>
							<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							<shiro:hasPermission name="emp:postmgr:add">
								<a id="addBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add">新增</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="emp:postmgr:edit">
								<a id="editBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">编辑</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="emp:postmgr:del">
								<a id="delBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove">删除</a>
							</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</div>
		    <!-- 新增与编辑弹出框  -->
		    <div id="addAndEditPopWin" class="easyui-window" modal="true" collapsible="false" 
		    	 minimizable="false" maximizable="false" closed="true" resizable="false"
		    	 style="width:320px;padding:10px;" title="岗位管理">
		        <form id="addEditForm" method="post">
		            <table cellpadding="5">
		            	<tr>
		                    <td>职务类型<font color="red">*</font>:</td>
		                    <td><sys:dictselect id="ud_postType" name="postType" dictType="<%=EmpConstant.DICT_EMPLOYEE_POST_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/></td>
		                </tr>
		                <tr>
		                    <td>岗位编号<font color="red">*</font>:</td>
		                    <td><input id="ud_code" class="easyui-textbox" type="text" name="code" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>岗位名称<font color="red">*</font>:</td>
		                    <td><input id="ud_name" class="easyui-textbox" type="text" name="name" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>备注:</td>
		                    <td><input id="ud_remark" class="easyui-textbox" name="description" data-options="multiline:true" style="height:60px"></input></td>
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