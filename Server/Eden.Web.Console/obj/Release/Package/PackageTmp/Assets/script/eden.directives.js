eden.directive("dispositionType", function () {
    return {
        restrict: 'EA',
        scope: {
            selectedValue: '=',
            name: '@',
            id: '@',
            cg: '&'
        },
        template: '<select id="{{id}}" name={{name}}><option ng-repeat="t in data" value="{{t.Id}}" ng-selected="selectedValue==t.Id">{{t.Name}}</option></select>',
        controller: function ($scope, $http) {
            $http.post('/EntityRestfull/Search', 'entity=dispositiontype').success(function (response) {
                $scope.data = response.Data;
            }).error(function () {
                alert('error');
            });
        },
        link: function (scope, element, attr, http) {

        }
    }
});

eden.directive("edenCurdConfig", function () {
    return {
        restrict: 'A',
        link: function (scope, element, attr, superCtrl) {
            scope.$emit('config-entity', attr.entity);
            scope.$emit('config-identity', attr.identity);
            if (attr.autoload)
                scope.$emit('autoload', '');
        }
    }
});

eden.directive("sxgTime", function () {
    return {
        restrict: 'AE',
        scope: {
            nameHour: '@',
            nameMinutes: '@',
            selectHour: '=',
            selectMinute: '='
        },
        template: '<select ng-model="selectHour" ng-options="h for h in hours"><option value="">-请选择-</option></select>:' +
                    '<select ng-model="selectMinute" ng-options="m for m in minutes"><option value="">-请选择-</option></select>' +
                    '<input type="hidden" name={{nameHour}} value={{selectHour}}><input type="hidden" name={{nameMinutes}} value={{selectMinute}}>',

        controller: function ($scope) {
            var hs = [];
            for (var i = 0; i < 24; i++) {
                hs.push(i);
            }
            var mis = [];
            for (var i = 0; i < 60; i++) {
                mis.push(i);
            }
            $scope.hours = hs;
            $scope.minutes = mis;
        }
    };
});

eden.directive("edenUploader", function () {
    return {
        restrict: 'AE',
        scope: {
            filter: '@',
            name: '@',
            req: '@'
        },
        template: '<div style=display:{{uploading?"":"none"}}>上传中...</div><div style=display:{{uploading?"none":""}}><input type="file" nv-file-select="" uploader="uploader"/><input type="hidden" name={{name}} value="{{fileName}}"}/></div>',
        controller: function ($scope, FileUploader) {
            $scope.fileName = "qqq";
            if (!$scope.filter)
                $scope.filter = '|jpg|png|jpeg|mp3|mp4|mp4v|';
            var uploader = $scope.uploader = new FileUploader({
                url: 'Media/Upload?category=temp'
            });
            $scope.uploading = false;

            uploader.filters.push({
                name: 'filter',
                fn: function (item /*{File|FileLikeObject}*/, options) {
                    var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
                    return $scope.filter.indexOf(type) !== -1;
                }
            });

            uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/, filter, options) {
                alert("不支持的文件格式，请重新选择文件");
            };
            uploader.onAfterAddingFile = function (fileItem) {
                console.info('onAfterAddingFile', fileItem);
                $scope.uploading = true;
                uploader.uploadAll();
            };
            uploader.onAfterAddingAll = function (addedFileItems) {
                console.info('onAfterAddingAll', addedFileItems);
            };
            uploader.onBeforeUploadItem = function (item) {
                console.info('onBeforeUploadItem', item);
            };
            uploader.onProgressItem = function (fileItem, progress) {
                console.info('onProgressItem', fileItem, progress);
            };
            uploader.onProgressAll = function (progress) {
                console.info('onProgressAll', progress);
            };
            uploader.onSuccessItem = function (fileItem, response, status, headers) {
                console.info('onSuccessItem', fileItem, response, status, headers);
                $scope.fileName = response.name;
                $scope.uploading = false;
            };
            uploader.onErrorItem = function (fileItem, response, status, headers) {
                console.info('onErrorItem', fileItem, response, status, headers);
                $scope.uploading = false;
                alert("上传失败，请重试");
            };
            uploader.onCancelItem = function (fileItem, response, status, headers) {
                console.info('onCancelItem', fileItem, response, status, headers);
                uploader.uploading = false;
            };
            uploader.onCompleteItem = function (fileItem, response, status, headers) {
                console.info('onCompleteItem', fileItem, response, status, headers);
            };
            uploader.onCompleteAll = function () {
                console.info('onCompleteAll');
            };
        }
    };
});

