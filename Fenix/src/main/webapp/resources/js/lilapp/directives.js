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
	        	if(!$scope.onLoaded())	        	
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
.directive('calendario', function($compile, $rootScope, userService, $templateCache, PreferenciasService){
    return {
        restrict: 'EA',
        scope: {
            procesarDia: '&',
            model: '=',
            height: '@',
            sources: '&',
            onLoaded: '&?', 
            onDrop: '&',
            onProcesarCita: '&?',
            onRender: '&?',
            onClick:'&?',
            onResize: '&?',
            onSelect: '&?'
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
            
        	var FC = $.fullCalendar;
        	
        	var grid=FC.DayGrid.extend({
      		
        		renderMoreLink: function(row, col, hiddenSegs) {
        			var dia=hiddenSegs[0].event.start;
        			var item=$('<a class="fc-event fc-draggable"><div class="fc-content"><span class="fc-title">'+row+' citas</span></div></a>');
        			item.on('click', function(ev) {
        				onClickCustom(dia);
					});
        			return item;        						
        		},
        		fgSegHtml: function(seg, disableResizing) {        			
        			var texto=(seg.event.id=="festivo")?"Festivo":"1 cita";        		
        			
        			return '<a class="fc-event fc-draggable"><div class="fc-content"><span class="fc-title">' + texto +'</span></div></a>';
        			
        		}
        	});
        	
        	var onClickCustom=function(dia){
        		element.fullCalendar('zoomTo', dia, 'agenda');
            	scope.$apply();
        	}
        	
        	FC.CustomView=FC.MonthView.extend({
        		dayGridClass:grid
        	});
        	
        	FC.views.custom = {
        			'class': FC.CustomView,
        			duration: { months: 1 }, // important for prev/next
        			defaults: {
        				fixedWeekCount: true
        			}
        		}
            
            scope.sourceHorario=function(start, end, timezone, callback){     
					var eventos=[];        					
					
					for(var i=0;i<end.diff(start, 'days');i++){
		    			var dia=start.clone().add(i,'days');
		    			var events=scope.procesarDia({dia:dia});
		    			if(events)
		    				eventos=eventos.concat(events);
		    			
		    		}
					console.log(eventos);
					callback(eventos);        					
				};
			sources.push(scope.sourceHorario);
            

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
                header: false,
                nowIndicator:true,
                defaultView: PreferenciasService.getVistaCalendario(),
                lang: 'es',
                allDaySlot: false,
                theme: true, //Do not remove this as it ruin the design
                selectable: true,
                selectHelper: true,
                editable: true,
                droppable: true,
                eventLimit: 1,
                height:function(){
                	return $(window).height()-scope.height;
                },
                slotDuration:PreferenciasService.getHuecosCalendario(),
                navLinks: true,
                navLinkDayClick: 'agendaWeek',
                eventReceive : function(event){     
                	scope.onDrop({evento:event,fecha:event.start});
                	element.fullCalendar('removeEvents', event._id);
                },
                eventDataTransform:function(data){                	
                	if(data && !data.rendering){
                		if(scope.onProcesarCita){
                			return scope.onProcesarCita({cita:data});
                		}
                	}
                	return data;
                },
                eventRender: function(event, ele) {
                    if(scope.onRender && element.fullCalendar("getView").name!="custom"){
                    	var evento={event:event, element:ele};
                    	scope.onRender({evento:evento});
                    }
                 },
                 eventClick: function(calEvent, jsEvent, view) {                	 
                	 if(scope.onClick){
                		 if(view.name!="custom"){
                			 scope.onClick({cita:calEvent});
                		 }else{
                			 onClickCustom(calEvent.start);
                		 }
                	 }                	 
                 },
                 viewRender:function(){
                	 scope.model.fecha=element.fullCalendar('getDate').format('dddd DD, MMM [de] YYYY');
                 },
                 eventResize:function(event, delta, revert){
                	 if(scope.onResize){
                		 scope.onResize({cita:event, revert:revert});
                	 }
                 },
                 eventDrop:function(event, delta, revert){
                	 if(scope.onResize){
                		 scope.onResize({cita:event, revert:revert});
                	 }
                 },
                 select:function(start, end){
                	 if(scope.onSelect){
                		 scope.onSelect({start:start, end:end});
                		 element.fullCalendar('unselect');
                	 }
                 }
                 
            });  
            
            scope.model.iniciar=function(){
            	angular.forEach(sources, function(source) {
            		element.fullCalendar('addEventSource',source);   				
    			});
            	
            	if(scope.onLoaded){
            		scope.onLoaded();
            	}
            }
            
            scope.model.onCambiarHorario=function(){
            	console.log("cambiando horario...");
            	element.fullCalendar( 'refetchEventSources', scope.sourceHorario )
            }
            
            scope.model.addCita=function(cita){
            	element.fullCalendar('renderEvent', cita, true);
            }
            
            scope.model.actualizarCita=function(cita){
            	element.fullCalendar('updateEvent', cita);
            }
            
            scope.model.modificarCita=function(cita){
            	scope.model.removeCita(cita.id);
        		cita.source=undefined;
        		scope.model.addCita(cita);
            }
            
            scope.model.removeCita=function(id){
            	element.fullCalendar('removeEvents', id);
            }
            
            scope.model.getCita=function(id){
            	return element.fullCalendar('clientEvents', id);
            }
            
            scope.model.siguiente=function(){
            	element.fullCalendar('next');
            }
            
            scope.model.anterior=function(){
            	element.fullCalendar('prev');
            }
            
            scope.model.verMes=function(){
            	element.fullCalendar( 'changeView', 'custom' );
            }
            
            scope.model.cambiarEstadoCita=function(cita, estado, actualizarCalendario){
            	cita.estado=estado.toUpperCase();
        		cita.icono="zmdi-"+estado.toLowerCase();
        		cita.className="fc-"+estado.toLowerCase();
        		
        		if(actualizarCalendario){
        			scope.model.modificarCita(cita);
        		}
            }
        }
        
    }
})
    // =========================================================================
    // VISTA CALENDARIO
    // =========================================================================
