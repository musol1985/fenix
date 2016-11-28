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
    });

// =========================================================================
// Centros
// =========================================================================
materialAdmin
    .controller('tablaCentros', function($rootScope, $scope, $http, limitToFilter, $filter, $sce, $q, ngTableParams, userService, centroService, ubicacionService, errorService, modalService, $uibModal) {
    	var self=this;
    	
    	$scope.datos=[];
    	$scope.centro={id:'-1'};
    	
    	$scope.cargarDatos=function(){
    		centroService.getAll().then(function(res){
        		console.log("Cargando centros...");
        		$scope.datos=res.data;        
        		
        		if($scope.centro.id!='-1'){    			
        			angular.forEach($scope.datos, function(centro) {
        				if($scope.centro.id==centro.id){
        					$scope.seleccionarCentro(centro);
        				}
        			});
        		}
            }, function(error){
            	errorService.alertaGrowl("Error al obtener centros", 'danger');
            });
    	}
    	
    	$scope.cargarDatos();
    	
    	$scope.seleccionarCentro=function(row){
    		if($scope.centro)
    			$scope.centro.seleccionado=false;
    		$scope.centro=row;
    		
    		$rootScope.$broadcast('onSeleccionarCentro', row);
    	}

    	/*$scope.modal={
    			nuevoCentro:{
    				centro:{'correoAdmin':'','nombre':'','ubicacion':'', 'tipo':'0', 'color':'teal'},
    				posicion:"",
    				nombreAdmin:""
    			},
    			
    			guardar:function(){    				

    				errorService.procesar(centroService.nuevoCentro($scope.modal.nuevoCentro),{
	    				 0:{
	    					 growl: true,   				 
	    					 texto: "Centro creado. Se le ha enviado un email al usuario admin",
	    					 tipo: "success",
	    					 onProcesar: function(){
	    						 $scope.refrescar();
	    					 }
	    				 },
	    				 2:{
		   					 titulo: "Atención",    				 
		   					 texto: "Ya existe un usuario registrado con ese correo",
		   					 tipo: "warning"
		   				 }
    				});
    				
    				$scope.modalInstance.close();
    			},
    			
    	        getUbicacion : function(val) {
    	        	return ubicacionService.getUbicacion(val);
    	        },
    	        
    	        setUbicacion : function(item, model) {    	
    	        	var ubicacion=ubicacionService.setUbicacion(item, model);
    	        	
    	        	this.nuevoCentro.centro.ubicacion=ubicacion.direccion;
    	        	this.nuevoCentro.posicion=ubicacion.posicion;  
    	        },
    	        
    	        setColor : function(color){
    	        	this.nuevoCentro.centro.color=color;
    	        },
    	        
    	        getColores :function(){
    	        	return   [
    	                         'lightblue',
    	                         'bluegray',
    	                         'cyan',
    	                         'teal',
    	                         'green',
    	                         'orange',
    	                         'blue',
    	                         'purples'
    	                     ]
    	        }
    	        
    	       
    	}*/
    	
    	$scope.refrescar=function(){
    		$scope.cargarDatos();    		    		    		    		
    	}
    	
    	$scope.refrescarUsuarios=function(){
    		if($scope.centro.id!='-1')
    			$scope.seleccionarCentro($scope.centro);    		    		    		    		
    	}
    	
    	/*$scope.nuevo = function () {
    		$scope.modalInstance=modalService.mostrar($uibModal, $scope.modal);
        };*/
         
        
        
        
        $scope.popup={
    		getUbicacion : function(val) {
 	        	return ubicacionService.getUbicacion(val);
 	        },
 	        
 	        setUbicacion : function(item, model) {    	
 	        	var ubicacion=ubicacionService.setUbicacion(item, model);
 	        	
 	        	this.data.centro.ubicacion=ubicacion.direccion;
 	        	this.data.posicion=ubicacion.posicion;  
 	        },        		        		
        };
        
        
    	
    	$scope.nuevo = function () {
    		var data={centro:{'correoAdmin':'','nombre':'','ubicacion':'', 'tipo':'0', 'color':'teal'},posicion:"",nombreAdmin:""}
    		$scope.popup.mostrar(data);
        };
        
		$scope.onNuevo=function(data){
			return centroService.nuevoCentro(data);
		}
		
		$scope.onPostGuardar=function(data, accion, modificando){

			errorService.procesar(accion,{
				 0:{
					 growl: true, texto: "Centro creado. Se le ha enviado un email al usuario admin", tipo: "success",
					 onProcesar: function(){
						 $scope.refrescar();
					 }
				 },
				 2:{
   					 titulo: "Atención",    				 
   					 texto: "Ya existe un usuario registrado con ese correo",
   					 tipo: "warning"
   				 }
			});
		}

    })
    ;

