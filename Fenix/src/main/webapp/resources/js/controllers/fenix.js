materialAdmin
.controller('contextoCtrl', function($scope, $rootScope, $http, $timeout, errorService, messageService, clienteService, userService, modalService, $uibModal) {
    	
    	$scope.cliente;      
        
        this.buscarCliente=function(valor){
        	return clienteService.buscar(valor, userService.getCentro().id);        	
        	
        }
        
        this.seleccionarCliente=function($item, $model){
        	if($item.id==-1){
        		this.crearCliente($item.busqueda);
        	}else{
        		console.log("Cliente seleccionado");
        		console.log($item);
        		$scope.cliente=$item;
        		
        		$rootScope.$broadcast('onSeleccionarCliente', $item);
        	}   
        	
        	angular.element('#header').removeClass('search-toggled');
        }
        
        this.crearCliente=function(valor){
        	if(this.isDNInoLetra(valor)){
        		valor+=this.calcularLetraDNI(valor);
        	}
        	
        	var dni=this.isDNIoNIE(valor)?valor:"";
        	var apellidos="";
        	var nombre="";
        	
        	if(this.isTexto(valor)){
        		if(valor.indexOf(" ")>-1){
        			nombre=valor.split(" ")[0];
        			apellidos=valor.split(" ")[1];
        		}else{
        			nombre=valor;
        		}
        	}
        	
        	var telefono=this.isTelefono(valor)?valor:"";
        	
        	
        	var data={dni:dni, nombre:nombre, sexo:1, telefono:telefono, apellidos:apellidos, centro:userService.getCentro().id};
        	$scope.modal.mostrar(data);
        }
        
        this.calcularLetraDNI=function(valor){
        	cadena="TRWAGMYFPDXBNJZSQVHLCKET" 
    		posicion = valor % 23 
    		return cadena.substring(posicion,posicion+1) 
        }
        
        this.isDNInoLetra=function(valor){
        	var regexp = /^\d{8}$/;
        	return regexp.test(valor);  
        }
        
        this.isDNIoNIE=function(valor){
        	var regexp = /(^\d{8}[A-Z]$|[A-Z]\d{7}[A-Z])/;
        	return regexp.test(valor);        	
        }
        
        this.isTexto=function(valor){
        	var regexp =/^\D+$/;
        	return regexp.test(valor);        	
        }
        
        this.isTelefono=function(valor){
        	var regexp = /^6\d{8}$/;
        	return regexp.test(valor);        	
        }
        
        this.hasCliente=function(){
        	return $scope.cliente!=undefined;
        }
        
        this.getCliente=function(){
        	return $scope.cliente;
        }
        
        this.getNombreCliente=function(){
        	if(this.hasCliente())
        		return this.getCliente().nombre+" "+this.getCliente().apellidos;
        	return "";
        }
        
        this.cancelarSeleccion=function(){
        	$scope.cliente=undefined;
        	$rootScope.$broadcast('onSeleccionarCliente', undefined);
        }
        
        this.modificarCliente=function(){
        	if(this.hasCliente()){
        		$scope.modal.mostrar($scope.cliente);
        	}
        }
        
        
        $scope.modal={
    			data:{}, 			    		
    			
    			mostrar:function(datos){
    				$scope.modal.data=datos;
    				$scope.modalInstance=modalService.mostrar($uibModal, $scope.modal, "resources/template/modals/cliente.html");
    			},
    			
    			guardar:function(seleccionar){    				
    				var data=$scope.modal.data;
    				var accion;
    				

    				accion=clienteService.nuevo(data);

    				
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
    				});
    				
    				$scope.modalInstance.close();
    			}   
    	}
    	

})


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
    		
    		patron.horas.forEach(function(value, key) {
    			console.log(start);
    			console.log(start.add(value.ini.horas, "h"));
    			console.log(start.add(value.ini.horas, "h").add(value.ini.minutos,"m"));
    			console.log(start.add(value.ini.horas, "h").add(value.ini.minutos,"m"));
    			var s=start.clone().add(value.ini.horas, "h").add(value.ini.minutos,"m");
    			console.log(s);
    			var e=start.clone().add(value.fin.horas, "h").add(value.fin.minutos,"m");
    			console.log(e);
    			
    			res.push({
					id: 'availableForMeeting',
					start: s,
					end: e,
					rendering: 'background',
					color: '#257e4a'
				});

    		});
    		
    		console.log(res);

    		return res;
    	}
    	
    	$scope.cargarPrestaciones=function(){
    		prestacionService.getAllByCentro(userService.getCentro().id).then(function(res){
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
   
