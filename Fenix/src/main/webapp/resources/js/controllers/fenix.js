materialAdmin
.controller('contextoCtrl', function($scope, $http, $timeout, errorService, messageService, clienteService, userService, modalService, $uibModal) {
    	
    	$scope.cliente={};      
        
        this.buscarCliente=function(valor){
        	return clienteService.buscar(valor, userService.getCentro().id);        	
        	
        }
        
        this.seleccionarCliente=function($item, $model){
        	if($item.id==-1){
        		this.crearCliente($item.texto);
        	}        	
        }
        
        this.crearCliente=function(valor){
        	$scope.modal.data.dni=valor;
        	$scope.modal.data.centro=userService.getCentro().id;
        	$scope.modal.mostrar(false);
        }
        
        
        $scope.modal={
    			data:{},
    	
    			getNew:function(){
    				return {dni:'', nombre:'', color:''};
    			},    			    		
    			
    			mostrar:function(reset){
    				if(reset)
    					$scope.modal.data=this.getNew();
    				$scope.modalInstance=modalService.mostrar($uibModal, $scope.modal, "resources/template/modals/cliente.html");
    			},
    			
    			guardar:function(){    				
    				var data=$scope.modal.data;
    				var accion;
    				

    					accion=clienteService.nuevo(data);

    				
    				errorService.procesar(accion,{
	    				 0:{
	    					 growl: true,   				 
	    					 texto: "Cliente creado",
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
    			}   
    	}
    	

})
