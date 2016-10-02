
'use strict';
// declare a module
var app = angular.module('app', []);

app.controller('AppController', ['$scope', '$document', AppController]);

function AppController($scope) {
    $scope.message = "Hello World (from Closure Compiler)!";
    $scope.templateMessage = "Hello from a template file!";
}
