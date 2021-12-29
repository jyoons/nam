"use strict";

const mainUi = (function (mainUi, $window) {
    mainUi.init = function () {
        // mainUi.mainPop.init();
        $('.main-popup').length > 0 && mainUi.mainPop.init();
        mainUi.mainVisual.init();
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
    mainUi.mainVisual = {
        init: function(){
            this.mainSlider();
        },
        mainSlider : function(){
            const barAni = 3000;

            $('.main-slider').on('init',function(event,slick){
                $(this).siblings('.slider-util').find('.bar').css({'animation-duration': (barAni/1000)+0.3+'s'});
            });        
            $('.main-slider').slick({
                dots: true,
                prevArrow: $('.util-prev'),
                nextArrow: $('.util-next'),
                pauseOnHover:false,
                infinite: true,
                speed: 1000,
                fade: true,
                slidesToShow: 1,
                slidesToScroll: 1,
                autoplay: true,
                autoplaySpeed: barAni,
                customPaging: function(slider,i){
                    function sliderPage (){
                        $('.page-num__current').text(i+1);
                        $('.page-num__total').text(slider.slideCount);
                    }
                    return sliderPage();
                }
            });
            $('.main-slider').on('afterChange',function(){
                $('.progress-bar .bar').addClass('bar-ani');
            });
            $('.main-slider').on('beforeChange',function(){
                $('.progress-bar .bar').removeClass('bar-ani');
            });
            // $('.util-pause').on('click',function(){
            //     $('.main-slider').slick('slickPause');
            //     $(this).removeClass('on').siblings('.util-play').addClass('on');
            // });
        }
    }
    mainUi.init();
    return mainUi;
})(window.mainUi || {}, $(window));