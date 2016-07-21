<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width, initial-scale=1">

<script>
var Page = {};
Page.basePath = "${ctx}";
var basePath = "${ctx}";
</script>

<!-- jquery -->
<script src="${ctxPlugins}/jquery-1.11.3.min.js"></script>
<script src="${ctxPlugins}/json2.js"></script>

<!-- jquery easyui -->
<link rel="stylesheet" href="${ctxPlugins}/jquery-easyui/1.4.3/themes/default/easyui.css">
<link rel="stylesheet" href="${ctxPlugins}/jquery-easyui/1.4.3/themes/icon.css">
<script src="${ctxPlugins}/jquery-easyui/1.4.3/jquery.easyui.min.js"></script>
<script src="${ctxPlugins}/jquery-easyui/1.4.3/locale/easyui-lang-zh_CN.js"></script>

<!-- My97DatePicker -->
<script src="${ctxPlugins}/My97DatePicker/4.72/WdatePicker.js"></script>

<!-- common js -->
<script src="${ctxScripts}/common/Common.js"></script>
<script src="${ctxScripts}/common/jquery-namespace.js"></script>

<!-- current -->
<link rel="stylesheet" type="text/css" href="${themePath }/css/css.css" />