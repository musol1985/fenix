// =========================================================================
// Citas
// =========================================================================
materialAdmin
    .controller('citasController', function($scope, $document, prestacionService, userService,clienteService, horarioService, citaService, modalService, errorService, $uibModal ) {
    	$scope.maestros={
    			profesionales:[],
    			horarios:[],
    			prestaciones:[]
    	};
    	
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
    	
    	$scope.procesarCita=function(cita){
    		if(cita.cliente)
    			cita.title=cita.cliente.descripcion;
    		return cita;
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
        	        getFechaString:function(){
        	        	var date=$scope.popup.fecha.valor;
        	        	return date.getDate()+"/"+(parseInt(date.getMonth())+1)+"/"+date.getFullYear();   
        	        },        	                	        
        	        getFechaIni: function(){
        	        	return $scope.popup.fecha.getFechaString()+" "+$scope.popup.data.hIni;
        	        },
        	        getFechaFin: function(){
        	        	return $scope.popup.fecha.getFechaString()+" "+$scope.popup.data.hFin;
        	        },
        	        getFechaISO: function(fecha){
        	        	return moment(fecha, "DD/MM/YYYY HH:mm").toISOString();
        	        }
    			},
    			
    			hIniKeyUp:function(){
    				if($scope.popup.data.hIni && $scope.popup.data.prestacion){
    					var m=moment($scope.popup.data.hIni, "HH:mm").add($scope.popup.data.prestacion.duracion,'m');
    					
    					$scope.popup.data.hFin=m.format("HH:mm");
    				}    				
    			},
    			//METODOS
    			iniciar:function(){
    				angular.copy($scope.maestros.profesionales, $scope.popup.profesionales);    			
    	    		$scope.popup.profesionales=$scope.popup.profesionales.splice(1);
    	    		$scope.popup.prestaciones=$scope.maestros.prestaciones;
    	    		
    	    		$scope.popup.cliente=clienteService.getSeleccionado();    	    		
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
    				data.json.centro=userService.getCentro().id;
    			},
    			buscarCliente:function(valor){
    	        	return clienteService.buscar(valor, userService.getCentro().id);        	
    	        },
    	        seleccionarCliente:function(model,valor){
    	        	$scope.popup.cliente=model;
    	        },
    	        seleccionarPrestacion:function(){
    	        	$scope.popup.data.importe=$scope.popup.data.prestacion.importe;
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
    				//var res=citaService.nueva(data.json);    				
    				var cita={};
    				
    				cita.json=data.json;
    				
    				cita.id=cita.json.fechaIni+"#"+cita.json.prestacion+"#"+cita.json.cliente;
    				cita.cliente=$scope.popup.cliente;
    				cita.start=moment(cita.json.fechaIni, "DD/MM/YYYY HH:mm");
    				cita.end=moment(cita.json.fechaFin, "DD/MM/YYYY HH:mm");
    				cita.title=cita.cliente.nombre+" "+cita.cliente.apellidos;
    				cita.constraint="laborable";
    				cita.color=data.prestacion.color;

    				data.cita=cita;
        			
        			$scope.calendario.addCita(cita);

        			return citaService.REST.nueva(cita.json);
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
    	  				 96:{
	   	  					 titulo: "Atención", texto: "Existen citas en el rango de esta cita", tipo: "warning", error:true
	   	  				 },
    	  				 onErrorResponse:function(){
    	  					$scope.calendario.removeCita(params.cita);
    	  				 },
    	  				 onError:function(){
    	  					$scope.popup.showPostError();
    					 }
    				});
    			}
    	};
    })