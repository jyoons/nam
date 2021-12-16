"use strict";

const uiCommon = (function (uiCommon, $window) {
  uiCommon.init = function () {
    uiCommon.gnb.init();
    uiCommon.lnb.init();
    uiCommon.select.init();
  };

  uiCommon.gnb = {
    init: function(){
      uiCommon.gnb.setDefault('.gnb');
      uiCommon.gnb.event('.gnb', '.gnb-btn', '.gnb-close');
    },
    setDefault:(elem) => {
      let el = $(elem);
      let elPos = el.attr('data-gnb');
      let elString = elPos.split(',');
      $('.gnb-1depth__items.'+ elString[0]).addClass('on');
      $('.gnb-1depth__items.'+ elString[0]).find('.gnb-2depth__items').eq(elString[1]).addClass('on');

    },
    event:(elem, target1, target2) => {
      let tl1 = gsap.timeline({});
      let tl2 = gsap.timeline({});
      $(target1).on('click', function(){
        tl1.to(elem, {duration:0.6, right:0, ease: "power3"});   
      });
      $(target2).on('click', function(){
        tl1.to(elem, {duration:0.6, right:'-100%', ease: "power3"});   
      });
      uiCommon.gnb.itemEvent('.gnb-1depth__items');
    },
    itemEvent:(target) => {
      $(target + '>a').on('click', function(){
          let _this = $(this);
          $(target).removeClass('on');
          _this.parents('.gnb-1depth__items').addClass('on');
      });
    }
  }

uiCommon.lnb = {
  init: () => {
    uiCommon.lnb.setDefault('.lnb');
  },
  setDefault: (elem) => {
    let el = $(elem);
    if(el.length > 0){
      let elPos = el.attr('data-lnb');
      let elString = elPos.split(',');
      $('.lnb-1depth .select-list-wrap .select-list__items.' + elString[0]).addClass('on');
      $('.lnb-2depth .select-list-wrap .select-list.' + elString[0]).css({'display':'block'});
      $('.lnb-2depth .select-list__items').eq(elString[1]).addClass('on');
      if(elString[0] == 'business'){
        $('.lnb-3depth .select-list').eq(elString[2]).css({'display':'block'});
        $('.lnb-3depth .select-list__items').eq(elString[2]).addClass('on');
      }
    }   
  }
}

uiCommon.select = {
  init: () => {
    uiCommon.select.setDefault('.select-wrap');
    uiCommon.select.event('.select-title', '.select-list__items');
  },
  setDefault: (elem) => {
    let el = $(elem);
    el.each(function(i){
        let title = el.eq(i).find('.select-list__items.on').children('a').text();
        el.eq(i).find('.select-title').text(title);
    });
  },
  event : (elem1, elem2) => {
    let target = $(elem1);
    let elem = $(elem2);
    target.on('click', function(){
      let _this = $(this);
      if(_this.hasClass('is-active')){
        close();
      }else{
        open();
      }      
      function open(){
        target.removeClass('is-active');
        target.next('.select-list-wrap').slideUp(200);
        _this.addClass('is-active');
        _this.next('.select-list-wrap').slideDown(200);
      }
      function close(){
        target.removeClass('is-active');
        target.next('.select-list-wrap').slideUp(200);
      }     
    });  
    elem.children('a').on('click', function(){
      let _this = $(this);    
      _this.parents('.select-list-wrap').slideUp(200);
      _this.parents('.select-list-wrap').find(elem2).removeClass('on');
      _this.closest(elem2).addClass('on');
      _this.parents('.select-wrap').find('.select-title').removeClass('is-active').text($(this).text());
    });
  }
}

uiCommon.init();
return uiCommon;
})(window.uiCommon || {}, $(window));