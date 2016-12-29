// =========================================================================
// Citas
// =========================================================================
materialAdmin
    .controller('citasController', function($scope, $compile, $document, prestacionService, userService,clienteService, horarioService, citaService, modalService, errorService, $uibModal ) {
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
    	
    	$scope.onDropPrestacion=function(evento,fecha){
    		/*if(clienteService.isSeleccionado() && $scope.profesional.id!="-1"){
    			
    		}else{    	*/		
    			evento.cliente=clienteService.getSeleccionado();
    			evento.prestacion=$scope.prestacion;
    			evento.profesional=$scope.profesional;
    			$scope.popup.modificar(evento);
    		//}
    	}
    	
    	$scope.onRenderCalendario=function(evento){    		
    		if(evento.event.prestacion){
    			evento.element.find(".fc-title").append(" ("+evento.event.prestacion.descripcion+")");
    			evento.element.find(".fc-time").append("<i class='zmdi "+evento.event.icono+" m-l-5'></i>");
    		}
    		
    		/*evento.element.attr('uib-popover', "Finally it's working")
    	    evento.element.attr('uib-popover-title', 'Hello world')
    	    evento.element.attr('uib-popover-trigger', 'mouseenter')
    	    $compile(evento.element)($scope)*/

    		/* evento.element.find(".fc-event").addClass("fc-programado");*/
    	}
    	
    	$scope.onClickCita=function(cita){
    		$scope.popup.modificar(cita);
    	}
    	
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
    			},
    			nuevo : function () {    				
    	    		var data={id:'', nombre:'',profesional:{}};    	    		
    	    		$scope.popup.cliente=clienteService.getSeleccionado();       	    		
    	    		$scope.popup.iniciar();
    	    		$scope.popup.mostrar(data);
    	        },
    	        modificar : function (data) {
    	        	//if(data.profesional && !data.profesional.id)data.profesional={id:data.profesional};
    	        	if(data.id)
    	        		data.old=angular.copy(data);
    	        	
    	        	if(data.cliente){
    	        		$scope.popup.cliente=angular.copy(data.cliente);    	        	    	        	
    	        		$scope.popup.cliente.texto= data.cliente.descripcion;
    	        	}
    	        	
    	        	$scope.popup.fecha.valor=data.start.toDate();
    	        	data.hIni=data.start.format("HH:mm");
    	        	data.hFin=data.end.format("HH:mm");
    	        	
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
    				data.json.importe=data.importe;
    				data.json.estado=data.estado;
    				if(data.id)
    					data.json.id=data.id;
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
    				cita.className="fc-programada";

    				data.cita=cita;
        			
        			$scope.calendario.addCita(cita);

        			return citaService.REST.nueva(cita.json);
    			},
    			onModificar:function(cita){
    				cita.start=moment(cita.json.fechaIni, "DD/MM/YYYY HH:mm");
    				cita.end=moment(cita.json.fechaFin, "DD/MM/YYYY HH:mm");
    				if(cita.prestacion.color)
    					cita.color=cita.prestacion.color;
    				
    				$scope.calendario.actualizarCita(cita);

    				return citaService.REST.modificar(cita.json);
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
    	  					 if(modificando){
    	  						var c=$scope.calendario.getCita(data.id)[0];
    	  						c.start=data.old.start;
    	  						c.end=data.old.end;
    	  						c.color=data.old.color;
    	  						c.title=data.old.title;
    	  						c.prestacion=data.old.prestacion;
    	  						c.profesional=data.old.profesional;
    	  						c.importe=data.old.importe;
    	  						$scope.calendario.actualizarCita(c);
    	  					 }else{
    	  						 $scope.calendario.removeCita(params.cita);
    	  					 }
    	  				 },
    	  				 onError:function(){
    	  					$scope.popup.showPostError();
    					 }
    				});
    			}
    	};
    })