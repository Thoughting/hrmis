<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%
	String url = request.getParameter("url");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>文件上传</title>
    <meta name="decorator" content="none" />
    <%@include file="/pages/include/head.jsp"%>
    
    <link rel="stylesheet" href="${ctxPlugins}/plupload/queue/css/jquery.plupload.queue.css" type="text/css"></link>
	<script type="text/javascript" src="${ctxPlugins}/plupload/plupload.js"></script>
	<script type="text/javascript" src="${ctxPlugins}/plupload/plupload.html4.js"></script>
	<script type="text/javascript" src="${ctxPlugins}/plupload/plupload.html5.js"></script>
	<script type="text/javascript" src="${ctxPlugins}/plupload/plupload.flash.js"></script>
	<script type="text/javascript" src="${ctxPlugins}/plupload/zh_CN.js"></script>
	<script type="text/javascript" src="${ctxPlugins}/plupload/queue/jquery.plupload.queue.js"></script>
	
	<script type="text/javascript">
 		//解决swf关闭刷新时_flash_removeChart异常错误
		function removeChart(){
    	    try{
    	       $("#uploader").empty();
    	    }catch(e){
    	    }
		}
	</script>
  <body style="padding: 0;margin: 0;" onunload="removeChart()">
    <div id="uploader">&nbsp;</div>
	<script type="text/javascript" charset="utf-8">
		var url = "<%=url%>";
		var files = [];
		var type = "file";
		var chunk = false;
		
		var max_file_size = "5mb";
		var filters = {title : "文档", extensions : "zip,doc,docx,xls,xlsx,ppt,pptx,txt,bin"};
		var result = [];	    
		$("#uploader").pluploadQueue($.extend({
			runtimes : "flash,html4,html5",
			url : url,
			headers:{Accept : "application/json; charset=utf-8"},
			max_file_size : max_file_size,
			file_data_name:"file",
			unique_names:true,
			filters : [],
			flash_swf_url : "${ctxPlugins}/plupload/plupload.flash.swf",
			init:{
				UploadFile : function(up,file){
					$.messager.progress("close");
					$.messager.progress({
						title: "请稍后",
						msg: "文件【"+file.name+"】上传中..."
					});
				},
				FileUploaded : function(uploader,file,res){
					if(res.response){
						result = $.parseJSON(res.response);
						files.push(file.name + "--" + decodeURI(result.message));
					}
				},
				UploadComplete : function(uploader,fs,res){
					$.messager.progress("close");
					var msg = "";
					if(files != null && files.length > 0){
						for(var i = 0;i < files.length;i++){
							msg += files[i] + "<br />";
						}
					}
					$.messager.alert('提示',msg);
				}
			}
		},(chunk ? {chunk_size:"1mb"} : {})));
	</script>
  </body>
</html>
