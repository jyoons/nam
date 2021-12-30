"use strict";

const mainUi = (function (mainUi, $window) {
    mainUi.init = function () {
        // mainUi.mainPop.init();
        $('.main-popup').length > 0 && mainUi.mainPop.init();
        $('.wrap.main').length > 0 && mainUi.headerCtr.init();
        $('.wrap.main').length > 0 && mainUi.mainVisual.init();
        $('.wrap.main').length > 0 && mainUi.scrollEv.init();
    };
    mainUi.headerCtr = {
        init: function() {
            //여기 수정하기
            // $('.header').removeClass('is-scroll');
        }
    }
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
    };
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
                    return{
                        sliderPage: function(){
                            $('.page-num__current').text(i+1);
                            $('.page-num__total').text(slider.slideCount);
                        }
                    }
                }
            });
            $('.main-slider').on('afterChange',function(){
                $('.progress-bar .bar').addClass('bar-ani');
            });
            $('.main-slider').on('beforeChange',function(){
                $('.progress-bar .bar').removeClass('bar-ani');
            });
            $('.util-pause').on('click',function(){
                $('.main-slider').slick('slickPause');
                $(this).removeClass('on').siblings('.util-play').addClass('on');
            });
            $('.util-play').on('click',function(){
                $('.main-slider').slick('slickPlay');
                $(this).removeClass('on').siblings('.util-pause').addClass('on');
            });
        }
    };
    mainUi.scrollEv = {
        init: function(){
            this.scrollInit();
            this.scrollEv();
            this.textAni();
        },
        scrollInit : function(){
            var sumHeight = 0,
            epilHeight = $('.main-epilogue').height(); //680

            setTimeout(function(){
                $('.quick-menu').animate({
                    scrollTop : 800
                }, 1400);
            }, 200);

            $('.main .section').each(function(index, item){
                var height = $(item).index() * $(window).height();
                if ( index == 3 ) {
                    height = height + 6000;
                }
                sumHeight = sumHeight + height;
                $(item).css({ 'top' :  height});
            });
            $('#wrap.main').css({'height' : sumHeight + epilHeight + 'px'}); // 높이 조절하기
        },
        scrollEv: function(){
            let $mainVi = $('.main-visual'),
                $sec01 = $('.section-01'),
                $sec02 = $('.section-02'),
                $sec03 = $('.section-03'),
                $navi = $('.main-indi li'),
                _top01 = $sec01.offset().top, //1057
                _top02 = $sec02.offset().top, //2114
                _top03 = $sec03.offset().top, //9171
                _apilTop = $('.main .main-epilogue').offset().top; 

            $navi.find('a').on('click', function() {
                var index = $(this).parent().index(),
                    posi;

                // $('.main .main-epilogue').css({
                //     'opacity':'1',
                //     'z-index':'-1'
                // })
                $('.main-indi').removeClass('color-change');
                $navi.removeClass('on').eq(index).addClass('on');

                switch ( index ) {
                    case 0 :
                        posi = 0;
                        break;
                    case 1 :
                        posi = _top01;
                        $('.main-indi').addClass('color-change');
                        break;
                    case 2 :
                        posi = _top02;
                        break;
                    case 3 :
                        posi = _top03;
                        $('.main-indi').addClass('color-change');
                        break;
                };

                $(window).scrollTop(posi);
            });

            $window.on('scroll',function(e){
                let scTop = $window.scrollTop(),
                    c = 0,
                    d = -50,
                    sec01_value = scTop * ( d - c ) / _top01 + c;

                
                sec01_value > -50 && $mainVi.css('top', sec01_value + "%");
                

                //section01 지날때
                if ( scTop > _top01 && scTop < _top02) { 
                    scTop = scTop - $(window).height();
                    let sec02_value = scTop * ( d - c ) / _top01 + c;
                    $mainVi.css({
                        'opacity':'0',
                        'position' : 'absolute'
                    });                  
                    $sec01.css({
                        'position' : 'fixed',
                        'top' : sec02_value + "%",
                    });
                } else {
                    $mainVi.css({
                        'opacity':'1',
                        'position':'fixed'
                    });
                    $sec01.css({
                        'position' : 'absolute',
                        'top' : _top01,
                    });
                }

                //section02 지날때
                if ( scTop > _top02 && scTop < _top03) { 
                    $sec02.css({
                        'position' : 'fixed',
                        'top' : 0
                    });
                    scTop = scTop - $(window).height() * 2 - 6000;
                    let sec03_value = scTop * ( d - c ) / _top01 + c;
                    $mainVi.css({'opacity':'0'});
                    if ( $(window).scrollTop() > _top02 + 6000 ) {
                        $sec02.css({
                            'top' : sec03_value + "%"
                        });
                    }
                } else {
                    $sec02.css({
                        'position' : 'absolute',
                        'top' : _top02,
                    });
                }

                //section03 지날때
                if ( scTop > _top03 ) {
                    console.log(scTop);//11965
                    scTop = scTop - $(window).height() * 3 - 6000;
                    console.log(scTop);//2794
                    let sec04_value = scTop * ( d - c ) / _top01 + c;
                    console.log(sec04_value);
                    $sec03.css({
                        'position' : 'fixed',
                        'top' : sec04_value + "%"
                    });
                    $mainVi.css({'opacity':'0'});
                    $('.main-epilogue').addClass('show');
                } else {
                    $sec03.css({
                        'position' : 'absolute',
                        'top' : _top03,
                    });
                    $('.main-epilogue').removeClass('show');
                }
                

                // nav scroll
                // if(scTop >= 0 && scTop < _top01){
                //     $navi.eq(0).addClass('on').siblings().removeClass('on');
                // }else if(scTop >= _top01 && scTop < _top02){
                //     $navi.eq(1).addClass('on').siblings().removeClass('on');
                // }else if(scTop >= _top02 && scTop < _top03){
                //     $navi.eq(2).addClass('on').siblings().removeClass('on');
                // }else{
                //     $navi.eq(3).addClass('on').siblings().removeClass('on');
                // }

                // console.log(_top01,_top02,_top03); 1057 2114 9171
                console.log(`스크롤탑 : ${scTop}`);

            });
        },
        textAni: function(){
            // 텍스트 애니메이션
        }
    };
    mainUi.init();
    return mainUi;
})(window.mainUi || {}, $(window));