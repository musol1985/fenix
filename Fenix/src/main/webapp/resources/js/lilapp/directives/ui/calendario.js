materialAdmin 
    // =========================================================================
    // BLOCKLY
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
            
            var sources=[];
            
            if(scope.sources){
            	sources=scope.sources();
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
})