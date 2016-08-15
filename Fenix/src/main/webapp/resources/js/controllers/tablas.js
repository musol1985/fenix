materialAdmin

   /* .controller('tablasCtrl', function($scope, $filter, $sce, ngTableParams, userService) {
    	var self=this;

    	this.tablaUsuarios={};
    	
    	this.tablaUsuarios.datos=[];
    	
    	this.tablaUsuarios.tabla=new ngTableParams({
            page: 1,            // show first page
            count: 10          // count per page
        }, {
            total: self.tablaUsuarios.datos.length, // length of data
            getData: function($defer, params) {
            	userService.getAll().then(function(data){
            		self.tablaUsuarios.datos=data;
                });
            }
        });
    	
    	
    	this.tUsuariosPendiente={};
    	
    	this.tUsuariosPendiente.datos=[];
    	
    	this.tUsuariosPendiente.tabla=new ngTableParams({
            page: 1,            // show first page
            count: 10          // count per page
        }, {
            total: self.tUsuariosPendiente.datos.length, // length of data
            getData: function($defer, params) {
            	userService.getAll().then(function(data){
            		self.tUsuariosPendiente.datos=data;
                });
            }
        });
    	
    	this.tUsuariosPendiente.usuario={correo:'', nombre:''}
    	
    	this.tUsuariosPendiente.nuevo=function(){
    		userService.nuevoPendiente(self.tUsuariosPendiente.usuario).then(function(data){
        		alert(data);
            });
    	}

    	this.tUsuariosPendiente.submit=function(){
    		self.tUsuariosPendiente.nuevo();
    	}
    
    })*/
    
    
    .controller('tablaUsuarios', function($scope, $filter, $sce, ngTableParams, userService, errorService, modalService, $uibModal) {
    	var self=this;
    	
    	this.datos=[];    
    	
    	$scope.todos=false;
    	$scope.centro="-1";
    	
    	this.tabla=new ngTableParams({
            page: 1,            // show first page
            count: 10          // count per page
        }, {
            getData: function($defer, params) {
            	if($scope.centro!="-1"){
	            	if($scope.todos){
		            	userService.getAllByCentro(params.page(), params.count(), $scope.centro).then(function(res){
		            		self.datos=res.data;
		            		params.total(res.total);
		            		$defer.resolve(self.datos);            		
		                }, function(error){
		                	errorService.alertaGrowl("Error al obtener usuarios", 'danger');
		                });
	            	}else{
	            		userService.getByCentro(params.page(), params.count(), $scope.centro).then(function(res){
		            		self.datos=res.data;
		            		params.total(res.total);
		            		$defer.resolve(self.datos);            		
		                }, function(error){
		                	errorService.alertaGrowl("Error al obtener usuarios", 'danger');
		                });
	            	}
            	}
            }
        });
    	
    	
    	
    	$scope.setTodos=function(valor){
    		$scope.todos=valor;
    	}
    	
    	$scope.setCentro=function(valor){
    		$scope.centro=valor;
    	}
    	
    	$scope.setCentroSesion=function(){    		
    		$scope.centro=userService.current.centro.sId;
    	}

    	$scope.refrescar=function(){
    		self.tabla.reload();
    	}
        
        $scope.eliminar = function(item){
        	console.log(item);
        	errorService.alertaSiNo("Eliminar", "¿Seguro que quieres eliminar el usuario?", function(){
        		errorService.procesar(userService.eliminar(item.sId),{
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
        	$scope.setCentro(centro.sId);
        	self.tabla.reload();
        });
    })
    
    .controller('tablaUsuarioPendiente', function($scope, $filter, $sce, ngTableParams, userService, errorService, modalService, $uibModal) {
    	var self=this;
    	
    	this.datos=[];
    	
    	this.tabla=new ngTableParams({
            page: 1,            // show first page
            count: 10          // count per page
        }, {
            getData: function($defer, params) {
            	userService.getPendientes(params.page(), params.count(), userService.current.centro.sId).then(function(res){
            		self.datos=res.data;
            		params.total(res.total);
            		$defer.resolve(self.datos);            		
                }, function(error){
                	errorService.alertaGrowl("Error al obtener usuarios pendientes", 'danger');
                });
            }
        });

    	$scope.modal={
    			usuario:{'correo':'','nombre':''},
    			
    			guardar:function(){    				
    				var request={centro: userService.current.centro.sId, usuario: $scope.modal.usuario};
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
    })
    
    .controller('tablaCentros', function($rootScope, $scope, $http, limitToFilter, $filter, $sce, $q, ngTableParams, userService, centroService, ubicacionService, errorService, modalService, $uibModal) {
    	var self=this;
    	
    	$scope.datos=[];
    	$scope.centro={sId:'-1'};
    	
    	$scope.cargarDatos=function(){
    		centroService.getAll().then(function(res){
        		console.log("Cargando centros...");
        		$scope.datos=res.data;        
        		
        		if($scope.centro.sId!='-1'){    			
        			angular.forEach($scope.datos, function(centro) {
        				if($scope.centro.sId==centro.sId){
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
    		row.seleccionado=true;
    		
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
    	                         'purple'
    	                     ]
    	        }
    	        
    	       
    	}
    	
    	$scope.refrescar=function(){
    		$scope.cargarDatos();    		    		    		    		
    	}
    	
    	$scope.refrescarUsuarios=function(){
    		if($scope.centro.sId!='-1')
    			$scope.seleccionarCentro($scope.centro);    		    		    		    		
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
        
        

    })