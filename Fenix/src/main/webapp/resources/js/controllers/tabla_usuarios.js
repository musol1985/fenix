materialAdmin

    .controller('tablasCtrl', function($scope, $filter, $sce, ngTableParams, userService) {
    	var self=this;
    	 this.hola="hola";

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
    	
    	this.tUsuariosPendiente.usuario={correo:'fdssdf', nombre:''}
    	
    	this.tUsuariosPendiente.nuevo=function(){
    		alert("nuevoUsuario");
    		userService.nuevoPendiente(self.tUsuariosPendiente.usuario).then(function(data){
        		alert(data);
            });
    	}

    	this.tUsuariosPendiente.submit=function(){
    		self.tUsuariosPendiente.nuevo();
    	}
    })
    
    .controller('tablaUsuarioPendiente', function($scope, $filter, $sce, ngTableParams, userService, $uibModal) {
    	var self=this;
    	
    	this.datos=[];
    	
    	this.tabla=new ngTableParams({
            page: 1,            // show first page
            count: 10          // count per page
        }, {
            total: self.datos.length, // length of data
            getData: function($defer, params) {
            	userService.getAll().then(function(data){
            		self.datos=data;
                });
            }
        });

    	$scope.modal={
    			usuario:{correo:'',nombre:''},
    			guardar:function(){
    				userService.nuevoPendiente($scope.modal.usuario).then(function(data){
    	        		alert(data);
    	            });
    			}
    	}    	
    	
    	function modalInstances(animation, size, backdrop, keyboard) {
    		alert('ie');
            var modalInstance = $uibModal.open({
                animation: animation,
                templateUrl: 'myModalContent.html',
                controller: 'ModalInstanceCtrl',
                size: size,
                backdrop: backdrop,
                keyboard: keyboard,
                resolve: {
                    content: function () {
                        return $scope.modal;
                    }
                }
            
            });
        }
    	
    	$scope.nuevo = function () {
            modalInstances(true, '', 'static', true)
        };
    })
        
