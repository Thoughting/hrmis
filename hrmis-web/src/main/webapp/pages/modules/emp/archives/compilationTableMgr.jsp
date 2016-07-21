<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编制表分类管理</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/emp/archives/compilationTableMgr.js"></script>
</head>
<body>
	<div class="easyui-layout" style="width:100%;height:650px;">
		<div data-options="region:'center'">
		    <table id="datagrid" class="easyui-datagrid" fit="true" singleSelect="true"
		            toolbar="#toolbar" border="false" fitColumns="true">
		        <thead>
		            <tr>
		            	<th field="name" width="50">分类名称</th>
		                <th field="deptsDescribe" width="50">部门项目</th>
		                <th field="postsDescribe" width="50">岗位</th>
		                <th field="remark" width="50">备注</th>
		            </tr>
		        </thead>
		    </table>
		    <div id="toolbar">
	    		<shiro:hasPermission name="emp:compilationtablemgr:add">
	    			<a id="addBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
	    		</shiro:hasPermission>
	    		<shiro:hasPermission name="emp:compilationtablemgr:edit">
	    			<a id="editBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true">编辑</a>
	    		</shiro:hasPermission>
	    		<shiro:hasPermission name="emp:compilationtablemgr:auth">
	    		 	<a id="addSettingBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">配置关联</a>
	    		</shiro:hasPermission>
	    		<shiro:hasPermission name="emp:compilationtablemgr:del">
	    		 	<a id="delBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
	    		</shiro:hasPermission>
	    	 </div>
		    <!-- 新增与编辑弹出框  -->
		    <div id="addAndEditPopWin" class="easyui-window" modal="true" collapsible="false" 
		    	 minimizable="false" maximizable="false" closed="true" resizable="false"
		    	 style="width:320px;padding:10px;" title="编制表分类管理">
		        <form id="addEditForm" method="post">
		            <table cellpadding="5">
		                <tr>
		                    <td>分类名称<font color="red">*</font>:</td>
		                    <td><input id="ud_name" class="easyui-textbox" type="text" name="name" data-options="required:true"></input></td>
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
		    
		    <!-- 配置树图弹出框  -->
		    <div id="setTreePopWin" class="easyui-window" title="配置关联" modal="true" collapsible="false" 
		    	 minimizable="false" maximizable="false" closed="true" resizable="false"
		    	 style="width:320px;height:300px;">
		    	 <div class="easyui-tabs" tools="#tabTools" fit="true" border="false">
			        <div title="部门项目" style="padding:10px">
			        	<ul id="setDeptTree" class="easyui-tree" cascadeCheck="false" animate="true" checkbox="true"></ul>
			        </div>
			        <div title="岗位" style="padding:10px">
			        	<ul id="setPostTree" class="easyui-tree" cascadeCheck="false" animate="true" checkbox="true"></ul>
			        </div>
			    </div>
			    <div id="tabTools" border="false">
			    	<a id="confirmSetBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true">应用</a>
			    </div>
		    </div>
		    
	    </div>
	</div>   
</body>
</html>