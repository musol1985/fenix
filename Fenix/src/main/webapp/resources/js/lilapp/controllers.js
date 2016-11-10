
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

    	$scope.modal={
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
    	        
    	       
    	}
    	
    	$scope.refrescar=function(){
    		$scope.cargarDatos();    		    		    		    		
    	}
    	
    	$scope.refrescarUsuarios=function(){
    		if($scope.centro.id!='-1')
    			$scope.seleccionarCentro($scope.centro);    		    		    		    		
    	}
    	
    	$scope.nuevo = function () {
    		$scope.modalInstance=modalService.mostrar($uibModal, $scope.modal);
        };
         

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
    .controller('editorHorario', function($state, $scope, $uibModal, modalService, horarioService, errorService) {
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
						$scope.blockly.cargarXML(LZString.decompressFromBase64(res.data.codigo));
				});
				
				return true;
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
    			$scope.horario=horarioService.newFromBlocky($scope.blockly);
    			$scope.calendario.actualizar();
    			
    		}
    	}
    	
    	$scope.eliminar=function(){   
    		if($state.params.id){
    			errorService.alertaSiNo("Eliminar", "¿Seguro que quieres eliminar el horario?", function(){
            		errorService.procesar(horarioService.REST.eliminar($state.params.id),{
    	   				 0:{
    	   					 growl: true,   				 
    	   					 texto: "Horario eliminado correctamente",
    	   					 tipo: "success",
    	   					 onProcesar: function(){
    	   						 $scope.refrescar();
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
    		var h=horarioService.newFromBlocky($scope.blockly);    		
    		h.horario.nombre=$scope.nombre;
    		if($state.params.id){
    			h.horario.id=$state.params.id;
    		}
    		
			accion=horarioService.REST.nuevo(h,"horario/guardar");			
			
			errorService.procesar(accion,{
				 0:{
					 growl: true,   				 
					 texto: "Horario guardado",
					 tipo: "success"
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
    .controller('prestaciones', function($scope, $http, userService, centroService, prestacionService, errorService, modalService, $uibModal) {
    
    	$scope.getDatos=function(params, onComplete){
    		prestacionService.REST.getByCentro(userService.getCentro().id, params.page(), params.count()).then(function(res){
    			onComplete(res.data, res.total);
            }, function(error){
            	errorService.alertaGrowl("Error al obtener prestaciones", 'danger');
            });
    	}

    	$scope.modal={
    			data:{},
    	
    			getNew:function(){
    				return {id:'', nombre:'', color:'', centro:userService.getCentro().id};
    			},    			    		
    			
    			mostrar:function(reset){
    				if(reset)
    					$scope.modal.data=this.getNew();
    				$scope.modalInstance=modalService.mostrar($uibModal, $scope.modal, "resources/template/modals/prestacion.html");
    			},
    			
    			guardar:function(){    				
    				var data=$scope.modal.data;
    				var accion;
    				
    				if(data.id==''){
    					accion=prestacionService.REST.nueva(data);
    				}else{
    					accion=prestacionService.REST.modificar(data);
    				}
    				
    				errorService.procesar(accion,{
	    				 0:{
	    					 growl: true,   				 
	    					 texto: "Prestación creada",
	    					 tipo: "success",
	    					 onProcesar: function(){
	    						 $scope.refrescar();
	    					 }
	    				 },
	    				 1:{
		   					 titulo: "Atención",    				 
		   					 texto: "La prestación no existe",
		   					 tipo: "warning"
		   				 },
	    				 2:{
		   					 titulo: "Atención",    				 
		   					 texto: "Ya existe una prestación con ese nombre",
		   					 tipo: "warning"
		   				 },
		   				onError:function(){
		   					$scope.modal.mostrar(false);
	    				 }
    				});
    				
    				$scope.modalInstance.close();
    			},
    	        
    	        setColor : function(color){
    	        	this.data.color=color;
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
    	                         'purple'
    	                     ]
    	        }    
    	}
    	
    	$scope.refrescar=function(tabla){
    		tabla.reload();		    		    		    		
    	}
    	
    	$scope.nuevo = function () {
    		$scope.modal.mostrar(true);
        };
         
        $scope.modificar = function (data) {
        	$scope.modal.data=angular.copy(data);
    		$scope.modal.mostrar(false);
        };
        
        $scope.eliminar = function(item){
        	console.log(item);
        	errorService.alertaSiNo("Eliminar", "¿Seguro que quieres eliminar la prestación?", function(){
        		errorService.procesar(prestacionService.REST.eliminar(item.id),{
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
    });

// =========================================================================
// Usuarios
// =========================================================================
materialAdmin
    .controller('usuarios', function($scope, $filter, $sce, ngTableParams, userService, errorService, modalService, $uibModal) {
    	
    	$scope.getDatosByCentro=function(params, onComplete){
    		if($scope.centro!="-1")
	    		userService.getAllByCentro(params.page(), params.count(), $scope.centro).then(function(res){
	    			onComplete(res.data, res.total);         		
	            }, function(error){
	            	errorService.alertaGrowl("Error al obtener usuarios", 'danger');
	            });
    	}  
    	
    	$scope.getDatos=function(params, onComplete){
    		userService.getByCentro(params.page(), params.count(), $scope.centro).then(function(res){
    			onComplete(res.data, res.total);               		
            }, function(error){
            	errorService.alertaGrowl("Error al obtener usuarios", 'danger');
            });
    	}
    	
    	$scope.centro="-1";
    	

    	$scope.setTodos=function(valor){
    		$scope.todos=valor;
    	}
    	
    	$scope.setCentro=function(valor){
    		$scope.centro=valor;
    	}
    	
    	$scope.setCentroSesion=function(){    		
    		$scope.centro=userService.current.centro.id;
    	}

    	$scope.refrescar=function(tabla){
    		if(tabla){
    			tabla.reload();
    		}else{
    			this.usuariosCtr.tabla.reload();
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
    
    .controller('tablaUsuarioPendiente', function($scope, $rootScope, $filter, $sce, ngTableParams, userService, errorService, modalService, $uibModal) {
    	var self=this;
    	
    	this.datos=[];
    	
    	this.tabla=new ngTableParams({
            page: 1,            // show first page
            count: 10          // count per page
        }, {
            getData: function($defer, params) {
            	userService.getPendientes(params.page(), params.count(), userService.current.centro.id).then(function(res){
            		self.datos=res.data;
            		params.total(res.total);
            		$defer.resolve(self.datos);            		
                }, function(error){
                	errorService.alertaGrowl("Error al obtener usuarios pendientes", 'danger');
                });
            }
        });

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
    		$scope.modalInstance=modalService.mostrar($uibModal, $scope.modal);
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
    