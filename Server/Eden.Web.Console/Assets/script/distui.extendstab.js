/*
扩展bootstrap tab功能
zhangliang 2015/9/15
*/
(function ($) {

    function removeThisTab() {
        var thisTab = $(this).parent();
        thisTab.parent().parent().extendstab('remove', thisTab.index());
    }

    var methods = {
        create: function (options) {
            var newId = ('t' + Math.random()).replace('.', '');
            var li = '<li role="presentation"><a href="#' + newId + '" id="tab_' + newId + '" aria-controls="' + newId + '" role="tab" data-toggle="tab">' + options.title + '</a><i class="icon-remove" id="close_' + newId + '"></i></li>';
            var content = '<div role="tabpanel" class="tab-pane"  id="' + newId + '">' + options.content + '</div>';
            var child = this.children();
            $(child[0]).append(li);
            $(child[1]).append(content);
            $(document.getElementById('tab_' + newId)).tab('show');
            $('#close_' + newId).bind('click', removeThisTab);
        },
        show: function (index) {
            var current = this.find('.tab-content>div.active');
            if (current.length > 0)
                $(current[0]).removeClass('active');
            $('#' + ($($(this.children()[0]).children()[index]).tab('show').children()[0].getAttribute('aria-controls').replace('#', ''))).addClass('active');
        },
        remove: function (index) {
            var child = this.children();
            var tab = $($(child[0]).children()[index]);
            var newActive;
            if (tab.hasClass('active')) {
                newActive = tab.prev();
                if (newActive.length == 0)
                    newActive = tab.next();
            }
            var content = $('#' + tab.children()[0].getAttribute('href').replace('#', ''));
            tab.remove();
            content.remove();
            if (newActive && newActive.length > 0) {
                newActive.tab('show');
                $('#' + newActive.children()[0].getAttribute('href').replace('#', '')).addClass('active');
            }
        },
        init: function () {
            var closeButtons = this.find('ul li i');
            closeButtons.each(function () {
                $(this).bind('click', removeThisTab);
            });
        }
    };

    $.fn.extendstab = function (method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.extendstab');
        }
    }

})(jQuery);
