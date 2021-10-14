var $libraryListData = {
	
	/**
	 * 초기화
	 */
	init : function() {
		this.fnAddEventListener();
	},
	
	/**
	 * 이벤트 등록
	 */
	fnAddEventListener : function() {
		$("#chkMaster").change(function() {
			$("table.over-x  td > input[type='checkbox']").prop( 'checked', this.checked );
	    });
		
		$("#prcenter-del").click(function() {
			$libraryListData.fnDel();
		});
	},
	
	
	fnDel : function() {
		if ($("table.over-x  td > input[type='checkbox']:checked").length < 1) {
			alert("삭제할 게시물을 선택 해주세요.");
			return;
		}
		
		var array= [];
		
		$("table.over-x  td > input[type='checkbox']:checked").each(function(index, item) {
		    var json = new Object();
		    json.idx = $(this).val();
		    array.push(json)
		});
		
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
					    	$notice.fnSearch();
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
	}
};	
	
$(function(){
	$libraryListData.init();
});