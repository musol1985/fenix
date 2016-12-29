materialAdmin 

    // =========================================================================
    // CALENDAR WIDGET
    // =========================================================================

    .directive('fullCalendar', function(){
        return {
            restrict: 'A',
            link: function(scope, element) {
                element.fullCalendar({
                    contentHeight: 'auto',
                    theme: true,
                    header: {
                        right: '',
                        center: 'prev, title, next',
                        left: ''
                    },
                    defaultDate: '2014-06-12',
                    editable: true,
                    events: [
                        {
                            title: 'All Day',
                            start: '2014-06-01',
                            className: 'bgm-cyan'
                        },
                        {
                            title: 'Long Event',
                            start: '2014-06-07',
                            end: '2014-06-10',
                            className: 'bgm-orange'
                        },
                        {
                            id: 999,
                            title: 'Repeat',
                            start: '2014-06-09',
                            className: 'bgm-lightgreen'
                        },
                        {
                            id: 999,
                            title: 'Repeat',
                            start: '2014-06-16',
                            className: 'bgm-blue'
                        },
                        {
                            title: 'Meet',
                            start: '2014-06-12',
                            end: '2014-06-12',
                            className: 'bgm-teal'
                        },
                        {
                            title: 'Lunch',
                            start: '2014-06-12',
                            className: 'bgm-gray'
                        },
                        {
                            title: 'Birthday',
                            start: '2014-06-13',
                            className: 'bgm-pink'
                        },
                        {
                            title: 'Google',
                            url: 'http://google.com/',
                            start: '2014-06-28',
                            className: 'bgm-bluegray'
                        }
                    ]
                });
            }
        }
    })
    

    // =========================================================================
    // MAIN CALENDAR
    // =========================================================================

    .directive('calendar', function($compile){
        return {
            restrict: 'A',
            scope: {
                select: '&',
                actionLinks: '=',
            },
            link: function(scope, element, attrs) {
                
                var date = new Date();
                var d = date.getDate();
                var m = date.getMonth();
                var y = date.getFullYear();

                //Generate the Calendar
                element.fullCalendar({
                    header: {
                        right: '',
                        center: 'prev, title, next',
                        left: ''
                    },

                    theme: true, //Do not remove this as it ruin the design
                    selectable: true,
                    selectHelper: true,
                    editable: true,

                    //Add Events
                    events: [
                        {
                            title: 'Hangout with friends',
                            start: new Date(y, m, 1),
                            allDay: true,
                            className: 'bgm-cyan'
                        },
                        {
                            title: 'Meeting with client',
                            start: new Date(y, m, 10),
                            allDay: true,
                            className: 'bgm-red'
                        },
                        {
                            title: 'Repeat Event',
                            start: new Date(y, m, 18),
                            allDay: true,
                            className: 'bgm-blue'
                        },
                        {
                            title: 'Semester Exam',
                            start: new Date(y, m, 20),
                            allDay: true,
                            className: 'bgm-green'
                        },
                        {
                            title: 'Soccor match',
                            start: new Date(y, m, 5),
                            allDay: true,
                            className: 'bgm-purple'
                        },
                        {
                            title: 'Coffee time',
                            start: new Date(y, m, 21),
                            allDay: true,
                            className: 'bgm-orange'
                        },
                        {
                            title: 'Job Interview',
                            start: new Date(y, m, 5),
                            allDay: true,
                            className: 'bgm-dark'
                        },
                        {
                            title: 'IT Meeting',
                            start: new Date(y, m, 5),
                            allDay: true,
                            className: 'bgm-cyan'
                        },
                        {
                            title: 'Brunch at Beach',
                            start: new Date(y, m, 1),
                            allDay: true,
                            className: 'bgm-purple'
                        },
                        {
                            title: 'Live TV Show',
                            start: new Date(y, m, 15),
                            allDay: true,
                            className: 'bgm-orange'
                        },
                        {
                            title: 'Software Conference',
                            start: new Date(y, m, 25),
                            allDay: true,
                            className: 'bgm-blue'
                        },
                        {
                            title: 'Coffee time',
                            start: new Date(y, m, 30),
                            allDay: true,
                            className: 'bgm-orange'
                        },
                        {
                            title: 'Job Interview',
                            start: new Date(y, m, 30),
                            allDay: true,
                            className: 'bgm-dark'
                        },
                    ],

                    //On Day Select
                    select: function(start, end, allDay) {
                        scope.select({
                            start: start, 
                            end: end
                        });
                    }
                });
                
                  
                //Add action links in calendar header
                element.find('.fc-toolbar').append($compile(scope.actionLinks)(scope));
            }
        }
    })
    

    //Change Calendar Views
    .directive('calendarView', function(){
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                element.on('click', function(){
                    $('#calendar').fullCalendar('changeView', attrs.calendarView);  
                })
            }
        }
    })
    
    
    // =========================================================================
    // CALENDARIO
    // =========================================================================
    .directive('testcalendario', function($compile, $rootScope, userService, $templateCache){
        return {
            restrict: 'A',
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
                
                if($scope.sources){
                	sources=$scope.sources();
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

    
    // =========================================================================
    // CALENDARIO
    // =========================================================================
    .directive('calendarioOld', function($compile, $rootScope, userService, $templateCache){
        return {
            restrict: 'A',
            scope: {
                select: '&',
                actionLinks: '=',
                onPreAjax: '&',
                onGetEvents: '&'
            },
            link: function(scope, element, attrs) {
                
                var date = new Date();
                var d = date.getDate();
                var m = date.getMonth();
                var y = date.getFullYear();
                
                var FC = $.fullCalendar; // a reference to FullCalendar's root namespace
                var View = FC.BasicView;      // the class that all views must inherit from
                var customMonthView= View.extend({});
                
                FC.views.customMes = {
                		'class': customMonthView,
                		duration: { months: 1 }, // important for prev/next
                		defaults: {
                			fixedWeekCount: true
                		}
                	};

                //Generate the Calendar
                element.fullCalendar({
                	customButtons: {
                        myCustomButton: {
                        	icon:'circle-triangle-d',
                        	themeIcon :'circle-triangle-d',     
                            click: function() {
                                alert('clicked the custom button!');
                            }
                        }
                    },
                    header: {
                        right: '',
                        center: 'prev, title, myCustomButton, next',
                        left: 'today'
                    },
                    defaultView: 'agendaDay',
                    lang: 'es',
                    allDaySlot: false,
                    theme: true, //Do not remove this as it ruin the design
                    selectable: true,
                    selectHelper: true,
                    editable: true,
                    droppable: true,
                    slotDuration:'00:10:00',
                    drop: function(date) { // this function is called when something is dropped
            			
        				// retrieve the dropped element's stored Event Object
        				var originalEventObject = $(this).data('eventObject');
        				
        				// we need to copy it, so that multiple events don't have a reference to the same object
        				var copiedEventObject = $.extend({}, originalEventObject);
        				
        				// assign it the date that was reported
        				copiedEventObject.start = date;
        				
        				// render the event on the calendar
        				// the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
        				//$('#calendar-widget').fullCalendar('renderEvent', copiedEventObject, true);
        				console.log("drag in calendar: ");
        				console.log($(this));
        				$rootScope.$broadcast('onDragCita', copiedEventObject);
        			},
        			eventSources: [{
        		        url: 'cita/in',
        		        type: 'GET',
        		        data: scope.onPreAjax,
        		        error: function() {
        		            	alert('there was an error while fetching events!');
        		        	}
        				},
        				function(start, end, timezone, callback){        					
        					callback(scope.onGetEvents({start:start, end:end}));
        				}        		                		
        				]
        			,
                    //On Day Select
                    select: function(start, end, allDay) {
                        scope.select({
                            start: start, 
                            end: end
                        });
                    },
                    /*eventRender: function(event, element) {
                        console.log("!!!!"+element);
                        element.append("<div class='lv-avatar bgm-red pull-left'>P</div>");
                    },*/
                    eventDataTransform:function(evento){
                    	evento.constraint="disponible";
                    	console.log("->"+evento);
                    	return evento;
                    }
                });
                
                  
                //Add action links in calendar header
                element.find('.fc-toolbar').append($compile(scope.actionLinks)(scope));
                
               /* var template=$templateCache.get("resources/template/datepicker/calendar.html");
                element.find('.fc-toolbar').append($compile(template)(scope));*/
                
            }
            
        }
    })
   

    //Vista
    
    
    
    