.directive('vistaCalendario', function(PreferenciasService){
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            element.on('click', function(){
            	PreferenciasService.setVistaCalendario(attrs.nombre);
                $('#calendar-widget').fullCalendar('changeView',attrs.nombre);                
            })
        }
    }
})
    // =========================================================================
    // Hueco CALENDARIO
    // =========================================================================
.directive('huecosCalendario', function(PreferenciasService){
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            element.on('click', function(){
            	PreferenciasService.setHuecosCalendario("00:"+attrs.minutos+":00");
                $('#calendar-widget').fullCalendar('option','slotDuration', "00:"+attrs.minutos+":00");                
            })
        }
    }
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
	});
materialAdmin 
	// =========================================================================
    // COLORES
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
	    	if($scope.onSeleccionar)
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
	// =========================================================================
    // POPUP
    // =========================================================================	
	materialAdmin .controller('uiInputController', function($sce, $scope,$uibModal, modalService) {
		$scope.getFormItem=function(){
			return $scope.form[$scope.nombre];			
		}			
		
		$scope.getInputCols=function(){
			if($scope.labelCols)
				return $scope.cols;
			return "";
		}
		
		$scope.getCols=function(){
			if(!$scope.labelCols)
				return $scope.cols;
			return "";
		}
	})
	
	.directive('uiInput', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
			    	form: '=',
			    	nombre: '@',
			    	label: '@',
			    	min: '@?',
			    	max: '@?',
			    	tipo: '@?',
			    	model: '=',
			    	placeholder: '@?',
			    	extra: '=?',
			    	cols: '@?',
			    	labelCols: '@?'
			},	
			templateUrl: function(element, attrs) {
			      return attrs.templateUrl || 'uib/template/input.html';
			},
		    controller: 'uiInputController',
		    controllerAs: 'ctrl',
		    link: function(scope, element, attrs) {
		    	if(attrs.obligatorio=="")scope.obligatorio=true;
		    	
		    	if(!scope.min)scope.min="0";
		    	if(!scope.max)scope.max="3200";
		    	if(!scope.tipo)scope.tipo="text";
		   }
		    /*link: function(scope, element, attrs) {

		    	 var input = element.find('input');
		    	 if(attrs.obligatorio)
		    		 input.attr('required','true');
		    	 if(scope.minLength)
		    		 input.attr('ng-minlength',scope.minLength);
		    	 //input = $compile(input)(scope);
		    	 //element.find('input').replaceWith(input);
		    }*/
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
	
	
	;
	// =========================================================================
    // POPUP
    // =========================================================================	
	materialAdmin .controller('uipopupController', function($scope,$uibModal, modalService) {
		$scope.modificando=function(data){
			if(!data)
				return false;
			
			if($scope.isUpdating){
				return $scope.isUpdating({data:data});
			}else if(data.id && data.id!=''){
				return true;
			}
			return false;
		}

		$scope.popup.mostrar=function(data){ 
			if($scope.modificando(data)){
				data=angular.copy(data);
			}
			
			$scope.popup.data=data;

			$scope.modalInstance=modalService.mostrar($uibModal, $scope.popup, $scope.template);
			$scope.popup.abierto=true;
		}

		$scope.popup.guardar=function(){
			var params={data:$scope.popup.data};
			if($scope.onPreSave)
				$scope.onPreSave(params);
			
			var accion;
			var modificando=false;
			
			if(!$scope.modificando(params.data)){
				accion=$scope.onNew(params);
			}else{
				accion=$scope.onUpdate(params);
				modificando=true;
			}
			
			if($scope.onPostSave && accion)
				$scope.onPostSave({data: params.data, accion:accion, modificando:modificando, params:params})

				$scope.popup.cerrar();
		}
		
		$scope.popup.showPostError=function(){
			$scope.modalInstance=modalService.mostrar($uibModal, $scope.popup, $scope.template);
		}
		
		$scope.popup.refrescar=function(){
			$scope.refrescarTabla.tabla.reload();
		}
    	
		$scope.popup.cerrar=function(){
			$scope.popup.abierto=false;
			$scope.modalInstance.close();
		}
		
		$scope.popup.isAbierto=function(){
			return $scope.popup.abierto;
		}
			
		$scope.popup.abierto=false;
	})
	
	.directive('uiPopup', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
			    	popup: '=',
			    	template: '@',
			    	isUpdating: '&?',
			    	onNew: '&',
			    	onUpdate: '&',
			    	onPreSave: '&?',
			    	onPostSave: '&?',
			    	refrescarTabla: '='
			},		  
		    controller: 'uipopupController',
		    controllerAs: 'ctrl'
		  };
	})
	