// =========================================================================
// Horarios v0.1
// =========================================================================
materialAdmin
    .controller('horarios', function($state, $scope, $http, userService, centroService, horarioService, errorService, modalService, $uibModal) {
    
    	$scope.getDatos=function(params, onComplete){
    		horarioService.REST.getByCentro(userService.getCentro().id, params.page(), params.count()).then(function(res){
    			onComplete(res.data, res.total);
            }, function(error){
            	errorService.alertaGrowl("Error al obtener horarios", 'danger');
            });
    	}

    	$scope.refrescar=function(tabla){
    		tabla.reload();		    		    		    		
    	}
    	
    	$scope.crear = function () {
    		$state.go('configuracion.horario');  
        };
         
        $scope.modificar = function (data) {
        	$state.go('configuracion.horario',{id: data.id}); 
        };
        
        $scope.eliminar = function(item){
        	console.log(item);
        	errorService.alertaSiNo("Eliminar", "¿Seguro que quieres eliminar el horario?", function(){
        		errorService.procesar(horarioService.REST.eliminar(item.id),{
	   				 0:{
	   					 growl: true,   				 
	   					 texto: "Prestación eliminada correctamente",
	   					 tipo: "success",
	   					 onProcesar: function(){
	   						 $scope.refrescar();
	   					 }
	   				 },
	   				 1:{
	   					 titulo: "Atención",    				 
	   					 texto: "No existe la prestación",
	   					 tipo: "warning"
	   				 }
        		});
        	});
        }
    })
    
 
// =========================================================================
// Editor Horarios v0.1
// ========================================================================= 
    .controller('editorHorario', function($state, $scope, $uibModal, modalService, horarioService, errorService, userService) {
    	$scope.onLoaded=function(){//callback del blocky
    		return $scope.cargar($state.params.id);
    	}
    	
    	$scope.cargar=function(id){
    		if(id){
	    		accion=horarioService.REST.getEditorById(id);			
				
				errorService.procesar(accion,{
						1:{
		   				 	titulo: "Atención",    				 
		   					 texto: "El horario no existe",
		   					 tipo: "warning"
		   				 }
					}, function(res){
						console.log("Cargando editor del horario");						
						$scope.nombre=res.data.model.nombre;
						$scope.blockly.cargarXML(LZString.decompressFromBase64(res.data.codigo));
						$scope.horario=res.data;
						horarioService.iniciarHorario($scope.horario); 
				});
				
				return true;
    		}else{    			
    			$scope.horario={model:{id:id,nombre:'NuevoHorario', centro:userService.getCentro().id}};   
    			horarioService.iniciarHorario($scope.horario); 
    		}
    		console.log("Cargando editor por defecto");
    		return false;
    	} 
    	
    	$scope.modal={
			mostrar:function(){
				$scope.modalInstance=modalService.mostrar($uibModal, $scope.modal, "resources/template/editores/horario.html");
			}    			
    	}
    	
    	$scope.test=function(){
    		$scope.modal.mostrar();
    	}
    	

    	$scope.mostrarCodigo=function(){
    		alert($scope.blockly.getCode());
    	}
    	
    	$scope.toggleEditor=function(vistaPrevia){
    		$scope.editor=!$scope.editor;
    		
    		if(vistaPrevia){
    			horarioService.updateFromBlocky($scope.horario, $scope.blockly);
    			$scope.horario.model.nombre=$scope.nombre;
    			$scope.horario.compilar();
    			$scope.calendario.actualizar();    			
    		}
    	}
    	
    	$scope.eliminar=function(){   
    		if($scope.horario.model.id){
    			errorService.alertaSiNo("Eliminar", "¿Seguro que quieres eliminar el horario?", function(){
            		errorService.procesar(horarioService.REST.eliminar($scope.horario.model.id),{
    	   				 0:{
    	   					 growl: true,   				 
    	   					 texto: "Horario eliminado correctamente",
    	   					 tipo: "success",
    	   					 onProcesar: function(){
    	   						$state.go('configuracion.horarios');  
    	   					 }
    	   				 },
    	   				 1:{
    	   					 titulo: "Atención",    				 
    	   					 texto: "No existe el horario",
    	   					 tipo: "warning"
    	   				 }
            		});
            	});
    		}
    	}
    	
    	$scope.guardar=function(){    		
    		 		
    		$scope.horario.model.nombre=$scope.nombre;
    		horarioService.updateFromBlocky($scope.horario, $scope.blockly);
    		
			accion=horarioService.REST.nuevo($scope.horario,"horario/guardar");			
			
			errorService.procesar(accion,{
				 0:{
					 growl: true,   				 
					 texto: "Horario guardado",
					 tipo: "success",
   					 onProcesar: function(res){
   						 $scope.horario=res.data;
   						horarioService.iniciarHorario($scope.horario); 
   					 }
				 },
				 1:{
   					 titulo: "Atención",    				 
   					 texto: "El horario no existe",
   					 tipo: "warning"
   				 },
				 2:{
   					 titulo: "Atención",    				 
   					 texto: "Ya existe un horario con ese nombre",
   					 tipo: "warning"
   				 }
			});
    	}
    	
    	$scope.aplicarHorario=function(dia){
    		if($scope.horario){
    			return $scope.horario.aplicar(dia);    		
    		}
    	} 	    	
    	
    	$scope.editor=true;
    });

