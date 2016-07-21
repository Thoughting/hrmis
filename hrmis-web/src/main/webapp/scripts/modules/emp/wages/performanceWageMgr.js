//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/emp/performancewage/list';
		Constant.$addEdit_form_url = basePath + '/api/emp/performancewageitem/addOrUpdate';
		Constant.$export_url = basePath + "/api/emp/performancewage/export";
	}
}

//界面组件
Component = {
	init : function(){
		
		Component.$grid1 = $("#datagrid1");
		Component.$grid1.datagrid({
			url : Constant.$grid_list_url,
			onBeforeLoad : function(param){
				param.type = 1;
				param.wageDateStr = $("#wageDate").val();
			}
		});
		
		Component.$grid2 = $("#datagrid2");
		Component.$grid2.datagrid({
			url : Constant.$grid_list_url,
			onBeforeLoad : function(param){
				param.type = 2;
				param.wageDateStr = $("#wageDate").val();
			}
		});
	}
}

//界面交互
Interactive = {
	init : function(){
		
	},
	exportBtnClickHandler : function(type){
		Common.openPostWindow2(Constant.$export_url,{
			type : type,
			wageDateStr : $("#wageDate").val()
		});
	},
	reloadData : function(){
		var index = $("#mainTab").tabs('getTabIndex',$('#mainTab').tabs('getSelected'));
		$("#datagrid" + (index + 1)).datagrid('reload');
	},
	editFormatter : function(value,row,index){
		var performanceWageId = row.performanceWageId;
		var showField = value.field;
		var showValue = 0;
		var performanceWageItemId = null;
		if (value.model != null) {
			showValue = value.model.value;
			performanceWageItemId = value.model.id;
		}
		if (hasGridEdit) {
			return '<input type="text" style="width: 60px;text-align:center;border: none;IME-MODE: disabled;" maxlength="6" onchange="Interactive.inputChangeHandler(\'' + performanceWageItemId +'\',\'' + performanceWageId + '\',\'' + showField + '\',this.value)" value="' + showValue + '"/>';
		} else {
			return showValue;
		}
	},
	inputChangeHandler : function(performanceWageItemId,performanceWageId,showField,value){
		$.ajax({
			url : Constant.$addEdit_form_url,
			type : 'post',
			dataType : 'json',
			data : {
				id : performanceWageItemId,
				performanceWageId : performanceWageId,
				field : showField,
				value : value
			},
			success : function(data) {
				if (data.success) {
					var index = $("#mainTab").tabs('getTabIndex',$('#mainTab').tabs('getSelected'));
					$("#datagrid" + (index + 1)).datagrid('reload');
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			}
		});
	}
}

$(function() {
	Constant.init();
	Component.init();
	Interactive.init();
});