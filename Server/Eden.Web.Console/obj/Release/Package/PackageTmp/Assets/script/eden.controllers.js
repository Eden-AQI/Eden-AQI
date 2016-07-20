
eden.controller('sysNavigatorCtrl', function ($scope, $http, $state, $rootScope) {
    //此处通过后台服务加载
    $scope.navigator = [
        //{ 'uisref': 'dashboard', 'label': '仪表盘', 'class': 'icon-dashboard icon-2x' },
        { 'uisref': 'device', 'label': '设备', 'class': 'icon-mobile-phone icon-2x' },
        { 'uisref': 'push', 'label': '通知', 'class': 'icon-volume-up icon-2x' },

        {
            'uisref': 'system', 'label': '系统', 'class': 'icon-cog icon-2x', 'items': [
                { 'uisref': 'system.version', 'label': '版本' },
                { 'uisref': 'system.sys', 'label': '系统' },
                { 'uisref': 'system.log', 'label': '日志' },
                { 'uisref': 'system.requestlog', 'label': '访问日志' },
                { 'uisref': 'system.task', 'label': '任务' },
                { 'uisref': 'system.config', 'label': '所有配置' }
            ]
        }
    ];

    $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
        $scope.state = toState.name;
        for (var i = 0; i < $scope.navigator.length; i++) {
            var nav = $scope.navigator[i];
            if (toState.name.indexOf(nav.uisref) == 0) {
                window.currentNavigator = nav;
                break;
            }
        }
    });

})

eden.controller('submenuCtrl', function ($scope, $http, $state) {
    //var currentNav = $state.$parent.current;
    //var submenus = $('#submenus li');
    //if (submenus.length == 0)
    //    return;

    var toFirstNav = true;
    $scope.navigator = window.currentNavigator;
    if (!$scope.navigator)
        return;
    $scope.current = $scope.navigator.items[0];
    var currentState = $state.current.name;
    for (var i = 0; i < $scope.navigator.items.length; i++) {
        var uisref = $scope.navigator.items[i].uisref;
        if (currentState.indexOf(uisref) == 0) {
            $scope.current = $scope.navigator.items[i];
            $scope.current.class = 'active';
            toFirstNav = false;
        }
    }
    if (toFirstNav) {
        $state.go($scope.current.uisref);
    }
    $scope.current.class = 'active';
    $scope.onSubmenuClick = function (label) {
        for (var i = 0; i < $scope.navigator.items.length; i++) {
            var item = $scope.navigator.items[i];
            item.class = item.label == label ? 'active' : '';
        }
    }
});


eden.controller('curd', function ($scope, $http, $element, $compile, ngDialog) {
    $scope.pagination = {};
    $scope.pagination.pages = [1];
    $scope.pagination.pageIndex = 1;
    $scope.pagination.pageCount = 0;
    $scope.pagination.dataCount = 0;
    $scope.searchKey = '';
    $scope.pageSize = 30;

    $scope.identity = '';
    $scope.form = {};
    $scope.list = [];
    $scope.selectAll = false;

    $scope.entityName = '';
    $scope.searchData = {};

    $scope.createOrUpdateLoaded = false;
    $scope.entityService = { url: '/EntityRestfull' };
    $scope.currentSearchParameter = null;

    $scope.toggleCheckAll = function () {
        for (var i = 0; i < $scope.list.length; i++) {
            $scope.list[i].checked = $scope.selectAll;
        }
    }

    $scope.doSearch = function () {
        $http.post($scope.entityService.url + "/Search", $scope.currentSearchParameter).success(function (response) {

            $scope.list = response.Data;

            $scope.refreshPagination(response);
        }).error(function () {
            alert('error');
        });

        if (!$scope.createOrUpdateLoaded) {
            $http.get('/Templates/CreateOrUpdate/' + $scope.entityName + '.html').success(function (response) {
                $(document.getElementById('form-' + $scope.identity)).html($compile(response)($scope));
                $scope.createOrUpdateLoaded = true;
            }).error(function () {
                alert('error');
            });
        }
    }

    $scope.search = function () {
        $scope.currentSearchParameter = $scope.getSearchData();
        $scope.doSearch();
    };

    //$scope.changePage = function () {
    //    $scope.doSearch();
    //}

    $scope.refreshPagination = function (data) {
        $scope.pagination.dataCount = data.Total;
        $scope.pagination.pageCount = data.PageCount;
        var ps = [];
        if ($scope.pagination.pageCount) {
            for (var i = 1; i <= $scope.pagination.pageCount; i++)
                ps.push(i);
        } else
            ps.push(1);
        $scope.pagination.pages = ps;
    };

    $scope.getSearchData = function () {
        var data = $(document.getElementById('conditions_' + $scope.identity)).serialize();
        return data + '&entity=' + $scope.entityName + '&pageIndex=' + ($scope.pagination.pageIndex - 1) + '&pageSize=' + $scope.pageSize;
    }

    $scope.create = function () {
        $scope.$broadcast('createnew');
    }

    $scope.update = function (id) {
        $scope.$broadcast('update', id);
    }

    $scope.delete = function (id) {
        if (!confirm('确定要删除吗?'))
            return;
        $http.post($scope.entityService.url + '/Delete', 'entity=' + $scope.entityName + '&id=' + id).success(function (response) {
            $scope.search();
            ngDialog.open({
                template: '<p>已删除</p>',
                plain: true
            });
        }).error(function () {
            alert('error');
        });
    }

    //$scope.$on('condition-changed', function (e, data) {
    //    $scope.searchData = data;
    //    $scope.search();
    //});
    $scope.$on('config-entity', function (e, data) {
        $scope.entityName = data;
    });

    $scope.$on('config-identity', function (e, data) {
        $scope.identity = data;
        e.stopPropagation();
    });
    $scope.$on('autoload', function (e, data) {
        $scope.search();
    });
    $scope.$on('refresh', function (e, data) {
        $scope.search();
        e.stopPropagation();
    });

    //form
    $scope.save = function () {
        mask(true);
        var data = $(document.getElementById('form_' + $scope.identity)).serialize();
        $http.post($scope.entityService.url + '/Put', data).success(function (response) {
            mask(false);
            $scope.$emit('refresh');
            var dialog = document.getElementById('dialog_' + $scope.identity);
            if (dialog) {
                $(dialog).modal('hide')
            }
            var msg = response;
            if (!msg)
                msg = '已保存';
            msg = '<p>' + msg + '</p>';
            ngDialog.open({
                template: msg,
                plain: true
            });
            $scope.form = {};
            //if (!$scope.isNew)
            //    $scope.update($scope.dataIdentity);
        }).error(function (a, b, c) {
            mask(false);
            alert(a);
        });
    };

    $scope.update = function (id) {
        $scope.form = {};
        document.getElementById('form_' + $scope.identity).reset();
        $scope.isNew = false;
        $scope.dataIdentity = id;
        $http.get($scope.entityService.url + '/Get?id=' + id + '&entity=' + $scope.entityName).success(function (response) {
            var d = {};
            for (var p in response) {
                var v = response[p];
                if (v && v.indexOf && v.indexOf('/Date(') == 0) {
                    d[p] = new Date(parseInt(v.substr(6)));
                } else {
                    d[p] = v;
                }
            }
            $scope.form = d;
            //$scope.form = response;
            $scope.$emit('form-loaded');
        }).error(function (a, b, c) {
            alert('error');
        });
    };

});


