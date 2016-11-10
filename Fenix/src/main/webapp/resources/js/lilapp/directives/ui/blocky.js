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
	        	$scope.onLoaded();
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
		        	console.log("Cargando WS");
		        	$scope.cargar(workspaceData);
		        }).error(function(){
		        	console.log("Error al obtener el workspace inicial "+$scope.startWorkspace);
		        });	
			}
        	
		}
		
		$scope.cargar=function(txt){
			console.log(txt);
			var xml = Blockly.Xml.textToDom(txt);
			console.log(xml);
        	Blockly.Xml.domToWorkspace(xml, $scope.workspace);
		}
		
		$scope.load();
		$scope.model={};
		
		if($scope.model){
			$scope.model.getCode=function(){
				Blockly.JavaScript.INFINITE_LOOP_TRAP = null;
			    return Blockly.JavaScript.workspaceToCode($scope.workspace);
			}
			
			$scope.model.getXML=function(){
				var xml = Blockly.Xml.workspaceToDom($scope.workspace);
	    		return Blockly.Xml.domToText(xml);
			}
			
			$scope.model.cargarXML=function(txt){
				$scope.cargar(txt);
			}
		}
	})

	.directive('uiBlockly', function($compile, $rootScope) {
		  return {
			restrict: 'E',
			scope: {
			    	toolbox: '@',
			    	width: '@',
			    	height: '@',
			    	startWorkspace: '@',
			    	model: '=',
			    	onLoaded: '&'
			},		  
		    controller: 'uiBlocklyController',
		    controllerAs: 'blockly',		    
		    templateUrl: function(element, attrs) {
		      return attrs.templateUrl || 'uib/template/blockly.html';
		    },
		    transclude: true
		  };
	})