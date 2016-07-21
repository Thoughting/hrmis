/**
 * 上传工具类
 */
Page.upload = {
	/**
	 * 创建上传窗口 公共方法
	 * @param ajaxUrl 上传提交请求路径
	 * @param chunk 是否分割大文件
	 * @param callBack 上传成功之后的回调
	 */
	open : function(ajaxUrl,callBack){
		var url = Page.basePath + "/pages/common/uploader.jsp?url="+ajaxUrl;
		var frame = $('<iframe/>');
		frame.attr({'src':url,width:'100%',height:'100%',frameborder:'0',scrolling:'no'});
		$('<div style="overflow: hidden;"/>').window({
			title: "上传文件",
			height:350,
			width:550,
			minimizable:false,
			modal:true,
			collapsible:false,
			maximizable:false,
			resizable:false,
			content: frame,
			onClose: function(){
				var fw = Page.upload.getFrameWindow(frame[0]);
				var files = fw.files;
				var result = fw.result;
				$(this).window('destroy');
				if(callBack)
					callBack.call(this,files, result);
			},
			onOpen:function(){
				var target = $(this);
				setTimeout(function(){
					var fw = Page.upload.getFrameWindow(frame[0]);
					fw.target = target;
				},100);
				$(this).window('center');
			}
		});
	},
	/**
	 * 根据iframe对象获取iframe的window对象
	 * @param frame
	 * @returns {Boolean}
	 */
	getFrameWindow : function(frame){
		return frame && typeof(frame)=='object' && frame.tagName == 'IFRAME' && frame.contentWindow;
	}
};