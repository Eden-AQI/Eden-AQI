/*
处理高度问题，将元素高度设置为全屏、在某元素之后填充到底、并设置上下间距
zhangliang 2015/9/12
*/
window.heightElements = [];
(function ($) {
    $.fn.autoheight = function (options) {
        var settings = $.extend({}, { fullscreen: false, after: null, marginTop: null, marginBottom: null }, options);
        return this.each(function () {
            heightElements.push({ settings: settings, ele: this });
        });
    };
})(jQuery);

function doCheckHeight() {
    for (var i = 0; i < heightElements.length; i++) {
        var target = heightElements[i];
        var settings = target.settings;
        var h = null;
        var winHeight = $(window).height();
        if (settings.fullscreen) {
            h = winHeight;
        } else if (null != settings.after) {
            var $after = $(settings.after);
            h = winHeight - $after.height() - $after.offset().top;
        }
        if (null != settings.marginTop) {
            if (null == h)
                h = winHeight;
            h -= settings.marginTop;
        }
        if (null != settings.marginBottom) {
            if (null == h)
                h = winHeight;
            h -= settings.marginBottom;
        }
        if (null != h)
            target.ele.style.height = h + 'px';
    }
}

$(window).resize(function () {
    doCheckHeight();
});

$(document).ready(function () { doCheckHeight(); });
