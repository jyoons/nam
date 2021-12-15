"use strict";

const uiCommon = (function (uiCommon, $window) {
  uiCommon.init = function () {
    uiCommon.gnb.init();


  };

uiCommon.gnb = {
  init: () => {

  }
}
uiCommon.init();
return uiCommon;
})(window.uiCommon || {}, $(window));