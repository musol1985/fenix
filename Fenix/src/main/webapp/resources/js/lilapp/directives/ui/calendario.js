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
            onClick:'&?'
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
                	if(!data.rendering){
                		if(scope.onProcesarCita){
                			return scope.onProcesarCita({cita:data});
                		}
                	}
                	return data;
                },
                eventRender: function(event, element) {
                    if(scope.onRender){
                    	var evento={event:event, element:element};
                    	scope.onRender({evento:evento});
                    }
                 },
                 eventClick: function(calEvent, jsEvent, view) {
                	 if(scope.onClick){                		 
                		 scope.onClick({cita:calEvent});
                	 }                	 
                 },
                 viewRender:function(){
                	 scope.model.fecha=element.fullCalendar('getDate').format('dddd DD, MMM [de] YYYY');
                 }/*,
                 dayRender:function( date, cell ) { 
 
                	 console.log(date.format("DD/MM/YYYY"));
                	 if(date.format("DD/MM/YYYY")=="28/12/2016"){
                	 console.log(cell.hasClass( "fc-event-container" ));
                	 debugger;
                	 }
                	 //console.log($(cell).find( ".fc-event" ).length)
                 }*/
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
            	element.fullCalendar( 'changeView', 'month' );
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
    