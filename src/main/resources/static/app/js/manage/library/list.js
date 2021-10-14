var $notice = {	
	
	 $list : $('#js-list'), // 리스트 영역
	
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
		this.fnSearch();
		$("#btnSearch").click(function(){
			$notice.setForm();
			$notice.fnSearch();
	    });
	},
	
	
	/**
	 * 리스트 폼 세팅
	 */
	setForm : function () {
	  var searchFrm = $('#listFrm');
	  searchFrm.find('#list-searchCtgy').val( $("#f-searchCtgy-sel").val().trim() );
	  searchFrm.find('#list-searchKeyword').val( $("#searchKeyword").val().trim() );
	},
	
	
	fnSearch : function () {
		$.ajax({
			type: 'POST',
		    url: 'list-data',
		    data: $('#listFrm').serialize(),
		    dataType: 'html',
		    success: function(data) {
				
				$notice.$list.empty();
		    	$notice.$list.html(data);
		    	
				$('.paging a').off("click").on("click", function(e) {
					e.preventDefault();
					$('#listFrm #list-currentPageNo').val(this.rel);
					$notice.fnSearch();
				});
			},
		    error: function(request, status, error) {
		    	console.log('code:' + request.status + '\n' + 'error:' + error);
		    },
		});
	}
	
};

$(function(){
	$notice.init();
});