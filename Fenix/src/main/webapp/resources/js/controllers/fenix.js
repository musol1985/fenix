materialAdmin



	// =========================================================================
    // Citas
    // =========================================================================
    
    .controller('citasController', function($rootScope, $scope, $http, $q,  prestacionService, userService, citaService, modalService, $uibModal) {
    	$scope.prestaciones=[];
    	$scope.profesionales=[];
    	$scope.cliente;
    	
    	$scope.onPreAjax=function(){
    		return {
	        	centro:userService.getCentro().id
	        };
    	}
    	
    	$scope.onGetEvents=function(start, end){
    		var centro=userService.getCentro();
    		
    		var patron=centro.horario.patrones[start.day()];
    		
    		var res=[];
    		
    		for(var i=0;i<end.diff(start, 'days');i++){
    			var dia=start.clone().add(i,'days').format("YYYY-MM-DD");
	    		patron.horas.forEach(function(value, key) {	    			
	    			res.push({
						id: 'disponible',
						start: dia+" "+value.ini,
						end: dia+" "+value.fin,
						rendering: 'background'
					});
	
	    		});
    		}
    		
    		console.log(res);

    		return res;
    	}
    	
    	$scope.cargarPrestaciones=function(){
    		prestacionService.REST.getAllByCentro(userService.getCentro().id).then(function(res){
        		console.log("Cargando prestaciones...");
        		$scope.prestaciones=res.data;        
            }, function(error){
            	errorService.alertaGrowl("Error al obtener las prestaciones", 'danger');
            });
    	}
    	
    	$scope.cargarProfesionales=function(){
    		userService.getListaByCentro(userService.getCentro().id).then(function(res){
        		console.log("Cargando profesionales...");        		
        		
        		angular.forEach(res.data, function(value, key) {
        			if(value.id==userService.current.id){
        				value.nombre+=" (YO)";  
        				value.grupo="";
        				$scope.profesional=value;
        			}else{
        				value.grupo="Otros profesionales";
        			}
    			});
        		
        		
        		res.data.splice(0, 0, {'id':'-1', 'nombre':'CUALQUIERA', grupo:''});
        		$scope.profesionales=res.data;  
            }, function(error){
            	errorService.alertaGrowl("Error al obtener las prestaciones", 'danger');
            });
    		
    	}
    	
    	$scope.cargarPrestaciones();
    	$scope.cargarProfesionales();
    	
    	$scope.onDragPrestacion=function(item, scope, prestacion){    		
    		$scope.prestacion=prestacion;
    	}
    	
    	
    	$scope.$on('onDragCita', function (event, cita) { 
    		if($scope.cliente && $scope.prestacion && $scope.profesional){
    			var request={};
    			request.fechaIni=cita.start.format("DD/MM/YYYY HH:mm:ss");
    			request.fechaFin=cita.start.add(120, "m").format("DD/MM/YYYY HH:mm:ss");
    			request.centro=userService.getCentro().id;
    			
    			request.prestacion=$scope.prestacion.id;
    			request.cliente=$scope.cliente.id;
    			
    			citaService.nueva(request);
    			
    			cita.color=$scope.prestacion.color;
    			cita.title=$scope.cliente.nombre+" "+$scope.cliente.apellidos;
    			$('#calendar-widget').fullCalendar('renderEvent', cita, true);
    		}else{

    		}    		
        });
    	
    	$scope.$on('onSeleccionarCliente', function (event, cliente) { 
    		$scope.cliente=cliente;
        });
    	
    	
    	$scope.nueva=function(){
    		$scope.modal.mostrar(true);
    	}
    	
    	
    	$scope.modal={
    			
    			data:{profesional:{}}, 		
    			profesionalesModal:[],
    			
    			mostrar:function(datos){
    				angular.copy($scope.profesionales, $scope.modal.profesionalesModal);    			
    				$scope.modal.profesionalesModal=$scope.modal.profesionalesModal.splice(1);
    				
    				//$scope.modal.data=datos;
    				
    				console.log($scope.profesional);
    				$scope.modal.data.profesional=$scope.profesional;
    				console.log($scope.modal.data.profesional);
    				$scope.modalInstance=modalService.mostrar($uibModal, $scope.modal, "resources/template/modals/cita.html");
    			},
    			
    			guardar:function(seleccionar){    				
    				var data=$scope.modal.data;
    				var accion;
    				

    				/*accion=clienteService.nuevo(data);

    				
    				errorService.procesar(accion,{
	    				 0:{
	    					 growl: true,   				 
	    					 texto: "Cliente creado",
	    					 tipo: "success",
	    					 onProcesar: function(res){
	    						 if(seleccionar){
	    							 $scope.cliente=res.data;
	    							 $rootScope.$broadcast('onSeleccionarCliente', res.data);
	    						 }
	    					 }
	    				 },
	    				 2:{
		   					 titulo: "Atención",    				 
		   					 texto: "Ya existe un cliente con ese DNI o NIE",
		   					 tipo: "warning"
		   				 },
		   				onError:function(){
		   					$scope.modal.mostrar(data);
	    				 }
    				});*/
    				
    				$scope.modalInstance.close();
    			}   
    	}
    })
    
    
    // =========================================================================
    // Citas
    // =========================================================================
    
    .controller('coloresController', function($scope, $rootScope) {
    	 //Tags
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
        
        //Select Tag
        $scope.color = '';
        
        $scope.onClickColor = function(tag, $index) {
            $scope.activeState = $index;
            $scope.color = tag;
            
            $rootScope.$broadcast('onSeleccionarColor', tag);
        } 
    })
   
