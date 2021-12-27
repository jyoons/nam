"use strict";

const mainUi = (function (mainUi, $window) {
    mainUi.init = function () {
        mainUi.mainPop.init();
    };

    mainUi.mainPop = {
        init:function(){
            this.setDefault('.main-popup');
            this.slide('.popup-slide');
            this.event();
        },
        setDefault:function(elem){
            $('body').addClass('scrollOff');
            $('body').append('<div class="dimmed"></div>');
            $(elem).addClass('is-active');
        },
        slide:function(elem){
            $(elem).slick({
                speed:200,
                arrows:false,
                initialSlide:0,
                dots:true,
                adaptiveHeight: true,
                infinite:false
            });
        },
        event:function(){
            let _this = this;
            $('.main-popup .popup-close').on('click', function(){
                _this.close('.main-popup');
            });
        },
        close:function(elem){
            $('body').removeClass('scrollOff');
            $(elem).removeClass('is-active');
            $('.dimmed').remove();
        }
    }
    mainUi.init();
    return mainUi;
})(window.mainUi || {}, $(window));