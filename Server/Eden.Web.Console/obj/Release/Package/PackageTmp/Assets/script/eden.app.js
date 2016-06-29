
(function (win) {
    win.eden = eden = angular.module('edenConsole', ['ui.router', 'angularFileUpload', 'ngDialog']);
    eden.config(function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.when("", "/dashboard");

        //仪表盘
        $stateProvider.state("dashboard", {
            url: "/dashboard",
            templateUrl: "/Templates/Dashboard.html"
        });

        //设备
        $stateProvider.state("device", {
            url: "/device",
            template: eden.templates.curd('device', '设备', true, true)
        });

        //用户
        $stateProvider.state("users", {
            url: "/users",
            templateUrl: "/UnReady.html"
        });

        //推送
        $stateProvider.state("push", {
            url: "/push",
            template: eden.templates.curd('push', '推送', false, true)
        });


        //系统
        $stateProvider.state("system", {
            url: "/system",
            template: eden.templates.submenu
        });
        $stateProvider.state("system.version", {
            url: "/version",
            templateUrl: "/Templates/VersionSetting.html"
        });
        $stateProvider.state("system.sys", {
            url: "/sys",
            templateUrl: "/Templates/SystemSetting.html"
        });
        $stateProvider.state("system.config", {
            url: "/config",
            template: eden.templates.curd('config', '配置')
        });
        $stateProvider.state("system.log", {
            url: "/log",
            template: eden.templates.curd('log', '日志', true, true)
        });
        $stateProvider.state("system.task", {
            url: "/task",
            template: eden.templates.curd('task', '任务', true, false)
        });

    });

    eden.config(function ($httpProvider) {
        $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
        $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
    });

    eden.filter("sxgdate", function () {
        return function (x) {
            return new Date(parseInt(x.substr(6)));
        };
    });

    eden.templates = {};
    eden.templates.submenu = '<div id="layout-column1" ng-controller="submenuCtrl"><div class="sub-menu-title">{{navigator.label}}</div><ul class="sub-menu" id="submenus"><li ng-repeat="nav in navigator.items" class="{{nav.class}}" ui-sref="{{nav.uisref}}" ng-click="onSubmenuClick(nav.label)">{{nav.label}}</li></ul></div><div id="layout-column3" data-ui-view=""></div>';
    eden.templates.curd = function (entity, title, notCreate, notUpdate) {
        var identity = Math.floor(Math.random() * 1000000);
        var html = '<div ng-controller="curd" eden-curd-config autoload="true" entity="' + entity + '" identity="' + identity + '">' +
                    '<div class="curd-list">' +
                        '<div class="curd-list-top" id="curd-list-top">' +
                            '<div class="curd-list-title">' +
                                '<i class="icon-heart"></i><span>&nbsp;&nbsp;' + title + '</span>' +
                            '</div>' +
                            (notCreate ? '' : '<div class="curd-list-tools"><a class="curd-list-toolitem" data-toggle="modal" data-target="#dialog_' + identity + '" ng-click="create()">添加</a></div>') +
                        '</div>' +
                        '<div class="curd-list-searchcontainer">' +
                            '<form id="conditions_' + identity + '">' +
                                '<div class="curd-conditions">' +
                                '<div class="input-group curd-list-searchinput"><input type="text" class="form-control" placeholder="关键字" name="Key"><span class="input-group-btn"><button class="btn btn-default" type="button" ng-click="search()">搜索</button></span></div>' +
                                '</div>' +
                                '<div class="curd-list-tags">' +
                                    '<label>总：{{pagination.dataCount}}</label>&nbsp;&nbsp;转到：' +
                                    '<select ng-change="search()" ng-model="pagination.pageIndex" ng-options="p for p in pagination.pages"></select>' +
                                '</div>' +
                            '</form>' +
                        '</div>' +
                        '<div id="curd-list-table">' + this.listitem[entity] + '</div>' +
                    '</div>' +
                    '<div id="dialog_' + identity + '" class="modal fade" role="dialog" aria-labelledby="myModalLabel"><div class="modal-dialog" role="document">' +
                        '<div class="modal-content"><form ng-submit="save()" id="form_{{identity}}"><input type="hidden" name="entity" value="{{entityName}}"/>' +
                        '<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button><h4 class="modal-title" id="myModalLabel">详情</h4></div>' +
                        '<div class="modal-body panel-scroll" id="form-' + identity + '" >' +

                        '</div>' +
                        '<div class="modal-footer">' +
                            '<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>' +
                            (notUpdate ? '' : '<button type="submit" class="btn btn-primary" ng-disabled="form.$invalid">保存</button>') +
                        '</div></form></div></div></div>' +
                '</div>';
        return html;
    };
    eden.templates.listitem = {
        'device': '<table class="table table-hover"><tr>' +
                '<th>设备编号</th><th width="200px">平台</th><th width="200px">类型</th><th width="200px">加入时间</th><th width="100px">操作</th></tr>' +
                '<tr ng-repeat="item in list">' +
                '<td>{{item.DeviceNumber}}</td><td>{{item.PlatformName}}</td><td>{{item.DeviceTypeName}}</td><td>{{item.CreateTime}}</td>' +
                '<td><a href="" ng-click="update(item.Id)" data-toggle="modal" data-target="#dialog_{{identity}}">查看</a></td>' +
                '</tr></table>',
        'push': '<table class="table table-hover"><tr>' +
                '<th width="100px">类型</th><th width="100px">发送给</th><th width="200px">推送时间</th><th>消息</th><th width="100px">操作</th></tr>' +
                '<tr ng-repeat="item in list">' +
                '<td>{{item.Type}}</td><td>{{item.PlatformName}}</td><td>{{item.CreateTimeString}}</td><td>{{item.Message}}</td>' +
                '<td><a href="" ng-click="update(item.Id)" data-toggle="modal" data-target="#dialog_{{identity}}">查看</a></td>' +
                '</tr></table>',
        'config': '<table class="table table-hover"><tr>' +
                '<th>名称</th><th width="200px">值</th><th width="100px">操作</th></tr>' +
                '<tr ng-repeat="item in list">' +
                '<td>{{item.Name}}</td><td>{{item.Value}}</td>' +
                '<td><a href="" ng-click="update(item.Id)" data-toggle="modal" data-target="#dialog_{{identity}}">修改</a>&nbsp;&nbsp;<a href="" ng-click="delete(item.Id)">删除</a></td>' +
                '</tr></table>',
        'log': '<table class="table table-hover"><tr>' +
                '<th width="80px">级别</th><th width="80px">来源</th><th>消息</th><th width="180px">创建时间</th><th width="100px">操作</th></tr>' +
                '<tr ng-repeat="item in list">' +
                '<td>{{item.Level}}</td><td>{{item.EventSourceString}}</td><td>{{item.ShortMessage}}</td><td>{{item.EventTimeString}}</td>' +
                '<td><a href="" ng-click="update(item.Id)" data-toggle="modal" data-target="#dialog_{{identity}}">修改</a>&nbsp;&nbsp;<a href="" ng-click="delete(item.Id)">删除</a></td>' +
                '</tr></table>',
        'task': '<table class="table table-hover"><tr>' +
                '<th>名称</th><th width="120px">运行周期（秒）</th><th width="80px">启用</th><th width="100px">错误后停止</th><th width="160px">最后一次运行开始时间</th><th width="160px">最后一次运行结束时间</th><th width="160px">最后一次运行成功</th><th width="120px">现在运行</th><th width="60px">操作</th></tr>' +
                '<tr ng-repeat="item in list">' +
                '<td>{{item.Name}}</td><td>{{item.Seconds}}</td><td>{{item.EnabledString}}</td><td>{{item.StopOnErrorString}}</td><td>{{item.LastStartTime}}</td><td>{{item.LastEndTime}}</td><td>{{item.LastSuccessTime}}</td><td><a href="" ng-controller="scheduleTaskRunCtrl" ng-click="run(item.Id)">运行</a></td>' +
                '<td><a href="" ng-click="update(item.Id)" data-toggle="modal" data-target="#dialog_{{identity}}">修改</a></td>' +
                '</tr></table>'

    };

    eden.entities = {
        //配置例外的实体服务
    };

})(window);