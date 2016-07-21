// 用户管理
$.namespace('sys.log.loginLogMgr');

//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/sys/log/list_operation';
	}
}

//界面组件
Component = {
	init : function(){
		Component.$grid = $("#datagrid");
		Component.$grid.datagrid({
			url : Constant.$grid_list_url,
			pageSize : 50,
			onBeforeLoad : function(param){
				param.loginName = $("#loginName").val();
				param.startTime = $("#startTime").val();
				param.endTime = $("#endTime").val();
			}
		});
	}
}

//界面交互
Interactive = {
	init : function(){
		//回车查询
		$("body").keydown(function(event) {
			var c = event.keyCode;
			if(c == 13){
				$("#searchBtn").click();
			}
		});
		
		//查询
		$("#searchBtn").click(function(){
			Component.$grid.datagrid("reload");
		});
	}
}

$(function() {
	Constant.init();
	Component.init();
	Interactive.init();
});