// =========================================================================
// Prestaciones v12
// =========================================================================
materialAdmin
    .controller('prestaciones', function($scope, $http, userService, centroService, prestacionService, errorService, modalService, horarioService, $uibModal) {    

    	$scope.getDatos=function(params, onComplete){
    		prestacionService.REST.getByCentro(userService.getCentro().id, params.page(), params.count()).then(function(res){
    			onComplete(res.data, res.total);
            }, function(error){
            	errorService.alertaGrowl("Error al obtener prestaciones", 'danger');
            });
    		horarioService.REST.getAllByCentro(userService.getCentro().id).then(function(res){    			
    			$scope.popup.horariosModal=res.data;
    			angular.forEach(res.data, function(horario) {
    				if(horario.generico){
    					$scope.horarioGen=horario;
    					console.log("Horario generic: "+horario.id+" "+horario.nombre);
    				}    				
    			});
            }, function(error){
            	errorService.alertaGrowl("Error al obtener horarios", 'danger');
            });
    	}
    	
    	$scope.refrescar=function(tabla){
    		tabla.reload();		    		    		    		
    	}
    	
    	$scope.popup={};
    	
    	$scope.nuevo = function () {
    		var data={id:'', nombre:'', color:'', centro:userService.getCentro().id, horario:$scope.horarioGen};
    		$scope.popup.mostrar(data);
        };
        
        $scope.modificar = function (data) {
        	if(!data.horario.id)data.horario={id:data.horario};
        	$scope.popup.mostrar(data);
        };
		
		$scope.onPreGuardar=function(data){
			data.horario=data.horario.id;
		}
		
		$scope.onNuevo=function(data){
			return prestacionService.REST.nuevo(data);
		}
		
		$scope.onModificar=function(data){
			return prestacionService.REST.modificar(data);
		}
		
		$scope.onPostGuardar=function(data, accion, modificando){
			var txtOK=modificando?"modificada":"creada";
			
			errorService.procesar(accion,{
				 0:{
					 growl: true, texto: "Prestación "+txtOK, tipo: "success",
					 onProcesar: function(){
						 $scope.popup.refrescar();
					 }
				 },
				 1:{
  					 titulo: "Atención", texto: "La prestación no existe", tipo: "warning"
  				 },
				 2:{
  					 titulo: "Atención", texto: "Ya existe una prestación con ese nombre", tipo: "warning"
  				 },
  				onError:function(){
  					data.horario={id:data.horario};
  					$scope.popup.showPostError();
				 }
			});
		}
       
        $scope.eliminar = function(item){
        	console.log(item);
        	errorService.alertaSiNo("Eliminar", "¿Seguro que quieres eliminar la prestación?", function(){
        		errorService.procesar(prestacionService.REST.eliminar(item.id),{
	   				 0:{
	   					 growl: true, texto: "Prestación eliminada correctamente",tipo: "success",
	   					 onProcesar: function(){
	   						 $scope.refrescar();
	   					 }
	   				 },
	   				 1:{ titulo: "Atención",texto: "No existe la prestación", tipo: "warning"}
        		});
        	});
        }
    });

