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
		$scope.model={};
		
		if($scope.model){
			$scope.model.getCode=function(){
				Blockly.JavaScript.INFINITE_LOOP_TRAP = null;
			    return Blockly.JavaScript.workspaceToCode($scope.workspace);
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
			    	model: '='
			},		  
		    controller: 'uiBlocklyController',
		    controllerAs: 'blockly',		    
		    templateUrl: function(element, attrs) {
		      return attrs.templateUrl || 'uib/template/blockly.html';
		    },
		    transclude: true
		  };
	});
materialAdmin 
	// =========================================================================
    // CARD
    // =========================================================================
    .controller('uicardController', function($q, $filter, $timeout,$scope, $http, ngTableParams, userService, centroService, prestacionService, errorService) {
    	
	})
	
	.directive('uicard', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
			    	model: '=',
			    	titulo: '@',
			    	descripcion: '@',
			    	onAction: '&'
			},		  
		    controller: 'uicardController',
		    controllerAs: 'ctrl',		    
		    templateUrl: function(element, attrs) {
		      return attrs.templateUrl || 'uib/template/card.html';
		    },
		    transclude: {
		    	'ul':'ul',		    
		    	'cuerpo':'cuerpo'
		    }
		  };
	})
	
	
	;
materialAdmin 
	// =========================================================================
    // LISTA
    // =========================================================================
    .controller('uilistaController', function($q, $filter, $timeout,$scope, $http, ngTableParams, userService, centroService, prestacionService, errorService) {
    	$scope.seleccionar=function(item){
    		item.seleccionado=true;
    		
    		$scope.onSeleccionar({item:item});
    	}
	})
	
	.directive('uilista', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
					onSeleccionar: '&',
			    	datos: '='
			},		  
		    controller: 'uilistaController',
		    controllerAs: 'ctrl',		    
		    templateUrl: function(element, attrs) {
		      return attrs.templateUrl || 'uib/template/lista.html';
		    },
		    transclude: {
		    	'ul':'ul',
		    	'listaitem':'listaitem'
		    }
		  };
	})
	
	
	;
materialAdmin 
	// =========================================================================
    // LISTA Item
    // =========================================================================

	
	.directive('uilistaitem', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
					titulo: '=',
			    	color: '=',
			    	descripcion: '=',
			    	subDescripcion: '='
			},    
		    templateUrl: function(element, attrs) {
		      return attrs.templateUrl || 'uib/template/listaitem.html';
		    }
		  };
	})
	
	
	