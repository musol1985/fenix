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
    .controller('uicardController', function($q, $filter, $timeout,$scope, $http, ngTableParams, userService, centroService, prestacionService, errorService) {
		/*$scope.model={};
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
		$scope.tabla=this.tabla;*/
    	$scope.model={};
    	$scope.model.datos = [];

    	  $scope.callServer = function callServer(tableState) {

    		  $scope.isLoading = true;

    	    var pagination = tableState.pagination;

    	    var start = pagination.start || 0;     // This is NOT the page number, but the index of item in the list that you want to use to display the table.
    	    var number = pagination.number || 10;  // Number of entries showed per page.

    	    $scope.cargarDatos(start+1, number, function(data, pages){
    	    	$scope.model.datos = data;
    	    	tableState.pagination.numberOfPages = pages;
    	    	$scope.isLoading = false;
    	    });
    	  };
    	  
    	  $scope.cargarDatos=function(start, count, onComplete){
	      		prestacionService.getByCentro(userService.getCentro().id, start, count).then(function(res){	      			
	          		onComplete(res.data, res.paginas);
	              }, function(error){
	              	errorService.alertaGrowl("Error al obtener prestaciones", 'danger');
	              });
	      	}
	})
	
	.directive('uicard', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
			    	model: '=',
			    	titulo: '@',
			    	descripcion: '@',
			    	onRefrescar: '&',
			    	onNuevo: '&'
			},		  
		    controller: 'uicardController',
		    controllerAs: 'ctrl',		    
		    templateUrl: function(element, attrs) {
		      return attrs.templateUrl || 'uib/template/card.html';
		    },
		    transclude: true
		  };
	})
	