eden.controller('scheduleTaskRunCtrl', function ($scope, $http, $state, ngDialog) {
    $scope.run = function (id) {
        mask(true);
        $http.post('/Task/RunNow?id=' + id).success(function (response) {
            mask(false);
            $scope.$parent.search();
        }).error(function () {
            mask(false);
            alert('失败，点击重试');
        });
    }
});

eden.controller('dashboard', function ($scope, $http, $state, ngDialog) {

    var myChart = echarts.init(document.getElementById('chartnewuser'));


    function randomData() {
        now = new Date(+now + oneDay);
        value = value + Math.random() * 21 - 10;
        return {
            name: now.toString(),
            value: [
                [now.getFullYear(), now.getMonth() + 1, now.getDate()].join('-'),
                Math.round(value)
            ]
        }
    }

    var data = [];
    var now = +new Date(2000, 9, 3);
    var oneDay = 24 * 3600 * 1000;
    var value = Math.random() * 1000;
    for (var i = 0; i < 1000; i++) {
        data.push(randomData());
    }

    var myChart = echarts.init(document.getElementById('chartnewuser'));
    var option = {
        title: {
            show: false
        },
        legend: {
            show: false
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                params = params[0];
                var date = new Date(params.name);
                return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear() + ' : ' + params.value[1];
            },
            axisPointer: {
                animation: false
            }
        },
        xAxis: {
            type: 'time',
            splitLine: {
                show: false
            }
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
                show: false
            }
        },
        series: [{
            name: '用户数据',
            type: 'line',
            showSymbol: false,
            hoverAnimation: false,
            data: data
        }]
    };
    myChart.setOption(option);

    var vipChart = echarts.init(document.getElementById('chartvip'));
    vipChart.setOption(option);
});

eden.controller('versionSetting', function ($scope, $http, $state, ngDialog) {

    $scope.loadData = function () {
        mask(true);
        $http.get('/Setting/Version').success(function (response) {
            mask(false);
            $scope.data = response;
        }).error(function () {
            mask(false);
            alert('数据加载失败');
        });
    };

    $scope.save = function () {
        mask(true);
        var data = $(document.getElementById('version-form')).serialize();
        $http.post('/Setting/Version', data).success(function (response) {
            mask(false);
            ngDialog.open({
                template: '已保存',
                plain: true
            });
        }).error(function () {
            mask(false);
            alert('保存失败');
        });
    }

    $scope.loadData();

});

eden.controller('systemSetting', function ($scope, $http, $state, ngDialog) {

    $scope.loadData = function () {
        mask(true);
        $http.get('/Setting/System').success(function (response) {
            mask(false);
            $scope.data = response;
        }).error(function () {
            mask(false);
            alert('数据加载失败');
        });
    };

    $scope.save = function () {
        mask(true);
        var data = $(document.getElementById('system-form')).serialize();
        $http.post('/Setting/System', data).success(function (response) {
            mask(false);
            ngDialog.open({
                template: '已保存',
                plain: true
            });
        }).error(function () {
            mask(false);
            alert('保存失败');
        });
    }

    $scope.loadData();
});

eden.controller('cacheCtrl', function ($scope, $http, $state, ngDialog) {
    $scope.clear = function (id) {
        mask(true);
        $http.post('/Command/ClearCache').success(function (response) {
            mask(false);
            ngDialog.open({
                template: '<p>已清理缓存</p>',
                plain: true
            });
        }).error(function () {
            mask(false);
            alert('失败，点击重试');
        });
    }
});

eden.controller('accountCtrl', function ($scope, $http, $state, ngDialog) {
    $scope.changePwd = function (id) {
        mask(true);
        var data = $(document.getElementById('form_changePassword')).serialize();
        $http.post('/Account/ChangePassword',data).success(function (response) {
            mask(false);
            var dialog = document.getElementById('dialog_changePassword');
            if (dialog) {
                $(dialog).modal('hide')
            }
            ngDialog.open({
                template: '<p>密码已修改</p>',
                plain: true
            });
        }).error(function (e,a,s) {
            mask(false);
            alert(e);
        });
    }
});