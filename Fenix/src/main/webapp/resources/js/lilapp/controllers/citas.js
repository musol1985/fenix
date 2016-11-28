// =========================================================================
// Citas
// =========================================================================
materialAdmin
    .controller('citasController', function($scope, $document, prestacionService, userService,clienteService, horarioService, citaService, modalService, $uibModal, uibDateParser ) {
    	$scope.maestros={
    			profesionales:[],
    			horarios:[],
    			prestaciones:[]
    	};
    	
    	$scope.cliente;    	
    	
    	$scope.prestacion={};
    	
    	$scope.getSource=function(){
    		return [{
		        url: 'cita/in',
		        type: 'GET',
		        data: {centro:userService.getCentro().id},
		        error: function() {
		            	alert('there was an error while fetching events!');
		        	}
				}];
    	}
    	
    	$scope.aplicarHorario=function(moment){
    		if($scope.horario){
    			return $scope.horario.aplicar(moment, true);
    		}
    	}
    	
    	
    	$scope.cargarMaestros=function(){
    		console.log("obteniendo maestros...");
    		citaService.REST.getMaestros(userService.getCentro().id).then(function(res){
    			console.log("Maestros obtenidos");
    			$scope.maestros=res;
    			
    			$scope.profesional=citaService.agruparProfesionales($scope.maestros);
    			$scope.horarioGen=citaService.prepararHorarios($scope.maestros);
    			$scope.horario=$scope.horarioGen;
    			
    			$scope.calendario.iniciar();
    			
    		}, function(error){
    			errorService.alertaGrowl("Error al obtener los maestros", 'danger');
    		});
    	}
    	
    	$scope.cargarMaestros();
    	
    	$scope.onPreDragPrestacion=function(event, element, prestacion){
    		
    		if($scope.prestacion==undefined || $scope.prestacion.id!=prestacion.id){
	    		$scope.prestacion=prestacion;
	    		
	    		element.data('event', {
	    			constraint:'laborable', 
	    			duration:"00:"+prestacion.duracion,
					title: prestacion.nombre, 
					stick: true 
				});
	    		
	    		$scope.horario=citaService.getHorarioForPrestacion(prestacion, $scope.maestros.horarios);
	    		
	    		$scope.calendario.onCambiarHorario();
    		}
    	}
    	
    	$scope.onDragPrestacionEnd=function(){
    		//$scope.horario=$scope.horarioGen;
    		//$scope.calendario.onCambiarHorario();
    		$scope.prestacion={};
    	}
    	
    	$scope.onDropPrestacion=function(p,d){
    		console.log(p);
    		console.log(d);
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
    			
    			cita.constraint="laborable";
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
    	
    	
    	$scope.popup={   	
    			profesionales:[],
    			prestaciones:[],
    			
    			fecha:{
    				dt:new Date(),
    				abrir: function($event, opened) {
        	            $event.preventDefault();
        	            $event.stopPropagation();
        	            $scope.popup.opened=true;
        	        },
        	        opciones:{
        	        	showWeeks:false,
        	        	add:function(dias){
        	        		var result = new Date();
        	        	    result.setDate(result.getDate() + dias);
        	        	    return result;        	        		     	        		
        	        	}
        	        },
        	        open : function($event, opened) {
        	            $event.preventDefault();
        	            $event.stopPropagation();
        	            $scope.popup.fecha.opened=true;
        	        },
        	        
        	        getFechaIni: function(){
        	        	 // Returns the date
        	        	getMonth() // Returns the month
        	        	getFullYear()
        	        	return moment($scope.popup.fecha.valor.getDate()+" "+$scope.popup.data.hIni, "DD/MM/YYYY HH:mm");
        	        },
        	        getFechaFin: function(){
        	        	return moment($scope.popup.fecha.valor+" "+$scope.popup.data.hFin, "DD/MM/YYYY HH:mm");
        	        }
    			},

    			//METODOS
    			iniciar:function(){
    				angular.copy($scope.maestros.profesionales, $scope.popup.profesionales);    			
    	    		$scope.popup.profesionales=$scope.popup.profesionales.splice(1);
    	    		$scope.popup.prestaciones=$scope.maestros.prestaciones;
    			},
    			nuevo : function () {
    	    		var data={id:'', nombre:'',profesional:{}};
    	    		$scope.popup.iniciar();
    	    		$scope.popup.mostrar(data);
    	        },
    	        modificar : function (data) {
    	        	if(!data.profesional.id)data.profesional={id:data.profesional};
    	        	$scope.popup.iniciar();
    	        	$scope.popup.mostrar(data);
    	        },
    	        onPreGuardar:function(data){
    	        	data.json={};
    				data.json.profesional=data.profesional.id;
    				data.json.prestacion=data.prestacion.id;
    				data.json.fechaIni=$scope.popup.fecha.getFechaIni();
    				data.json.fechaFin=$scope.popup.fecha.getFechaFin();
    				data.json.cliente=$scope.popup.cliente.id;
    			},
    			buscarCliente:function(valor){
    	        	return clienteService.buscar(valor, userService.getCentro().id);        	
    	        },
    	        seleccionarCliente:function(v,m){
    	        	$scope.popup.cliente=m;
    	        },
    	        borrar:function(item){
    	        	if(item.texto){
    	        		item.texto="";
    	        	}else{
    	        		item="";
    	        	}
    	        },
    	        
    			//EVENTOS
    			onNuevo:function(data){
    				var res=citaService.nueva(data.json);    				
    				
    				data.cita={};
    				
    				data.cita.start=data.fechaIni;
    				data.cita.end=data.fechaFin;
    				data.cita.title=data.cliente.nombre+" "+cita.cliente.apellidos;
    				data.cita.constraint="laborable";
    				data.cita.color=data.prestacion.color;
        			
        			$scope.calendario.addCita(cita);
        			
        			data.cita=cita;
    				
    				/*
    				cita.fechaIni=cita.start.format("DD/MM/YYYY HH:mm:ss");
    				cita.fechaFin=cita.start.add(120, "m").format("DD/MM/YYYY HH:mm:ss");
    				cita.centro=userService.getCentro().id;
        			
    				var color=cita.prestacion.color;
    				var title=cita.cliente.nombre+" "+cita.cliente.apellidos;
    				
    				cita.prestacion=cita.prestacion.id;
    				cita.cliente=cita.cliente.id;
        			
        			var res=citaService.nueva(cita);
        			
        			cita.constraint="laborable";
        			cita.color=color;
        			cita.title=title;
        			
        			$scope.calendario.addCita(cita);
        			*/
    				return res;
    			},
    			onModificar:function(cita){
    				angular.copy(cita, cita.old);
    				return prestacionService.REST.modificar(data);
    			},
    			onPostGuardar:function(data, accion, modificando, params){
    				var txtOK=modificando?"modificada":"creada";
    				
    				errorService.procesar(accion,{
    					 0:{
    						 growl: true, texto: "Cita "+txtOK, tipo: "success"
    					 },
    					 1:{
    	  					 titulo: "Atención", texto: "La prestación no existe", tipo: "warning"
    	  				 },
    					 2:{
    	  					 titulo: "Atención", texto: "Ya existe una prestación con ese nombre", tipo: "warning"
    	  				 },
    	  				onError:function(){
    	  					//TODO eliminar la cita del calendario en caso de creacion
    	  					$scope.calendario.removeCita(params.cita);
    	  					$scope.popup.showPostError();
    					 }
    				});
    			}
    	};
    })