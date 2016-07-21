<%@page import="com.eastcom.baseframe.web.modules.sys.entity.Resource"%>
<%@page import="com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache"%>
<%@page import="com.eastcom.baseframe.web.modules.sys.entity.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<%
request.setAttribute("logonUser", SecurityCache.getLoginUser());

//一级菜单
List<Resource> menus = SecurityCache.getAllAuthResourceList();
request.setAttribute("menus", menus);
request.setAttribute("menusCount", menus.size());

//当前位置
String href = request.getRequestURL().toString().split(request.getContextPath())[1];
request.setAttribute("menuNamePath", SecurityCache.getMenuNamePath(href));

%>
<link rel="stylesheet" href="${ctxPlugins}/zTree_v3/3.5.18/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${ctxPlugins}/zTree_v3/3.5.18/js/jquery.ztree.core-3.5.js"></script>

<script type="text/javascript">
	var treeSetting = {
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onClick: function(event, treeId, treeNode, clickFlag){
				if(treeNode.href != null){
					window.open(basePath + treeNode.href,"_self");
				}
			}
		}
	};
	$(function() {
		var menus = $("#menuUL > li");
		$("#menuUL > li").each(function() {
			$(this).bind("mouseenter", function() {
				$(this).addClass("n1");
				$(this).removeClass("list");
			})
			$(this).bind("mouseleave", function() {
				$(this).addClass("list");
				$(this).removeClass("n1");
			})
		})
		
		$("#logoutBtn").click(function(){
			$.messager.confirm('提示', '确定注销吗？', function(r){
				if (r){
					window.open('${ctx }/logout','_self');
				}
			});
		});
		
		//密码修改
		$('#passwordEditForm').form({
			url : '${ctx }/api/sys/user/updatePassword',
			onSubmit : function(){
				var isValid = $(this).form('validate');
				if (!isValid){
					$.messager.progress('close');
				}
				if($("#ud_newPassword").val() != $("#ud_newPassword2").val()){
					$.messager.alert("提示", "2次输入新密码不一样!请重新输入");
					$.messager.progress('close');
					return false;
				}
				return isValid;
			},
			success: function(data){
				$.messager.progress('close');
				var data = eval('(' + data + ')');
				if (data.success) {
					$.messager.alert("提示", "操作成功");
					$("#passwordEditPopWin").window('close');
				} else {
					$.messager.alert('提示','操作失败!');
				}
			}
		});
		$("#editPasswordBtn").click(function(){
			$('#passwordEditForm').form("reset");
			$("#passwordEditPopWin").window('open');
			$("#passwordEditPopWin").window('center');
		});
		$("#passwordSubmitBtn").on("click",function(){
			$.messager.progress({msg:'请稍后...'});
			$('#passwordEditForm').form('submit');
		});
		$("#passwordCancleBtn").on("click",function(){
			$("#passwordEditPopWin").window('close');
		})
		
	})
</script>
<div id="header">
	<div class="top">
		<div class="logo"></div>
		<div class="emenu">
			<ul id="menuUL" class="level">
				<c:if test="${not empty menus}">
					<c:forEach items="${menus}" var="menu" varStatus="menuStatus">
						<shiro:hasPermission name="${menu.permission }">
							<li class="eli">
								<c:choose>
									<c:when test="${menu.href != ''}"><a href="${ctx }${menu.href}"></c:when>
									<c:otherwise><a href="#"></c:otherwise>
								</c:choose>
								<span class="espan use${menuStatus.count }"></span><br />${menu.name }</a>
								<c:if test="${not empty menu.children}">
									<ul class="leve2" style="width: 256px;margin-top:6px;margin-left:${(menuStatus.count - menusCount) * 72 + 206 gt 0 ? -((menuStatus.count - menusCount) * 72 + 206) : -10}px;">
										<sys:navTree id="navTree${menuStatus.count }" children="${menu.children }"/>
										<script type="text/javascript">
										$(function() {
											$("#navTree${menuStatus.count }").tree({
												onClick: function(node){
													window.open(basePath + node.attributes.url,"_self");
												}
											});
											$("#navTree${menuStatus.count } li div").each(function() {
												$(this).css("background","none");
											});
										});
										</script>
									</ul>
								</c:if>
							</li>
						</shiro:hasPermission>
					</c:forEach>
				</c:if>
				<!-- <li class="eli"><a href="#"><span class="espan use1"></span><br />数据查询</a>
					<ul class="leve2" style="width: 256px;">
						<ul id="treeDemo" class="ztree"></ul>
					</ul>
				</li>
				<li class="eli"><a href="#"><span class="espan use2"></span><br />数据下载</a>
					<ul class="leve2" style="width: 256px; margin-left: -62px;">
						<a href="#">关键字11</a>
					</ul></li>
				<li class="eli"><a href="#"><span class="espan use3"></span><br />统计分析</a>
					<ul class="leve2" style="width: 256px; margin-left: -134px;">
						<a href="#">关键字22</a>
					</ul></li>
				<li class="eli"><a href="#"><span class="espan use4"></span><br />系统管理</a>
					<ul class="leve2" style="width: 256px; margin-left: -206px;">
						<a href="#">关键字33</a>
					</ul>
				</li> -->
			</ul>
		</div>
		<div class="short_menu">
			<font color="white">当前位置: ${menuNamePath }</font>
			<div style="float: right;">
				<span class="use">
					<div style="float:left;width:150px; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">
          				欢迎您：${logonUser.name }
          			</div>
          			<a id="editPasswordBtn" href="javascript:void(0)">修改密码</a> <a id="logoutBtn" href="javascript:void(0)">注销</a> 
          		</span>
			</div>
		</div>
		<!-- 修改密码 -->
		<div id="passwordEditPopWin" class="easyui-window" modal="true" collapsible="false" 
			 minimizable="false" maximizable="false" closed="true" resizable="false" title="修改密码"
			 style="width:330px;padding:10px;">
		    <form id="passwordEditForm" method="post">
		        <table cellpadding="5">
		            <tr>
		                <td>旧密码<font color="red">*</font>:</td>
		                <td><input id="ud_oldPassword" class="easyui-textbox" type="password" name="oldPassword" data-options="required:true"></input></td>
		            </tr>
		            <tr>
		                <td>新密码<font color="red">*</font>:</td>
		                <td><input id="ud_newPassword" class="easyui-textbox" type="password" name="newPassword" data-options="required:true"></input></td>
		            </tr>
		            <tr>
		                <td>再输一次新密码:</td>
		                <td><input id="ud_newPassword2" class="easyui-textbox" type="password" name="newPassword2" data-options="required:true"></input></td>
		            </tr>
		            <tr>
		                <td></td>
		                <td align="right">
		                	<a id="passwordSubmitBtn" href="javascript:void(0)" class="easyui-linkbutton">确定</a>
		                	<a id="passwordCancleBtn" href="javascript:void(0)" class="easyui-linkbutton">取消</a>
		                </td>
		            </tr>
		        </table>
		    </form>
		</div>
	</div>
</div>