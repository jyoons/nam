"use strict";

$(window).on('beforeunload', function () {
  $(window).scrollTop(0);
});

var mainUi = function (mainUi, $window) {
  mainUi.init = function () {
    $('.main-popup').length > 0 && mainUi.mainPop.init();
    $('.wrap.main').length > 0 && mainUi.headerCtr.init();
    $('.wrap.main').length > 0 && mainUi.mainVisual.init();
    $('.wrap.main').length > 0 && mainUi.scrollEv.init();
    $('.wrap.main').length > 0 && mainUi.boardCtr.init();
  };

  mainUi.headerCtr = {
    init: function init() {
      $window.on('scroll', function () {
        var scTop = $(this).scrollTop();
        $('.header').removeClass('is-scroll');

        if (scTop > $('.main-visual').height()) {
          $('.header').addClass('is-scroll');
        } else {
          $('.header').removeClass('is-scroll');
        }
      });
    }
  };
  mainUi.mainPop = {
    init: function init() {
      this.setDefault('.main-popup');
      this.slide('.popup-slide');
      this.event();
    },
    setDefault: function setDefault(elem) {
      $('body').addClass('scrollOff');
      $('body').append('<div class="dimmed"></div>');
      $(elem).addClass('is-active');
    },
    slide: function slide(elem) {
      $(elem).slick({
        speed: 200,
        arrows: false,
        initialSlide: 0,
        dots: true,
        adaptiveHeight: true,
        infinite: false
      });
    },
    event: function event() {
      var _this = this;

      $('.main-popup .popup-close').on('click', function () {
        _this.close('.main-popup');
      });
    },
    close: function close(elem) {
      $('body').removeClass('scrollOff');
      $(elem).removeClass('is-active');
      $('.dimmed').remove();
    }
  };
  mainUi.mainVisual = {
    init: function init() {
      this.mainSlider(); // this.quickMenu();
    },
    mainSlider: function mainSlider() {
      var barAni = 18000,
          $el_length = $('.main-slider li').length,
          $bar = $('.progress-bar .bar'),
          timeW = $('.progress-bar').width();
      var index = $('.page-num__current').text();
      $('.main-slider').on({
        init: function init(event, slick) {
          $(this).siblings('.slider-util').find('.bar').css({
            'animation-duration': barAni / 1000 + 0.3 + 's'
          });
          $('.page-num__total').text($el_length);
          $bar.stop().animate({
            width: timeW
          }, barAni, "linear", function () {
            $(this).css({
              'width': '0'
            });
          });
        },
        beforeChange: function beforeChange(slick, currentSlide, nextSlide) {
          $bar.removeClass('bar-ani');
          $bar.stop().width(0);
        },
        afterChange: function afterChange(event, slick, currentSlide) {
          $('.page-num__current').text(currentSlide + 1);
          $bar.addClass('bar-ani');
          $('.video' + index).trigger('play');
          $bar.stop().width(0);
          $bar.stop().animate({
            width: timeW
          }, barAni, "linear", function () {
            $(this).css({
              'width': '0'
            });
          });
        }
      });
      $('.main-slider').slick({
        dots: false,
        prevArrow: $('.util-prev'),
        nextArrow: $('.util-next'),
        pauseOnHover: false,
        infinite: true,
        speed: 1000,
        fade: true,
        slidesToShow: 1,
        slidesToScroll: 1,
        autoplay: true,
        autoplaySpeed: barAni
      });
      $('.util-pause').on('click', function () {
        var index = $('.page-num__current').text(),
            w = $('.progress-bar .bar').width();
        $('.main-slider').slick('slickPause');
        $(this).removeClass('on').siblings('.util-play').addClass('on');
        $bar.width(w).stop();
        $('.video' + index).trigger('pause');
      });
      $('.util-play').on('click', function () {
        var index = $('.page-num__current').text(),
            w = $('.progress-bar .bar').width();
        $('.main-slider').slick('slickPlay');
        $(this).removeClass('on').siblings('.util-pause').addClass('on');
        $('.video' + index).trigger('play');
        $bar.stop().animate({
          width: timeW
        }, barAni, "linear", function () {
          $('.video' + index).trigger('pause');
          $('.video' + index).get(0).currentTime = 0;
          $(this).css({
            'width': '0'
          });
        });
      });
      $('.slick-arrow').on('click', function () {
        $('.util-play').removeClass('on').siblings('.util-pause').addClass('on');
        $('.video' + index).trigger('play');
      });
    }
  };
  mainUi.scrollEv = {
    init: function init() {
      this.scrollInit();
      this.scrollEv();
      this.sectionAni();
    },
    scrollInit: function scrollInit() {
      var sumHeight = 0,
          epilHeight = $('.main-epilogue').height(); //680

      setTimeout(function () {
        $('.quick-menu').animate({
          scrollTop: 800
        }, 1400);
      }, 200);
      $('.main .section').each(function (index, item) {
        var height = $(item).index() * $(window).height();

        if (index == 3) {
          height = height + 6000;
        }

        sumHeight = sumHeight + height;
        $(item).css({
          'top': height
        });
      });
      $('#wrap.main').css({
        'height': sumHeight + epilHeight + 300 + 'px'
      }); // $('#wrap.main').css({'height' : sumHeight - epilHeight - 180 + 'px'});
    },
    scrollEv: function scrollEv() {
      var $mainVi = $('.main-visual'),
          $sec01 = $('.section-01'),
          $sec02 = $('.section-02'),
          $sec03 = $('.section-03'),
          $navi = $('.main-indi li'),
          _top01 = $sec01.offset().top,
          //1057
      _top02 = $sec02.offset().top,
          //2114
      _top03 = $sec03.offset().top; //9171

      $navi.find('a').on('click', function () {
        var index = $(this).parent().index(),
            posi; // $('.main .main-epilogue').css({
        //     'opacity':'1',
        //     'z-index':'-1'
        // })

        $('.main-indi').removeClass('color-change');
        $navi.removeClass('on').eq(index).addClass('on');

        switch (index) {
          case 0:
            posi = 0;
            break;

          case 1:
            posi = _top01;
            $('.main-indi').addClass('color-change');
            break;

          case 2:
            posi = _top02;
            break;

          case 3:
            posi = _top03;
            $('.main-indi').addClass('color-change');
            break;
        }

        ;
        $(window).scrollTop(posi);
      });
      $window.on('scroll', function (e) {
        var scTop = $window.scrollTop(),
            c = 0,
            d = -50,
            sec01_value = scTop * (d - c) / _top01 + c; // mainvisual

        sec01_value > -50 && $mainVi.css('top', sec01_value + "%"); // sec01_value > -50 && $mainVi.css('top',0);
        // $('.quick-menu').css({
        //     'transform': 'tramslateY('+ sec01_value + '"%")'
        // })
        //section01 지날때

        if (scTop > _top01 && scTop < _top02) {
          scTop = scTop - $(window).height();
          var sec02_value = scTop * (d - c) / _top01 + c;
          $mainVi.css({
            'opacity': '0',
            'position': 'absolute'
          });
          $sec01.css({
            'position': 'fixed',
            'top': sec02_value + "%"
          });
        } else {
          $mainVi.css({
            'opacity': '1',
            'position': 'fixed'
          });
          $sec01.css({
            'position': 'absolute',
            'top': _top01
          });
        } //section02 지날때


        if (scTop > _top02 && scTop < _top03) {
          $sec02.css({
            'position': 'fixed',
            'top': 0
          });
          $mainVi.css({
            'opacity': '0',
            'position': 'absolute'
          });
          scTop = scTop - $(window).height() * 2 - 6000;
          var sec03_value = scTop * (d - c) / _top01 + c;

          if ($(window).scrollTop() > _top02 + 6000) {
            $sec02.css({
              'top': sec03_value + "%"
            });
          }
        } else {
          $sec02.css({
            'position': 'absolute',
            'top': _top02
          });
        } //section03 지날때


        if (scTop > _top03) {
          scTop = scTop - $(window).height() * 3 - 6000;
          var sec04_value = scTop * (d - c) / _top01 + c;
          $mainVi.css({
            'opacity': '0',
            'position': 'absolute'
          });
          $sec03.css({
            'position': 'fixed',
            'top': sec04_value + "%"
          });
          $('.main-epilogue').addClass('show');
        } else {
          $sec03.css({
            'position': 'absolute',
            'top': _top03
          });
          $('.main-epilogue').removeClass('show');
        } // if ( scTop > _top01 -400 && scTop < _top02 -400) {
        //     $navi.eq(1).addClass('on').siblings().removeClass('on');
        // }else{
        //     $navi.eq(0).addClass('on').siblings().removeClass('on');
        // }
        // if ( scTop > _top02 -400 && scTop < _top03 -400) {
        //     $navi.eq(2).addClass('on').siblings().removeClass('on');
        // }else{
        //     $navi.eq(1).addClass('on').siblings().removeClass('on');
        // }

      });
    },
    sectionAni: function sectionAni() {
      var $section = $('.main .section'),
          $sec01 = $('.section-01'),
          $sec02 = $('.section-02'),
          $sec03 = $('.section-03'),
          $navi = $('.main-indi li'),
          $secInfo = $('.section-02 .section-02-text'),
          $sec02_title = $('.section-02-title'),
          _top01 = $sec01.offset().top - 400,
          //657 
      _top02 = $sec02.offset().top - 400,
          //1714
      _top03 = $sec03.offset().top - 400,
          //8771
      _sec02Posi01 = $sec02.offset().top + 1500,
          //3614
      _sec02Posi02 = $sec02.offset().top + 3000,
          //5114
      _sec02Posi03 = $sec02.offset().top + 4500,
          //6614
      _sec03Posi = $sec03.offset().top + 2200,
          _count01 = new countUp.CountUp('count01', 5000),
          _count02 = new countUp.CountUp('count02', 5000),
          _count01Flag = false,
          _count02Flag = false;

      $window.on('scroll', function () {
        var scTop = $window.scrollTop(); // console.log(scTop);

        if (scTop < _top01) {
          //657미만
          $('.main-indi').removeClass('color-change');
          $navi.removeClass('on').eq(0).addClass('on');
          $section.removeClass('active');
        } else if (scTop < _top02) {
          //1714미만
          $('.main-indi').addClass('color-change');
          $navi.removeClass('on').eq(1).addClass('on');
          $secInfo.removeClass('active');
          $section.removeClass('active').eq(1).addClass('active');
        } else if (scTop < _top03) {
          //8771 미만
          $('.main-indi').removeClass('color-change');
          $navi.removeClass('on').eq(2).addClass('on');
          $section.removeClass('active').eq(2).addClass('active');

          if (scTop < _sec02Posi01) {
            //3614 미만
            $secInfo.removeClass('active').eq(0).addClass('active').css('top', '-30%').stop().animate({
              'top': '0',
              'opacity': '1',
              'z-index': '0'
            }, 200);
            $secInfo.eq(1).stop().animate({
              'top': '80%',
              'opacity': '0',
              'z-index': '-1'
            }, 200);
          } else if (scTop < _sec02Posi02) {
            if (!_count01Flag) {
              setTimeout(function () {
                _count01.start();

                _count01Flag = true;
              }, 700);
            } else {
              setTimeout(function () {
                _count01.reset();

                _count01.update(5000);
              }, 700);
            }

            $secInfo.removeClass('active').eq(0).removeClass('active').stop().animate({
              'top': '-80%',
              'opacity': '0',
              'z-index': '-1'
            }, 200);
            $secInfo.removeClass('active').eq(1).addClass('active').css('top', '-30%').stop().animate({
              'top': '0',
              'opacity': '1',
              'z-index': '0'
            }, 200);
            $secInfo.eq(2).stop().animate({
              'top': '80%',
              'opacity': '0',
              'z-index': '-1'
            }, 200);
          } else if (scTop < _sec02Posi03) {
            if (!_count02Flag) {
              setTimeout(function () {
                _count02.start();

                _count02Flag = true;
              }, 700);
            } else {
              setTimeout(function () {
                _count02.reset();

                _count02.update(5000);
              }, 700);
            }

            $sec02_title.removeClass('on').eq(0).addClass('on').css('top', '-30%').stop().animate({
              'top': '0',
              'opacity': '1',
              'z-index': '0'
            }, 200);
            $sec02_title.eq(1).stop().animate({
              'top': '80%',
              'opacity': '0',
              'z-index': '-1'
            }, 200);
            $secInfo.removeClass('active').eq(1).removeClass('active').stop().animate({
              'top': '-80%',
              'opacity': '0',
              'z-index': '-1'
            }, 200);
            $secInfo.removeClass('active').eq(2).addClass('active').css('top', '-30%').stop().animate({
              'top': '0',
              'opacity': '1',
              'z-index': '0'
            }, 200);
            $secInfo.eq(3).stop().animate({
              'top': '80%',
              'opacity': '0',
              'z-index': '-1'
            }, 200);
          } else if (_sec02Posi03 < scTop) {
            $sec02_title.removeClass('on').eq(0).removeClass('on').stop().animate({
              'top': '-80%',
              'opacity': '0',
              'z-index': '-1'
            }, 200);
            $sec02_title.removeClass('on').eq(1).addClass('on').css('top', '80%').stop().animate({
              'top': '0',
              'opacity': '1',
              'z-index': '0'
            }, 200);

            if ($secInfo.eq(2).hasClass('active')) {
              $secInfo.removeClass('active').eq(2).removeClass('active').stop().animate({
                'top': '-80%',
                'opacity': '0',
                'z-index': '-1'
              }, 200);
            }

            if (!$secInfo.eq(3).hasClass('active')) {
              $secInfo.removeClass('active').eq(3).addClass('active').css('top', '80%').stop().animate({
                'top': '0',
                'opacity': '1',
                'z-index': '0'
              }, 200);
            }
          }
        } else {
          $('.main-indi').addClass('color-change');
          $navi.removeClass('on').eq(3).addClass('on');
          $section.removeClass('active').eq(3).addClass('active');
        }
      });
    }
  };
  mainUi.boardCtr = {
    init: function init() {
      var $mainTab = $('.main-tab-list__item'),
          $mainTabConts = $('.main-layout__conts');
      $mainTab.on('click', function () {
        $(this).addClass('on').siblings().removeClass('on');
        var tabItem = $(this).attr('data-tab');
        $mainTabConts.find('.' + tabItem).addClass('on').siblings().removeClass('on');
      });
    }
  };
  mainUi.init();
  return mainUi;
}(window.mainUi || {}, $(window));