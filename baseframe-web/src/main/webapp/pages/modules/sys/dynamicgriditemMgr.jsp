<%@page import="com.eastcom.baseframe.web.modules.sys.SysConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>表格项管理</title>
<meta name="decorator" content="default" />

<!-- 加入easyui tree grid 拖拽扩展 -->
<script src="${ctxPlugins}/jquery-easyui/extended/treegrid-dnd.js"></script>
<script src="${ctxScripts}/modules/sys/dynamicgriditemMgr.js"></script>
</head>
<body>   
	<div class="easyui-layout" style="width:100%;height:500px;">
		<div data-options="region:'center'">
		    <table id="datagrid" class="easyui-treegrid" fit="true" idField="id" treeField="name"
		    	   toolbar="#toolbar" border="false" fitColumns="true">
		        <thead>
		            <tr>
		            	<th field="name" width="50">字段名</th>
		            	<th field="title" width="50">显示头</th>
		                <th field="width" width="50">头宽度</th>
		                <th field="align" width="50">对齐方式</th>
		                <th field="formatter" width="50">格式化函数</th>
		                <th field="remarks" width="50">描述</th>
		            </tr>
		        </thead>
		    </table>
		    <div id="toolbar">
		    	所属表格：
	        	<select id="dynamicgrid" name="dynamicgrid" style='height:22px;'>
					<option value=''>--请选择--</option>
				</select>
		    	<%-- <shiro:hasPermission name="sys:dynamicgriditemmgr:add"> --%>
		    		<a id="addChildrenBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增子节点</a>
		        	<a id="addBrotherBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true">新增兄弟节点</a>
		        	<a id="editBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">编辑</a>
		    	<%-- </shiro:hasPermission> --%>
		    	<%-- <shiro:hasPermission name="sys:dynamicgriditemmgr:del"> --%>
		        	<a id="delBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
		        <%-- </shiro:hasPermission> --%>
		        <%-- <shiro:hasPermission name="sys:dynamicgriditemmgr:sort"> --%>
		        	<a id="saveSort" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" disabled="true">保存排列顺序</a>
		        	<a id="cancelSort" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" disabled="true">取消更改</a>
		        <%-- </shiro:hasPermission> --%>
    		</div>
		    
		    <!-- 新增与编辑弹出框  -->
		    <div id="addAndEditPopWin" class="easyui-window" modal="true" collapsible="false" 
		    	 minimizable="false" maximizable="false" closed="true" resizable="false"
		    	 style="width:300px;padding:10px;" title="表格项管理">
		        <form id="addEditForm" method="post">
		            <table cellpadding="5">
		                <tr>
		                    <td>字段名<font color="red">*</font>:</td>
		                    <td><input id="ud_field" class="easyui-textbox" type="text" name="field" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>显示头<font color="red">*</font>:</td>
		                    <td><input id="ud_title" class="easyui-textbox" type="text" name="title" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>头宽度<font color="red">*</font>:</td>
		                    <td><input id="ud_width" class="easyui-textbox" type="text" name="width" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>对齐方式<font color="red">*</font>:</td>
		                    <td><input id="ud_align" class="easyui-textbox" type="text" name="align" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>格式函数:</td>
		                    <td><input id="ud_formatter" class="easyui-textbox" type="text" name="formatter"></input></td>
		                </tr>
		                <tr>
		                    <td>区域描述:</td>
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