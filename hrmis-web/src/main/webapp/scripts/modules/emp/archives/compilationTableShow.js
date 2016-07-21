// 用户管理
$.namespace('emp.archives.compilationTableShow');

//界面变量
Constant = {
	init : function(){
		Constant.$compilationcount_addOrUpdate_url = basePath + "/api/emp/compilationcount/addOrUpdate";
		Constant.$export_url = basePath + "/api/emp/compilationcount/export";
		Constant.$total_url = basePath + "/api/emp/compilationcount/total";
	}
}

//界面组件
Component = {
	init : function(){
		for(var i = 1;i <= gridCount;i++){
			$("#datagrid_" + i).datagrid({
				onLoadSuccess : function(data){
					if (data.rows != null && data.rows.length > 0) {
						//合并纵向单元格
						var index = 0;
						var rowspan = 1;
						var leaderName = "";
						for(var j = 0;j < data.rows.length;j++){
							if (j == 0) {
								leaderName = data.rows[j].leaderName;
								continue;
							}
							if (leaderName != data.rows[j].leaderName) {
								leaderName = data.rows[j].leaderName;
								$(this).datagrid('mergeCells',{
				                    index: index,
				                    field: 'leaderName',
				                    rowspan: rowspan
				                });
								index = j;
								rowspan = 1;
							} else {
								rowspan++;
							}
						}
						$(this).datagrid('mergeCells',{
		                    index: index,
		                    field: 'leaderName',
		                    rowspan: rowspan
		                });
						//合并最后小计横向单元格
						$(this).datagrid('mergeCells',{
		                    index: data.rows.length - 1,
		                    field: 'leaderName',
		                    colspan: 2
		                });
					}
				}
			});
		}
	}
}

//界面交互
Interactive = {
	init : function(){
		Interactive.getTotal();
		$('#mainTab').tabs({
			onSelect : function(title,index){
				Interactive.getTotal();
			}
		});
	},
	exportBtnClickHandler : function(){
		var tab = $('#mainTab').tabs('getSelected');
		Common.openPostWindow2(Constant.$export_url,{
			id : $(tab).attr('id')
		});
	},
	getTotal : function(){
		var tab = $('#mainTab').tabs('getSelected');
		var id = $(tab).attr('id');
		$.ajax({
			url : Constant.$total_url,
			type : 'post',
			dataType : 'json',
			data : {
				id : id
			},
			success : function(data) {
				if (data.success) {
					var tab = $('#mainTab').tabs('getSelected');
					$("#" + $(tab).attr('id') + "_total").empty();
					$("#" + $(tab).attr('id') + "_total").append(data.model);
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			}
		});
	},
	dingFormatter : function(value,row,index){
		var compilationId = row.compilationId;
		var deptId = row.deptId;
		if (isNaN(value)) {
			var postId = value.postId;
			var showValue = 0;
			var comilationCountId = null;
			if (value.model != null) {
				showValue = value.model.count;
				comilationCountId = value.model.id;
			}
			if (hasGridEdit) {
				return '<div style="background-color:#CCFFCC"><input type="text" style="width: 35px;text-align:center;background-color:#CCFFCC;border: none;IME-MODE: disabled;" maxlength="4" onkeyup="Interactive.inputKeyupHandler(this)" onafterpaste="Interactive.inputAfterpaste(this)" onchange="Interactive.inputFocusout(\'' + comilationCountId +'\',\'' + compilationId + '\',\'' + deptId + '\',\'' + postId + '\',this.value)" value="' + showValue + '"/></div>';
			} else {
				return '<div style="background-color:#CCFFCC">' + showValue + '</div>';
			}
		} else {
			return '<div style="background-color:#CCFFCC">' + value + '</div>';
		}
	},
	queFormatter : function(value,row,index){
		return '<div style="background-color:#FFFF99">' + value + '</div>';
	},
	inputFocusout : function(comilationCountId,compilationId,deptId,postId,value){
		$.ajax({
			url : Constant.$compilationcount_addOrUpdate_url,
			type : 'post',
			dataType : 'json',
			data : {
				id : comilationCountId,
				compilationId : compilationId,
				deptId : deptId,
				postId : postId,
				count : value
			},
			success : function(data) {
				if (data.success) {
					var index = $("#mainTab").tabs('getTabIndex',$('#mainTab').tabs('getSelected'));
					$("#datagrid_" + (index + 1)).datagrid('reload');
					Interactive.getTotal();
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			}
		});
	},
	inputKeyupHandler : function(target){
		target.value=target.value.replace(/\D/g,'');
	},
	inputAfterpaste : function(target){
		target.value=target.value.replace(/\D/g,'');
	}
}

$(function() {
	Constant.init();
	Component.init();
	Interactive.init();
});