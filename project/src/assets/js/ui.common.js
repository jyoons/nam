"use strict";

const uiCommon = (function (uiCommon, $window) {
  uiCommon.init = function () {
    uiCommon.lnb.init();
    uiCommon.select.init();

  };

uiCommon.lnb = {
  init: () => {
    uiCommon.lnb.setDefault('.lnb');
  },
  setDefault: (elem) => {
    let el = $(elem);
    if(el.length > 0){
      let elPos = el.attr('data-lnb');
      let elString = elPos.split(',');
      let targetD1 = elString[0];
      let targetD2 = elString[1];
      let targetD3 = elString[2];  
      // $('.lnb-1depth.select-wrap .select-list-wrap, .lnb-2depth.select-wrap .select-list-wrap').css({'display':'block'});
      $('.lnb-1depth .select-list-wrap .select-list__items.' + targetD1).addClass('on');
      $('.lnb-2depth .select-list-wrap .select-list.' + targetD1).css({'display':'block'});
      $('.lnb-2depth .select-list__items').eq(targetD2).addClass('on');
      $('.lnb-3depth .select-list__items').eq(targetD3).addClass('on');
      // $('.lnb-3depth.select-wrap .select-list-wrap .select-list .select-list__items').eq(targetD2).addClass('on');
    }   
  }
}

uiCommon.select = {
  init: () => {
    uiCommon.select.setDefault('.select-wrap');
    uiCommon.select.event('.select-title');
  },
  setDefault: (elem) => {
    let el = $(elem);
    el.each(function(i){
        let title = el.eq(i).find('.select-list__items.on').children('a').text();
        el.eq(i).find('.select-title').text(title);
    });
  },
  event : (elem) =>{
    let target = $(elem);
    target.on('click', function(){
      let _this = $(this);
      if(_this.hasClass('is-active')){
        close();
      }else{
        open();
      }
      function open(){
        console.log('open')
        target.removeClass('is-active');
        target.next('.select-list-wrap').slideUp(200);
        _this.addClass('is-active');
        _this.next('.select-list-wrap').slideDown(200);
      }
      function close(){
        console.log('close')
        target.removeClass('is-active');
        target.next('.select-list-wrap').slideUp(200);
      }     
    });
  }
}
uiCommon.init();
return uiCommon;
})(window.uiCommon || {}, $(window));