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
	});
materialAdmin 
    // =========================================================================
    // CALENDARIO
    // =========================================================================
.directive('calendario', function($compile, $rootScope, userService, $templateCache){
    return {
        restrict: 'EA',
        scope: {
            procesarDia: '&',
            model: '=',
            height: '@',
            sources: '&'
        },
        transclude: true,
        link: function(scope, element, attrs) {
        	scope.model={};
        	scope.model.actualizar=function(){
        		element.fullCalendar( 'refetchEvents' )
            }
            
            var date = new Date();
            var d = date.getDate();
            var m = date.getMonth();
            var y = date.getFullYear();
            
        	var sources=scope.sources();
        	if(!sources){
        		sources=[];
        	}
            
            
            sources.push(
            	function(start, end, timezone, callback){     
					var eventos=[];        					
					
					for(var i=0;i<end.diff(start, 'days');i++){
		    			var dia=start.clone().add(i,'days');
		    			var events=scope.procesarDia({dia:dia});
		    			if(events)
		    				eventos=eventos.concat(events);
		    			
		    		}
					console.log(eventos);
					callback(eventos);        					
				});
            
            

            //Generate the Calendar
            element.fullCalendar({
            	customButtons: {
                    btnMonth: {
                    	icon:'circle-triangle-d',
                    	themeIcon :'circle-triangle-d',     
                        click: function() {
                        	 element.fullCalendar( 'changeView', 'month' );
                        }
                    }
                },
                header: {
                    right: '',
                    center: 'prev, title, btnMonth, next',
                    left: 'today'
                },
                defaultView: 'month',
                lang: 'es',
                allDaySlot: false,
                theme: true, //Do not remove this as it ruin the design
                selectable: true,
                selectHelper: true,
                editable: true,
                droppable: true,
                slotDuration:'00:15:00',
                height: parseInt(scope.height),
                navLinks: true,
                navLinkDayClick: 'agendaWeek',
                eventSources: sources
            });  

        }
        
    }
});
materialAdmin 
	// =========================================================================
    // CARD
    // =========================================================================
    .controller('uiColoresController', function($scope) {
	    if (angular.isUndefined($scope.colores)){
	    	 $scope.colores = [
	             'teal',
	             'red',
	             'pink',
	             'blue',
	             'lime',
	             'green',
	             'cyan',
	             'orange',
	             'purple',
	             'gray',
	             'black',
	         ]
	    }	
	    
	    $scope.onClickColor=function(tag, $index){
	    	$scope.model=tag;
	    	$scope.activeState = $index;
	    	$scope.onSeleccionar({color:tag, index:$index});	    	
	    }
	    
	    function iniciar(){
	    	$scope.activeState=$scope.colores.indexOf($scope.model);
	    }
	    
	    iniciar();	    
	})
	
	.directive('uicolores', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
			    	model: '=',
			    	colores: '@?',
			    	onSeleccionar: '&?'
			},		  
		    controller: 'uiColoresController',
		    controllerAs: 'ctrl',		    
		    templateUrl: function(element, attrs) {
		      return attrs.templateUrl || 'uib/template/colores.html';
		    }
		  };
	})
	
	
	;
materialAdmin 
	// =========================================================================
    // CARD
    // =========================================================================
    .controller('uicardController', function($scope) {
	    if (angular.isUndefined($scope.ocultarAction))
	        $scope.ocultarAction = false;
    	
	})
	
	.directive('uicard', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
			    	model: '=',
			    	titulo: '@',
			    	descripcion: '@',
			    	onAction: '&?',
			    	ocultarAction: '@?' 
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
	
	
	