eden.directive("sxgCity", function () {
    return {
        restrict: 'AE',
        scope: {
            name: '@',
            selectedValue: '='
        },
        template: '<select ng-model="selectedProvince" ng-change="selectedCity=\'\'" ng-options="province.name for province in data" ><option value="">-请选择省-</option></select>&nbsp;&nbsp;' +
			     '<select ng-model="selectedCity" ng-change="selectedDistrict=\'\'" ng-options="city.name for city in selectedProvince.items"><option value="">-请选择市-</option></select>&nbsp;&nbsp;' +
			     '<select ng-model="selectedDistrict" ng-change="val=selectedDistrict.id" ng-options="district.name for district in selectedCity.items"><option value="">-请选择区-</option></select>' +
                 '<input name={{name}} required type="hidden" value="{{val}}"/>',
        controller: function ($scope, $http) {
            $http.get('/CoreData/GeographicLocation').success(function (response) {
                $scope.data = eval(response);
                $scope.selectToValue();
            }).error(function () {
                alert('error');
            });

            $scope.$watch('selectedValue', function () {
                $scope.val = $scope.selectedValue;
                $scope.selectToValue();
            });

            $scope.selectToValue = function () {
                if ($scope.data && $scope.selectedValue) {
                    var selectedLink = $scope.findItem($scope.selectedValue);
                    if (selectedLink) {
                        $scope.selectedProvince = selectedLink.province;
                        $scope.selectedCity = selectedLink.city;
                        $scope.selectedDistrict = selectedLink.district;
                    }
                }
            };
            $scope.findItem = function (id) {
                for (var i = 0; i < $scope.data.length; i++) {
                    var p = $scope.data[i];
                    for (var j = 0; j < p.items.length; j++) {
                        var c = p.items[j];
                        for (var k = 0; k < c.items.length; k++) {
                            var d = c.items[k];
                            if (d.id == id) {
                                return { province: p, city: c, district: d };
                            }
                        }
                    }
                }
                return null;
            }
        },
        link: function (scope, element, attr, superCtrl) {

        }
    }
});


eden.directive("sxgSelect", function () {
    return {
        restrict: 'AE',
        scope: {
            name: '@',
            selectedValue: '=',
            datasource: '@'
        },
        template: '<select ng-model="selectValue" ng-options="d.Label for d in data" ><option value="">-请选择-</option></select>' +
                  '<input name={{name}} required type="hidden" value="{{selectValue.Value}}"/>',
        controller: function ($scope, $http) {
            //$scope.$watch('dataSource', function () {
            //    $scope.loadData();
            //});

            $scope.$watch('selectedValue', function () {
                $scope.val = $scope.selectedValue;
                $scope.selectToValue();
            });

            $scope.selectToValue = function () {
                if ($scope.data && $scope.selectedValue) {
                    var sv = $scope.findItem($scope.selectedValue);
                    $scope.selectedValue = sv;
                }
            };
            $scope.findItem = function (id) {
                for (var i = 0; i < $scope.data.length; i++) {
                    var p = $scope.data[i];
                    if (p.Value == id)
                        return p;
                }
                return null;
            };

            if (!$scope.datasource)
                return;
            $http.get($scope.datasource).success(function (response) {
                $scope.data = eval(response);
                $scope.selectToValue();
            }).error(function () {
                alert('error');
            });
        }
    }
});

eden.directive("sxgCheckboxs", function () {
    return {
        restrict: 'AE',
        scope: {
            name: '@',
            selectedValue: '=',
            datasource: '@'
        },
        template: '<div><span ng-repeat="item in data" class="checkboxItem"><input ng-checked="item.checked" type="checkbox" id="checkbox_{{item.Value}}" ng-click="updateValue(item)" value="{{item.Id}}"/><label for="checkbox_{{item.Value}}">{{item.Label}}</label></span></div>' +
                  '<input name={{name}} required type="hidden" value="{{val}}"/>',
        controller: function ($scope, $http) {

            $scope.$watch('selectedValue', function () {
                $scope.val = $scope.selectedValue;
                $scope.selectToValue();
            });

            $scope.selectToValue = function () {
                if ($scope.data) {
                    var vals = $scope.selectedValue ? $scope.selectedValue : '';
                    var newDataToUpdate = [];
                    for (var i = 0; i < $scope.data.length; i++) {
                        var d = $scope.data[i];
                        if (vals.indexOf(d.Value) >= 0) {
                            d.checked = true;
                        } else {
                            d.checked = false;
                        }
                        newDataToUpdate.push(d);
                    }
                    $scope.data = newDataToUpdate;
                    $scope.val = vals;
                }
            };

            $scope.updateValue = function (item) {
                var sv = '';
                for (var i = 0; i < $scope.data.length; i++) {
                    var d = $scope.data[i];
                    if (d.Value == item.Value) {
                        d.checked = !d.checked;
                    }
                    if (d.checked)
                        sv += d.Value + ',';
                }
                $scope.val = sv;
            };

            if (!$scope.datasource)
                return;
            $http.get($scope.datasource).success(function (response) {
                $scope.data = eval(response);
                $scope.selectToValue();
            }).error(function () {
                alert('error');
            });
        },
        link: function (scope, element, attr, superCtrl) {

        }
    }
});

