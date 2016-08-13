materialAdmin

    .controller('tablasCtrl', function($scope, $filter, $sce, ngTableParams, userService) {
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
    })
    
    
    .controller('tablaUsuarios', function($scope, $filter, $sce, ngTableParams, userService, errorService, modalService, $uibModal) {
    	var self=this;
    	
    	this.datos=[];
    	
    	this.tabla=new ngTableParams({
            page: 1,            // show first page
            count: 10          // count per page
        }, {
            getData: function($defer, params) {
            	userService.getAll(params.page(), params.count()).then(function(res){
            		self.datos=res.data;
            		params.total(res.total);
            		$defer.resolve(self.datos);            		
                }, function(error){
                	errorService.alertaGrowl("Error al obtener usuarios", 'danger');
                });
            }
        });
    	
    	$scope.refrescar=function(){
    		self.tabla.reload();
    	}
        
        $scope.eliminar = function(item){
        	
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
    })
    
    .controller('tablaUsuarioPendiente', function($scope, $filter, $sce, ngTableParams, userService, errorService, modalService, $uibModal) {
    	var self=this;
    	
    	this.datos=[];
    	
    	this.tabla=new ngTableParams({
            page: 1,            // show first page
            count: 10          // count per page
        }, {
            getData: function($defer, params) {
            	userService.getPendientes(params.page(), params.count()).then(function(res){
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

    				errorService.procesar(userService.nuevoPendiente($scope.modal.usuario),{
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
        
.controller('tablaUsuarios', function($scope, $filter, $sce, ngTableParams, userService, errorService, modalService, $uibModal) {
    	var self=this;
    	
    	this.datos=[];
    	
    	this.tabla=new ngTableParams({
            page: 1,            // show first page
            count: 10          // count per page
        }, {
            getData: function($defer, params) {
            	userService.getAll(params.page(), params.count()).then(function(res){
            		self.datos=res.data;
            		params.total(res.total);
            		$defer.resolve(self.datos);            		
                }, function(error){
                	errorService.alertaGrowl("Error al obtener usuarios", 'danger');
                });
            }
        });
    	
    	$scope.refrescar=function(){
    		self.tabla.reload();
    	}
        
        $scope.eliminar = function(item){
        	
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
    })
    
    .controller('tablaCentros', function($scope, $filter, $sce, ngTableParams, userService, centroService, errorService, modalService, $uibModal) {
    	var self=this;
    	
    	this.datos=[];
    	
    	this.tabla=new ngTableParams({
            page: 1,            // show first page
            count: 10          // count per page
        }, {
            getData: function($defer, params) {
            	userService.getPendientes(params.page(), params.count()).then(function(res){
            		self.datos=res.data;
            		params.total(res.total);
            		$defer.resolve(self.datos);            		
                }, function(error){
                	errorService.alertaGrowl("Error al obtener usuarios pendientes", 'danger');
                });
            }
        });

    	$scope.modal={
    			centro:{'correoAdmin':'','nombre':'','poblacion':''},
    			
    			guardar:function(){    				

    				errorService.procesar(centroService.nuevoCentro($scope.modal.centro),{
	    				 0:{
	    					 growl: true,   				 
	    					 texto: "Centro creado. Se le ha enviado un email al usuario admin",
	    					 tipo: "success",
	    					 onProcesar: function(){
	    						 var pagina=parseInt(self.tabla.total()/self.tabla.count())+1;
	    						 self.tabla.page(pagina);
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