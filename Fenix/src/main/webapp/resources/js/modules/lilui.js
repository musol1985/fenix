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
	})
	
	
	// =========================================================================
    // MANTENIMIENTO
    // =========================================================================
    .controller('uimantenimientoController', function($scope, $http, ngTableParams, userService, centroService, prestacionService, errorService) {
		$scope.model={};
		var self=this;

		$scope.model.getCode=function(){
			Blockly.JavaScript.INFINITE_LOOP_TRAP = null;
		    return Blockly.JavaScript.workspaceToCode($scope.workspace);
		}
		
		$scope.refrescar=function(){
			if($scope.onRefrescar)
				$scope.onRefrescar({test:"dddd"});			
		}
		
		$scope.nuevo=function(){
			if($scope.onNuevo)
				$scope.onNuevo();
		}
		
		//self.datos=$scope.model.datos;

		this.tabla=new ngTableParams({
            page: 1,            // show first page
            count: 10          // count per page
        }, {
        	getData: function($defer, params) {
        		$scope.getDatos({params:params, callback:function(data, total){
        			self.datos=data;
            		params.total(total); 
            		$scope.model.datos=self.datos;
        		}});
        		$defer.resolve(self.datos);            		
            }
        });
		$scope.tabla=this.tabla;
		
		
	})
	
	.directive('uimantenimiento', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
			    	model: '=',
			    	nombre: '@',
			    	descripcion: '@',
			    	onRefrescar: '&',
			    	onNuevo: '&',
			    	getDatos: '&'
			},		  
		    controller: 'uimantenimientoController',
		    controllerAs: 'mant',		    
		    templateUrl: function(element, attrs) {
		      return attrs.templateUrl || 'uib/template/mantenimiento.html';
		    },
		    transclude: true
		  };
	})