// =========================================================================
// Usuarios
// =========================================================================
materialAdmin
    .controller('usuarios', function($scope, $filter, $sce, ngTableParams, userService, horarioService, errorService, modalService, $uibModal) {
    	
    	$scope.getDatosByCentro=function(params, onComplete){
    		if($scope.centro!="-1"){
	    		userService.getAllByCentro(params.page(), params.count(), $scope.centro).then(function(res){
	    			onComplete(res.data, res.total);         		
	            }, function(error){
	            	errorService.alertaGrowl("Error al obtener usuarios", 'danger');
	            });
	    		
	    		
    		}
    	}  
    	
    	$scope.getDatos=function(params, onComplete){
    		userService.getByCentro(params.page(), params.count(), $scope.centro).then(function(res){
    			onComplete(res.data, res.total);               		
            }, function(error){
            	errorService.alertaGrowl("Error al obtener usuarios", 'danger');
            });
    		
    		horarioService.REST.getAllByCentro(userService.getCentro().id).then(function(res){    			
    			$scope.modal.horariosModal=res.data;
    			angular.forEach(res.data, function(horario) {
    				if(horario.generico){
    					$scope.horarioGen=horario;
    					console.log("Horario generic: "+horario.id+" "+horario.nombre);
    				}    				
    			});
            }, function(error){
            	errorService.alertaGrowl("Error al obtener horarios", 'danger');
            });
    	}
    	
    	$scope.centro=userService.current.centro.id;
    	
    	$scope.setCentro=function(valor){
    		$scope.centro=valor;
    	}

    	$scope.refrescar=function(tabla){
    		if(tabla){
    			tabla.reload();
    		}else{
    			this.usuariosCtr.tabla.reload();
    		}
    	}
    	
    	$scope.modificar=function(tabla){
    		if(tabla){
    			tabla.reload();
    		}else{
    			this.usuariosCtr.tabla.reload();
    		}
    	}
    	
    	$scope.modificarHorario=function(usuario){
    		$scope.modal.mostrar(usuario);
    	}
    	
    	$scope.modal={
    			data:{horario:''},    			 	
    			horariosModal:[],			    		
    			
    			mostrar:function(usuario){
    				$scope.modal.data=angular.copy(usuario);;
    				$scope.modal.data.horario={id:$scope.modal.data.horario};
    				
    				console.log($scope.modal.data);
    				$scope.modalInstance=modalService.mostrar($uibModal, $scope.modal, "resources/template/modals/profesional.html");
    			},
    			
    			guardar:function(){    	
    				var data=$scope.modal.data;

    				data.horario=data.horario.id;
    				console.log($scope.modal.data);
    				
    				var accion=userService.modificar(data);
    				
    				errorService.procesar(accion,{
	    				 0:{
	    					 growl: true,   				 
	    					 texto: "Usuario modificado",
	    					 tipo: "success",
	    					 onProcesar: function(){	    						 
	    						 $scope.refrescar($scope.ctr.tabla);
	    					 }
	    				 },
	    				 1:{
		   					 titulo: "Atención",    				 
		   					 texto: "El usuario no existe",
		   					 tipo: "warning"
		   				 },
		   				onError:function(){
		   					$scope.modal.mostrar(false);
	    				 }
    				});
    				
    				$scope.modalInstance.close();
    			},
    			
    			onSeleccionarColor:function(color){
    				console.log($scope);
    			}
    	}
        
        $scope.eliminar = function(item){
        	console.log(item);
        	errorService.alertaSiNo("Eliminar", "¿Seguro que quieres eliminar el usuario?", function(){
        		errorService.procesar(userService.eliminar(item.id),{
	   				 0:{
	   					 growl: true,   				 
	   					 texto: "Usuario eliminado correctamente",
	   					 tipo: "success",
	   					 onProcesar: function(){
	   						 $scope.refrescar();
	   					 }
	   				 },
	   				 1:{
	   					 titulo: "Atención",    				 
	   					 texto: "No existe ese usuario",
	   					 tipo: "warning"
	   				 }
        		});
        	});
        }
        
        $scope.$on('onSeleccionarCentro', function (event, centro) { 
        	$scope.setCentro(centro.id);
        	$scope.refrescar();
        });
    })
 // =========================================================================
