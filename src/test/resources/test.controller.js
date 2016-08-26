/* global angular */
(function() {
    'use strict';
    // declare a module
    var myAppModule = angular.module('exampleApp', []);

    myAppModule.controller('AppController', ['$scope', '$document', AppController]);

    function AppController($scope) {
        $scope.message = "Hello World (from Closure Compiler)!";
    }
})();