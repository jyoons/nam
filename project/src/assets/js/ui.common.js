"use strict";

const uiCommon = (function (uiCommon, $window) {
  uiCommon.init = function () {
    uiCommon.gnb.init();
    uiCommon.lnb.init();
    uiCommon.select.init();
    uiCommon.titImg.init();
    uiCommon.titText.init();
  };


  uiCommon.titImg = {
    init: function(){
      uiCommon.titImg.setDefault();
      uiCommon.titImg.event();
    },
    setDefault: function(){
      let h1 = $('.header').outerHeight();
      let h2 = $('.container-header').outerHeight();
      let h3 = Number($('.container-header').css('padding-bottom').replace(/[^-\d\.]/g,''));
      $('.container-header-fixImg').css('top', (h1 + h2 - h3 - 45));
    },
    event: function(){
      $(window).on('scroll', function(){
        var _thisTop = $(window).scrollTop();
        gsap.to('.container-header-fixImg', {duration:0, y:-(_thisTop * 0.86), ease: "power1"});  
        gsap.to('.container-header-fixImg>img', {duration:0, y:(_thisTop * 0.4), ease: "power1"});
      });
    }
  }
  uiCommon.titText = {
    init: function(){
      uiCommon.titText.event();
    },
    event: function(){
      let tl = gsap.timeline({
        scrollTrigger: {
          trigger: ".container-header__title",
          toggleActions: "restart none restart none"
        },
      });
      function text1(name){
        var textLen = $('.container-header__title>div').length;
        for(var i=1; i<(textLen + 1); i++){
          name.to('.container-header__title>div:nth-child(' + i +')>span', {duration:0.5, top:0, opacity:1, ease:'power1', delay:(i-1)*0.2}, 0);
        }
      }

      tl.add(text1(tl));
      tl.to('.container-header__text', {duration:0.4, right:0, opacity:1, ease:'power1'});
    }
  }

uiCommon.gnb = {
  init: function(){
    uiCommon.gnb.setDefault('.gnb');
    uiCommon.gnb.event('.gnb', '.gnb-btn', '.gnb-close');
  },
  setDefault:(elem) => {
    let el = $(elem);
    let elPos = el.attr('data-gnb');
    let elString = elPos.split(',');
    $('.gnb-1depth__items').removeClass('on');
    $('.gnb-1depth__items.'+ elString[0]).addClass('on');
    $('.gnb-1depth__items.'+ elString[0]).find('.gnb-2depth__items').eq(elString[1]).addClass('on');
    gsap.set('.gnb-1depth__items.on .gnb-2depth .gnb-2depth__items', {left:0}); 
    gsap.set('.gnb-1depth__items.on .gnb-2depth', {height:'auto'}); 

  },
  event:(elem, target1, target2) => {
    let tl1 = gsap.timeline();
    let tl2 = gsap.timeline();
    $(target1).on('click', function(){
      uiCommon.gnb.open('.gnb'); 
    });
    $(target2).on('click', function(){
      uiCommon.gnb.close('.gnb');      
    });
    uiCommon.gnb.itemEvent('.gnb-1depth__items');
  },
  close:(elem) => {
    // $('.dimmed').remove();
    gsap.to(elem, {duration:0.6, right:'-100%', ease: "power2", delay:0.3});
    gsap.to('.active-menu',{duration:0.4, x:-30, ease: "power2", onComplete:function(){
      $('.logo').removeClass('active-menu');
      gsap.to('.logo', {duration:1, x:0, ease: "power2"});
    }});
    gsap.to('.pc_gnbImage', {duration:0.4, left:'50%', ease: "power1"});  
  },
  open:(elem) => {
    // $('body').append("<div class='dimmed' onclick='uiCommon.gnb.close()'></div>");
    gsap.to(elem, {duration:0.4, right:0, ease: "power2"});  
    $('.logo').addClass('active-menu');
    gsap.fromTo('.active-menu',{x:-30, opacity:0}, {duration:0.6, x:0, opacity:1, ease: "power2", delay:0.4}); 
    gsap.to('.pc_gnbImage', {duration:0.6, left:0, ease: "power2", delay:0.3});    
  },
  itemEvent:(target) => {
    $(target + '>a').on('click', function(){
        let _this = $(this);
        $(target).removeClass('on');
        _this.parents('.gnb-1depth__items').addClass('on');
        gsap.set('.gnb-1depth__items .gnb-2depth .gnb-2depth__items', {left:'-100%'}); 
        gsap.fromTo('.gnb-1depth__items.on .gnb-2depth', {height:0}, {duration:0.4, height:'auto', ease: "power1"}); 
        let items = $('.gnb-1depth__items.on .gnb-2depth .gnb-2depth__items'); 
        let itemsLen = items.length;
        for(var i=0; i<itemsLen; i++){
          gsap.to(items.eq(i), {duration:0.4, left:0, ease: "power1", delay:i*0.2}); 
        }     
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
      $('.lnb-1depth .select-list__items, .lnb-2depth .select-list__items').removeClass('on');
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