// Usuarios Pendientes
// =========================================================================   
    .controller('tablaUsuarioPendiente', function($scope, $rootScope, $filter, $sce, ngTableParams, userService, errorService, modalService, $uibModal) {
    	var self=this;
    	

    	$scope.getDatos=function(params, onComplete){
    		userService.getPendientes(params.page(), params.count(), userService.current.centro.id).then(function(res){
    			onComplete(res.data, res.total);               		
            }, function(error){
            	errorService.alertaGrowl("Error al obtener usuarios", 'danger');
            });
    	}
    	

    	$scope.modal={
    			usuario:{'correo':'','nombre':'','color':''},
    			
    			guardar:function(){    				
    				var request={centro: userService.current.centro.id, usuario: $scope.modal.usuario};
    				errorService.procesar(userService.nuevoPendiente(request),{
	    				 0:{
	    					 growl: true,   				 
	    					 texto: "Usuario pendiente creado. Se le ha enviado un email a su correo",
	    					 tipo: "success",
	    					 onProcesar: function(){
	    						 var pagina=parseInt(self.tabla.total()/self.tabla.count())+1;
	    						 self.tabla.page(pagina);
	    						 $scope.refrescar();
	    					 }
	    				 },
	    				 2:{
	    					 titulo: "Atención",    				 
	    					 texto: "Ya existe un usuario con ese correo. Se le ha reenviado el codigo de activación a su correo.",
	    					 tipo: "warning"
	    				 },
	    				 onError:function(){
	    					 $scope.nuevo(); 
	    				 }
    				});
    				
    				$scope.modalInstance.close();
    			}
    	}
    	
    	$scope.refrescar=function(){
    		self.tabla.reload();
    	}
    	
    	$scope.nuevo = function () {
    		$scope.modalInstance=modalService.mostrar($uibModal, $scope.modal, "resources/template/modals/nuevoProfesional.html");    		
        };
        
        $scope.eliminar = function(item){
        	
        	errorService.alertaSiNo("Eliminar", "¿Seguro que quieres eliminar el usuario?", function(){
        		errorService.procesar(userService.eliminarPendiente(item.id),{
	   				 0:{
	   					 growl: true,   				 
	   					 texto: "Usuario pendiente eliminado correctamente",
	   					 tipo: "success",
	   					 onProcesar: function(){
	   						 $scope.refrescar();
	   					 }
	   				 },
	   				 1:{
	   					 titulo: "Atención",    				 
	   					 texto: "No existe un usuario pendiente con ese correo",
	   					 tipo: "warning"
	   				 }
        		});
        	});
        }
        
        $scope.enviarMail =function(item){
    		errorService.procesar(userService.enviarCorreo(item.id),{
   				 0:{
   					 growl: true,   				 
   					 texto: "Correo de activacion enviado a "+item.correo,
   					 tipo: "success",
   					 onProcesar: function(response){
   						//var item=$filter('filter')(self.datos, function (d) {return response.data.id === item.id;})[0];

   						 //item.fechaEnvio=response.data.fechaEnvio;   	
   						 $scope.refrescar();
   					 }
   				 },
   				 1:{
   					 titulo: "Atención",    				 
   					 texto: "No existe un usuario pendiente con ese correo",
   					 tipo: "warning"
   				 }
    		});
        }
        
        $scope.$on('onSeleccionarColor', function (event, color) { 
    		$scope.modal.usuario.color=color;
    		console.log($scope.modal.usuario);
        });
    })
    