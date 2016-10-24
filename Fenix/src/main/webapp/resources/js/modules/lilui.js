materialAdmin 

    // =========================================================================
    // BLOCKLY
    // =========================================================================
	.controller('uiBlocklyController', function($scope, $http) {
		$scope.load=function(){
			$http({
	            method: 'GET',
	            url: $scope.toolbox
	        }).success(function(toolboxData){
	        	$scope.workspace = Blockly.inject('blocklyDiv', {toolbox: toolboxData});
	        	
	        	$scope.cargarWorkSpace();
	        }).error(function(){
	        	console.log("Error al obtener el toolbox "+$scope.toolbox);
	        });
		}
		
		$scope.cargarWorkSpace=function(){
			if($scope.startWorkspace){
				$http({
		            method: 'GET',
		            url: $scope.startWorkspace
		        }).success(function(workspaceData){
		        	var xml = Blockly.Xml.textToDom(workspaceData);
		        	Blockly.Xml.domToWorkspace(xml, $scope.workspace);
		        }).error(function(){
		        	console.log("Error al obtener el workspace inicial "+$scope.startWorkspace);
		        });	
			}
        	
		}
		
		$scope.load();
	})

	.directive('uiBlockly', function($compile, $rootScope) {
		  return {
			restrict: 'E',
			scope: {
			    	toolbox: '@',
			    	width: '@',
			    	height: '@',
			    	startWorkspace: '@'
			},		  
		    controller: 'uiBlocklyController',
		    controllerAs: 'blockly',		    
		    templateUrl: function(element, attrs) {
		      return attrs.templateUrl || 'uib/template/blockly.html';
		    },
		    transclude: true
		  };
	})
    