
var $noticeView = {	
	/**
	 * 초기화
	 */
	init : function () {
		this.fnAddEventListener();
	},

	fnAddEventListener : function () {
		$('#btnReply').click( function() { $noticeView.replyData(); });
		$('#btnReg').click( function() { $noticeView.updData();	});
		$('#btnDel').click( function() { $noticeView.delData( $("#thisIdx").val() ); });
		$('#btnBack').click( function() { $noticeView.fnGoIndex(); });
 	},
	
	
	delData : function ( delIdx ){
		var json = new Object();
	    json.idx = delIdx;
	    
	    var array = new Array();
		array.push(json);
		
		$commUtil.fnConfirmDialog('삭제 하시겠습니까?').then(function(response){
			if(response.value){
				$.ajax({
					type: 'POST',
				    url: 'del.act',
				    contentType: 'application/json',
				    data: JSON.stringify(array),
				    dataType: 'json',
				    
				    success: function(data) {
				    	if (data.success) {
				    		$noticeView.fnGoIndex();
					    } else {
					    	alert(data.message);
					    }
					},
				    error: function(request, status, error) {
				    	console.log('code:' + request.status + '\n' + 'error:' + error);
				    }
				});
			}
		});
	},
	
	updData : function () {
		var data = new Object();
		data.name = "Idx";
		data.value = $("#thisIdx").val();
		
		var array = new Array();
		array.push(data);

		$noticeView.createFrm("Post", "frm", "./editPost", array);
		document.frm.submit();
	},
	
	replyData : function () {
		var data = new Object();
		data.name = "replyIdx";
		data.value = $("#thisIdx").val();
		
		var array = new Array();
		array.push(data);
		
		$noticeView.createFrm("Post", "frm", "./regPost", array);
		document.frm.submit();
	},
	
	fnGoIndex : function (){
		ref.go("/community/board");
	},
	
	createFrm : function (method, name, action, obj) {
		var form = document.createElement("form");
		form.setAttribute("charset", "UTF-8");
		form.setAttribute("method", method);
		form.setAttribute("name", name);
		form.setAttribute("action", action);
		
		obj.forEach(function(elem) {
			var hiddenField = document.createElement("input");
			hiddenField.setAttribute("type", "hidden");
			hiddenField.setAttribute("name", elem.name);
			hiddenField.setAttribute("value", elem.value);
			form.appendChild(hiddenField);
		});
		document.body.appendChild(form);
	}

	
};	
	
$(function(){
	$noticeView.init();
});