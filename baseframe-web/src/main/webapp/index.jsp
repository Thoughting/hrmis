<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="default" />
<title>首页</title>
</head>

<body>
	<div class="con_itembox">
		<div style="height:100px">
			<div style="width:23%;height:80px;float:left;border:2px solid #ccc;margin-top:10px;margin-bottom:10px;margin-right:1%;">
        		<a href="#">
            		<div style="width:180px;text-align:center;font-size:20px;float:left;margin-top: 30px;">机构代码查询</div>
            		<img style="float:right;margin-top: 10px;margin-right:10px;"  src="${ctx}/static/themes/default/images/jgdmQuery.png"/>
        		</a>
   			</div>
			<div style="width:23%;height:80px;float:left;background-color:#fff;border:2px solid #ccc;margin-top:10px;margin-bottom:10px;margin-left:1%;margin-right:1%;">
        		<a href="#">
            		<div style="width:180px;text-align:center;font-size:20px;float:left;margin-top: 30px;">标准信息查询</div>
            		<img style="float:right;margin-top: 10px;margin-right:10px;"  src="${ctx}/static/themes/default/images/bzxxQuery.png"/>
        		</a>
   			</div>
			<div style="width:23%;height:80px;float:left;background-color:#fff;border:2px solid #ccc;margin-top:10px;margin-bottom:10px;margin-left:1%;margin-right:1%;">
        		<a href="#">
            		<div style="width:180px;text-align:center;font-size:20px;float:left;margin-top: 30px;">机构代码下载</div>
            		<img style="float:right;margin-top: 10px;margin-right:10px;"  src="${ctx}/static/themes/default/images/jgdmDownload.png">
        		</a>
   			</div>
			<div style="width:23%;height:80px;float:left;background-color:#fff;border:2px solid #ccc;margin-top:10px;margin-bottom:10px;margin-left:1%;">
        		<a href="#">
            		<div style="width:180px;text-align:center;font-size:20px;float:left;margin-top: 30px;">标准信息下载</div>
            		<img style="float:right;margin-top: 10px;margin-right:10px;"  src="${ctx}/static/themes/default/images/bzxxDownload.png"/>
        		</a>
   			</div>
		</div>
	</div>
	<div style="">
		<div class="con_itembox">
			<div class="con_item_title">
				<div class="bg1 left"></div>
				<div class="bg2 right"></div>
				<div class="title">
					<h5>平台访问统计</h5>
				</div>
			</div>
			<div class="con_item_list">
				<div id="chart1" style="height:400px;"></div>
			</div>
		</div>
	</div>

	<!-- 引入echart -->
	<script src="${ctxPlugins}/echarts/2.2.7/echarts.js"></script>
	<script type="text/javascript">
		// 路径配置
		require.config({
			paths : {
				echarts : 'http://echarts.baidu.com/build/dist'
			}
		});
	
		// 使用柱状图就加载bar模块，按需加载
		require([ 'echarts', 'echarts/chart/bar'], function(ec) {
			// 基于准备好的dom，初始化echarts图表
			var myChart1 = ec.init(document.getElementById('chart1'));
			var option = {
					tooltip : {
						trigger : 'axis'
					},
					legend : {
						data : [ '月访问量']
					},
					calculable : true,
					xAxis : [ {
						type : 'category',
						data : [ '1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月',
								'10月']
					} ],
					yAxis : [ {
						type : 'value'
					} ],
					series : [
							{
								name : '月访问量',
								type : 'bar',
								data : [ 32, 43, 73, 45, 35, 79,135, 152,
										54,29],
								markPoint : {
									data : [ {
										type : 'max',
										name : '最大值'
									}, {
										type : 'min',
										name : '最小值'
									} ]
								},
								
							}
							]
				};
	
			// 为echarts对象加载数据 
			myChart1.setOption(option,true);
		
			
			setTimeout(function (){
			    window.onresize = function () {
			    	myChart1.resize();	
			    }
			},200)
		});
	</script>
	

	
</body>
</html>